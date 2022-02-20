package game.gameLogic;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damageCards.*;
import game.gameObjects.cards.programmingCards.regularProgrammingCards.Again;

import game.gameObjects.cards.programmingCards.regularProgrammingCards.*;
import game.gameObjects.gamefield.*;

import game.gameObjects.otherObjects.CardDeck;
import game.gameObjects.otherObjects.DamageCardDecks;
import game.gameObjects.otherObjects.Register;
import game.gameObjects.otherObjects.SandTimer;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;
import networking.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Game
 * This class contains the game logic for roborally.
 */
public class Game extends Thread {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Game");
    // variables
    public ArrayList<Integer> players = new ArrayList<Integer>();
    private int numPlayers;
    private CardDeck upgradeShopDeck;

    private String maptype;
    private int currentID;

    private int currentPlayer;
    private PlayerThread currentPlayerThread;
    private int activePhase;

    private SandTimer timer;
    private boolean timesOver;
    private int round;
    private ArrayList<Integer> startingPoints;

    private JSONObject currentGameField = new JSONObject();
    private JSONObject currentStartingBoard = new JSONObject();
    private FieldObject[][] wholeMap = new FieldObject[13][10];

    private ArrayList<FieldObject> blueBelts = new ArrayList<>();
    private ArrayList<FieldObject> greenBelts = new ArrayList<>();

    private ArrayList<FieldObject> gears = new ArrayList<>();
    private ArrayList<FieldObject> pushPanels = new ArrayList<>();       //es gibt keine

    private ArrayList<FieldObject> controlPoints = new ArrayList<>();
    private HashMap<FieldObject, Boolean> energySpaces = new HashMap<>();
    //            <energySpace, hasEnergy
    private HashMap<Integer, Integer> lasers = new HashMap<>();
    //            <Position, orientation>
    private ArrayList<FieldObject> obstacles = new ArrayList<>();
    private FieldObject restartPoint;            // new FieldObject(7,3,"RestartPoint");

    ArrayList<Integer> playerIDsOrder = new ArrayList<>();      //an ArrayList that contains the playerIDs sorted by the order

    private boolean gameIsOver;
    private Server server;
    private int drawnCard;
    private boolean startingPointTaken;

    private DamageCardDecks damageCardDecks;
    private Boolean allDone;

    /**
     * Constructor:
     * A new game is initiated
     *
     * @param server: the server on which the game is created.
     */
    public Game(Server server) {
        this.server = server;
        this.startingPointTaken = false;
        this.startingPoints = new ArrayList<>();
        this.timesOver = false;
        startingPoints.add(14);
        startingPoints.add(39);
        startingPoints.add(53);
        startingPoints.add(66);
        startingPoints.add(78);
        startingPoints.add(105);

        this.damageCardDecks = new DamageCardDecks();
        damageCardDecks.initiateDecks();

    }
    int counter = 0;

    /**
     * run():
     * all the magic happens here. This method contains the game's logic.
     *  @author Vivien Pfeiffer, Franziska Leitmeir
     */
    public void run() {
        initializeGameField();

        server.fillFieldObjects(wholeMap);

        initializePosition();

        buildingPhase();
        counter = 0;
        boolean everyoneFinished;
        while (!gameIsOver) {
            initializePosition();
            programmingPhase();
            everyoneFinished = false;
            timesOver = false;
            synchronized (this) {

                while (!timesOver && !everyoneFinished) {// ^ = XOR
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    everyoneFinished = everyoneFinished();
                }
            }

            activationPhase();
            everyoneNotFinished();
        }
    }

    /**
     * everyoneNotFinished():
     * sets all players' selectionFinished boolean to false
     *
     * @author Shady Mansour
     */
    public void everyoneNotFinished() {
        PlayerThread thread;
        for (Integer player : players) {
            thread = server.getClients().get(player);
            thread.setSelectionFinished(false);
        }
    }

    /**
     * checks if every player is finished and returns true if so
     * everyoneFinished():
     * @author Vincent Oeller
     */
    public boolean everyoneFinished() {
        boolean everyone = true;
        for (Integer player : players) {
            PlayerThread thread = server.getClients().get(player);
            everyone = everyone && thread.getSelectionFinished();
        }

        if (everyone && timer != null) {
            timer.cancel();

            this.timer = null;
            logger.info("Timer Cancelled");
        }
        return everyone;
    }

    int i = 1;
    /**
     * placeOnRegisters():
     * Puts card on register. Once the 5th register is filled, a selectionFinished message is sent to all       players
     * and if a timer is not running, a new timer will be started.
     *
     * @param register: register on which the card should be placed
     * @param card: card to be placed.
     * @param player: the player placing the card
     */
    public void placeOnRegister(int register, Card card, PlayerThread player) {

        player.getRegisters().get(register - 1).setCard(card);

        player.getEmptyRegisters().set(register - 1, card == null);
        JSONObject cardselected = new JSONObject();
        cardselected.put("playerID", player.getUserId());
        cardselected.put("register", register);
        server.broadcast("CardSelected", cardselected, null);


        if (!player.getEmptyRegisters().contains(true)) {

            if (timer == null) {
                timer = new SandTimer();
                timer.startTimer(player);
                logger.info("Game's timer no." + i + " started");
                i++;
            }

            player.setSelectionFinished(true);

            synchronized (this) {
                notifyAll();
            }

            for(Register reg : player.getRegisters()){
                Card car = reg.getCard();
                if (car instanceof DamageCards){
                    for (int j = 0; j < player.getDrawnCards().length; j++) {
                        if(player.getDrawnCards()[j] !=null && player.getDrawnCards()[j].toString().equals(car.toString())) {
                            player.setOneCard(j);
                            damageCardDecks.add(car.toString());
                            break;
                        }
                    }
                }
            }
            JSONObject msgBody = new JSONObject();
            msgBody.put("playerID", player.getUserId());
            server.broadcast("SelectionFinished", msgBody, null);
        }
    }

    /**
     * puts all 9 Programming cards the discardPile after the 5th register
     * has been played, damageCards are put in their damageCardPiles
     *
     * @param player PlayerThread
     * @author Vincent Oeller
     */
    public void discardRemainingCards(PlayerThread player) {
        Card[] drawnCards = player.getDrawnCards();     //hier sind alle 9 Karten drinnen

        for (Card card : drawnCards) {
            if (card != null) {
                player.getDiscardedPile().add(card);
            }
        }
        //resets drawn cards
        Arrays.fill(player.getDrawnCards(), null);

    }

    /**
     * discardHand():
     * empties player's registers and discards the cards in the discarded pile.
     * when the player couldn't fill his registers in time.
     *
     * @param player player who the timer ran out on during card selection
     * @author Shady Mansour
     */
    public void discardHand(PlayerThread player) {
        ArrayList<String> cardsInRegisters = new ArrayList<>();
        for (Register register : player.getRegisters()) {
            if (register.getCard() != null)
                cardsInRegisters.add(register.getCard().toString());
        }
        Card[] drawnCards = player.getDrawnCards();
        for (int i = 0; i < drawnCards.length; i++) {
            if (drawnCards[i] != null) {
                if (cardsInRegisters.contains(drawnCards[i].toString())) {
                    cardsInRegisters.remove(drawnCards[i].toString());
                } else {
                    player.getDiscardedPile().add(drawnCards[i]);
                    player.setOneCard(i);
                }

            }
        }
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", player.getUserId());
        server.broadcast("DiscardHand", msgBody, null);
        logger.debug("Discarded player " + player.getUserId() + "'s hand");
        drawCards(player, true);
    }

    /**
     * discardCard():
     * removes a card from the register and adds it to the discardedPile
     *
     * @param register: the register that contains the card
     * @author Shady Mansour
     */
    public void discardCard(Register register, PlayerThread player) {
        register.setCard(null);
    }

    /**
     * drawCard():
     * The method gives the current player cards from his card deck
     *
     * @param player: player who'll be dealt the cards
     * @param force:  true if timer ended. player is given 5 cards to be added to his registers
     * @author Shady Mansour
     **/
    public void drawCards(PlayerThread player, boolean force) {
        if (!force) {
            Card[] drawnCards = new Card[9];
            for (int i = 0; i < 9; i++) {
                drawnCards[i] = player.getProgrammingCardDeck().pop();
            }

            player.setDrawnCards(drawnCards);
        } else {
            Card[] drawnCards = new Card[5];
            for (int i = 0; i < 5; i++) {
                if (player.getRegisters().get(i).getCard() == null) {
                    Card card = player.getProgrammingCardDeck().pop();
                    if (i == 0 && card instanceof Again) {
                        player.getProgrammingCardDeck().add(card);
                        i--;
                        continue;
                    }
                    drawnCards[i] = card;
                    logger.debug("Player " + player.getUserId() + " got a forced hand.");
                } else {
                    drawnCards[i] = player.getRegisters().get(i).getCard();
                }
            }
            player.fillRegisters(drawnCards);
        }
    }

