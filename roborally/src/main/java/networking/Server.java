package networking;


import game.gameLogic.Game;
import game.gameObjects.gamefield.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Chat Server
 * This class represents a server that allows users to chat and play
 * the game "Roborally" together.
 *
 * @author Shady Mansour and Thomas Richter
 */
public class Server extends Thread{

// 1.) Variables

    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Server");
    private static int TCPPORT;                           // contains the TCP Port where the the Server can be reached.
    private double protocol = 1.0;
    private int id = 1000;
    private static HashMap<Integer, PlayerThread> clients =      // is a Hashmap that contains all currently connected clients and their ... threads.
            new HashMap<Integer, PlayerThread>();
    private static ArrayList<Integer> figures = new ArrayList<>();
    private static ArrayList<JSONObject> playersAdded = new ArrayList<>();
    private static ArrayList<JSONObject> setStatus = new ArrayList<>();
    //array with playeradded json objects

    private ArrayList<PlayerThread> readyPlayersList = new ArrayList<>();

    private boolean gameIsOn = false;                           // a boolean that is set to true when a game starts.

    private static int playersInLobby = 0;
    private static int readyPlayers = 0;

    private int gameSpotsLeft = 6;                              // shows the remaining number of free spots in a game.
    private Game game;                                          // is a Game object. Simply the game!

    JSONArray gameField = new JSONArray();                   //the chosen Map
    private static boolean gameFieldSet = false;
    private int connections = 0;


// 2.) Main method

    /**
     * Server's main method
     * A server is initiated and a connection with the socket is
     * established.
     *
     * @param args: -p PortNumber
     * @author Shady Mansour
     */
    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("No arguments provided. Flags: -p Port.");
        if (args.length == 1) {
            if (args[0].charAt(0) == '-') {
                throw new IllegalArgumentException("Expected argument after: " + args[0]);

            } else {
                throw new IllegalArgumentException("Illegal Argument: " + args[0]);
            }
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String nextArg;
            if (i + 1 == args.length || args[i + 1].charAt(0) == '-') {
                throw new IllegalArgumentException("Expected argument after: " + arg);
            } else {
                nextArg = args[i + 1];
            }
            switch (arg) {
                case "-p":
                    int port = Integer.parseInt(nextArg);
                    if (port < 1024 || port > 65535)
                        throw new IllegalArgumentException("Port number: " + port + " is invalid");
                    TCPPORT = port;
                    i++;
                    break;
                default:
                    throw new IllegalArgumentException("Illegal Argument: " + arg);
            }
        }

