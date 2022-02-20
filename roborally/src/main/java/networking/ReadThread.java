package networking;

import game.gameObjects.bot.Bot;
import game.gameObjects.gamefield.ExtractJSON;
import game.gameObjects.gamefield.FieldObject;
import javafx.application.Platform;
import javafx.util.Pair;
import model.DataModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;


/**
 * ReadThread:
 * the class is responsible for reading messages from the server and
 * printing it to the console
 * @author Vincent Oeller, Shady Mansour, Thomas Richter
 */
public class ReadThread extends Thread {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.User");

    private Socket socket;
    private User client;
    private DataModelManager dataModel;

    // private BufferedReader buffReader;
    private ObjectInputStream input;
    private JSONObject serverMessage;

    /**
     * Constructor:
     * initializes the variables and creates a reader to read data
     * from the server.
     *
     * @param client: the client to whom this read thread belongs
     * @param socket: the socket on which the connection is established
     */
    public ReadThread(User client, Socket socket) {
        this.client = client;
        this.socket = socket;
        this.dataModel = client.getDataModel();
        try {
            input = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ioEx) {
            logger.fatal("Error getting input stream: " + ioEx.getMessage());
            ioEx.printStackTrace();
        }
    }

    /**
     * run():
     * it runs in a while loop until the user disconnects,
     * gets the messages from the server and prints it to the console
     */
    public void run() {
        JSONParser parser = new JSONParser();
        while (true) {
            try {

                String response = (String) input.readObject();
                serverMessage = (JSONObject) parser.parse(response);
                getPayload(serverMessage);

            } catch (EOFException | OptionalDataException e) {

            }catch (IOException ioEx) {
                ioEx.printStackTrace();
                logger.error("Server is down. Please try again later. " + ioEx.getMessage());
                return;
            }catch (Exception e){
                e.printStackTrace();
                logger.error("Parse Exception: " + e.getMessage() );
                return;
            }
        }
    }

    /**
     * @param file: the JSONObject, containing all the information
     * getPayload() takes an JSONObject and extracts the information inside the
     * MessageBody and changes the variables inside the DataModelManager of the Client
     * @author Vincent Oeller, Shady Mansour, Thomas Richter
     */
    public void getPayload(JSONObject file) {
        String msgType = file.get("messageType").toString();
        JSONObject msgBody = (JSONObject) file.get("messageBody");
        switch (msgType) {
//2.
            case "HelloClient":
                helloClient(msgBody);
                break;
            case "Welcome":
                welcome(msgBody);
                break;
//3.
            case "PlayerAdded":
                playerAdded(msgBody);
                break;
            case "PlayerStatus":
                playerStatus(msgBody);
                break;
            case "SelectMap":
                selectMap(msgBody);
                break;
            case "MapSelected":
                mapSelected(msgBody);
                if(client instanceof Bot) {
                    ((Bot) client).checkMessage(msgType, msgBody);
                }else if(client.getAI()){
                    client.getBot().checkMessage(msgType, msgBody);
                }
                break;
//4.
            case "ReceivedChat":
                receivedChat(msgBody);
                break;
            case "GameStarted":
                gameStarted(msgBody);
                break;
//5.
            case "Error":
                errorMessage(msgBody);
                break;
            case "ConnectionUpdate":
                connectionUpdate((JSONObject)file.get("messageBody"));
                break;
//7.
            case "CurrentPlayer":
                currentPlayer(msgBody);
                break;
            case "ActivePhase":
                activePhase((JSONObject)file.get("messageBody"));
                break;
            case "StartingPointTaken":
                startingPointTaken(msgBody);
                break;
            case "YourCards":
                yourCards(msgBody);
                break;
            case "NotYourCards":
                notYourCards(msgBody);
                break;
            case "ShuffleCoding":
                shuffleCoding(msgBody);
                break;
            case "CardSelected":
                cardSelected(msgBody);
                break;
            case "TimerStarted":
                timerStarted();
                break;
            case "TimerEnded":
                timerEnded(msgBody);
                break;
            case "DiscardHand":
                discardHand(msgBody);
                break;
            case "CardsYouGotNow":
                cardsYouGotNow(msgBody);
                break;
            case "CurrentCards":
                currentCards(msgBody);
                break;
//8.
            case "Movement":
                movement(msgBody);
                break;
            case "DrawDamage":
                drawDamage(msgBody);
                break;
            case "PickDamage":
                pickDamage(msgBody);
                break;
            case "PlayerShooting":
                playerShooting();
                break;
            case "Damaged":
                damaged(msgBody);
                break;
            case "Reboot":
                reboot(msgBody);
                break;
            case "PlayerTurning":
                playerTurning(msgBody);
                break;
            case "Energy":
                energy(msgBody);
                break;
            case "CheckpointReached":
                checkpointReached(msgBody);
                break;
            case "GameWon":
                gameWon(msgBody);
                break;
            case "SelectionFinished":
                if(client instanceof Bot) {
                    ((Bot) client).checkMessage(msgType, msgBody);
                }else if(client.getAI()){
                    client.getBot().checkMessage(msgType, msgBody);
                }
                break;

        }
        logger.info("Received \"" + msgType+"\"");
    }


