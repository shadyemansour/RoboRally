package game.gameObjects.bot;

import game.gameObjects.gamefield.*;
import javafx.util.Pair;
import model.DataModelManager;
import networking.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import view.RoboRallyApplication;

import java.util.*;
import java.util.stream.Collectors;
import static java.lang.Math.toIntExact;


public class Bot extends User {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Bot");
    private Boolean serverBot;
    private WriteThread write;
    private ReadThread read;

    private QLearning qlearning;
    private LinkedList<Integer> bestPath;
    private int currentPosition;

    private int activePhase;
    private int orientation;
    private int startingPoint;
    private List<Integer> takenStartingPoints;
    private JSONObject currentGameField;
    private JSONObject startingBoard;
    private int userID;
    private Boolean reached;
    private Boolean playedCards;
    private int position;
    private int expectedPosition;
    private int tempOrientation;
    private String[] cardsToBePlayed;
    private int rebootField;
    public DataModelManager dataModel;
    private int checkpoint;
    private LinkedList<Integer> checkpoints;
    private String mapType;
    private Boolean allReached;
    private Iterator cpIterator;
    private FieldObject[][] fullMap;



    public Bot(User user, Boolean serverBot) {
        super.setBot(this);
        setGroup("Kursive Kationen");
        setServerProtocol(1.0);
        RoboRallyApplication.player = this;

        this.serverBot = serverBot;
        this.takenStartingPoints = new ArrayList<>();
        this.orientation = 2;
        this.reached = false;
        this.playedCards = false;
        if (user != null) {
            if (!serverBot) {
                this.dataModel = user.getDataModel();
                this.write = user.getWrite();
                this.read = user.getRead();
            }
        }
    }