        Server server = new Server();
        logger.info("Server started on port number " + server.TCPPORT);
        server.start();
    }

    /**
     * run():
     * The method creates a server socket with the given TCP-Port
     * and waits for clients to establish a connection. Once the
     * connection is established, a dedicated socket and a thread
     * are created for the client. The Thread is then connected to
     * the dedicated socket to enable the client to communicate with
     * the server and the server to continue hearing on the server socket.
     *
     * @author Shady Mansour
     */
    public void run() {


        try (ServerSocket serverSocket = new ServerSocket(TCPPORT)) {
            logger.info("Chat Server is up and waiting for connections");
            logger.info("TCP Port: " + TCPPORT);
            logger.info("Status: running");
            System.out.println("Server can be closed by pressing CTRL-C");


            this.game = new Game(this);

            while (true) {

                Socket socket = serverSocket.accept();
                connections++;
                if (connections > 6) {
                    sorryRequestRefused(socket, "Sorry, we've reached our maximum capacity. Please try again later.");
                    connections--;
                    logger.info("Player tried to connect. Maximum number of connections reached -> Connection refused");
                } else if (gameIsOn){
                    sorryRequestRefused(socket, "Sorry, the game already started without you. Please try again later.");
                    connections--;
                    logger.info("Player tried to connect. Game already started -> Connection refused");
                }else{
                    logger.info("New player connected");

                    // Transfers above connected player to a thread to be able to accept other connections
                    PlayerThread player = new PlayerThread(socket, this);
                    // Starts thread
                    player.start();
                }

            }

        } catch (IOException oops) {
            logger.error("Server Error: " + oops.getMessage());
            logger.warn("Status: DOWN");

        }
    }

    /**
     * sorryRequestRefused():
     * sends an error message to the user trying to connect to the server
     * if his connection can't be accepted (game's on or max capacity)
     * @param socket
     * @author Shady Mansour
     */
    private void sorryRequestRefused(Socket socket,String str){
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            JSONObject msg = new JSONObject();
            JSONObject body = new JSONObject();
            body.put("error", str);
            msg.put("messageType", "Error");
            msg.put("messageBody", body);
            output.writeObject(msg.toString());
            output.close();
        }catch (IOException e) {
            logger.error("Error creating output stream for 6+ client. \n" + e.getMessage());
        }
    }

    /**
     * addUser():
     * Adds a player to the map of connected Players
     *
     * @param id           : is the id of the user to be added.
     * @param playerThread : the player's thread.
     * @author Shady Mansour
     */
    public void addUser(int id, PlayerThread playerThread) {

        clients.put(playerThread.getUserId(), playerThread);
    }

    /**
     * removeUser():
     * Removes a player to the map of connected Players
     *
     * @param id: is the id of the user to be removed.
     * @author Shady Mansour
     */
    public void removeUser(Integer id) {
        connections--;
        playersInLobby--;
        PlayerThread player = clients.get(id);
        game.removePlayer(id);

        if (player.getReady()) {
            readyPlayers--;
            gameSpotsLeft++;
            readyPlayersList.remove(player);
        }
        playersAdded.remove(player.getOrder());
        for (int i = 0; i < figures.size(); i++) {
            if (figures.get(i) == player.getFigure())
                figures.remove(i);
        }

        clients.remove(id);
        changeOrders(player.getOrder());
        logger.info("Removed Player with id: " + id);
        logger.info("Connected Users: " + clients.size());

    }

    /**
     * checkWelcome():
     * checks user's welcome messages and gives a corresponding response
     *
     * @param body : is the message body fetched by PlayerThread from the user's welcome
     *             JSON file
     * or an error message and ends communication
     * @author Shady Mansour
     */
    public void checkWelcome(JSONObject body, PlayerThread player) {
        JSONObject msgBody = new JSONObject();
        String type;
        int UID;

        if ((double) (long) body.get("protocol") == protocol) {
            player.setGroup((String) body.get("group"));
            player.setAi((Boolean) body.get("isAi"));
            //catch exceptions thrown by wrong messages
            UID = idGenerator();
            player.setUserId(UID);
            game.addPlayer(UID);
            addUser(UID, player);
            logger.info("Currently connected players: " + clients.keySet().size());
            msgBody.put("playerID", UID);
            type = "Welcome";
            logger.debug("Received Welcome from player "+ UID );

        } else {
            msgBody.put("error", "Sorry, the protocol you're using isn't supported by our service. You'll now be disconnected.");
            type = "Error";

        }
        player.sendMessage(type, msgBody);
        if (playersAdded.size() > 0) {
            informPlayer(player);
        }

    }

    int order = 0;
    /**
     * checkPlayerValues():
     * checks player's values and gives a corresponding response
     *
     * @param body : is the message body fetched by PlayerThread from the user's player values
     * JSON file
     * @author Vivien Pfeiffer
     */
    public void checkPlayerValues(JSONObject body, PlayerThread player) {

        JSONObject msgBody = new JSONObject();
        String type;

        String name = (String) body.get("name");
        int figure = toIntExact((long) body.get("figure"));
        if (!figures.contains(figure)) {
            player.setUsername(name);
            player.setFigure(figure);
            figures.add(figure);

            JSONObject playerX = new JSONObject();

            playerX.put("playerID", player.getUserId());
            playerX.put("name", name);
            playerX.put("figure", figure);
            msgBody.put("player", playerX);
            type = "PlayerAdded";

            player.setOrder(order);
            playersAdded.add(order++, msgBody);

            broadcast(type, msgBody, player);
            logger.info("Added player values: name: " + name + " figure: " + figure + " for id: " + player.getUserId());

        } else {
            msgBody.put("error", "Sorry, the figure is already used. Choose a different one. ");
            type = "Error";
        }
        player.sendMessage(type, msgBody);
    }

    /**
     * changeOrders():
     * called by removePlayer() to change the order (index) of all players affected
     * by removing the player with the given order
     *
     * @param order the order of the player removed
     * @author Shady Mansour
     */
    public void changeOrders(int order) {
        for (int id : clients.keySet()) {
            PlayerThread player = clients.get(id);
            int playerOrder = player.getOrder();
            if (playerOrder > order) {
                player.setOrder(playerOrder - 1);
            }
        }
        this.order--;
    }

    /**
     * checkStatus():
     * checks user's status and gives a corresponding response
     *
     * @param body : is the message body fetched by PlayerThread from the user's status
     *             JSON file
     * @author Vivien Pfeiffer
     */
    public void checkStatus(JSONObject body, PlayerThread player) {

        playersInLobby = playersAdded.size();

        JSONObject msgBody = new JSONObject();
        String type;

        player.setReady((Boolean) body.get("ready"));

        if ((Boolean) body.get("ready")) {
            readyPlayers = readyPlayers + 1;
            gameSpotsLeft = gameSpotsLeft - 1;
            readyPlayersList.add(player);
        } else {
            readyPlayers = readyPlayers - 1;
            gameSpotsLeft = gameSpotsLeft + 1;
            readyPlayersList.remove(player);
        }

        msgBody.put("playerID", player.getUserId());
        msgBody.put("ready", player.getReady());
        type = "PlayerStatus";
        setStatus.add(msgBody);
        broadcast(type, msgBody, null);
        logger.info("Player " + player.getUserId() + " status update -> " + body.get("ready"));

        if (readyPlayers == 1) {
            selectMap();
        }


        if (readyPlayers >= 2 && readyPlayers == playersInLobby && gameFieldSet && !gameIsOn) {
            startGameField(gameField);
        }
    }

    /**
     * checkStartingPoint():
     * checks user's starting point and gives a corresponding response
     *
     * @param body : is the message body fetched by PlayerThread from the user's starting point
     *             JSON file
     * or an error message
     * @author Vivien Pfeiffer
     */
    public void checkStartingPoint(JSONObject body, PlayerThread player) {

        JSONObject msgBody = new JSONObject();
        String type;

        int startingPoint = toIntExact((long) body.get("position"));
        if (game.getStartingPoints().contains(startingPoint)) {
            game.deleteStartingPoints(startingPoint);
            player.setStartingPoint(startingPoint);
            logger.debug("Player "+player.getUserId() + "chose startingPoint in field no."+ startingPoint);
            msgBody.put("playerID", player.getUserId());
            msgBody.put("position", startingPoint);
            type = "StartingPointTaken";
            broadcast(type, msgBody, null);
            game.setStartingPointTaken(true);
            player.getRobot().setStartingPosition(startingPoint);
            player.getRobot().setPosition(startingPoint);
            synchronized (game) {
                game.notifyAll();
            }

        } else {
            msgBody.put("error", "Sorry, the starting point is not valid. Choose a different one. ");
            type = "Error";
            player.sendMessage(type, msgBody);

        }

    }


    /**
     * idGenerator():
     * generates an id
     *
     * @return user id
     * @author Shady Mansour
     */
    private int idGenerator() {
        return id++;
    }


    /**
     * informPlayer():
     * sends the PlayersAdded and PlayerStatus msgBodies containing the figures ids, and status of
     * all players who connected to the server before "player". If a racing course has been set, the
     * player will also be informed
     *
     * @param player: Player's Thread requesting the messages
     * @author Shady Mansour
     */
    public void informPlayer(PlayerThread player) {
        for (JSONObject added : playersAdded) {
            JSONObject playerY = (JSONObject) added.get("player");

            if ((int) playerY.get("playerID") != player.getUserId()) {
                player.sendMessage("PlayerAdded", added);
            }
        }
        if (!setStatus.isEmpty()) {
            for (JSONObject status : setStatus) {

                if ((int) status.get("playerID") != player.getUserId()) {
                    player.sendMessage("PlayerStatus", status);
                }

            }
        }
        if (gameFieldSet) {
            JSONObject body = new JSONObject();
            body.put("map", gameField);
            player.sendMessage("MapSelected", body);
        }
    }

    /**
     * broadcast():
     * broadcasts a message to all users
     *
     * @param message      : message to be sent
     * @param excludedUser : the thread of the user who sent the message
     * @author Shady Mansour
     */
    public void broadcast(String type, JSONObject message, PlayerThread excludedUser) {
        for (Integer client : clients.keySet()) {
            if (excludedUser == null || client != excludedUser.getUserId()) {
                clients.get(client).sendMessage(type, message);
            }
        }


    }

    /**
     * chatWithUser():
     * sends a message to a selected user
     *
     * @param type:    type of the message (here: "ReceivedChat")
     * @param message: message to be sent
     * @param id:      id of the user to which the message was sent
     * @author Franziska Leitmeir
     */
    public void chatWithUser(String type, JSONObject message, int id) {
        for (Integer client : clients.keySet()) {
            if (clients.get(client).getUserId() == id) {
                clients.get(client).sendMessage(type, message);
            }
        }
    }

    /**
     * selectDamage():
     * puts the selected damageCards into the discard Pile
     * and adds them to the drawDamage List, so the JSONObject "drawDamage"
     * can be sent to the Client
     *
     * @param msgBody of msgType "SelectDamage"
     * @param player
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void selectDamage(JSONObject msgBody, PlayerThread player) {
        ArrayList<String> damageCards = new ArrayList<>();
        JSONArray JArray = (JSONArray) msgBody.get("cards");
        Iterator<String> iterator = JArray.iterator();
        while (iterator.hasNext()) {
            String card = iterator.next();
            damageCards.add(card);
        }
        for (String dmgCard : damageCards) {
            //adds the cards to the discardPile
           game.getDamageCardDecks().pop(dmgCard.toLowerCase()+"Deck", player);
            //adds them to the drawDamage List
        }
        //sends out drawDamage, after selectDamage has been received by the Server
        player.drawDamage(player.getUserId(), player.getDrawDamage());
        if (player.getPickDamage()==0) {
            player.setDamageSelected(true);
        }

        synchronized (game){
            game.notifyAll();
        }

    }

    /**
     * startGame():
     * Starts the game
     *
     * @author Shady Mansour
     */
    public void startGame() {
        game.start();
        gameIsOn = true;
        logger.info("Game started.");
    }

    /**
     * timerEnded():
     * broadcasts a JSONObject containing the ids of the players who haven't finished
     * selecting their cards before the timer ended.
     * The method is called from the SandTimer class once the timer ends.
     *
     * @author Shady Mansour
     */
    public void timerEnded() {
        game.setTimesOver(true);
        List<Integer> playerIDs = new ArrayList();
        JSONArray jarray = new JSONArray();
        for (Integer id : clients.keySet()) {
            if (clients.get(id).getSelectionFinished()) {
                continue;
            } else {
                playerIDs.add(id);
                jarray.add(id);
            }
            JSONObject msgBody = new JSONObject();
            msgBody.put("playerIDs", jarray);
            broadcast("TimerEnded", msgBody, null);
        }

        for (Integer id : playerIDs) {
            PlayerThread player = clients.get(id);
            game.discardHand(player);
        }
        synchronized (game) {
            game.notifyAll();
        }
        game.setTimer(null);
    }


    /**
     * setMap():
     * @param messageBody
     * @author Franziska Leitmeir
     */
    public void setMap(JSONArray messageBody) {

        gameField = messageBody;
        logger.debug("Setted map");
        gameFieldSet = true;

        JSONObject obj = new JSONObject();
        obj.put("map", gameField);

        broadcast("MapSelected", obj, null);
    }

    /**
     * startGameField():
     * will be called when the client selects a gameField for the game
     *
     * @param messageBody: contains the information which course the client wants to play
     * @author Franziska Leitmeir
     */
    public void startGameField(JSONArray messageBody) {

        String chosenMap = messageBody.get(0).toString();

        switch (chosenMap) {
            case "DizzyHighway":
                DizzyHighway dizzy = new DizzyHighway();
                game.setCurrentGameField(dizzy.returnGamefield());
                game.setMaptype("DizzyHighway");
                broadcast("GameStarted", game.getCurrentGameField(), null);

                StartDizzy startDizzy = new StartDizzy();
                game.setCurrentStartingBoard(startDizzy.returnStartfield());

                break;

            case "ExtraCrispy":
                ExtraCrispy crispy = new ExtraCrispy();
                game.setCurrentGameField(crispy.returnGamefield());
                game.setMaptype("ExtraCrispy");
                broadcast("GameStarted", game.getCurrentGameField(), null);

                StartCrispy startCrispy = new StartCrispy();
                game.setCurrentStartingBoard(startCrispy.returnStartfield());

                break;
        }
        logger.debug("Started Gamefield:  "+ chosenMap);
        startGame();

    }

    /**
     * fillFieldObjects():
     * extracts all special fields for the game logic and saves them in the corresponding variables
     *
     * @param wholeMap: FieldObject[][] of the selected and complete gamecourse
     * @author Vincent Oeller, Vivien Pfeiffer, Franziska Leitmeir
     */
    public void fillFieldObjects(FieldObject[][] wholeMap){     //und RestartPoint
        for (int i = 0; i < 13; i++) {                                                          //x
            for (int j = 0; j < 10; j++) {
                String t = wholeMap[i][j].getType();
                int s =wholeMap[i][j].getSpeed();

                if (t.contains("Wall") || t.contains("Pit"))
                    game.getObstacles().add(wholeMap[i][j]);

                if (t.equals("RestartPoint"))
                    game.setRestartPoint(wholeMap[i][j]);

                if (t.equals("Gear"))
                    game.getGears().add(wholeMap[i][j]);

                if (t.toLowerCase().contains("controlpoint"))
                    game.getControlPoints().add(wholeMap[i][j]);

                if (t.equals("PushPanel"))
                    game.getPushPanels().add(wholeMap[i][j]);

                if (t.toLowerCase().contains("energyspace"))
                    game.getEnergySpaces().put(wholeMap[i][j], true);

                if (t.contains("Belt")) {
                    if (s == 1) {
                        game.getGreenBelts().add(wholeMap[i][j]);
                    } else if (s == 2) {
                        game.getBlueBelts().add(wholeMap[i][j]);
                    }
                }

            }
        }
    }

    /**
     * selectMap():
     * sends the protocoll Message "SelectMap" to the first ready player
     * with the names of the camecourses the player can select
     * @author Franziska Leitmeir
     */
    public void selectMap() {
        JSONObject msgBody = new JSONObject();
        JSONArray availableMaps = new JSONArray();
        availableMaps.add("DizzyHighway");
        availableMaps.add("ExtraCrispy");
        msgBody.put("availableMaps", availableMaps);

        readyPlayersList.get(0).sendMessage("SelectMap", msgBody);

    }

    //setters and getters
    public Game getGame() {
        return this.game;
    }

    public double getProtocol() {
        return protocol;
    }

    public void setProtocol(double protocol) {
        this.protocol = protocol;
    }

    public boolean getGameIsOn() {
        return gameIsOn;
    }

    public static HashMap<Integer, PlayerThread> getClients() {
        return clients;
    }

    public static ArrayList<Integer> getFigures() {
        return figures;
    }

    public static void setFigures(ArrayList<Integer> figures) {
        Server.figures = figures;
    }

    public static ArrayList<JSONObject> getPlayersAdded() {
        return playersAdded;
    }

    public static void setPlayersAdded(ArrayList<JSONObject> playersAdded) {
        Server.playersAdded = playersAdded;
    }

    public static ArrayList<JSONObject> getSetStatus() {
        return setStatus;
    }

    public static void setSetStatus(ArrayList<JSONObject> setStatus) {
        Server.setStatus = setStatus;
    }

    public ArrayList<PlayerThread> getReadyPlayersList() {
        return readyPlayersList;
    }

    public void setReadyPlayersList(ArrayList<PlayerThread> readyPlayersList) {
        this.readyPlayersList = readyPlayersList;
    }

    public boolean isGameIsOn() {
        return gameIsOn;
    }

    public void setGameIsOn(boolean gameIsOn) {
        this.gameIsOn = gameIsOn;
    }

    public static int getPlayersInLobby() {
        return playersInLobby;
    }

    public static void setPlayersInLobby(int playersInLobby) {
        Server.playersInLobby = playersInLobby;
    }

    public static int getReadyPlayers() {
        return readyPlayers;
    }

    public static void setReadyPlayers(int readyPlayers) {
        Server.readyPlayers = readyPlayers;
    }

    public int getGameSpotsLeft() {
        return gameSpotsLeft;
    }

    public void setGameSpotsLeft(int gameSpotsLeft) {
        this.gameSpotsLeft = gameSpotsLeft;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public JSONArray getGameField() {
        return gameField;
    }

    public void setGameField(JSONArray gameField) {
        this.gameField = gameField;
    }

    public static boolean isGameFieldSet() {
        return gameFieldSet;
    }

    public static void setGameFieldSet(boolean gameFieldSet) {
        Server.gameFieldSet = gameFieldSet;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }
}