    /**
     * connectionUpdate():
     * updates the dataModel according to the action taken by the server.
     *
     * @author Shady Mansour
     * @param messageBody the msgBody of the JSON file received from server
     */
    private void connectionUpdate(JSONObject messageBody) {
        String action = (String) messageBody.get("action");
        Integer playerID = Integer.parseInt(messageBody.get("playerID").toString());

        switch (action){
            case "Remove":
                dataModel.players.remove(playerID);

                if(dataModel.playerIdTwonky != null && dataModel.playerIdTwonky.intValue() ==playerID.intValue()){
                    dataModel.playerNameTwonky = null;
                    dataModel.playerIdTwonky = null;
                    dataModel.availableTwonky = true;
                break;
                }else if(dataModel.playerIdHulkX90 != null && dataModel.playerIdHulkX90.intValue() ==playerID.intValue()) {
                    dataModel.playerNameHulkX90 = null;
                    dataModel.playerIdHulkX90 = null;
                    dataModel.availableHulkX90 = true;
                    break;
                } else if(dataModel.playerIdHammerBot != null && dataModel.playerIdHammerBot.intValue() ==playerID.intValue()){
                    dataModel.playerNameHammerBot = null;
                    dataModel.playerIdHammerBot = null;
                    dataModel.availableHammerBot = true;
                    break;
                }else if(dataModel.playerIdSmashBot != null && dataModel.playerIdSmashBot.intValue() ==playerID.intValue()) {
                    dataModel.playerNameSmashBot = null;
                    dataModel.playerIdSmashBot = null;
                    dataModel.availableSmashBot = true;
                    break;
                }else if(dataModel.playerIdZoomBot != null && dataModel.playerIdZoomBot.intValue() ==playerID.intValue()){
                    dataModel.playerNameZoomBot = null;
                    dataModel.playerIdZoomBot = null;
                    dataModel.availableZoomBot = true;
                    break;
                }else if(dataModel.playerIdSpinBot != null && dataModel.playerIdSpinBot.intValue() ==playerID.intValue()){
                    dataModel.playerNameSpinBot = null;
                    dataModel.playerIdSpinBot = null;
                    dataModel.availableSpinBot = true;
                    break;
                }
            case "AIControl":
                break;
            default:
                break;

        }
        logger.info("Player " +playerID +" connection update. Action -> " +action);

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * helloClient():
     * sets ServerProtocol in the DatamodelManager and sends out helloServer
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void helloClient (JSONObject file) {
        double serverProtocol = (double) file.get("protocol");
        client.setServerProtocol(serverProtocol);
        client.getWrite().helloServer(dataModel.protocol, dataModel.group, dataModel.isAI);
    }


    /**
     * welcome():
     * Method extracts the information whether the connection between client and server was
     * successful. Based on that evaluation the Boolean connectionAccepted at the Client instantiation
     * is set either to true or false
     * @author Vincent Oeller
     */
    public void welcome (JSONObject msgBody) {
        // Implementation here
        int id = Integer.parseInt(msgBody.get("playerID").toString()) ;
        dataModel.playerID = id;
        if(client.getAI()){
            client.getBot().setUserID(id);
        }
        Platform.runLater(()-> {

            client.setConnectionAccepted(true);
            dataModel.updateViewModels();
        });

        //client.getDataModel().updateViewModels();
    }


    /**
     * once the server accepts "PlayerValues" it sends out a JSONObject with id, name and figure
     * to all Clients. This function sets your own name and figure(if you were the client who
     * sent the "PlayerValues" Object), and if it was someone else this function just sets
     * playerName*Robot*, playerID*Robot* and available*Robot* in every dataModel.
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void playerAdded (JSONObject file) {

        JSONObject player = (JSONObject) file.get("player");

        Integer playerID = get_playerID(player);
        String name = playerAdded_name(file);
        int figure = playerAdded_figure(file);


        if(dataModel.playerID == playerID){  // name and figure is set by server
            dataModel.name = name;
            dataModel.figure = figure;
            dataModel.setPlayerAdded(true);
        } else {
            dataModel.players.put(playerID,name);
        }

        switch (figure) {                  // figure is received as int
            case 1:
                dataModel.playerNameTwonky = name;
                dataModel.playerIdTwonky = playerID;
                dataModel.availableTwonky = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().equalsIgnoreCase("twonky") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }
                break;
            case 2:
                dataModel.playerNameHulkX90 = name;
                dataModel.playerIdHulkX90 = playerID;
                dataModel.availableHulkX90 = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().equalsIgnoreCase("hulkx90") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }
                break;
            case 3:
                dataModel.playerNameHammerBot = name;
                dataModel.playerIdHammerBot = playerID;
                dataModel.availableHammerBot = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().equalsIgnoreCase("hammerbot") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }
                break;
            case 4:
                dataModel.playerNameSmashBot = name;
                dataModel.playerIdSmashBot = playerID;
                dataModel.availableSmashBot = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().equalsIgnoreCase("smashbot") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }
                break;
            case 5:
                dataModel.playerNameZoomBot = name;
                dataModel.playerIdZoomBot = playerID;
                dataModel.availableZoomBot = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().toLowerCase().equals("zoombot") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }

                break;
            case 6:
                dataModel.playerNameSpinBot = name;
                dataModel.playerIdSpinBot = playerID;
                dataModel.availableSpinBot = false;
                if (!(dataModel.getRobot() == null)) {
                    if (dataModel.getRobot().toLowerCase().equals("spinbot") && !(dataModel.playerID == playerID)) {
                        dataModel.setRobot(null);
                    }
                }

                break;
        }

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
        logger.info("PlayerAdded: Name: " + name + ", ID: " + playerID + ", Figure: " + figure  );
    }
    public String playerAdded_name(JSONObject msgBody){
        JSONObject player = (JSONObject) msgBody.get("player");
        String playerID = player.get("name").toString() ;
        return playerID;
    }
    public static int playerAdded_figure(JSONObject msgBody){
        JSONObject player = (JSONObject) msgBody.get("player");
        int playerID = Integer.parseInt(player.get("figure").toString()) ;
        return playerID;
    }

    /**
     * sets readyIDs and readyPlayers in the DatamodelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void playerStatus(JSONObject file){

        int playerID = Integer.parseInt(file.get("playerID").toString());
        boolean isReady = playerStatus_ready(file);
        if (dataModel.playerID == playerID){
            dataModel.readyToPlay = isReady;
            logger.info("Updated status-> "+isReady);
        }

        if (isReady) {
            dataModel.readyIDs.put(playerID, isReady);
            dataModel.readyPlayers = dataModel.readyPlayers + 1;
            logger.info("Player " + playerID+ " updated his status-> "+isReady);

            if (playerID == dataModel.playerID) {
                logger.info("Own Player added to readyPlayers");
            }

        } else if (!isReady) {
            dataModel.readyIDs.remove(playerID);
            dataModel.readyPlayers = dataModel.readyPlayers -1;
            logger.info("Player " + playerID+ " updated his status-> "+isReady);
        }



        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }
    public boolean playerStatus_ready(JSONObject msgBody){
        boolean ready = Boolean.parseBoolean(msgBody.get("ready").toString());
        return  ready;
    }

    /**
     * selectMap(JSONObject)
     * Realizes the selectMap MessageType of protocol which is sent from the server to the client.
     * @param msgBody
     * @author Thomas Richter
     */
    public void selectMap(JSONObject msgBody){
        String[] maps = new String[2];
        int postion = 0;
        JSONArray JArray = (JSONArray) msgBody.get("availableMaps");
        Iterator<String> iterator = JArray.iterator();
        while (iterator.hasNext()){
            String map = iterator.next();
            dataModel.availableMaps[postion] = map;
            postion++;

        }
        if(client.getAI()){
            client.getBot().checkMessage("SelectMap",msgBody);
        }

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }

    /**
     * mapSelected(JSONObject)
     * Realizes the mapSelected messageType of protocol which sends the selected racing course to the server
     * @param msgBody
     * @author Thomas Richter
     */
    public void mapSelected(JSONObject msgBody){

        JSONArray messageBody = (JSONArray) msgBody.get("map");
        String map = (String) messageBody.get(0);

        dataModel.racingCourse = map;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }

    /**
     * gameStarted():
     * extracts the JSON file from Server and converts it to an FieldObject[][]. It calculates the
     * Lasers and saves everything in the DataModelManager
     *
     * @param file
     * @author Franziska Leitmeir
     */
    public void gameStarted (JSONObject file) {

        FieldObject [][] temporaryField = new FieldObject[10][10];
        ExtractJSON fileExtract = new ExtractJSON(file);

        for (int i = 0; i <100;  i++) {

            FieldObject field = fileExtract.extractMap(i);
            int x = field.getX();
            int y = field.getY();
            String t = field.getType();
            int s = field.getSpeed();
            double o = field.getOrientation();
            ArrayList<String> os = field.getOrientations();
            int c = field.getCounter();
            boolean iC = field.getIsCrossing();

            FieldObject pField = new FieldObject(x, y, t, s, o, os, c, iC);

            temporaryField[x][y] = pField;

            dataModel.gameMap[x][y] = pField;

        }
        //checks all fields for Lasers and fills the next field with the right type for the right picture
        for (int x = 0; x < 10;  x++) {
            for (int y = 0; y < 10; y ++) {
                if (temporaryField[x][y].getType().contains("Laser")) {
                    double orientation = temporaryField[x][y].getOrientation();
                    checkNextField(temporaryField, x, y, orientation);

                }
            }
        }

        for (int x = 0; x < 10;  x++) {
            for (int y = 0; y < 10; y ++) {
                if (temporaryField[x][y].getType().contains("Wall")) {
                    double orientation = temporaryField[x][y].getOrientation();
                    if (orientation == 0) {
                        dataModel.walls [x + 4][y+ 1] = "east";
                    } else if (orientation == 90.0) {
                        dataModel.walls [x + 4][y + 1] = "south";
                    } else if (orientation == 180.0) {
                        dataModel.walls [x + 4][y + 1] = "west";
                    } else if (orientation == -90.0) {
                        dataModel.walls [x+ 4][y + 1] = "north";
                    }
                }
            }
        }

        dataModel.gameStarted = true;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }

    /**
     * checkNextField():
     * calculates the next field the laser will hit and returns the new FieldObject at this position with a new type
     * @param fieldObjects: the ArrayList of FieldObjects
     * @param x: current xPosition where the laser is
     * @param y: current yPosition where the laser is
     * @param o: current orientation the laser got
     * @return the FieldObject with the new type
     */
    public FieldObject checkNextField(FieldObject[][] fieldObjects, int x, int y, double o){
        FieldObject nextField = fieldObjects[x][y];
        String type;
        switch ((int) o) {
            //right
            case 0:
                nextField = fieldObjects[x-1][y];
                type = nextField.getType();
                if (type.contains("ControlPoint")) {

                }else if (type.contains("Laser")) {

                }

                else if (type.contains("Wall")) {
                        type = "LWall";
                        nextField.setType(type);
                        dataModel.gameMap[x-1][y] = nextField;
                    }
                else if (type.contains("Empty")) {
                        type = "L";
                        nextField.setType(type);
                        nextField.setOrientation(o);
                        dataModel.gameMap[x-1][y] = nextField;
                        checkNextField (fieldObjects, x-1, y, o);
                    }
                else if (type.contains("Belt")) {
                        type = "BeltL";
                        nextField.setType(type);
                        dataModel.gameMap[x-1][y] = nextField;
                        checkNextField (fieldObjects, x-1, y, o);
                    }
                break;

            // down
            case 90:
                nextField = fieldObjects[x][y-1];
                type = nextField.getType();
                if (type.contains("ControlPoint")) {

                }else if (type.contains("Laser")) {

                }
                else if (type.contains("Wall")) {
                    type = "LWall";
                    nextField.setType(type);
                    dataModel.gameMap[x][y-1] = nextField;
                }
                else if (type.contains("Empty")) {
                    type = "L";
                    nextField.setType(type);
                    nextField.setOrientation(o);
                    dataModel.gameMap[x][y-1] = nextField;
                    checkNextField (fieldObjects, x, y-1, o);
                }
                else if (type.contains("Belt")) {
                    type = "BeltL";
                    nextField.setType(type);
                    dataModel.gameMap[x][y-1] = nextField;
                    checkNextField (fieldObjects, x, y-1, o);
                }

                break;

            //left
            case 180:
                nextField = fieldObjects[x+1][y];
                type = nextField.getType();
                if (type.contains("ControlPoint")) {

                }
                else if (type.contains("Laser")) {

                }
                else if (type.contains("Wall")) {
                    type = "LWall";
                    nextField.setType(type);
                    dataModel.gameMap[x+1][y] = nextField;
                }
                else if (type.contains("Empty")) {
                    type = "L";
                    nextField.setType(type);
                    nextField.setOrientation(o);
                    dataModel.gameMap[x+1][y] = nextField;
                    checkNextField (fieldObjects, x+1, y, o);
                }
                else if (type.contains("Belt")) {
                    type = "BeltL";
                    nextField.setType(type);
                    dataModel.gameMap[x+1][y] = nextField;
                    checkNextField (fieldObjects, x+1, y, o);
                }
                break;

            // up
            case -90:
                nextField = fieldObjects[x][y+1];
                type = nextField.getType();
                if (type.contains("ControlPoint")) {

                }else if (type.contains("Laser")) {

                }
                else if (type.contains("Wall")) {
                    type = "LWall";
                    nextField.setType(type);
                    dataModel.gameMap[x][y+1] = nextField;
                }
                else if (type.contains("Empty")) {
                    type = "L";
                    nextField.setType(type);
                    nextField.setOrientation(o);
                    dataModel.gameMap[x][y+1] = nextField;
                    checkNextField (fieldObjects, x, y+1, o);
                }
                else if (type.contains("Belt")) {
                    type = "BeltL";
                    nextField.setType(type);
                    dataModel.gameMap[x][y+1] = nextField;
                    checkNextField (fieldObjects, x, y+1, o);
                }
                    }
        return nextField;
    }

    /**
     * sets chatHistory in the DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller, Thomas Richter
     */
    public void receivedChat(JSONObject file){
        String message = receivedChat_message(file);
        String from = receivedChat_from(file);
        boolean privateMsg = receivedChat_private(file);
        String isPrivate;
        if (privateMsg){ isPrivate = "private";
        }else{isPrivate ="public";}
        dataModel.chatHistory.replaceAll("(?m)^[ \t]*\r?\n", "");
        dataModel.chatHistory += ("[" + from + "]: " + "(" + isPrivate + ") " + message + "\n");
        dataModel.chatHistory.replaceAll("\n", "");

        dataModel.newChat = true;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });


    }
    public String receivedChat_message(JSONObject msgBody){
        String message = msgBody.get("message").toString();
        return message;
    }
    public static String receivedChat_from(JSONObject msgBody){
        String from = msgBody.get("from").toString();
        return from;
    }
    public boolean receivedChat_private(JSONObject msgBody){
        boolean privateChat = Boolean.parseBoolean(msgBody.get("private").toString());
        return  privateChat;
    }

    /**
     * writes the error message in the chat
     * @param msgbody of the JSONObject
     * @author Vincent Oeller
     */
    public void errorMessage(JSONObject msgbody){
        //schreibt die error Message in den Chat
        String errorMsg = error_error(msgbody);
        dataModel.chatHistory += ("[ERROR]: " + errorMsg + "\n");
        dataModel.receivedError = true;
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    public String error_error(JSONObject msgBody) {
        String error = msgBody.get("error").toString();
        return error;
    }

    /**
     * sets currentPlayer in the Datamodelmanager
     * @param msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void currentPlayer(JSONObject msgBody){

        int playerID = get_playerID(msgBody);
        dataModel.currentPlayer = playerID;

        logger.info("Current Player: " + playerID);


        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

        if(client.getAI()){
            client.getBot().checkMessage("CurrentPlayer",msgBody);
        }
    }

    /**
     * sets activePhase in the DataModelmanager
     * @param msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void activePhase(JSONObject msgBody){
        int phase = activePhase_phase(msgBody);
        dataModel.activePhase = phase;

        logger.info(dataModel.robot + " got Active Phase: " + phase);
        dataModel.updateForActivePhase();

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
        if(client instanceof Bot){
            ((Bot)client).checkMessage("ActivePhase",msgBody);
        }else if(client.getAI()){
        client.getBot().checkMessage("ActivePhase",msgBody);
        }
    }
    public int activePhase_phase(JSONObject msgBody){
        int phase = Integer.parseInt(msgBody.get("phase").toString());
        return phase;
    }

    /**
     * sets startingPoints in DataModelManager
     * @param file
     * @author Vincent Oeller, Thomas Richter
     */
    public void startingPointTaken (JSONObject file){
        int playerID = get_playerID(file);
        int position = startingPointTaken_position(file);
        dataModel.startingPoints.put(playerID,position);

        Pair <Double, Double> currentPosition = getStartingPoint(position);

        if (dataModel.playerIdTwonky != null && dataModel.playerIdTwonky == playerID) {
            dataModel.positionTwonky = currentPosition;

        } else if (dataModel.playerIdHulkX90 != null &&dataModel.playerIdHulkX90 == playerID) {
            dataModel.positionHulkX90 = currentPosition;

        } else if (dataModel.playerIdHammerBot != null &&dataModel.playerIdHammerBot == playerID) {
            dataModel.positionHammerBot = currentPosition;

        } else if (dataModel.playerIdSmashBot != null &&dataModel.playerIdSmashBot == playerID) {
            dataModel.positionSmashBot = currentPosition;

        } else if (dataModel.playerIdZoomBot != null &&dataModel.playerIdZoomBot == playerID) {
            dataModel.positionZoomBot = currentPosition;

        } else if (dataModel.playerIdSpinBot != null &&dataModel.playerIdSpinBot == playerID) {
            dataModel.positionSpinBot = currentPosition;

        }

        if (playerID == dataModel.playerID) {
            dataModel.ownPosition = position;
            dataModel.settedStartingPoint = true;
            if(client.getAI()){
                client.getBot().setStartingPoint(position);
            }else if(client instanceof Bot){
                ((Bot)client).setStartingPoint(position);
            }
        } else {
            if(client.getAI()){
                client.getBot().getTakenStartingPoints().add(position);
            }else if(client instanceof Bot){
                ((Bot)client).getTakenStartingPoints().add(position);
            }
        }

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    public int startingPointTaken_position(JSONObject msgBody){
        int position = Integer.parseInt(msgBody.get("position").toString());
        return position;
    }

    /**
     * sets programmingCards and programmingCardsInitial in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void yourCards (JSONObject file){
        String[] cards = yourCards_cards(file);
        dataModel.setProgrammingCards(cards);
        dataModel.setProgrammingCardsInitial(cards);
                dataModel.currentRegister = 0;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
        if(client instanceof Bot) {
            ((Bot) client).chooseCards(cards);
        }else if(client.getAI()){
                client.getBot().chooseCards(cards);
            }
    }
    public static String[] yourCards_cards (JSONObject msgBody){
        String[] cardsArr = new String[9];
        int postion = 0;
        JSONArray JArray = (JSONArray) msgBody.get("cards");
        Iterator<String> iterator = JArray.iterator();
        while (iterator.hasNext()){
            String card = iterator.next();
            cardsArr[postion] = card;
            postion++;
        }
        return cardsArr;
    }

    /**
     * sets notYourCards in the DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void notYourCards (JSONObject file){
        int playerID = get_playerID(file);
        int cards = notYourCards_cards(file);

        dataModel.notYourCards.put(playerID,cards);
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    public int notYourCards_cards(JSONObject msgBody){
        int cards = Integer.parseInt(msgBody.get("cards").toString());
        return cards;
    }

    /**
     * sets shuffleCards in the DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void shuffleCoding (JSONObject file){
        int playerID = get_playerID(file);
        if(playerID == dataModel.playerID) {
            dataModel.shuffleCards = true;
            logger.info("I shuffled");
        }

        logger.info(playerID + " shuffled");

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }


    /**
     * the HashMaps for each robot contains the 5 registers as keys and a bool
     * (register is used/empty).
     * this function fills the HashMaps
     * @param file the msgBody of the JSON file
     * @author Vincent Oeller
     */
    public void cardSelected (JSONObject file){
        int playerID = get_playerID(file);
        int register = cardSelected_register(file);
        if (dataModel.playerIdTwonky!= null && playerID == dataModel.playerIdTwonky){
            dataModel.cardSelectedTwonky.put(register,true);
        }
        else if (dataModel.playerIdHulkX90!= null && playerID == dataModel.playerIdHulkX90){
            dataModel.cardSelectedHulkX90.put(register,true);
        }
        else if (dataModel.playerIdHammerBot!= null && playerID == dataModel.playerIdHammerBot){
            dataModel.cardSelectedHammerBot.put(register,true);
        }
        else if (dataModel.playerIdSmashBot!= null && playerID == dataModel.playerIdSmashBot){
            dataModel.cardSelectedSmashBot.put(register,true);
        }
        else if (dataModel.playerIdSpinBot!= null && playerID == dataModel.playerIdSpinBot){
            dataModel.cardSelectedSpinBot.put(register,true);
        }
        else if (dataModel.playerIdZoomBot!= null && playerID == dataModel.playerIdZoomBot){
            dataModel.cardSelectedZoomBot.put(register,true);
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    public int cardSelected_register(JSONObject msgBody){
        int register = Integer.parseInt(msgBody.get("register").toString());
        return register;
    }

    /**
     * sets timerStarted in DataModelManager to true
     * @author Vincent Oeller
     */
    public void timerStarted (){
        dataModel.timerStarted = true;
        logger.info("Timer Started from Server: " + dataModel.timerStarted);
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * sets timerStarted in DataModelManager to false
     * and sets timerEndedPlayers in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void timerEnded (JSONObject file){
        int[]latePlayers = timerEnded_playerIDs(file);
        dataModel.timerStarted = false;
        dataModel.timerEndedPlayers = latePlayers;
        logger.info("Timer Ended from Server: " + dataModel.timerStarted);
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    public static int[] timerEnded_playerIDs (JSONObject msgBody){
        int[] latePlayers = new int[5];
        int postion = 0;
        JSONArray JArray = (JSONArray) msgBody.get("playerIDs");
        Iterator<Long> iterator = JArray.iterator();
        while (iterator.hasNext()){
            int playerID = Integer.parseInt(iterator.next().toString());
            latePlayers[postion] = playerID;
            postion++;
        }
        return latePlayers;         //man muss die 0er am Ende des Arrays ignorieren
    }

    /**
     *
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void discardHand (JSONObject file){
        int playerID = get_playerID(file);

        dataModel.timerStarted = false;
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * sets myRegisters in DataModelManager
     * @param file of "CardsYouGotNow"
     * @author Vincent Oeller
     */
    public void cardsYouGotNow (JSONObject file){
        String[] registers = cardsYouGotNow_cards(file);
        dataModel.myRegisters = registers;

        dataModel.shuffledCards = true;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * @param msgBody of "CardsYouGotNow"
     * @return a String[] (length 5) containing the information of hte "cards" JSONArray
     */
    public static String[] cardsYouGotNow_cards(JSONObject msgBody){
        String[] cardsArr = new String[5];
        int postion = 0;
        JSONArray JArray = (JSONArray) msgBody.get("cards");
        Iterator<String> iterator = JArray.iterator();
        while (iterator.hasNext()){
            String card = iterator.next();
            cardsArr[postion] = card;
            postion++;
        }
        return cardsArr;
    }

    /**
     * sets currentCards in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void currentCards (JSONObject file){
        HashMap<Integer,String> currentCards = currentCards_activeCards(file);
        dataModel.currentCards = currentCards;

        dataModel.saveCurrentCards();

        if (dataModel.activePhase != 3) {
            dataModel.activePhase = 3;
        }
        if (dataModel.timerStarted) {
            dataModel.timerStarted = false;
        }
        if(client instanceof Bot) {
            ((Bot) client).checkMessage("CurrentCards",file);
        }else if(client.getAI()){
            client.getBot().checkMessage("CurrentCards",file);
        }

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    /**
     * @param msgBody of "CurrentCards"
     * @author Vincent Oeller
     * @return a HashMap containing the information of hte "activeCards" JSONArray
     */
    public static HashMap<Integer, String> currentCards_activeCards(JSONObject msgBody) {
        HashMap<Integer, String> currentCards = new HashMap<>();
        JSONArray JArray = (JSONArray) msgBody.get("activeCards");
        Iterator<JSONObject> iterator = JArray.iterator();
        while(iterator.hasNext()){
            JSONObject content = iterator.next();
            int playerID = Integer.parseInt(content.get("playerID").toString());
            String card = content.get("card").toString();
            currentCards.put(playerID,card);
        }
        return currentCards;
    }


    //8. Aktionen, Ereignisse, Effekte
    /**
     * calls updatePositions() in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void movement (JSONObject file){
        int playerID = get_playerID(file);
        int to = movement_To(file);

        dataModel.updatePositions(playerID, to);
        //dataModel.movement.put(playerID,to);

        if(client.getAI()&&playerID==dataModel.playerID){
            client.getBot().setPosition(to);
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }
    public int movement_To(JSONObject msgBody){
        int to = Integer.parseInt(msgBody.get("to").toString());
        return to;
    }

    /**
     * drawDamage extracts the playerID and the JSONArray full of damageCards
     * and writes them into an ArrayList (damageCards).
     * then it loops thorugh the ArrayList and put the damage Cards in the
     * corresponding DataModelManager.damageCards*robot*
     * @param file: messageBody of type "DrawDamage"
     * @author Vincent Oeller, Thomas Richter
     */
    public void drawDamage (JSONObject file){
        int playerID = get_playerID(file);
        ArrayList<String> dmgCards = drawDamage_cards(file);

        if (playerID == dataModel.playerID) {
            int spamCount = 0;
            int virusCount = 0;
            int trojanCount = 0;
            int wormCount = 0;

            for (int i = 0; i < dmgCards.size(); i++) {
                if (dmgCards.get(i).equalsIgnoreCase("Spam")) {
                    spamCount = spamCount + 1;
                } else if (dmgCards.get(i).equalsIgnoreCase("Virus")) {
                    virusCount = virusCount + 1;
                } else if (dmgCards.get(i).equalsIgnoreCase("Trojan")) {
                    trojanCount = trojanCount + 1;
                } else if (dmgCards.get(i).equalsIgnoreCase("Worm")) {
                    wormCount = wormCount + 1;
                }
            }

            dataModel.drawnDamageCardsCount[0] = spamCount;
            dataModel.drawnDamageCardsCount[1] = virusCount;
            dataModel.drawnDamageCardsCount[2] = trojanCount;
            dataModel.drawnDamageCardsCount[3] = wormCount;

            dataModel.showDrawDamage = true;

            Platform.runLater(()-> {
                dataModel.updateViewModels();
            });

        }

        if (!(dataModel.playerIdTwonky == null) && playerID == dataModel.playerIdTwonky){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsTwonky.add(dmgCards.get(i));
            }

        }
        else if (!(dataModel.playerIdHulkX90 == null) && playerID == dataModel.playerIdHulkX90){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsHulkx90.add(dmgCards.get(i));
            }
        }
        else if (!(dataModel.playerIdHammerBot == null) && playerID == dataModel.playerIdHammerBot){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsHammerBot.add(dmgCards.get(i));
            }
        }
        else if (!(dataModel.playerIdSmashBot == null) && playerID == dataModel.playerIdSmashBot){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsSmashBot.add(dmgCards.get(i));
            }
        }
        else if (!(dataModel.playerIdSpinBot == null) && playerID == dataModel.playerIdSpinBot){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsSpinBot.add(dmgCards.get(i));
            }
        }
        else if (!(dataModel.playerIdZoomBot == null) && playerID == dataModel.playerIdZoomBot){
            for (int i = 0; i < dmgCards.size(); i++) {
                dataModel.damageCardsZoomBot.add(dmgCards.get(i));
            }
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

        dataModel.damageCardsTwonky.clear();
        dataModel.damageCardsHulkx90.clear();
        dataModel.damageCardsHammerBot.clear();
        dataModel.damageCardsSmashBot.clear();
        dataModel.damageCardsZoomBot.clear();
        dataModel.damageCardsSpinBot.clear();
    }
    public static ArrayList<String> drawDamage_cards (JSONObject msgBody){
        ArrayList<String> damageCards = new ArrayList<>();
        JSONArray JArray = (JSONArray) msgBody.get("cards");
        Iterator<String> iterator = JArray.iterator();
        while (iterator.hasNext()){
            String card = iterator.next();
            damageCards.add(card);
        }
        return damageCards;
    }

    /**
     * sets pickDamage and damageToPick in DataModelManager
     * @param file
     * @author Vincent Oeller
     */
    public void pickDamage (JSONObject file){
        int pickDamage = get_count(file);
        dataModel.pickDamage = pickDamage;
        dataModel.damageToPick = pickDamage;
        if(client instanceof Bot) {
            ((Bot) client).checkMessage("PickDamage",file);
        }else if(client.getAI()) {
            client.getBot().checkMessage("PickDamage",file);
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * Part of protocol, but not needed.
     * @author Vincent Oeller
     */
    public void playerShooting () {             //nur fuer Animationen
        dataModel.playerShooting = true;
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * @param file msgBody of the JSONObject
     */
    public void reboot (JSONObject file){
        int playerID = get_playerID(file);
        Arrays.fill(dataModel.myRegisters, "LockedRegister");   //alle register werden geleert beim reboot
        dataModel.reboot.put(playerID, true);

        if (!(dataModel.playerIdTwonky == null) && dataModel.playerIdTwonky.equals(playerID)) {
            dataModel.rotationTwonky = 270.0;
        } else if (!(dataModel.playerIdHulkX90 == null) && dataModel.playerIdHulkX90.equals(playerID)){
            dataModel.rotationHulkX90 = 270.0;
        } else if (!(dataModel.playerIdHammerBot == null) && dataModel.playerIdHammerBot.equals(playerID)){
            dataModel.rotationHammerBot = 270.0;
        } else if (!(dataModel.playerIdSmashBot== null) && dataModel.playerIdSmashBot.equals(playerID)){
            dataModel.rotationSmashBot = 270.0;
        } else if (!(dataModel.playerIdZoomBot == null) && dataModel.playerIdZoomBot.equals(playerID)){
            dataModel.rotationZoomBot = 270.0;
        } else if (!(dataModel.playerIdSpinBot == null) && dataModel.playerIdSpinBot.equals(playerID)){
            dataModel.rotationSpinBot = 270.0;
        }

        if(client.getAI()&&playerID==dataModel.playerID){
            client.getBot().reboot();
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * calls updateRotations() in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void playerTurning (JSONObject file){
        int playerID = get_playerID(file);
        String direction = playerTurning_direction(file);

        dataModel.updateRotations(playerID, direction);
        if(client.getAI()&&playerID==dataModel.playerID){
            client.getBot().setOrientation(direction);
        }


        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }
    public String playerTurning_direction(JSONObject msgBody){
        String direction = msgBody.get("direction").toString();
        return direction;
    }

    /**
     * damaged(JSONObject)
     * Addition to normal protocol. Gets information from Server when a robot got damaged so that
     * it can be displayed in the GUI
     * @param msgBody msgBody of the JSONObject
     * @author Thomas Richter
     */
    public void damaged(JSONObject msgBody) {
        int playerID = Integer.parseInt(msgBody.get("playerID").toString());
        dataModel.setRobotDamage(playerID);

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }

    /**
     * sets energy in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void energy (JSONObject file){
        int playerID = get_playerID(file);
        int count = get_count(file);

        if (dataModel.energy.containsKey(playerID)) {
            int oldValue = dataModel.energy.get(playerID);
            count = count + oldValue;
            dataModel.energy.replace(playerID, count);
        } else {
            dataModel.energy.put(playerID,5 + count);
        }

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * sets checkpointReached in DataModelManager
     * @param file msgBody of the JSONObject
     * @author Vincent Oeller
     */
    public void checkpointReached (JSONObject file){
        int playerID = get_playerID(file);
        int number = checkpoint_number(file);
        if(client.getAI()&&playerID==dataModel.playerID){
            client.getBot().checkMessage("CheckpointReached" ,file);
        }

        dataModel.checkpointReached.put(playerID,number);


        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * @author Vincent Oeller
     */
    public int checkpoint_number(JSONObject msgBody){
        int number = Integer.parseInt(msgBody.get("number").toString());
        return number;
    }

    /**
     * sets gameWonPlayerID in DataModelManager
     * @param file
     * @author Vincent Oeller
     */
    public void gameWon (JSONObject file){
        int playerID = get_playerID(file);

        dataModel.gameWonPlayerID = playerID;

        if (dataModel.playerID == playerID) {
            dataModel.gameWon = true;
            logger.debug("GAME WON");
        } else {
            dataModel.gameLost = true;
            logger.debug("GAME LOST");
        }


        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * this function is called whenever the "playerID" has to be extracted out of a JSON file
     * @param msgBody msgBody of the JSONObject
     * @return the "playerID" thats inside the JSONObject
     * @author Vincent Oeller
     */
    public int get_playerID(JSONObject msgBody){
        int playerID = Integer.parseInt(msgBody.get("playerID").toString());
        return playerID;
    }

    /**
     * this function is called whenever the "count" has to be extracted out of a JSON file
     * @param msgBody msgBody of the JSONObject
     * @return the "count" thats inside the JSONObject
     */
    public int get_count(JSONObject msgBody){
        int count = Integer.parseInt(msgBody.get("count").toString());
        return count;
    }

    /**
     * getStartingPoint(int)
     * Gets the information from Server which StartingPoint is taken. Is part of startingPointTaken method.
     * @param fieldID
     * @return Pair (Double) : X,Y Value of robot in GUI
     */
    public Pair<Double,Double> getStartingPoint (int fieldID) {
        Pair<Double, Double> currentRobotPosition = new Pair<Double, Double>(1000.0, 1000.0);
        if (fieldID == 14) {
            currentRobotPosition = new Pair<Double, Double>(100.0,50.0);
        } else if (fieldID == 39) {
            currentRobotPosition = new Pair<Double, Double>(50.0,150.0);
        } else if (fieldID == 53) {
            currentRobotPosition = new Pair<Double, Double>(100.0,200.0);
        } else if (fieldID == 66) {
            currentRobotPosition = new Pair<Double, Double>(100.0,250.0);
        } else if (fieldID == 78) {
            currentRobotPosition = new Pair<Double, Double>(50.0,300.0);
        } else if (fieldID == 105) {
            currentRobotPosition = new Pair<Double, Double>(100.0,400.0);
        }

        return currentRobotPosition;
    }

}