    /**
     * initializePosition():
     * gives every player a position in the game
     */
    public void initializePosition() {
        currentPlayer = 0;
        currentID = players.get(currentPlayer);
        currentPlayerThread = server.getClients().get(currentID);

    }


    /**
     * initializeGameField():
     * extracts the JSONObjects currentGameField and currentStartingBoard and combines them to one big map
     *
     * @author Franziska Leitmeir
     */
    public void initializeGameField() {
        FieldObject[][] gameField = extractBoard(currentGameField, 10, 10);
        logger.debug("Extracted GameField");
        FieldObject[][] startingField = extractBoard(currentStartingBoard, 3, 10);
        logger.debug("Extracted StartingBoard");

        for (int i = 0; i < 13; i++) {                                                          //x
            for (int j = 0; j < 10; j++) {                                                      //y
                if (i < 3) {
                    wholeMap[i][j] = startingField[i][j];
                } else {
                    wholeMap[i][j] = gameField[i - 3][j];
                    wholeMap[i][j].setX(i);
                }
            }
        }
        logger.debug("Map Filled");
    }

    /**
     * giveCards():
     * calls drawCards() for every player
     * @author Vivien Pfeiffer
     */
    public void giveCards() {
        for (int playerID : players) {
            PlayerThread player = server.getClients().get(playerID);
            drawCards(player, false);
            logger.info("Player " + playerID + " was dealt his cards");
        }
    }

    /**
     * currentPlayer():
     * sends the current player id
     *
     * @author Vivien Pfeiffer
     */
    public void currentPlayer() {
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", currentID);
        server.broadcast("CurrentPlayer", msgBody, null);
        logger.info("Current Player: " + currentID);

    }

    /**
     * extractBoard():
     * extracts the JSON-file into a FieldObject[][]
     *
     * @param currentBoard: the board, which should be extracted
     * @param width:        width of the board
     * @param height:       height of the board
     * @return the extracted Board as FieldObject[][]
     * @author Franziska Leitmeir
     */
    private FieldObject[][] extractBoard(JSONObject currentBoard, int width, int height) {
        FieldObject[][] extractedBoard = new FieldObject[width][height];
        ExtractJSON fileExtract = new ExtractJSON(currentBoard);

        for (int i = 0; i < (width * height); i++) {

            FieldObject field = fileExtract.extractMap(i);
            int x = field.getX();
            if (width == 3) {
                x = x + 3;
            }

            int y = field.getY();
            String t = field.getType();
            int s = field.getSpeed();
            double o = field.getOrientation();
            ArrayList<String> os = field.getOrientations();
            int c = field.getCounter();
            boolean iC = field.getIsCrossing();

            FieldObject pField = new FieldObject(x, y, t, s, o, os, c, iC);
            extractedBoard[x][y] = pField;
        }
        return extractedBoard;
    }

    /**
     * getNextPlayer():
     * gets the next player and sets currentPlayer, currentID and currentPlayerThread
     *
     * @return next player's id
     * @author Shady Mansour
     */
    public int getNextPlayer() {
        if (currentPlayer == numPlayers - 1) currentPlayer = -1;
        currentPlayer++;
        currentID = players.get(currentPlayer);
        currentPlayerThread = server.getClients().get(currentID);
        logger.debug("Got next Player: " + currentID);
        return currentID;
    }

    /**
     * activePhase():
     * current phase
     *
     * @author Vivien Pfeiffer
     */
    public void activePhase() {
        JSONObject msgBody = new JSONObject();
        msgBody.put("phase", getActivePhase());
        server.broadcast("ActivePhase", msgBody, null);
        logger.info("Phase " + activePhase + " started");
    }

    /**
     * buildingPhase():
     * method checks if currentPlayer set his starting point
     * if the player did, it's the next players turn to choose his starting point
     *
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void buildingPhase() {
        activePhase = 0;
        activePhase();
        while (counter < players.size()) {
            currentPlayer();

            synchronized (this) {
                while (!startingPointTaken) {
                    logger.debug("Not all startingPoints have been set");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            startingPointTaken = false;
            counter++;
            getNextPlayer();
            logger.info("All players chose there starting points");
        }
        counter = 0;
    }

    /**
     * upgradePhase():
     * not necessary
     */
    @Deprecated
    public void upgradePhase() {
        activePhase = 1;
        activePhase();
    }

    /**
     * programmingPhase():
     * the players get their cards
     */
    public void programmingPhase() {
        priotityAntenna();
        activePhase = 2;
        activePhase();
        giveCards();
    }

    /**
     * activationPhase():
     * the effect of the cards in the player's register will be called
     * after the 5th register pickDamage will be called to get the damage cards
     * after that the registers will be cleared for the next round
     *
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void activationPhase() {
        activePhase = 3;
        activePhase();
        for (int i = 1; i <= 5; i++) {
            round = i;
            logger.debug("Round " + round + " started");
            currentCards();

            allDone = false;
            synchronized (this) {
                while (!allDone) {
                    logger.debug("Some players didn't send playIt. Will wait! ");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    allDone = checkPlayIt();
                }

            }
            for (Integer playerID : playerIDsOrder) {
                PlayerThread player = server.getClients().get(playerID);
                Register register = player.getRegisters().get(round - 1);
                Card currentCard = register.getCard();
                if (currentCard == null) {
                    continue;
                }
                logger.debug("Player \"" + playerID + "\" played the " + currentCard.toString() + " card.");
                currentCard.effect(player, player.getRobot(), round);
            }
            blueBeltMovement();
            logger.debug("All blue belts were moved");
            greenBeltMovement();
            logger.debug("all green belts were moved");
            gearRotation();
            logger.debug("All gears were rotated");

            calculateLaserPosition();
            logger.debug("All lasers' positions were calculated");
            shootAllLasers();
            logger.debug("All lasers were shot");

            energySpacesEffect();
            logger.debug("All energy cubes were given");
            checkPointReached();
            logger.debug("Checked all checkpoints");

            priotityAntenna();

            //when the 5th register was played, pickDamage is called for every Player and all
            //damageCards you got during Activationphase will be sent to Client

            if (round == 5) {
                for (Integer playerID : players) {
                    PlayerThread player = server.getClients().get(playerID);
                    if (!player.getDrawDamage().isEmpty()) {
                        player.drawDamage(playerID, player.getDrawDamage());
                    }
                    if (!damageCardDecks.deckIsEmpty("all")) {
                        int pickDamageCounter = player.getPickDamage();

                        while (pickDamageCounter != 0) {

                            synchronized (this) {
                                while (!player.getDamageSelected()) {
                                    player.pickDamage(pickDamageCounter);

                                    try {
                                        this.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    pickDamageCounter = player.getPickDamage();
                                }
                            }
                        }
                        player.setDamageSelected(false);
                    }
                    discardRemainingCards(player);
                    logger.debug("Discarded player " + playerID + "'s cards");
                    player.getRobot().setIsRebooting(false);
                    logger.debug("Player " + playerID + " isRebooting -> false");
                }
            }
        }
        round = 1;
        for (Integer playerID : players) {
            PlayerThread player = server.getClients().get(playerID);
            ArrayList<Register> registers = player.getRegisters();
            for (Register register : registers) {
                register.setCard(null);
            }
            logger.debug("Resetted player " + playerID + "'s registers");
            player.resetEmptyRegisters();
            logger.debug("Resetted player " + playerID + "'s emptyRegisters");


        }
    }

    /**
     * currentCards():
     * shows the current cards in your register
     *
     * @author Vivien Pfeiffer
     */
    public void currentCards() {
        JSONObject msgBody = new JSONObject();
        JSONArray activeCards = new JSONArray();
        JSONObject jCard = new JSONObject();
        for (Integer playerID : players) {
            Register register = server.getClients().get(playerID).getRegisters().get(round - 1);
            Card card = register.getCard();
            if (card == null) {
                continue;
            }
            jCard.put("playerID", playerID);
            jCard.put("card", card.toString());
            activeCards.put(jCard);
            logger.debug("Player " + playerID + "'s current Card: " + card.toString());
        }
        msgBody.put("activeCards", activeCards);
        server.broadcast("CurrentCards", msgBody, null);

    }

    /**
     * checkPlayIt():
     * checks if all players sent playIt.
     * @return true if all players sent playIt
     * @author Shady Mansour
     */
    public Boolean checkPlayIt() {
        Boolean allDone = true;
        for (Integer player : players) {
            PlayerThread thread = server.getClients().get(player);
            allDone = allDone && thread.getPlayIt();
        }
        if (allDone) {
            logger.debug("All players sent playIt");
            for (Integer player : server.getClients().keySet()) {
                server.getClients().get(player).setPlayIt(false);
            }
            logger.debug("All players' playIt set -> false");

        }
        return allDone;
    }


