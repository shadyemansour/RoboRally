package game.gameObjects.bot;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import game.gameObjects.gamefield.*;
import game.gameObjects.otherObjects.Register;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;
import networking.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

import static java.lang.Math.toIntExact;

public class ServerBot extends Bot {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Bot");
    private PlayerThread player;
    private Server server;
    private Boolean serverBot;


    private QLearning qlearning;
    private LinkedList<Integer> bestPath;

    private String mapType;
    private int phase;
    private int startingPoint;
    private Robot robot;
    private int activePhase;
    private int currentPlayer;
    private int expectedPosition;
    private Boolean reached;
    private int tempOrientation;
    private String[] cardsToBePlayed;
    private Boolean playedCards;
    private Game game;
    private int userID;
    private ArrayList<Register> registers;
    private FieldObject[][] fullMap;
    private List<Integer> takenStartingPoints;
    private int rebootField;
    private LinkedList<Integer> checkpoints;
    private int checkpoint;
    private Boolean allReached;
    private Iterator cpIterator;
    private JSONObject currentGameField;
    private JSONObject startingBoard;


    private String[] drawnCards;

    public ServerBot(PlayerThread player, Server server) {
        super(null, true);
        this.player = player;
        this.server = server;
        this.serverBot = true;
        this.robot = player.getRobot();
        this.game = server.getGame();
        this.activePhase = game.getActivePhase();
        this.reached = false;
        this.playedCards = false;
        this.userID = player.getUserId();
        this.registers = player.getRegisters();
        this.takenStartingPoints = new ArrayList<>();

        if (game.getWholeMap()[0][0].getType().toLowerCase().contains("restartpoint")) {
            this.mapType = "ExtraCrispy";
            currentGameField = (new ExtraCrispy()).returnGamefield();
            startingBoard = (new StartCrispy()).returnStartfield();
        } else {
            this.mapType = "DizzyHighway";
            currentGameField = (new DizzyHighway()).returnGamefield();
            startingBoard = (new StartDizzy()).returnStartfield();
        }

        buildMap(currentGameField, startingBoard);


    }