    /**
     * buildMap():
     * called when MapSelected message is received to build
     * the 2D array representation of the full map
     *
     * @param racingCourse:  racing course
     * @param startingBoard: starting board
     */
    public void buildMap(JSONObject racingCourse, JSONObject startingBoard) {
        FieldObject[][] map = new FieldObject[10][13];
        FieldObject[][] start = jsonToArray(10, 3, startingBoard);
        FieldObject[][] race = jsonToArray(10, 10, racingCourse);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 13; j++) {
                map[i][j] = j < 3 ? start[i][j] : race[i][j - 3];
                String t = map[i][j].getType();
            }
        }
        setFullMap(map);
    }

    /**
     * chooseStartingPoint():
     * chooses starting point according to the map
     */
    private void chooseStartingPoint() {
        if (mapType.equals("DizzyHighway")) {
            if (!takenStartingPoints.contains(105)) {
                startingPoint = 105;
            } else if (!takenStartingPoints.contains(78)) {
                startingPoint = 78;
            } else if (!takenStartingPoints.contains(54)) {
                startingPoint = 54;
            } else if (!takenStartingPoints.contains(39)) {
                startingPoint = 39;
            } else if (!takenStartingPoints.contains(14)) {
                startingPoint = 14;
            } else if (!takenStartingPoints.contains(66)) {
                startingPoint = 66;
            }
            rebootField = 46;
            checkpoint = 51;
        } else {
            if (!takenStartingPoints.contains(14)) {
                startingPoint = 14;
            } else if (!takenStartingPoints.contains(39)) {
                startingPoint = 39;
            } else if (!takenStartingPoints.contains(54)) {
                startingPoint = 54;
            } else if (!takenStartingPoints.contains(66)) {
                startingPoint = 66;
            } else if (!takenStartingPoints.contains(78)) {
                startingPoint = 78;
            } else if (!takenStartingPoints.contains(105)) {
                startingPoint = 105;
            }
            rebootField = 1;
            Integer[] tempCheck = {36, 96, 101, 31};
            checkpoints = new LinkedList<>(Arrays.asList(tempCheck));
            cpIterator = checkpoints.iterator();
            checkpoint = (Integer) cpIterator.next();

        }
        setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));

        write.setStartingPoint(startingPoint);
        getLogger().debug("Bot chose startingPoint");
    }

    /**
     * chooseCard():
     * chooses cards
     *
     * @param cards: cards dealt by server
     */
    public void chooseCards(String[] cards) {
        getLogger().info("Finding Best Cards");
        Boolean random = false;
        if (random) {
            for (int i = 0; i < 5; i++) { //zufaellige Zuege fuer jetzt
                write.selectCard(cards[i], i);
            }
        } else {
            setExpectedPosition(getPosition());
            getLogger().debug("Bot got position: " + getExpectedPosition());
            List<String> bestCards = new ArrayList<>();
            int f = 0;
            setTempOrientation(getOrientation());

            do {
                bestCards = bestCards(bestCards, getExpectedPosition());
                getLogger().debug("Before checking cards got from bestCards(): " + bestCards.toString());
                getLogger().debug("Round " + f++ + " of choosing Cards");
                bestCards = checkMoves(bestCards);
                getLogger().debug("After checking cards got from bestCards(): " + bestCards.toString());
            } while (bestCards.size() < 5 && !getReached());
            getLogger().debug("Final best Cards: " + bestCards.toString());


            List<String> list = new ArrayList(Arrays.asList(cards));
            getLogger().debug("Cards dealt from Server: " + list.toString());
            HashMap<String, Long> givenCards =
                    (HashMap<String, Long>) list.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

            getLogger().debug("Setting cardsToBePlayed");
            setCardsToBePlayed(compareGivenAndBest(givenCards, bestCards));

            String debugCards = "Cards to be played: [ ";
            for (String card : getCardsToBePlayed()) {
                debugCards += (card + " ");
            }
            getLogger().debug(debugCards + "]");
            getLogger().info("Chose best Cards");

        }

    }

    /**
     * playCards():
     * places the cards on the player's registers when the timer starts
     */
    public void playCards() {
        for (int i = 0; i < cardsToBePlayed.length; i++) { //place cards
            write.selectCard(cardsToBePlayed[i], i);
        }
        setPlayedCards(true);
        cardsToBePlayed = null;
        getLogger().info("Played Cards");
    }

    /**
     * compareGivenAndBest():
     * compares best possible cards with the cards dealt to player
     *
     * @param givenCards: cards dealt to player
     * @param bestCards:  cards to be played in best case
     * @return a list containing the cards chosen from dealt cards
     */
    private String[] compareGivenAndBest(HashMap<String, Long> givenCards, List<String> bestCards) {
        setExpectedPosition(getPosition());
        setTempOrientation(getOrientation());
        String[] toBePlayed = new String[5];
        int j = 0;
        for (int i = 0; i < bestCards.size(); i++) {
            //MoveII
            if (bestCards.get(i).equals(("MoveII"))) {
                if (givenCards.containsKey("MoveII")) {
                    toBePlayed[j] = "MoveII";
                    removeCard(givenCards, "MoveII", 1);
                    j++;
                } else if (givenCards.containsKey("MoveI") && givenCards.containsKey("Again")) {
                    toBePlayed[j] = "MoveI";
                    removeCard(givenCards, "MoveI", 1);
                    j++;
                    if (j < 5) {
                        toBePlayed[j] = "Again";
                        removeCard(givenCards, "Again", 1);
                        j++;
                    }
                } else if (givenCards.containsKey("MoveI") && givenCards.get("MoveI") > 1) {
                    toBePlayed[j] = "MoveI";
                    removeCard(givenCards, "MoveI", 1);
                    j++;
                    if (j < 5) {
                        toBePlayed[j] = "MoveI";
                        removeCard(givenCards, "MoveI", 1);
                        j++;
                    }
                } else if (givenCards.containsKey("MoveI")) {
                    toBePlayed[j] = "MoveI";
                    removeCard(givenCards, "MoveI", 1);
                    j++;

                } else {
                    String random = chooseRandom(givenCards,j);
                    toBePlayed[j] = random;
                    removeCard(givenCards, random, 1);
                    j++;
                }
                //MoveIII
            } else if (bestCards.get(i).equals(("MoveIII"))) {
                if (givenCards.containsKey("MoveIII")) {
                    toBePlayed[j] = "MoveIII";
                    removeCard(givenCards, "MoveIII", 1);
                    j++;
                } else if (givenCards.containsKey("MoveII") && givenCards.containsKey("MoveI")) {
                    toBePlayed[j] = "MoveII";
                    removeCard(givenCards, "MoveII", 1);
                    j++;

                    if (j < 5) {
                        toBePlayed[j] = "MoveI";
                        removeCard(givenCards, "MoveI", 1);
                        j++;
                    }
                } else if (givenCards.containsKey("MoveI") && givenCards.containsKey("Again")) {
                    if (givenCards.get("Again") > 1) {
                        toBePlayed[j] = "MoveI";
                        removeCard(givenCards, "MoveI", 1);
                        j++;
                        for (int k = 0; k < 2 && j < 5; k++) {
                            toBePlayed[j] = "Again";
                            removeCard(givenCards, "Again", 1);
                            j++;
                        }

                    } else if (j < 2 && givenCards.get("MoveI") > 1) {
                        toBePlayed[j] = "MoveI";
                        j++;
                        toBePlayed[j] = "MoveI";
                        j++;
                        removeCard(givenCards, "MoveI", 2);


                        toBePlayed[j] = "Again";
                        removeCard(givenCards, "Again", 1);
                        j++;

                    } else if (j == 2) {
                        toBePlayed[j] = "MoveI";
                        removeCard(givenCards, "MoveI", 1);
                        j++;

                        toBePlayed[j] = "Again";
                        removeCard(givenCards, "Again", 1);
                        j++;
                    }
                } else if (givenCards.containsKey("MoveI")) {
                    if (j == 4) {
                        toBePlayed[j] = "MoveI";
                        removeCard(givenCards, "MoveI", 1);
                        j++;
                    } else if (j == 3) {
                        if (givenCards.get("MoveI") > 1) {
                            for (int k = 0; k < 2 && j < 5; k++) {
                                toBePlayed[j] = "MoveI";
                                removeCard(givenCards, "MoveI", 1);
                                j++;
                            }
                        }
                    } else { // j < 3
                        if (givenCards.get("MoveI") > 2) {
                            for (int k = 0; k < 3 && j < 5; k++) {
                                toBePlayed[j] = "MoveI";
                                removeCard(givenCards, "MoveI", 1);
                                j++;
                            }
                        } else if (givenCards.get("MoveI") > 1) {
                            for (int k = 0; k < 2 && j < 5; k++) {
                                toBePlayed[j] = "MoveI";
                                removeCard(givenCards, "MoveI", 1);
                                j++;
                            }

                            String random = chooseRandom(givenCards,j);
                            toBePlayed[j] = random;
                            removeCard(givenCards, random, 1);
                            j++;

                        } else {
                            toBePlayed[j] = "MoveI";
                            removeCard(givenCards, "MoveI", 1);
                            j++;
                            String random = chooseRandom(givenCards,j);
                            toBePlayed[j] = random;
                            removeCard(givenCards, random, 1);
                            j++;
                            random = chooseRandom(givenCards,j);
                            toBePlayed[j] = random;
                            removeCard(givenCards, random, 1);
                            j++;
                        }
                    }
                } else if (givenCards.containsKey("MoveII")) {
                    toBePlayed[j] = "MoveII";
                    removeCard(givenCards, "MoveII", 1);
                    j++;
                } else {
                    String random = chooseRandom(givenCards,j);
                    toBePlayed[j] = random;
                    removeCard(givenCards, random, 1);
                    j++;
                }
            } else if (bestCards.get(i).equals("UTurn")) {
                if (givenCards.containsKey("UTurn")) {
                    toBePlayed[j] = "UTurn";
                    j++;
                    removeCard(givenCards, "UTurn", 1);
                } else if (j < 4) {
                    if (i+1 < bestCards.size() && bestCards.get(i + 1).contains("Move")) { //muss
                        if (i+1 < bestCards.size() && bestCards.get(i + 1).equals("MoveI")) {
                            if (givenCards.containsKey("MoveI")) {
                                if (givenCards.containsKey("UTurn")) {
                                    toBePlayed[j] = "UTurn";
                                    j++;
                                    removeCard(givenCards, "UTurn", 1);
                                } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                    if (givenCards.containsKey("TurnRight")) {
                                        toBePlayed[j] = "TurnRight";
                                        j++;
                                        removeCard(givenCards, "TurnRight", 1);
                                        if (j < 5) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                        }
                                    } else {
                                        toBePlayed[j] = "TurnLeft";
                                        j++;
                                        removeCard(givenCards, "TurnLeft", 1);

                                        if (j < 5) {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);
                                        }

                                    }
                                }
                                if (j < 5) {
                                    toBePlayed[j] = "MoveI";
                                    j++;
                                    removeCard(givenCards, "MoveI", 1);
                                }
                            } else if (givenCards.containsKey("BackUp")) {
                                toBePlayed[j] = "BackUp";
                                j++;
                                removeCard(givenCards, "BackUp", 1);
                                bestCards.remove(i);
                                i--;
                                if (j < 5) {
                                    if (givenCards.containsKey("UTurn")) {
                                        toBePlayed[j] = "UTurn";
                                        j++;
                                        removeCard(givenCards, "UTurn", 1);
                                    } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                        if (givenCards.containsKey("TurnRight")) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                            if (j < 5) {
                                                toBePlayed[j] = "TurnRight";
                                                j++;
                                                removeCard(givenCards, "TurnRight", 1);
                                            }
                                        } else {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);

                                            if (j < 5) {
                                                toBePlayed[j] = "TurnLeft";
                                                j++;
                                                removeCard(givenCards, "TurnLeft", 1);
                                            }

                                        }
                                    }
                                    i++;

                                }
                            }
                        } else if (i+1 < bestCards.size() && bestCards.get(i + 1).equals("MoveII")) {
                            if (givenCards.containsKey("MoveII")) {
                                if (givenCards.containsKey("UTurn")) {
                                    toBePlayed[j] = "UTurn";
                                    j++;
                                    removeCard(givenCards, "UTurn", 1);
                                } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                    if (givenCards.containsKey("TurnRight")) {
                                        toBePlayed[j] = "TurnRight";
                                        j++;
                                        removeCard(givenCards, "TurnRight", 1);
                                        if (j < 5) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                        }
                                    } else {
                                        toBePlayed[j] = "TurnLeft";
                                        j++;
                                        removeCard(givenCards, "TurnLeft", 1);

                                        if (j < 5) {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);
                                        }

                                    }
                                }
                                if (j < 5) {
                                    toBePlayed[j] = "MoveII";
                                    j++;
                                    removeCard(givenCards, "MoveII", 1);
                                }
                            } else if (givenCards.containsKey("BackUp")) {
                                toBePlayed[j] = "Backup";
                                j++;
                                bestCards.set(i + 1, "MoveI");
                                i--;
                                removeCard(givenCards, "BackUp", 1);
                                if (j < 5) {
                                    if (givenCards.containsKey("UTurn")) {
                                        toBePlayed[j] = "UTurn";
                                        j++;
                                        removeCard(givenCards, "UTurn", 1);
                                    } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                        if (givenCards.containsKey("TurnRight")) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                            if (j < 5) {
                                                toBePlayed[j] = "TurnRight";
                                                j++;
                                                removeCard(givenCards, "TurnRight", 1);
                                            }
                                        } else {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);

                                            if (j < 5) {
                                                toBePlayed[j] = "TurnLeft";
                                                j++;
                                                removeCard(givenCards, "TurnLeft", 1);
                                            }

                                        }
                                    }
                                    i++;

                                }
                            }
                        } else if (i+1 < bestCards.size() && bestCards.get(i + 1).equals("MoveIII")) {
                            if (givenCards.containsKey("MoveIII")) {
                                if (givenCards.containsKey("UTurn")) {
                                    toBePlayed[j] = "UTurn";
                                    j++;
                                    removeCard(givenCards, "UTurn", 1);
                                } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                    if (givenCards.containsKey("TurnRight")) {
                                        toBePlayed[j] = "TurnRight";
                                        j++;
                                        removeCard(givenCards, "TurnRight", 1);
                                        if (j < 5) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                        }
                                    } else {
                                        toBePlayed[j] = "TurnLeft";
                                        j++;
                                        removeCard(givenCards, "TurnLeft", 1);

                                        if (j < 5) {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);
                                        }

                                    }
                                }
                                if (j < 5) {
                                    toBePlayed[j] = "MoveIII";
                                    j++;
                                    removeCard(givenCards, "MoveIII", 1);
                                }
                            } else if (givenCards.containsKey("BackUp")) {
                                toBePlayed[j] = "BackUp";
                                j++;
                                bestCards.set(i + 1, "MoveII");
                                i--;
                                removeCard(givenCards, "BackUp", 1);
                                if (j < 5) {
                                    if (givenCards.containsKey("UTurn")) {
                                        toBePlayed[j] = "UTurn";
                                        j++;
                                        removeCard(givenCards, "UTurn", 1);
                                    } else if ((givenCards.containsKey("TurnRight") && givenCards.get("TurnRight") > 1) || (givenCards.containsKey("TurnLeft") && givenCards.get("TurnLeft") > 1)) {
                                        if (givenCards.containsKey("TurnRight")) {
                                            toBePlayed[j] = "TurnRight";
                                            j++;
                                            removeCard(givenCards, "TurnRight", 1);
                                            if (j < 5) {
                                                toBePlayed[j] = "TurnRight";
                                                j++;
                                                removeCard(givenCards, "TurnRight", 1);
                                            }
                                        } else {
                                            toBePlayed[j] = "TurnLeft";
                                            j++;
                                            removeCard(givenCards, "TurnLeft", 1);

                                            if (j < 5) {
                                                toBePlayed[j] = "TurnLeft";
                                                j++;
                                                removeCard(givenCards, "TurnLeft", 1);
                                            }

                                        }
                                    }
                                    i++;

                                }
                            }
                        }
                    }
                } else if (j == 4 || (i+1 < bestCards.size() && !bestCards.get(i + 1).contains("Move"))) {
                    if (givenCards.containsKey("UTurn")) {
                        toBePlayed[j] = "UTurn";
                        j++;
                        removeCard(givenCards, "UTurn", 1);
                    } else if (givenCards.containsKey("TurnRight")) {
                        if (givenCards.get("TurnRight") > 1) {
                            toBePlayed[j] = "TurnRight";
                            j++;
                            removeCard(givenCards, "TurnRight", 1);
                            if (j < 5) {
                                toBePlayed[j] = "TurnRight";
                                j++;
                                removeCard(givenCards, "TurnRight", 1);
                            }
                        } else {
                            toBePlayed[j] = "TurnRight";
                            j++;
                            removeCard(givenCards, "TurnRight", 1);

                            String random = chooseRandom(givenCards,j);
                            toBePlayed[j] = random;
                            removeCard(givenCards, random, 1);
                            j++;

                        }
                    } else if (givenCards.containsKey("TurnLeft")) {
                        if (givenCards.get("TurnLeft") > 1) {
                            toBePlayed[j] = "TurnLeft";
                            j++;
                            removeCard(givenCards, "TurnLeft", 1);
                            if (j < 5) {
                                toBePlayed[j] = "TurnLeft";
                                j++;
                                removeCard(givenCards, "TurnLeft", 1);
                            }
                        } else {
                            toBePlayed[j] = "TurnLeft";
                            j++;
                            removeCard(givenCards, "TurnLeft", 1);

                            String random = chooseRandom(givenCards,j);
                            toBePlayed[j] = random;
                            removeCard(givenCards, random, 1);
                            j++;
                        }
                    }
                }
            }else {
                String play = bestCards.get(i);
                if (givenCards.containsKey(play)) {
                    toBePlayed[j] = play;
                    j++;
                    removeCard(givenCards, play, 1);
                } else {
                    String random = chooseRandom(givenCards,j);
                    toBePlayed[j] = random;
                    removeCard(givenCards, random, 1);
                    j++;
                }
            }

            if (j == 5) {
                break;
            }
            int f = getTempOrientation() / 13;
            int g = getTempOrientation() - f * 13;

            FieldObject field = getFullMap()[f][g];
            if ((field.getType().toLowerCase().contains("belt") && field.getSpeed()==2)|| field.getType().toLowerCase().contains("gear")) {
                if(field.getType().toLowerCase().contains("gear")){
                    setTempOrientation(checkGear(field, getTempOrientation()));
                }else {
                    Pair<Integer, Integer> pair = getQlearning().blueBeltMovementBot(getTempOrientation(), getTempOrientation());
                    if (pair.getKey() != -1) {
                        setExpectedPosition(pair.getKey());
                        setTempOrientation(pair.getValue());
                    }
                }
                ArrayList<String> tempCards = new ArrayList<>();
                do {
                    bestCards = bestCards(tempCards, getExpectedPosition());
                    getLogger().debug("Before checking cards got from bestCards(): " + bestCards.toString());
                    getLogger().debug("Round " + f++ + " of choosing Cards");
                    bestCards = checkMoves(tempCards);
                    getLogger().debug("After checking cards got from bestCards(): " + bestCards.toString());
                } while (tempCards.size() < 5 && !getReached());
                int h = 0;
                for (int k = i + 1; k < bestCards.size(); k++) {
                    bestCards.set(k, tempCards.get(h));
                    h++;
                }
            }
            for (int k = 0; k < toBePlayed.length; k++) {
                if (toBePlayed[k] == null) {
                    String random = chooseRandom(givenCards,j);
                    toBePlayed[k] = random;
                    removeCard(givenCards, random, 1);
                }
            }
        }

        return toBePlayed;
    }

    /**
     * chooseRandom():
     * chooses a random card from the given HashMap
     *
     * @param givencards : Hashmap containing the dealt cards
     * @param i : register no.
     * @return a random card
     */
    private String chooseRandom(HashMap<String, Long> givencards,int i) {
        if (givencards.containsKey("PowerUp")) {
            return "PowerUp";
        } else if (givencards.containsKey("BackUp")) {
            return "BackUp";
        } else {
            Random random = new Random();
            String card = (String) givencards.keySet().toArray()[random.nextInt(givencards.keySet().size())];
            if(i==0){
                while(card.equals("Again"))
                    card = (String) givencards.keySet().toArray()[random.nextInt(givencards.keySet().size())];
            }
            getLogger().debug("Chose random card: " + card);
            return card;
        }
    }

    /**
     * removeCard():
     * removes "count" number of cards from the given Hashmap
     * and callssetPositionAndOrientation()
     * @param givenCards: cards dealt to player
     * @param card:       name of the card to be removed
     * @param count:      quantity need to be removed
     * @return Hashmap after modification
     */
    private HashMap<String, Long> removeCard(HashMap<String, Long> givenCards, String card, long count) {
        getLogger().debug("Card to be removed: " + card + " From: " + givenCards.keySet().toString());
        if (givenCards.get(card) == count) {
            givenCards.remove(card);
        } else {
            givenCards.put(card, givenCards.get(card) - count);
        }
        setPositionAndOrientation(card,toIntExact(count));

        getLogger().debug("Removed " + card + " from givenCards");
        return givenCards;

    }

    /**
     * setPositionAndOrientation():
     * updates tempPosition and tempOrientation accordingly
     * @param card: card played
     * @param count: quantity
     */
    private void setPositionAndOrientation(String card, int count){
        int or=getTempOrientation();
        switch(or) {
            case 1:
                switch (card) {
                    case "MoveI":
                        setExpectedPosition(getExpectedPosition() - 13 * count);
                        break;
                    case "MoveII":
                        setExpectedPosition(getExpectedPosition() - 26 * count);
                        break;
                    case "MoveIII":
                        setExpectedPosition(getExpectedPosition() - 39 * count);
                        break;
                    case "BackUp":
                        setExpectedPosition(getExpectedPosition() + 13 * count);
                        break;
                    case "TurnLeft":
                        setTempOrientation(5-1*count);
                        break;
                    case "TurnRight":
                        setTempOrientation(or+count);
                        break;
                    case "UTurn":
                        setTempOrientation(3);
                        break;
            }
            break;
            case 2:
                switch (card) {
                    case "MoveI":
                        setExpectedPosition(getExpectedPosition() + 1 * count);
                        break;
                    case "MoveII":
                        setExpectedPosition(getExpectedPosition() + 2 * count);
                        break;
                    case "MoveIII":
                        setExpectedPosition(getExpectedPosition() + 3 * count);
                        break;
                    case "BackUp":
                        setExpectedPosition(getExpectedPosition() - 1 * count);
                        break;
                    case "TurnLeft":
                        setTempOrientation(or-count);
                        break;
                    case "TurnRight":
                        setTempOrientation(or+count);
                        break;
                    case "UTurn":
                        setTempOrientation(4);
                        break;
                }
                break;
            case 3:
                switch (card) {
                    case "MoveI":
                        setExpectedPosition(getExpectedPosition() + 13 * count);
                        break;
                    case "MoveII":
                        setExpectedPosition(getExpectedPosition() + 26 * count);
                        break;
                    case "MoveIII":
                        setExpectedPosition(getExpectedPosition() + 39 * count);
                        break;
                    case "BackUp":
                        setExpectedPosition(getExpectedPosition() - 13 * count);
                        break;
                    case "TurnLeft":
                        setTempOrientation(or*count);
                        break;
                    case "TurnRight":
                        setTempOrientation(or+count);
                        break;
                    case "UTurn":
                        setTempOrientation(1);
                        break;
                }
                break;
            case 4:
                switch (card) {
                    case "MoveI":
                        setExpectedPosition(getExpectedPosition() - 1 * count);
                        break;
                    case "MoveII":
                        setExpectedPosition(getExpectedPosition() - 2 * count);
                        break;
                    case "MoveIII":
                        setExpectedPosition(getExpectedPosition() - 3 * count);
                        break;
                    case "BackUp":
                        setExpectedPosition(getExpectedPosition() + 1 * count);
                        break;
                    case "TurnLeft":
                        setTempOrientation(or-1*count);
                        break;
                    case "TurnRight":
                        setTempOrientation(0+count);
                        break;
                    case "UTurn":
                        setTempOrientation(2);
                        break;
                }
                break;

        }
    }

    /**
     * checkMoves():
     * combines Move Cards
     *
     * @param bestCards List of cards to be checked
     * @return compressed List of Cards
     */
    private static List<String> checkMoves(List<String> bestCards) {
        List<String> checkedMoves = new ArrayList<>();
        for (int i = 0; i < bestCards.size() - 1; i++) {
            if (bestCards.get(i).equals("MoveI")) {
                if (bestCards.get(i).equals(bestCards.get(i + 1))) {
                    if ((i + 2 < bestCards.size()) && bestCards.get(i + 2).equals("MoveI")) {

                        checkedMoves.add("MoveIII");
                        bestCards.set(i, "MoveIII");
                        bestCards.set(i + 1, null);
                        bestCards.set(i + 2, null);
                        i += 2;
                        bestCards = reorder(bestCards);
                        getLogger().debug("Combined 3 MoveI cards");

                    } else {
                        checkedMoves.add("MoveII");
                        bestCards.set(i, "MoveII");
                        bestCards.set(i + 1, null);
                        i += 1;
                        bestCards = reorder(bestCards);
                        getLogger().debug("Combined 2 MoveI cards");
                    }
                }
            } else if (bestCards.get(i).equals("MoveII") && bestCards.get(i + 1).equals("MoveI")) {
                bestCards.set(i, "MoveIII");
                bestCards.set(i + 1, null);
                i++;
                bestCards = reorder(bestCards);
                getLogger().debug("Combined MoveII and MoveI cards");
            }
        }
        return bestCards;
    }

    /**
     * reorder():
     * called by checkMoves after replacing MoveI cards
     * to remove nulls from bestCards list
     *
     * @param bestCards
     * @return reorder list
     */
    private static List<String> reorder(List<String> bestCards) {
        for (int i = 0; i < bestCards.size(); i++) {
            if (bestCards.get(i) == null) {
                bestCards.remove(i);
                i--;
            }
        }
        return bestCards;
    }

    /**
     * bestCards():
     * chooses best cards to be played based on the bestPath
     * provided by QLearning.
     * called by chooseCards()
     *
     * @param chosen:   cards chosen till this point
     * @param position: starting position to choose card
     * @return
     */
    private List<String> bestCards(List<String> chosen, int position) {
        List<String> cards = chosen;
        LinkedList<Integer> bestPath = getQlearning().getBestPath(position);
        getLogger().debug("Got best path: " + bestPath.toString());
        ListIterator iterator = bestPath.listIterator(1);

        getLogger().debug("Orientation: " + getTempOrientation());
        int currentPosition = position;
        int nextPosition = (int) iterator.next();
        int i = cards.size();
        while (i < 5 && nextPosition != -1 && cards.size() < 5) {
            //MoveI
            if (nextPosition == currentPosition + 1) { //right
                if (getTempOrientation() == 2) {
                    cards.add("MoveI");
                    i++;
                    currentPosition = nextPosition;
                    if (iterator.hasNext()) {
                        nextPosition = (int) iterator.next();
                    } else {
                        nextPosition = -1;
                    }
                } else if (getTempOrientation() == 1) {
                    cards.add("TurnRight");
                    setTempOrientation(2);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 3) {
                    cards.add("TurnLeft");
                    setTempOrientation(2);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }

                } else if (getTempOrientation() == 4) {
                    cards.add("UTurn");
                    setTempOrientation(2);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                }
            } else if (nextPosition == currentPosition - 1) { //left
                if (getTempOrientation() == 4) {
                    cards.add("MoveI");
                    i++;
                    currentPosition = nextPosition;
                    if (iterator.hasNext()) {
                        nextPosition = (int) iterator.next();
                    } else {
                        nextPosition = -1;
                    }
                } else if (getTempOrientation() == 3) {
                    cards.add("TurnRight");
                    setTempOrientation(4);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 1) {
                    cards.add("TurnLeft");
                    setTempOrientation(4);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 2) {
                    cards.add("UTurn");
                    setTempOrientation(4);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                }
            } else if (nextPosition == currentPosition + 13) { //down
                if (getTempOrientation() == 3) {
                    cards.add("MoveI");
                    i++;
                    currentPosition = nextPosition;
                    if (iterator.hasNext()) {
                        nextPosition = (int) iterator.next();
                    } else {
                        nextPosition = -1;
                    }
                } else if (getTempOrientation() == 2) {
                    cards.add("TurnRight");
                    setTempOrientation(3);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 4) {
                    cards.add("TurnLeft");
                    setTempOrientation(3);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 1) {
                    cards.add("UTurn");
                    setTempOrientation(3);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                }
            } else if (nextPosition == currentPosition - 13) { //up
                if (getTempOrientation() == 1) {
                    cards.add("MoveI");
                    i++;
                    currentPosition = nextPosition;
                    if (iterator.hasNext()) {
                        nextPosition = (int) iterator.next();
                    } else {
                        nextPosition = -1;
                    }
                } else if (getTempOrientation() == 4) {
                    cards.add("TurnRight");
                    setTempOrientation(1);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 2) {
                    cards.add("TurnLeft");
                    setTempOrientation(1);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                } else if (getTempOrientation() == 3) {
                    cards.add("UTurn");
                    setTempOrientation(1);
                    i++;
                    if (i < 4) {
                        cards.add("MoveI");
                        i++;
                        currentPosition = nextPosition;
                        if (iterator.hasNext()) {
                            nextPosition = (int) iterator.next();
                        } else {
                            nextPosition = -1;
                        }
                    }
                }
            }
            getLogger().debug("bestCards() chose: " + cards.get(i - 1));

        }
        if (nextPosition == -1) {
            setReached(true);
        }
        setExpectedPosition(currentPosition);
        return cards;
    }

    /**
     * checkMessage():
     * checks messages sent to player that are relevant to the bot.
     *
     * @param type: message type
     * @param body: message body
     */
    public void checkMessage(String type, JSONObject body) {
        switch (type) {
            case "CurrentPlayer":
                int id = Integer.parseInt(body.get("playerID").toString());
                if (id == userID && activePhase == 0) {
                    chooseStartingPoint();
                }
                break;
            case "ActivePhase":
                int phase = Integer.parseInt(body.get("phase").toString());
                activePhase = phase;
                if (phase == 3)
                    setPlayedCards(false);
                break;
            case "SelectionFinished":
                int iD = Integer.parseInt(body.get("playerID").toString());
                if (iD != userID && !playedCards) {
                    playCards();
                }
                break;
            case "CheckpointReached":
                if (cpIterator != null && cpIterator.hasNext()) {
                    checkpoint = (Integer) cpIterator.next();
                    setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));
                } else {
                    reached = true;
                    allReached = true;
                }
                break;
            case "SelectMap":

                ((JSONArray) body.get("availableMaps")).get(0);
                write.selectMap((String) ((JSONArray) body.get("availableMaps")).get(0));
                break;
            case "MapSelected":
                JSONArray JArray = (JSONArray) body.get("map");
                mapType = JArray.get(0).toString();
                currentGameField = mapType.equals("DizzyHighway") ? (new DizzyHighway()).returnGamefield() : (new ExtraCrispy()).returnGamefield();
                startingBoard = mapType.equals("DizzyHighway") ? (new StartDizzy()).returnStartfield() : (new StartCrispy()).returnStartfield();
                buildMap(currentGameField, startingBoard);
                break;

            case "CurrentCards":
                write.playIt();
                break;
            case "PickDamage":
                int count = Integer.parseInt(body.get("count").toString());
                selectDamage(count);
                break;
        }
    }

    /**
     * selectDamage():
     * selects damage when pickDamage is received
     * @param count: number of cards to be chosen
     */
    private void selectDamage(int count) {
        String[] dCards = new String[count];
        Random random = new Random();
        int j;
        for (int i = 0; i < count; i++) {
            j = random.nextInt(4);
            switch (j){
                case 0 :
                    dCards[i] = "Trojan";
                    break;
                case 1 :
                    dCards[i] ="Spam";
                    break;
                case 2 :
                    dCards[i] ="Virus";
                    break;
                case 3 :
                    dCards[i] ="Worm";
                    break;
            }
        }
        write.selectDamage(dCards);

    }


    /**
     * jsonToArray():
     *
     * @param length: board length
     * @param width:  board width
     * @param json:   json object of the board to be extracted
     * @return FieldObject 2D Array representing the board
     */
    public FieldObject[][] jsonToArray(int length, int width, JSONObject json) {
        FieldObject[][] map = new FieldObject[length][width];
        int k = 0;
        ExtractJSON fileExtract = new ExtractJSON(json);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = fileExtract.extractMap(k++);
            }
        }
        return map.clone();
    }

    /**
     * reboot():
     * changes robot's position to the reboot field.
     * Called when a reboot message is receiced
     *
     * @author Shady Mansour
     */
    public void reboot() {
        Integer[] mod = {0, 1, 2};
        List<Integer> mods = new ArrayList<>(Arrays.asList(mod));
        setPosition(mapType.equals("DizzyHighway") ? (mods.contains(position%13) ? startingPoint : rebootField) : rebootField);
        setOrientation(1);
    }

    /**
     * checkGear():
     *
     * @return orientation after gear rotation
     * @author Shady Mansour
     */
    private int checkGear(FieldObject gear, int pOrientation){
        int gearRotation = getQlearning().calculateDirection(gear.getOrientation());
        int orientation=pOrientation;
        if (gearRotation == 2) {
            if (pOrientation == 4) {
                orientation =1;
            } else {
                orientation++;
            }
        } else {                      //left
            if (pOrientation == 1) {
                orientation = 4;
            } else {
                orientation--;
            }
        }
        return orientation;
    }


    //getters and setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int id) {
        userID = id;
    }


    public void setStartingPoint(int sP) {
        startingPoint = sP;
        position = startingPoint;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setOrientation(String direction) {
        if (direction.equalsIgnoreCase("clockwise")) {
            if (orientation == 4/*4*/) {
                orientation = 1;
            } else {
                orientation += 1;
            }
        } else {
            if (orientation == 1 /*1*/) {
                orientation = 4;
            } else {
                orientation -= 1;
            }
        }
    }

    public static Logger getLogger() {
        return logger;
    }


    public QLearning getQlearning() {
        return qlearning;
    }

    public void setQlearning(QLearning qlearning) {
        this.qlearning = qlearning;
    }


    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getActivePhase() {
        return activePhase;
    }

    public void setActivePhase(int activePhase) {
        this.activePhase = activePhase;
    }

    public List<Integer> getTakenStartingPoints() {
        return takenStartingPoints;
    }


    public Boolean getReached() {
        return reached;
    }

    public void setReached(Boolean reached) {
        this.reached = reached;
    }



    public void setPlayedCards(Boolean playedCards) {
        this.playedCards = playedCards;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public int getExpectedPosition() {
        return expectedPosition;
    }

    public void setExpectedPosition(int expectedPosition) {
        this.expectedPosition = expectedPosition;
    }

    public int getTempOrientation() {
        return tempOrientation;
    }

    public void setTempOrientation(int tempOrientation) {
        this.tempOrientation = tempOrientation;
    }

    public String[] getCardsToBePlayed() {
        return cardsToBePlayed;
    }

    public void setCardsToBePlayed(String[] cardsToBePlayed) {
        this.cardsToBePlayed = cardsToBePlayed;
    }


    public FieldObject[][] getFullMap() {
        return fullMap;
    }

    public void setFullMap(FieldObject[][] fullMap) {
        this.fullMap = fullMap;
    }

}