    /**
     * addPlayer():
     * adds a player to the game.
     *
     * @param playerID: the userID of the player to be added.
     * @author Shady Mansour
     */
    public void addPlayer(int playerID) {
        players.add(playerID);
        logger.debug("New player added: ID:" + playerID);
        numPlayers++;
        logger.debug("Number of players: " + numPlayers);
    }

    /**
     * removePlayer():
     * removes a player from the game.
     *
     * @param playerID: the userID of the player to be removed.
     * @author Shady Mansour
     */
    public void removePlayer(int playerID) {
        players.remove(playerID);
        logger.debug("Removed player. ID:" + playerID);
        numPlayers--;
        logger.debug("Number of players: " + numPlayers);
    }


    /**
     * deleteStartingPoints():
     * deletes a starting point from the list of startingpoints
     * @param startingPoint:
     * @author
     */
    public void deleteStartingPoints(int startingPoint) {
        int length = startingPoints.size();

        for (int i = 0; i < length; i++) {
            if (startingPoints.get(i) == startingPoint) {
                startingPoints.remove(i);
                logger.debug("removed" + startingPoint);
                break;
            }
        }

    }

    /**
     * checkWall():
     * checks if there is a wall in front of the player (on the field he stands or on the field he wants to go)
     *
     * @param currentX:    xPosition of the field the player stands at the moment
     * @param currentY:    yPosition of the field the player stands at the moment
     * @param nextX:       xPosition of the field the player wants to go
     * @param nextY:       yPosition of the field the player wants to go
     * @param orientation: the orientation of the robot
     * @return true, if there is a wall in the way
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean checkWall(int currentX, int currentY, int nextX, int nextY, int orientation) {
        if((nextX == 0 && nextY == 4)){
            return true;
        }
        for (int i = 0; i < obstacles.size(); i++) {
            //the current position
            if (obstacles.get(i).getX() == currentX && obstacles.get(i).getY() == currentY) {
                if (obstacles.get(i).getType().contains("Wall")) {
                    if (orientation == 1) {
                        if (obstacles.get(i).getOrientation() == -90) {              //up=-90, right=0, down=90, left = 180
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: -90" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 2) {
                        if (obstacles.get(i).getOrientation() == 0) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 0" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 3) {
                        if (obstacles.get(i).getOrientation() == 90) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 90" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 4) {
                        if (obstacles.get(i).getOrientation() == 180) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 180" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < obstacles.size(); i++) {
            //the desired Position
            if (obstacles.get(i).getX() == nextX && obstacles.get(i).getY() == nextY) {
                if (obstacles.get(i).getType().contains("Wall")) {
                    if (orientation == 1) {
                        if (obstacles.get(i).getOrientation() == 90) {             //up=-90, right=0, down=90, left = 180
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 90" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 2) {
                        if (obstacles.get(i).getOrientation() == 180) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 180" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 3) {
                        if (obstacles.get(i).getOrientation() == -90) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: -90" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    } else if (orientation == 4) {
                        if (obstacles.get(i).getOrientation() == 0) {
                            logger.debug("Wall found: Orientaion: " + orientation + " Wall Orientation: 0" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
                            return true;
                        }
                    }
                }
            }
        }

        logger.debug("Wall NOT found: Orientaion: " + orientation + " Wall Orientation: 0" + " CurrentX: " + currentX + " CurrentY: " + currentY + " nextX: " + nextX + " NextY: " + nextY);
        return false;
    }

    /**
     * checkPit():
     * checks if there is a pit at the next player position
     *
     * @param nextX: xPosition of the field the player wants to go
     * @param nextY: yPosition of the field the player wants to go
     * @return true, if there is a pit on the given position
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean checkPit(int nextX, int nextY) {
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).getX() == nextX && obstacles.get(i).getY() == nextY) {
                if (obstacles.get(i).getType().contains("Pit")) {
                    logger.debug("Pit found: nextX: " + nextX + " NextY: " + nextY);
                    return true;
                }
            }
        }
        logger.debug("Pit NOT found: nextX: " + nextX + " NextY: " + nextY);
        return false;
    }

    /**
     * checkMapEnd():
     * checks if there is a map end at the next player's position
     *
     * @param nextX: xPosition of the field the player wants to go
     * @param nextY: yPosition of the field the player wants to go
     * @return true, if there is a map end
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean checkMapEnd(int nextX, int nextY) {
        if (nextY < 0 || nextX > 12 || nextY > 9 || nextX < 0) {
            logger.debug("MapEnd found: nextX: " + nextX + " NextY: " + nextY);
            return true;
        }
        logger.debug("MapEnd NOT found: nextX: " + nextX + " NextY: " + nextY);
        return false;
    }

    /**
     * checkRobot():
     * checks if there is a robot in the way of the current robot
     * if there is a robot, the other robot will be pushed after checking if there is a wall
     *
     * @param nextX
     * @param nextY
     * @param orientation
     * @param player
     * @return true, if there is a robot, which can be moved
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     **/
    public boolean checkRobot(int nextX, int nextY, int orientation, PlayerThread player) {
        for (Integer client : server.getClients().keySet()) {
            PlayerThread toCheck = server.getClients().get(client);
            if (player != toCheck) {
                int xPosRobot = toCheck.getRobot().getxPosition();      //Position wo der andere Roboter steht
                int yPosRobot = toCheck.getRobot().getyPosition();

                if (nextX == xPosRobot && nextY == yPosRobot) {
                    //if a robot is in the way...
                    int nextXRobot = -1;            //position, where the robot is supposed to be pushed
                    int nextYRobot = -1;
                    if (orientation == 1) {
                        nextXRobot = xPosRobot;
                        nextYRobot = yPosRobot - 1;
                    } else if (orientation == 2) {
                        nextXRobot = xPosRobot + 1;
                        nextYRobot = yPosRobot;
                    } else if (orientation == 3) {
                        nextXRobot = xPosRobot;
                        nextYRobot = yPosRobot + 1;
                    } else if (orientation == 4) {
                        nextXRobot = xPosRobot - 1;
                        nextYRobot = yPosRobot;
                    }
                    if (checkWall(xPosRobot, yPosRobot, nextXRobot, nextYRobot, orientation)) {
                        // a wall is in the way of the pushed robot --> nothing happens
                        logger.debug("Player " + player.getUserId() + " can't be pushed because of a wall");
                        player.getRobot().setCanMove(false);
                        return false;

                    } else if (checkMapEnd(nextXRobot, nextYRobot) || checkPit(nextXRobot, nextYRobot)) {
                        //if the robot is pushed off the Map or into a Pit ->
                        //own roboter moved 1, pushed Robot has to Reboot
                        int newPosition = nextY * 13 + nextX;
                        player.move(player.getUserId(), newPosition);
                        player.getRobot().setTaken(true);

                        logger.debug("Player " + toCheck.getUserId() + "'s robot was pushed in a pit/out of the map");
                        rebootRobot(server.getClients().get(client).getRobot(), server.getClients().get(client));
                        return true;
                    } else if (nextXRobot != -1 && checkRobot(nextXRobot, nextYRobot, orientation, toCheck)) {
                        //behind the pushed Robot is another Robot, THAT CAN AND HAS BEEN BE MOVED
                        int pushedRobotX = toCheck.getRobot().getxPosition();
                        int pushedRobotY = toCheck.getRobot().getyPosition();
                        int pushedRobotNextX = -1;
                        int pushedRobotNextY = -1;
                        if (orientation == 1) {
                            pushedRobotNextX = pushedRobotX;
                            pushedRobotNextY = pushedRobotY - 1;
                        } else if (orientation == 2) {
                            pushedRobotNextX = pushedRobotX + 1;
                            pushedRobotNextY = pushedRobotY;
                        } else if (orientation == 3) {
                            pushedRobotNextX = pushedRobotX;
                            pushedRobotNextY = pushedRobotY + 1;
                        } else if (orientation == 4) {
                            pushedRobotNextX = pushedRobotX - 1;
                            pushedRobotNextY = pushedRobotY;
                        }
                        if (pushedRobotNextX >= 0 && pushedRobotNextY >= 0) {
                            //happens if the pushedRobot is pushed off the Map --> he is allready rebooting, so he doesnt move
                            int pushedRobotNextPosition = pushedRobotNextY * 13 + pushedRobotNextX;
                            toCheck.move(toCheck.getUserId(), pushedRobotNextPosition);
                            logger.debug("Player " + toCheck.getUserId() + "'s robot was pushed to: position " + pushedRobotNextPosition);
                        }
                        return true;
                    } else {
                        player.getRobot().setCanMove(false);
                        //the pushed robot is moved
                        if (server.getClients().get(client).getRobot().getCanMove()) {
                            server.getClients().get(client).getRobot().setxPosition(nextXRobot);
                            server.getClients().get(client).getRobot().setyPosition(nextYRobot);
                            int robotID = server.getClients().get(client).getUserId();
                            int robotPosition = nextYRobot * 13 + nextXRobot;
                            server.getClients().get(client).move(robotID, robotPosition);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * checkRobotStartingPosition():
     *
     * @param originalBot the rebooting robot
     * @param movedRobot  the robot that looks if another robot is standing in his way (first recursion: also the rebooting robot)
     * @param preferredX  the X position the robot wants to move to
     * @param preferredY  the Y position the robot wants to move to
     * @return true if the originalBot can reboot on his Startingboard, false if he can't
     * @author Vincent Oeller
     */
    public boolean checkRobotStartingPosition(Robot originalBot, Robot movedRobot, int preferredX, int preferredY) {
        for (PlayerThread player : server.getClients().values()) {
            if (preferredX == player.getRobot().getxPosition()
                    && preferredY == player.getRobot().getyPosition()
                    && movedRobot.getPlayer().getUserId() != player.getUserId()) {
                //if someone is standing on your StartingPosition (or in the way)...
                int playerX = player.getRobot().getxPosition();
                int playerY = player.getRobot().getyPosition();
                if (!checkWall(playerX, playerY, playerX, playerY - 1, 1)) {
                    if (checkRobotStartingPosition(originalBot, player.getRobot(), playerX, playerY - 1)) {
                        //there is no wall above any Robot (if there was a wall above one robot,
                        //then it would return false and you would not get into this if-statement
                        //-> just move the moved robot, the robot that is one recursion above, will
                        //move on his own because there is no one blocking him, so at the end of the for loop he is moved already
                        int newPosition = 13 * preferredY + preferredX;
                        movedRobot.getPlayer().move(movedRobot.getPlayer().getUserId(), newPosition);
                        return true;
                    } else {
                        //if you land here, it must be because there was a wall above a robot,
                        //otherwise checkRobotStartingPosition returns true
                        int openStartingBoard = searchOpenStartingboard();
                        originalBot.getPlayer().move(originalBot.getPlayer().getUserId(), openStartingBoard);
                        //this will be called multiple times, but it doesn't matter because the
                        //openStartingBoard is random anyways
                        return false;
                    }
                } else {
                    //there is a wall above a robot --> no one moves
                    return false;
                }
            }
        }
        //for loop ist durch, d.h. niemand blockiert die gewuenschte Position
        //bewege den movedRobot und return True, weil er sich ja bewegen konnte
        int newPosition = 13 * preferredY + preferredX;
        movedRobot.getPlayer().move(movedRobot.getPlayer().getUserId(), newPosition);
        return true;
    }

    /**
     * checkPosition():
     * checks if a player is on a certain Position
     *
     * @param position
     * @return true, if someone is standing on a given Position
     * @author Vincent Oeller
     */
    public boolean checkPosition(int position) {
        for (PlayerThread player : server.getClients().values()) {
            int playerPosition = (player.getRobot().getxPosition() + 13 * player.getRobot().getyPosition());
            if (playerPosition == position) {
                return true;
            }
        }
        return false;
    }

    /**
     * searchOpenStartingBoard():
     * loops through all starting boards and returns a random, open one
     *
     * @return the position of a random, open starting board
     * @author Vincent Oeller
     */
    public int searchOpenStartingboard() {
        Collections.shuffle(startingPoints);
        for (int position : startingPoints) {
            if (!checkPosition(position)) {       //if no one is standing on a starting board, return it
                return position;
            }
        }
        //this case does not happen because its impossible for all 6 robots to be on their startingboard and one rebooting
        return -1;
    }

    /**
     * checkRobotRestartPoint():
     * looks if there is a robot standing on the RestartPoint
     * if yes, it pushes the robot into the direction of the "RestartPoint"
     * and checks recursively if another Robot is blocking that spot
     *
     * @param movedRobot       the rebooting Robot
     * @param restartDirection the direction of the RebootPoint
     * @author Vincent Oeller
     */
    public void checkRobotRestartPoint(Robot movedRobot, int restartDirection) {
        if (restartDirection == 3) {                              //Dizzy Highway (restartDirection = 3)
            for (PlayerThread player : server.getClients().values()) {
                if (movedRobot.getxPosition() == player.getRobot().getxPosition()
                        && movedRobot.getyPosition() == player.getRobot().getyPosition()
                        && movedRobot.getPlayer().getUserId() != player.getUserId()) {
                    //if a player is blocking the RestartPoint...
                    int playerX = player.getRobot().getxPosition();
                    int playerY = player.getRobot().getyPosition();
                    int newPlayerPosition = 13 * (playerY + 1) + playerX;
                    player.move(player.getUserId(), newPlayerPosition);
                    //check recursively if another robot is blocking that position and push him down
                    checkRobotRestartPoint(player.getRobot(), restartDirection);
                }
            }
        } else if (restartDirection == 2) {                      //Extra Crispy  (restartDirection = 2)
            for (PlayerThread player : server.getClients().values()) {
                if (movedRobot.getxPosition() == player.getRobot().getxPosition()
                        && movedRobot.getyPosition() == player.getRobot().getyPosition()
                        && movedRobot.getPlayer().getUserId() != player.getUserId()) {
                    //if someone is blocking the RestartPoint...
                    int playerX = player.getRobot().getxPosition();
                    int playerY = player.getRobot().getyPosition();
                    int newPlayerPosition = 13 * playerY + playerX + 1;
                    player.move(player.getUserId(), newPlayerPosition);
                    //check recursively if another robot is blocking that position and push him to the right
                    checkRobotRestartPoint(player.getRobot(), restartDirection);
                }
            }
        }
    }

    /**
     * setRobotPoint():
     * sets the robots position to the restartPoint OR to the StartingPoint
     * sets the robots orientation to 1 (north)
     * sends out a JSONObject with messageType "Movement"
     *
     * @param yourRobot the robot that has to reboot
     * @param player    the playerThread that has to reboot
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void setRebootPoint(Robot yourRobot, PlayerThread player) {
        int xPosition = yourRobot.getxPosition();
        if (xPosition > 2) {
            //NOT on StartingBoard
            yourRobot.setOrientation(1);
            int rebootFieldX = restartPoint.getX();
            int rebootFieldY = restartPoint.getY();
            int rebootFieldPosition = 13 * rebootFieldY + rebootFieldX;
            yourRobot.getPlayer().move(yourRobot.getPlayer().getUserId(), rebootFieldPosition);
            int restartPushDirection = calculateDirection(restartPoint.getOrientation());
            checkRobotRestartPoint(yourRobot, restartPushDirection);
        } else {
            //onStartingBoard
            yourRobot.setOrientation(1);
            int startingPos = yourRobot.getStartingPosition();
            int startingPosY = startingPos / 13;
            int startingPosX = startingPos - startingPosY * 13;
            checkRobotStartingPosition(yourRobot, yourRobot, startingPosX, startingPosY);
            //checks if there is already someone on your StartingPosition
        }
    }

    /**
     * rebootRobot():
     * empties the registers
     * deactivates the Robots Lasers
     * sends out a JSONObject with messageType "Reboot"
     *
     * @param robot  robot that has to reboot
     * @param player playerThread of the robot
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void rebootRobot(Robot robot, PlayerThread player) {
        for (Register register : player.getRegisters()) {
            discardCard(register, player);
        }
        logger.debug("Player " + player.getUserId() + " rebooted");

        player.getDamaged("Reboot");

        damageCardDecks.pop("spamDeck", player);
        logger.debug("Player " + player.getUserId() + " received a spam card");
        damageCardDecks.pop("spamDeck", player);
        logger.debug("Player " + player.getUserId() + " received a spam card");

        player.reboot(player.getUserId());
        robot.setIsRebooting(true);
        logger.debug("Player " + player.getUserId() + "'s isRebooting -> true");

        logger.debug("Setting rebootPoint...");
        setRebootPoint(robot, player);
    }

    /**
     * damageEffect():
     * this effect is called when a Spam, TrojanHorse, Virus Card is played
     * it plays the top programming card of your deck and puts the damageCard
     * back to the DamageCardDeck
     *
     * @param player     PlayerThread of the Robot
     * @param robot      Robot that played the damageCard
     * @param damageCard the damageCard, that was played
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void damageEffect(PlayerThread player, Robot robot, String damageCard) {
        Card topCard = player.getProgrammingCardDeck().pop();
        logger.debug("Got topCard from player " + player.getUserId() + "'s programmingDeck -> " + topCard.toString());
        while (topCard instanceof Again) {
            if (round != 1) {
                logger.debug("topCard is Again and round != 1");
                break;
            } else {
                logger.debug("topCard is Again and round == 1");
                player.getProgrammingCardDeck().add(topCard);
                topCard = player.getProgrammingCardDeck().pop();
                logger.debug("Got another topCard from player " + player.getUserId() + "'s programmingDeck -> " + topCard.toString());
            }
        }
        logger.debug("Damage Effect -> damageCard was replaced by: " + topCard.toString());
        topCard.effect(player, robot, round);
    }

    /**
     * virusRange():
     * looks at all the robots in a 6 space radius around the robot that
     * played the virus card and gives them a virus card.
     *
     * @param robot
     * @author Vincent Oeller
     */
    public void virusRange(Robot robot, PlayerThread player) {
        ArrayList<PlayerThread> virusPlayers = new ArrayList<>();       //players that receive a virusCard
        int yourX = robot.getxPosition();
        int yourY = robot.getyPosition();

        for (PlayerThread playerThread : server.getClients().values()) {
            if (playerThread.getUserId() != player.getUserId()) {
                int robotXPos = playerThread.getRobot().getxPosition();
                int robotYPos = playerThread.getRobot().getyPosition();
                if (Math.abs(yourX - robotXPos) <= 6 && Math.abs(yourY - robotYPos) <= 6) {
                    virusPlayers.add(playerThread);
                    logger.debug("Player " + player.getUserId() + " is in virus range");
                }
            }
        }

        for (int i = 0; i < virusPlayers.size(); i++) {
            damageCardDecks.pop("virusDeck", virusPlayers.get(i));

            logger.debug("Player " + virusPlayers.get(i) + " received a virus damageCard");

            virusPlayers.get(i).getDamaged("Virus");
        }
    }


    /**
     * calculateDirection():
     *
     * @param directionDeg the orientation of a FieldObject, -90,0,90,180
     * @return the direction as an int 1,2,3,4
     * @author Vincent Oeller
     */
    public int calculateDirection(double directionDeg) {
        switch ((int) directionDeg) {    //up=-90, right=0, down=90, left = 180
            case -90:
                return 1;
            case 0:
                return 2;
            case 90:
                return 3;
            case 180:
                return 4;
        }
        //this case happens if the function is called incorrectly
        return -1;
    }

    /**
     * calculateLaserPosition():
     * this function calculates the startingpoint and orientation of
     * ALL lasers (robot and stationary) and puts them into the HashMap lasers
     *
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void calculateLaserPosition() {
        //all stationary lasers
        lasers.clear();
        for (int i = 0; i < obstacles.size(); i++) {
            if (obstacles.get(i).getType().contains("Laser")) {
                int laserX = obstacles.get(i).getX();
                int laserY = obstacles.get(i).getY();
                int laserPosition = 13 * laserY + laserX;
                double laserOrientationDeg = obstacles.get(i).getOrientation();
                int laserOrientation = calculateDirection(laserOrientationDeg);
                switch (laserOrientation) {
                    case 1:
                        laserOrientation = 3;
                        break;
                    case 2:
                        laserOrientation = 4;
                        break;
                    case 3:
                        laserOrientation = 1;
                        break;
                    case 4:
                        laserOrientation = 2;
                        break;
                }
                lasers.put(laserPosition, laserOrientation);
            }
        }
        //all robot lasers
        for (PlayerThread player : server.getClients().values()) {
            if (player.getRobot().getIsRebooting() == false) {
                int robotXPos = player.getRobot().getxPosition();
                int robotYPos = player.getRobot().getyPosition();
                int robotLaserX = robotXPos;
                int robotLaserY = robotYPos;
                int orientation = player.getRobot().getOrientation();
                switch (orientation) {
                    case 1:
                        robotLaserY--;
                        break;
                    case 2:
                        robotLaserX++;
                        break;
                    case 3:
                        robotLaserY++;
                        break;
                    case 4:
                        robotLaserX--;
                        break;
                }
                int position = 13 * robotLaserY + robotLaserX;
                lasers.put(position, orientation);
            }
        }
    }

    /**
     * loops through all Lasers and calls checkLaserObstacles for them
     *
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void shootAllLasers() {
        for (int position : lasers.keySet()) {
            int laserPosition = position;
            int laserY = laserPosition / 13;
            int laserX = laserPosition - laserY * 13;
            int orientation = lasers.get(position);
            checkLaserObstacles(laserX, laserY, orientation);
        }
    }

    /**
     * checkLaserObstacles():
     * checks if a robot is on the Position of the "moving" Laser
     * and if the laser isn't stopped by anything it moves one Position further.
     * the player gets a spam card if hit
     *
     * @param xPos
     * @param yPos
     * @param orientation
     * @return true, if the laser hit nothing on the given x and y coordinates
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean checkLaserObstacles(int xPos, int yPos, int orientation) {
        //checks if a robot is on the laser
        if(xPos == 0 && yPos == 4){
            return false;
        }
        for (PlayerThread player : server.getClients().values()) {
            if (player.getRobot().getxPosition() == xPos
                    && player.getRobot().getyPosition() == yPos) {
                //player is hit by laser
                damageCardDecks.pop("spamDeck", player);
                player.getDamaged("Laser");
                return false;
            }
        }
        //looks if the laser hit the end of the Map
        if (xPos < 0 || xPos > 12 || yPos < 0 || yPos > 12) {
            logger.debug("Laser hit map's end");
            return false;
        }
        //checks if any wall is in the way of the laser
        int nextX = xPos;
        int nextY = yPos;
        switch (orientation) {
            case 1:
                nextY--;
                break;
            case 2:
                nextX++;
                break;
            case 3:
                nextY++;
                break;
            case 4:
                nextX--;
                break;
        }
        //if there is a wall on the way...
        if (checkWall(xPos, yPos, nextX, nextY, orientation)) {
            logger.debug("Wall is blocking laser. x: " + xPos + " -> " + nextX + " y: " + yPos + " -> " + nextY);
            return false;
        }
        //when there's nothing
        logger.debug("Nothing is blocking laser. Moving to next field. x: " + nextX + " y: " + nextY);

        return checkLaserObstacles(nextX, nextY, orientation);
    }

    /**
     * blueBeltMovement():
     * checks if a player is standing on a blueBelt and moves them
     *
     * @author Vincent Oeller
     */
    public void blueBeltMovement() {
        HashMap<Integer, Integer> newPositions = new HashMap<>();
        //      <playerID, Position the robot is moved to
        HashMap<Integer, String> rotationDirections = new HashMap<>();
        //     <playerID, rotationDirection, if the player is rotated by a rotating belt
        for (PlayerThread player : server.getClients().values()) {          //for all Robots
            for (FieldObject belt1 : blueBelts) {
                int xRobot = player.getRobot().getxPosition();
                int yRobot = player.getRobot().getyPosition();
                if (belt1.getX() == xRobot && belt1.getY() == yRobot) {
                    //if a Robot is standing on a blue belt it is moved 1 in the direction of the belt
                    int belt1Dir = -1;
                    if (belt1.getType().equals("Belt")) {
                        double beltDirectionDeg = belt1.getOrientation();
                        belt1Dir = calculateDirection(beltDirectionDeg);
                    } else {      //if it is a rotating Belt
                        String beltDirectionStr = belt1.getOrientations().get(0);
                        switch (beltDirectionStr) {
                            case "up":
                                belt1Dir = 1;
                                break;
                            case "right":
                                belt1Dir = 2;
                                break;
                            case "down":
                                belt1Dir = 3;
                                break;
                            case "left":
                                belt1Dir = 4;
                                break;
                        }
                    }
                    int nextX = belt1.getX();
                    int nextY = belt1.getY();
                    switch (belt1Dir) {
                        case 1:
                            nextY--;
                            break;
                        case 2:
                            nextX++;
                            break;
                        case 3:
                            nextY++;
                            break;
                        case 4:
                            nextX--;
                            break;
                    }
                    //         ---------------------------- the player moves (1/2) --------------------------------
                    int blockedBeltX = -1;
                    int blockedBeltY = -1;
                    if (checkPit(nextX, nextY)) {
                        rebootRobot(player.getRobot(), player);
                        // --> Pit
                    }
                    for (FieldObject belt2 : blueBelts) {
                        if (nextX == belt2.getX() && nextY == belt2.getY()
                                && nextX != blockedBeltX && nextY != blockedBeltY) {
                            if (belt2.getType().equals("RotatingBelt")) {
                                //if it is a rotating belt...
                                String rBelt2OrientationStr = belt2.getOrientations().get(0);
                                int rotatingBelt2Dir = -1;
                                switch (rBelt2OrientationStr) {
                                    case "up":
                                        rotatingBelt2Dir = 1;
                                        break;
                                    case "right":
                                        rotatingBelt2Dir = 2;
                                        break;
                                    case "down":
                                        rotatingBelt2Dir = 3;
                                        break;
                                    case "left":
                                        rotatingBelt2Dir = 4;
                                        break;
                                }
                                if (belt1Dir == rotatingBelt2Dir) {
                                    //          --- the player is not rotated, only moved (2/2) ---
                                    switch (rotatingBelt2Dir) {
                                        case 1:
                                            nextY--;
                                            break;
                                        case 2:
                                            nextX++;
                                            break;
                                        case 3:
                                            nextY++;
                                            break;
                                        case 4:
                                            nextX--;
                                            break;
                                    }
                                    blockedBeltX = nextX;
                                    blockedBeltY = nextY;
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX() && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                newPositions.put(player.getUserId(), desiredPosition);       // robot.setPosition

                                                String rotationDirection3 = belt3.getOrientations().get(1);           //left, right
                                                if (rotationDirection3.equals("right")) {
                                                    rotationDirections.put(player.getUserId(), "clockwise");
                                                    // --> RotatingBelt(no rotation) --> RotatingBelt(with rotation) 1
                                                } else {
                                                    rotationDirections.put(player.getUserId(), "counterClockwise");
                                                    // --> RotatingBelt(no rotation) --> RotatingBelt(with rotation) 1
                                                }

                                            } else {
                                                int desiredPosition = 13 * nextY + nextX;
                                                newPositions.put(player.getUserId(), desiredPosition);
                                                // --> RotatingBelt(no rotation) --> BlueBelt 2
                                            }
                                        }
                                    }
                                } else {
                                    //          --- the player is rotated and moved (2/2) ---
                                    switch (rotatingBelt2Dir) {
                                        case 1:
                                            nextY--;
                                            break;
                                        case 2:
                                            nextX++;
                                            break;
                                        case 3:
                                            nextY++;
                                            break;
                                        case 4:
                                            nextX--;
                                            break;
                                    }
                                    blockedBeltX = nextX;
                                    blockedBeltY = nextY;
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX() && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                newPositions.put(player.getUserId(), desiredPosition);
                                                // --> RotatingBelt(with rotation) --> RotatingBelt(with rotation) 3
                                            } else {
                                                int desiredPosition = nextY * 13 + nextX;
                                                newPositions.put(player.getUserId(), desiredPosition);

                                                String rBelt2DirectionStr = belt2.getOrientations().get(1);   //left,right
                                                if (rBelt2DirectionStr.equals("right")) {
                                                    rotationDirections.put(player.getUserId(), "clockwise");
                                                    // --> RotatingBelt(with rotation) --> BlueBelt 4
                                                } else {
                                                    rotationDirections.put(player.getUserId(), "counterClockwise");
                                                    // --> RotatingBelt(with rotation) --> BlueBelt 4
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (belt2.getType().equals("Belt")) {
                                int bBelt2Orientation = calculateDirection(belt2.getOrientation());
                                switch (bBelt2Orientation) {
                                    case 1:
                                        nextY--;
                                        break;
                                    case 2:
                                        nextX++;
                                        break;
                                    case 3:
                                        nextY++;
                                        break;
                                    case 4:
                                        nextX--;
                                        break;
                                }
                                blockedBeltX = nextX;
                                blockedBeltY = nextY;
                                // if there is a pit --> reboot
                                if (checkPit(nextX, nextY)) {
                                    rebootRobot(player.getRobot(), player);
                                    // --> blueBelt --> Pit 8
                                } else {
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX() && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int belt2Orientation = calculateDirection(belt2.getOrientation());
                                                // belt2Orientation == belt3Orientation
                                                String rbelt3OrientationStr = belt3.getOrientations().get(0);
                                                int rbelt3Orientation = -1;
                                                switch (rbelt3OrientationStr) {
                                                    case "up":
                                                        rbelt3Orientation = 1;
                                                        break;
                                                    case "right":
                                                        rbelt3Orientation = 2;
                                                        break;
                                                    case "down":
                                                        rbelt3Orientation = 3;
                                                        break;
                                                    case "left":
                                                        rbelt3Orientation = 4;
                                                        break;
                                                }
                                                if (belt2Orientation == rbelt3Orientation) {
                                                    int desiredPosition = nextY * 13 + nextX;
                                                    newPositions.put(player.getUserId(), desiredPosition);
                                                    // --> BlueBelt --> RotatingBelt(no rotation) 5
                                                } else {
                                                    int desiredPosition = nextY * 13 + nextX;
                                                    newPositions.put(player.getUserId(), desiredPosition);

                                                    String rBelt3Direction = belt3.getOrientations().get(1);      //left, right
                                                    if (rBelt3Direction.equals("right")) {
                                                        rotationDirections.put(player.getUserId(), "clockwise");
                                                        // --> BlueBelt --> RotatingBelt(with rotation) 6
                                                    } else {
                                                        rotationDirections.put(player.getUserId(), "counterClockwise");
                                                        // --> BlueBelt --> RotatingBelt(with rotation) 6
                                                    }
                                                }
                                            } else if (belt3.getType().equals("Belt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                newPositions.put(player.getUserId(), desiredPosition);
                                                // --> BlueBelt --> BlueBelt 7
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //if 2 robots end up on the same spot, nobody moves
        ArrayList<Integer> dontMove = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pos1 : newPositions.entrySet()) {
            for (Map.Entry<Integer, Integer> pos2 : newPositions.entrySet()) {
                if (pos1.getKey() != pos2.getKey()) {     //if its not the same player
                    //if positions of 2 robots are the same, cancel their planned move
                    if (pos1.getValue() == pos2.getValue()) {
                        dontMove.add(pos1.getKey());
                        dontMove.add(pos2.getKey());
                        //if those 2 robots were rotated, then also cancel their planned rotation
                        if (rotationDirections.containsKey(pos1.getKey())) {
                            rotationDirections.remove(pos1.getKey());
                        }
                        if (rotationDirections.containsKey(pos2.getKey())) {
                            rotationDirections.remove(pos2.getKey());
                        }
                    }
                }
            }
        }
        for (int playerID : dontMove) {
            newPositions.remove(Integer.valueOf(playerID));
        }
        //all remaining robots are moved
        for (Map.Entry<Integer, Integer> robot : newPositions.entrySet()) {
            server.getClients().get(robot.getKey()).move(robot.getKey(), robot.getValue());
            //the move function also changes the Position in Robot.java
        }
        //all remaining robots are turned
        for (Map.Entry<Integer, String> robot : rotationDirections.entrySet()) {
            server.getClients().get(robot.getKey()).turning(robot.getKey(), robot.getValue());
            int origOrientation = server.getClients().get(robot.getKey()).getRobot().getOrientation();
            int newOrientation;
            if (robot.getValue().equals("counterClockwise")) {
                if (origOrientation == 1) {
                    newOrientation = 4;
                } else {
                    newOrientation = origOrientation - 1;
                }
            } else {     // robot.getValue().equals("right")
                if (origOrientation == 4) {
                    newOrientation = 1;
                } else {
                    newOrientation = origOrientation + 1;
                }
            }
            server.getClients().get(robot.getKey()).getRobot().setOrientation(newOrientation);
        }
    }

    /**
     * greenBeltMovement():
     * checks if a player is standing on a greenBelt and moves them
     *
     * @author Vincent Oeller
     */
    public void greenBeltMovement() {
        for (PlayerThread player : server.getClients().values()) {
            int blockedBeltX = -1;
            int blockedBeltY = -1;
            for (FieldObject belt1 : greenBelts) {
                int xRobot = player.getRobot().getxPosition();
                int yRobot = player.getRobot().getyPosition();
                if (belt1.getX() == xRobot && belt1.getY() == yRobot
                        && blockedBeltX != xRobot && blockedBeltY != yRobot) {
                    //if the robot is standing on a greenBelt...
                    int belt1Dir = -1;
                    if (belt1.getType().equals("Belt")) {
                        double beltDirectionDeg = belt1.getOrientation();
                        belt1Dir = calculateDirection(beltDirectionDeg);
                    } else {      //if it is a "RotatingBelt"
                        String beltDirectionStr = belt1.getOrientations().get(0);
                        switch (beltDirectionStr) {
                            case "up":
                                belt1Dir = 1;
                                break;
                            case "right":
                                belt1Dir = 2;
                                break;
                            case "down":
                                belt1Dir = 3;
                                break;
                            case "left":
                                belt1Dir = 4;
                                break;
                        }
                    }
                    int nextX = belt1.getX();
                    int nextY = belt1.getY();
                    switch (belt1Dir) {
                        case 1:
                            nextY--;
                            break;
                        case 2:
                            nextX++;
                            break;
                        case 3:
                            nextY++;
                            break;
                        case 4:
                            nextX--;
                            break;
                    }
                    //check if there is a player in the way
                    if (checkPosition((13 * nextY + nextX))) {
                        //dont move the player if there is a player
                    } else {
                        //the player moves 1
                        if (checkGreenBelt(nextX, nextY)) {
                            for (FieldObject belt2 : greenBelts) {
                                if (nextX == belt2.getX() && nextY == belt2.getY()) {
                                    int newPosition = nextY * 13 + nextX;
                                    blockedBeltX = nextX;
                                    blockedBeltY = nextY;
                                    player.move(player.getUserId(), newPosition);
                                    String rbelt2Direction = belt2.getOrientations().get(1);  //left, right
                                    int origOrientation = player.getRobot().getOrientation();
                                    int newOrientation;
                                    if (rbelt2Direction.equals("left")) {
                                        if (origOrientation == 1) {
                                            newOrientation = 4;
                                        } else {
                                            newOrientation = origOrientation - 1;
                                        }
                                    } else {     // rbelt2Direction.equals("right")
                                        if (origOrientation == 4) {
                                            newOrientation = 1;
                                        } else {
                                            newOrientation = origOrientation + 1;
                                        }
                                    }
                                    player.getRobot().setOrientation(newOrientation);
                                    player.turning(player.getUserId(), rbelt2Direction.equals("left")? "counterClockwise": "clockwise");
                                }
                            }
                        } else if (belt1.getType().equals("RotatingBelt")) {
                            rebootRobot(player.getRobot(), player);
                        } else {
                            int newPosition = nextY * 13 + nextX;
                            blockedBeltX = nextX;
                            blockedBeltY = nextY;
                            player.move(player.getUserId(), newPosition);
                        }
                    }
                }
            }
        }
    }

    /**
     * checkGreenBelt():
     *
     * @param xPos
     * @param yPos
     * @return
     * @author Vincent Oeller
     */
    public boolean checkGreenBelt(int xPos, int yPos) {
        for (FieldObject belt2 : greenBelts) {
            if (xPos == belt2.getX() && yPos == belt2.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * gearRotation():
     * checks if a player is standing on a gear and rotates them
     *
     * @author Vincent Oeller
     */
    public void gearRotation() {
        for (PlayerThread player : server.getClients().values()) {
            if (!gears.isEmpty()) {       //dizzyHighway has no gears
                for (FieldObject gear : gears) {
                    if (player.getRobot().getxPosition() == gear.getX()
                            && player.getRobot().getyPosition() == gear.getY()) {
                        int playerOrientation = player.getRobot().getOrientation();
                        int gearRotation = calculateDirection(gear.getOrientation());     //2 or 4
                        String gearRotationStr = "";
                        if (gearRotation == 2) {
                            gearRotationStr = "clockwise";
                        } else {
                            gearRotationStr = "counterClockwise";
                        }
                        //JSONObject Turning is send out
                        player.turning(player.getUserId(), gearRotationStr);
                        if (gearRotation == 2) {     //right
                            if (playerOrientation == 4) {
                                player.getRobot().setOrientation(1);
                            } else {
                                player.getRobot().setOrientation(playerOrientation + 1);
                            }
                        } else {                      //left
                            if (playerOrientation == 1) {
                                player.getRobot().setOrientation(4);
                            } else {
                                player.getRobot().setOrientation(playerOrientation - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * energySpaceEffect():
     * checks if a player is standing on a energySpace and gives them one energy
     *
     * @author Vincent Oeller
     */
    public void energySpacesEffect() {
        for (PlayerThread player : server.getClients().values()) {
            for (FieldObject energy : energySpaces.keySet()) {
                if (player.getRobot().getxPosition() == energy.getX()
                        && player.getRobot().getyPosition() == energy.getY()
                        && (energySpaces.get(energy) || (round == 5))) {
                    //the energySpace doesn't contain energy anymore for this game
                    energySpaces.put(energy, false);
                    player.getRobot().incrementEnergyCubes();
                    player.energy(player.getUserId(), 1);
                    logger.debug("Gave energy cube to player " + player.getUserId() + "in energySpace x: " + energy.getX() + " y : " + energy.getY());
                }
            }
        }
    }

    /**
     * checks if any player is standing on his next checkPoint
     * if yes the checkPoint Counter is increased
     * if checkPointCounter == controlPoints.size() --> player won the game
     * checkPointReached():
     *
     * @author Vincent Oeller
     */
    public void checkPointReached() {
        for (PlayerThread player : server.getClients().values()) {
            for (FieldObject cP : controlPoints) {
                if (player.getRobot().getxPosition() == cP.getX()
                        && player.getRobot().getyPosition() == cP.getY()) {
                    //if you're standing on a checkpoint...
                    if (cP.getCounter() == player.getRobot().getCheckPointsReached() + 1) {
                        //if its the next checkPoint...
                        player.getRobot().incrementCheckPointsReached();
                        player.checkPointReached(player.getUserId(), cP.getCounter());
                        logger.debug("Player " + player.getUserId() + " reached checkPoint " + cP.getCounter());

                    }
                    if (player.getRobot().getCheckPointsReached() == controlPoints.size()) {      //last checkPoint reached --> gameWon
                        player.gameWon(player.getUserId());
                        logger.debug("Player " + player.getUserId() + " reached all checkPoints.");
                        logger.debug("Player " + player.getUserId() + " won the game.");
                    }
                }
            }
        }
    }

    /**
     * sets the order on which the players move
     *
     * @author Vincent Oeller
     */
    public void priotityAntenna() {
        playerIDsOrder.clear();
        HashMap<Integer, Integer> playerDistances = new HashMap<>();
        //     <playerID, distance>
        for (PlayerThread player : server.getClients().values()) {
            int playerX = player.getRobot().getxPosition();
            int playerY = player.getRobot().getyPosition();
            int distance = calculateAntennaDistance(playerX, playerY);
            playerDistances.put(player.getUserId(), distance);
        }
        ArrayList<Integer>[] possibleDistances = new ArrayList[18];   //an array full of ArrayLists, there are 17 different possible distances (Distance 0 is not possible)
        //arrayList contains the playerIDs
        for (int i = 0; i < 18; i++) {
            possibleDistances[i] = new ArrayList<>();
        }
        for (Map.Entry<Integer, Integer> playerDistance : playerDistances.entrySet()) {
            possibleDistances[playerDistance.getValue()].add(playerDistance.getKey());
            //adds the playerIDs to the ArrayList that corresponds with their distance to the priorityAntenna
        }

        //loop through the array full of ArrayLists
        for (int i = 0; i < possibleDistances.length; i++) {
            if (!possibleDistances[i].isEmpty()) {
                if (possibleDistances[i].size() == 1) {
                    //if there is only one with a
                    playerIDsOrder.add(possibleDistances[i].get(0));
                    possibleDistances[i].clear();
                } else {
                    calculatePriority(possibleDistances[i]);
                    //call calculateAntennaLineDistance and add them to the result array
                }
            }
        }
    }

    /**
     * @param xPos xPosition of the Robot
     * @param yPos yPosition of the Robot
     * @return returns the distance to the priorityAntenna
     * @author Vincent Oeller
     */
    public int calculateAntennaDistance(int xPos, int yPos) {
        int xDifference = xPos;                     //x-Value of antenna = 0
        int yDifference = Math.abs(yPos - 4);       //y-Value of antenna = 4
        int distance = xDifference + yDifference;
        return distance;
    }

    /**
     * this function is called, when multiple robots have the same distance to the priorityAntenna
     * @param arrList an arrayList with at least 2 playerIDs which have the same distance to the antenna
     * sorts the players depending on their distance to the imaginary line of the priorityAntenna
     * @author Vincent Oeller
     */
    public void calculatePriority(ArrayList<Integer> arrList) {
        while (!arrList.isEmpty()) {
            int smallestDistancePlayerID = arrList.get(0);
            int smallestDistanceToLine = 100;
            for (int playerID1 : arrList) {
                for (int playerID2 : arrList) {
                    if (playerID1 != playerID2) {
                        int distanceToLine1 = server.getClients().get(playerID1).getRobot().getyPosition() - 4;
                        int distanceToLine2 = server.getClients().get(playerID2).getRobot().getyPosition() - 4;

                        if ((distanceToLine1 >= 0) && (distanceToLine2 >= 0)) {      //4 because the line drawn by the priority Antenna is at y=4
                            if (distanceToLine1 < distanceToLine2 && distanceToLine1 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID1;
                                smallestDistanceToLine = distanceToLine1;
                            } else if (distanceToLine1 > distanceToLine2 && distanceToLine2 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID2;
                                smallestDistanceToLine = distanceToLine2;
                            }
                        } else if ((distanceToLine1 >= 0) && (distanceToLine2 < 0)) {
                            if (distanceToLine1 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID1;
                                smallestDistanceToLine = distanceToLine1;
                            }
                        } else if ((distanceToLine1 < 0) && (distanceToLine2 >= 0)) {
                            if (distanceToLine2 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID2;
                                smallestDistanceToLine = distanceToLine2;
                            }
                        } else if ((distanceToLine1 < 0) && (distanceToLine2 < 0)) {
                            if (distanceToLine1 < distanceToLine2 && distanceToLine1 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID1;
                                smallestDistanceToLine = distanceToLine1;
                            } else if (distanceToLine1 > distanceToLine2 && distanceToLine2 < smallestDistanceToLine) {
                                smallestDistancePlayerID = playerID2;
                                smallestDistanceToLine = distanceToLine2;
                            }
                        }
                    }
                }
            }
            playerIDsOrder.add(smallestDistancePlayerID);
            arrList.remove(Integer.valueOf(smallestDistancePlayerID));
        }
    }


    /**
     * cheating():
     * this function is for cheating to check single functions/effects of the game
     * by getting a message
     *
     * @param message the received message from the chat
     * @param player  the player who sent the message
     * @author Franziska Leitmeir, Vivien Pfeiffer
     */
    public static void cheating(String message, PlayerThread player) {
        Game currentGame = player.getServer().getGame();
        if (message.equals("#MoveI")) {
            Card card = new MoveI();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#MoveII")) {
            Card card = new MoveII();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#MoveIII")) {
            Card card = new MoveIII();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#TurnLeft")) {
            Card card = new TurnLeft();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#TurnRight")) {
            Card card = new TurnRight();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#UTurn")) {
            Card card = new UTurn();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#BackUp")) {
            Card card = new BackUp();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#PowerUp")) {
            Card card = new PowerUp();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.contains("#Teleport")) {
            try {
                int to = Integer.parseInt(message.substring(9));

                if (0 <= to && to <= 129) {
                    int newPosY = to / 13;
                    int newPosX = to - newPosY * 13;
                    player.getRobot().setxPosition(newPosX);
                    player.getRobot().setyPosition(newPosY);
                    player.move(player.getUserId(), to);
                    currentGame.checkRobot(newPosX, newPosY, player.getRobot().getOrientation(), player);
                    currentGame.cheatingEffects();

                } else {
                    JSONObject msg = new JSONObject();
                    msg.put("error", "To Teleport to a field you must choose a number between 0 and 129");
                    player.sendMessage("Error", msg);
                }
            } catch (NumberFormatException e) {
                JSONObject msg = new JSONObject();
                msg.put("error", "To Teleport to a field you must choose a number between 0 and 129 " + e);
                player.sendMessage("Error", msg);
            }
        } else if (message.equals("#Spam")) {
            Card card = new Spam();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#TrojanHorse")) {
            Card card = new Trojan();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#Virus")) {
            Card card = new Virus();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else if (message.equals("#Worm")) {
            Card card = new Worm();
            card.effect(player, player.getRobot(), 0);
            currentGame.cheatingEffects();
        } else {
            JSONObject msg = new JSONObject();
            msg.put("error", "this ist not a valid cheat. Please try again.");
            player.sendMessage("Error", msg);
        }

    }

    /**
     * cheatingEffects():
     * this function will be called by the cheating function
     * to run the field effects even when you are not in the activation phase
     *
     * @author Franziska Leitmeir, Vivien Pfeiffer
     */
    public void cheatingEffects() {
        blueBeltMovement();
        greenBeltMovement();
        gearRotation();
        calculateLaserPosition();
        shootAllLasers();
        energySpacesEffect();
        checkPointReached();
        priotityAntenna();
    }


    // Getter and Setter that were needed:


    public static Logger getLogger() {
        return logger;
    }

    public ArrayList<Integer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Integer> players) {
        this.players = players;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public CardDeck getUpgradeShopDeck() {
        return upgradeShopDeck;
    }

    public void setUpgradeShopDeck(CardDeck upgradeShopDeck) {
        this.upgradeShopDeck = upgradeShopDeck;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String maptype) {
        this.maptype = maptype;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerThread getCurrentPlayerThread() {
        return currentPlayerThread;
    }

    public void setCurrentPlayerThread(PlayerThread currentPlayerThread) {
        this.currentPlayerThread = currentPlayerThread;
    }

    public SandTimer getTimer() {
        return timer;
    }

    public void setTimer(SandTimer timer) {
        this.timer = timer;
    }

    public boolean isTimesOver() {
        return timesOver;
    }

    public void setTimesOver(boolean timesOver) {
        this.timesOver = timesOver;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public JSONObject getCurrentGameField() {
        return currentGameField;
    }

    public void setCurrentGameField(JSONObject currentGameField) {
        this.currentGameField = currentGameField;
    }

    public JSONObject getCurrentStartingBoard() {
        return currentStartingBoard;
    }

    public void setCurrentStartingBoard(JSONObject currentStartingBoard) {
        this.currentStartingBoard = currentStartingBoard;
    }

    public FieldObject[][] getWholeMap() {
        return wholeMap;
    }

    public void setWholeMap(FieldObject[][] wholeMap) {
        this.wholeMap = wholeMap;
    }

    public ArrayList<FieldObject> getBlueBelts() {
        return blueBelts;
    }

    public void setBlueBelts(ArrayList<FieldObject> blueBelts) {
        this.blueBelts = blueBelts;
    }

    public ArrayList<FieldObject> getGreenBelts() {
        return greenBelts;
    }

    public void setGreenBelts(ArrayList<FieldObject> greenBelts) {
        this.greenBelts = greenBelts;
    }

    public ArrayList<FieldObject> getGears() {
        return gears;
    }

    public void setGears(ArrayList<FieldObject> gears) {
        this.gears = gears;
    }

    public ArrayList<FieldObject> getPushPanels() {
        return pushPanels;
    }

    public void setPushPanels(ArrayList<FieldObject> pushPanels) {
        this.pushPanels = pushPanels;
    }

    public ArrayList<FieldObject> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(ArrayList<FieldObject> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public HashMap<FieldObject, Boolean> getEnergySpaces() {
        return energySpaces;
    }

    public void setEnergySpaces(HashMap<FieldObject, Boolean> energySpaces) {
        this.energySpaces = energySpaces;
    }

    public HashMap<Integer, Integer> getLasers() {
        return lasers;
    }

    public void setLasers(HashMap<Integer, Integer> lasers) {
        this.lasers = lasers;
    }

    public ArrayList<FieldObject> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<FieldObject> obstacles) {
        this.obstacles = obstacles;
    }

    public FieldObject getRestartPoint() {
        return restartPoint;
    }

    public void setRestartPoint(FieldObject restartPoint) {
        this.restartPoint = restartPoint;
    }

    public ArrayList<Integer> getPlayerIDsOrder() {
        return playerIDsOrder;
    }

    public void setPlayerIDsOrder(ArrayList<Integer> playerIDsOrder) {
        this.playerIDsOrder = playerIDsOrder;
    }

    public boolean isGameIsOver() {
        return gameIsOver;
    }

    public void setGameIsOver(boolean gameIsOver) {
        this.gameIsOver = gameIsOver;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getDrawnCard() {
        return drawnCard;
    }

    public void setDrawnCard(int drawnCard) {
        this.drawnCard = drawnCard;
    }

    public DamageCardDecks getDamageCardDecks() {
        return damageCardDecks;
    }

    public void setDamageCardDecks(DamageCardDecks damageCardDecks) {
        this.damageCardDecks = damageCardDecks;
    }

    public Boolean getAllDone() {
        return allDone;
    }

    public void setAllDone(Boolean allDone) {
        this.allDone = allDone;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getCurrentID() {
        return currentID;
    }

    public int getActivePhase() {
        return activePhase;
    }

    public void setActivePhase(int activePhase) {
        this.activePhase = activePhase;
    }

    public List<Integer> getStartingPoints() {
        return startingPoints;
    }

    public void setStartingPoints(ArrayList<Integer> startingPoints) {
        this.startingPoints = startingPoints;
    }

    public int getRound() {
        return this.round;
    }

    public boolean isStartingPointTaken() {
        return startingPointTaken;
    }

    public void setStartingPointTaken(boolean startingPointTaken) {
        this.startingPointTaken = startingPointTaken;
    }

}






