    /**
     * start():
     * is called when a server bot is initiated to
     * take over. Sees in which phase the game is currently
     * in and acts accordingly.
     */
    public void start() {
        logger.debug("Server Bot activated for player " + userID);
        switch (activePhase) {
            case 0:
                logger.debug("Started in phase 0");
                if (player.getStartingPoint() == -1 && game.getCurrentID() == userID) {
                    chooseStartingPoint();
                }
                break;
            case 2:
                logger.debug("Started in phase 2");
                setPosition(robot.getPosition());
                setCurrentPosition(robot.getPosition());
                setStartingPoint(robot.getStartingPosition());
                if (mapType.equals("DizzyHighway")) {
                    rebootField = 46;
                    checkpoint = 51;
                } else {
                    rebootField = 1;
                    Integer[] tempCheck = {36, 96, 101, 31};
                    checkpoints = new LinkedList<>(Arrays.asList(tempCheck));
                    cpIterator = checkpoints.iterator();
                    checkpoint = (Integer) cpIterator.next();
                }
                setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));
                logger.debug("Set checkpoints, rebootfield, positions and QLearning for map: " + mapType);
                if (!player.getSelectionFinished()) {
                    logger.debug("Player quitted without finishing selection.");
                    for (int i = 0; i < registers.size(); i++) {
                        if (!registers.get(i).isEmpty()) {
                            game.placeOnRegister(i + 1, null, player);
                        }
                    }
                    logger.debug("Emptied player's registers");

                    Card[] temp = player.getDrawnCards();
                    int size = temp.length;
                    drawnCards = new String[9];
                    for (int i = 0; i < size; i++) {
                        drawnCards[i] = temp[i].toString();
                    }
                    logger.debug("Got Drawn Cards: [" + drawnCards[0] + ", " + drawnCards[1] + ", " + drawnCards[2] + ", " + drawnCards[3] + ", " + drawnCards[4] + ", " + drawnCards[5] + ", " + drawnCards[6] + ", " + drawnCards[7] + ", " + drawnCards[8] + "]");
                    chooseCards(drawnCards);
                }
                break;
            case 3:
                logger.debug("Started in phase 3");
                setPosition(robot.getPosition());
                setCurrentPosition(robot.getPosition());
                setStartingPoint(robot.getStartingPosition());
                if (mapType.equals("DizzyHighway")) {
                    rebootField = 46;
                    checkpoint = 51;
                } else {
                    rebootField = 1;
                    Integer[] tempCheck = {36, 96, 101, 31};
                    checkpoints = new LinkedList<>(Arrays.asList(tempCheck));
                    cpIterator = checkpoints.iterator();
                    checkpoint = (Integer) cpIterator.next();
                }
                player.setPlayIt(true);
                logger.debug("PlayIt set to true");
                synchronized (server.getGame()) {
                    server.getGame().notifyAll();
                }
                setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));
                logger.debug("Set checkpoints, rebootfield, positions and QLearning for map: " + mapType);
                break;
        }

    }

    /**
     * chooseStartingPoint():
     * chooses starting point according to the map
     */
    private void chooseStartingPoint() {
        long k = 0;
        if (mapType.equals("DizzyHighway")) {
            if (!takenStartingPoints.contains(105)) {
                k = 105;
            } else if (!takenStartingPoints.contains(78)) {
                k = 78;
            } else if (!takenStartingPoints.contains(54)) {
                k = 54;
            } else if (!takenStartingPoints.contains(39)) {
                k = 39;
            } else if (!takenStartingPoints.contains(14)) {
                k = 14;
            } else if (!takenStartingPoints.contains(66)) {
                k = 66;
            }
            rebootField = 46;
            checkpoint = 51;
            startingPoint = (int) k;
        } else {
            if (!takenStartingPoints.contains(14)) {
                k = 14;
            } else if (!takenStartingPoints.contains(39)) {
                k = 39;
            } else if (!takenStartingPoints.contains(54)) {
                k = 54;
            } else if (!takenStartingPoints.contains(66)) {
                k = 66;
            } else if (!takenStartingPoints.contains(78)) {
                k = 78;
            } else if (!takenStartingPoints.contains(105)) {
                k = 105;
            }
            rebootField = 1;
            startingPoint = (int) k;
            Integer[] tempCheck = {36, 96, 101, 31};
            checkpoints = new LinkedList<>(Arrays.asList(tempCheck));
            cpIterator = checkpoints.iterator();
            checkpoint = (Integer) cpIterator.next();

        }
        setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));
        JSONObject msgBody = new JSONObject();
        msgBody.put("position", k);

        server.checkStartingPoint(msgBody, player);
        startingPoint = toIntExact(checkpoint);
        logger.debug("Server Bot chose startingPoint");
    }

    /**
     * called by setDrawnCards from PlayerThread
     *
     * @param drawnCards: cards dealt from server
     */
    public void setDrawnCards(String[] drawnCards) {
        this.drawnCards = drawnCards;
        logger.debug("Bot's drawn cards set");
        chooseCards(drawnCards);
    }

    /**
     * playCards():
     * places the cards on the player's registers when the timer starts
     */
    @Override
    public void playCards() {
        for (int i = 0; i < cardsToBePlayed.length; i++) { //place cards
            player.getServer().getGame().placeOnRegister(i + 1, player.whichCard((String) cardsToBePlayed[i]), player);
            logger.debug("Placed " + cardsToBePlayed[i] + " on register " + i + 1);
        }
        logger.debug("Played Cards");
        playedCards = true;
        cardsToBePlayed = null;
    }

    /**
     * checkMessage():
     * checks messages sent to player that are relevant to the bot.
     *
     * @param type: message type
     * @param body: message body
     */
    @Override
    public void checkMessage(String type, JSONObject body) {
        switch (type) {
            case "CurrentPlayer":
                int id = Integer.parseInt(body.get("playerID").toString());
                if (id == userID && activePhase == 0) {
                    logger.debug("Serverbot is CurrentPlayer. Will choose StartingPoint.");
                    chooseStartingPoint();
                }
                break;
            case "ActivePhase":
                int phase = Integer.parseInt(body.get("phase").toString());
                activePhase = phase;
                logger.debug("Serverbot received Active phase: " + activePhase);
                if (phase == 3) {
                    playedCards = false;
                    logger.debug("playedCards -> false");
                }
                break;
            case "SelectionFinished":
                int userID = Integer.parseInt(body.get("playerID").toString());
                if (userID != this.userID && !playedCards) {
                    logger.debug("Serverbot received first SelectionFinished. Will play cards.");
                    playCards();
                }
                break;
            case "CurrentCards":
                player.setPlayIt(true);
                logger.debug("Serverbot received CurrenCards. PlayIt -> true");
                synchronized (server.getGame()) {
                    server.getGame().notifyAll();
                }
                break;
        }


    }

    /**
     * selectDamage():
     * selects damage when pickDamage is received
     * @param count: number of cards to be chosen
     */
    public void selectDamage(int count){
        JSONArray dCards = new JSONArray();
        Random random = new Random();
        int j;
        for (int i = 0; i < count; i++) {
            j = random.nextInt(4);
            switch (j){
                case 0 :
                    dCards.add("Trojan");
                    break;
                case 1 :
                    dCards.add("Spam");
                    break;
                case 2 :
                    dCards.add("Virus");
                    break;
                case 3 :
                    dCards.add("Worm");
                    break;
            }
        }
        JSONObject body = new JSONObject();
        body.put("cards", dCards);
        server.selectDamage(body, player);
    }

    /**
     * checkPointReached():
     * loads the new checkpoint position in the variable checkpoint
     * and updates everything accordingly.
     * If the map is DizzyHighway, it will be assumed that the game is
     * over because it only has one checkpoint.
     */
    public void checkPointReached() {
        if (mapType.equals("ExtraCrispy")) {
            if (cpIterator.hasNext()) {
                checkpoint = (Integer) cpIterator.next();
                setQlearning(new QLearning(fullMap, startingPoint, checkpoint, mapType));
            } else {
                reached = true;
                allReached = true;
            }
        } else {
            reached = true;
            allReached = true;
        }
    }

    //getters and setters
    public Boolean getPlayedCards() {
        return playedCards;
    }

    @Override
    public void setPlayedCards(Boolean playedCards) {
        this.playedCards = playedCards;
    }

    public PlayerThread getPlayer() {
        return player;
    }

    public void setPlayer(PlayerThread player) {
        this.player = player;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Boolean getServerBot() {
        return serverBot;
    }

    public void setServerBot(Boolean serverBot) {
        this.serverBot = serverBot;
    }

    public QLearning getQlearning() {
        return qlearning;
    }

    @Override
    public void setQlearning(QLearning qlearning) {
        this.qlearning = qlearning;
    }

    public LinkedList<Integer> getBestPath() {
        return bestPath;
    }

    public void setBestPath(LinkedList<Integer> bestPath) {
        this.bestPath = bestPath;
    }


    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getStartingPoint() {
        return startingPoint;
    }

    @Override
    public void setStartingPoint(int startingPoint) {
        this.startingPoint = startingPoint;
    }


    @Override
    public int getActivePhase() {
        return activePhase;
    }

    @Override
    public void setActivePhase(int activePhase) {
        this.activePhase = activePhase;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public int getExpectedPosition() {
        return expectedPosition;
    }

    @Override
    public void setExpectedPosition(int expectedPosition) {
        this.expectedPosition = expectedPosition;
    }

    @Override
    public Boolean getReached() {
        return reached;
    }

    @Override
    public void setReached(Boolean reached) {
        this.reached = reached;
    }

    @Override
    public int getTempOrientation() {
        return tempOrientation;
    }

    @Override
    public void setTempOrientation(int tempOrientation) {
        this.tempOrientation = tempOrientation;
    }

    @Override
    public String[] getCardsToBePlayed() {
        return cardsToBePlayed;
    }

    @Override
    public void setCardsToBePlayed(String[] cardsToBePlayed) {
        this.cardsToBePlayed = cardsToBePlayed;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getUserID() {
        return userID;
    }

    @Override
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ArrayList<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(ArrayList<Register> registers) {
        this.registers = registers;
    }

    @Override
    public FieldObject[][] getFullMap() {
        return fullMap;
    }

    @Override
    public void setFullMap(FieldObject[][] fullMap) {
        this.fullMap = fullMap;
    }

    @Override
    public List<Integer> getTakenStartingPoints() {
        return takenStartingPoints;
    }

    public void setTakenStartingPoints(List<Integer> takenStartingPoints) {
        this.takenStartingPoints = takenStartingPoints;
    }

    public int getRebootField() {
        return rebootField;
    }

    public void setRebootField(int rebootField) {
        this.rebootField = rebootField;
    }

    public LinkedList<Integer> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(LinkedList<Integer> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public int getCheckpoint() {
        return checkpoint;
    }


    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }


    public Boolean getAllReached() {
        return allReached;
    }


    public void setAllReached(Boolean allReached) {
        this.allReached = allReached;
    }

    public JSONObject getStartingBoard() {
        return startingBoard;
    }


    public void setStartingBoard(JSONObject startingBoard) {
        this.startingBoard = startingBoard;
    }

    public String[] getDrawnCards() {
        return drawnCards;
    }


    public JSONObject getCurrentGameField() {
        return currentGameField;
    }


    public void setCurrentGameField(JSONObject currentGameField) {
        this.currentGameField = currentGameField;
    }
}
