package networking;

import game.gameObjects.bot.Bot;
import javafx.application.Platform;
import model.DataModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * WriteThread
 * this class is responsible for sending the client's messages
 * to the server.
 *
 * @author Vincent Oeller, Thomas Richter
 */
public class WriteThread extends Thread {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.User");

    // 1.) Variables
    private Socket socket;

    private ObjectOutputStream output;
    private DataModelManager dataModel;
    private User client;

    /**
     * Constructor:
     * initializes the variables and creates a writer to send data
     * to the server.
     *
     * @param socket: the socket on which the connection is established
     */
    public WriteThread(User client, Socket socket) throws IOException {
        this.socket = socket;
        this.dataModel = client.getDataModel();
        this.client = client;

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            logger.info("Initiated output stream");

        } catch (IOException oops) {
            logger.error("Error creating output stream. " + oops.getMessage());
            oops.printStackTrace();
        }
    }


    /**
     * This function is called when you send a message to a player
     * It creates a JSONObject
     * @param msgFromGUI the message that was written in the GUI
     * @author Vincent Oeller
     */
    public void sendChat(String msgFromGUI, int to) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("message", msgFromGUI);
        msgBody.put("to", to);      //if ("to" == -1), Nachricht an alle Spiele

        JSONObject obj = new JSONObject();
        obj.put("messageType", "SendChat");
        obj.put("messageBody", msgBody);

        String isPrivate = to == -1 ? "public" : "private";

        try {
            output.writeObject(obj.toString());
            logger.info("Sent SendChat");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }

        dataModel.chatHistory += ("[" + client.getUserName() + "]:" + " (" + isPrivate + ") " + msgFromGUI + "\n");
        dataModel.chatHistory.replaceAll("\n", "");

        Platform.runLater(() -> {
            dataModel.updateViewModels();
        });

    }

    /**
     * this function is called when the Client received the "HelloClient" JSONObj
     * it returns a JSONObject
     * "messageType" : "PlayerValues"
     * "messageBody" : protocol version, group and if the Player is an AI or not
     *
     * @param isAI:     says whether the client is an AI or not
     * @param protocol: the current protocol
     * @author Vincent Oeller
     */
    public void helloServer(double protocol, String group, boolean isAI) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("protocol", protocol);
        msgBody.put("group", group);
        msgBody.put("isAI", isAI);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "HelloServer");
        obj.put("messageBody", msgBody);

        client.setAI(isAI);
        if (isAI) {
            client.setBot(new Bot(client, false));
        }
        try {
            output.writeObject(obj.toString());
            logger.info("Sent HelloServer");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }

    }

    /**
     * creates the JSONObject
     * "messageType" : PlayerValues"
     * "messageBody" : player name and chosen robot
     *
     * @param name:   name of the player
     * @param figure: selected Robot
     * @author Vincent Oeller
     */
    public void playerValues(String name, int figure) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("name", name);
        msgBody.put("figure", figure);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "PlayerValues");
        obj.put("messageBody", msgBody);
        try {
            output.writeObject(obj.toString());
            logger.info("Sent PlayerValues");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * creates the JSONObject
     * "messageType" : PlayerValues"
     * "messageBody" : player name and chosen robot
     *
     * @param status: says whether the player is ready or not
     * @author Vincent Oeller
     */
    public void setStatus(boolean status) {

        JSONObject msgBody = new JSONObject();
        msgBody.put("ready", status);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "SetStatus");
        obj.put("messageBody", msgBody);

        try {
            output.writeObject(obj.toString());
            logger.info("Sent SetStatus");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * selectMap(String)
     * Method realizes the selectMap messageType of protocol
     * @param racingCourse
     * @author Thomas Richter
     */
    public void selectMap(String racingCourse) {
        JSONArray msgBody = new JSONArray();
        msgBody.add(racingCourse);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "MapSelected");
        obj.put("messageBody", msgBody);

        try {
            output.writeObject(obj.toString());
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }
    }


    //7. Spielzug abhandeln

    /**
     * sends out a JSONObject
     * @param position the blocked position
     * @author Vincent Oeller
     */
    public void setStartingPoint(int position) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("position", position);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "SetStartingPoint");
        obj.put("messageBody", msgBody);
        try {
            output.writeObject(obj.toString());
            logger.info("Sent SetStartingPoint");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param card String of the Card
     * @param register The register it was placed in
     * @author Vincent Oeller
     */
    public void selectCard(String card, int register) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("card", card);
        msgBody.put("register", register + 1);
        JSONObject obj = new JSONObject();
        obj.put("messageType", "SelectCard");
        obj.put("messageBody", msgBody);

        logger.info(dataModel.robot + " selected " + card + " for register " + (register + 1));

        try {
            output.writeObject(obj.toString());
            logger.info("Sent SelectCard");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    //8. Aktionen Ereignisse, Effekte

    /**
     * @param cards the damageCards that were selected
     * @author Vincent Oeller
     */
    public void selectDamage(String[] cards) {
        JSONObject msgBody = new JSONObject();
        JSONArray JArray = new JSONArray();
        for (int i = 0; i < cards.length; i++) {
            JArray.add(i,cards[i]);
        }

        msgBody.put("cards", JArray);

        JSONObject obj = new JSONObject();
        obj.put("messageType", "SelectDamage");
        obj.put("messageBody", msgBody);
        try {
            output.writeObject(obj.toString());
            logger.info("Sent SelectDamage");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * playIt()
     * Method realizes the playIt messageType of protocol
     * @author Thomas Richter
     */
    public void playIt() {
        JSONObject msgBody = new JSONObject();

        JSONObject obj = new JSONObject();
        obj.put("messageType", "PlayIt");
        obj.put("messageBody", msgBody);
        try {
            output.writeObject(obj.toString());
            logger.info("Sent PlayIt");
        } catch (IOException e) {
            logger.error("WriteThread can't write object. " + e.getMessage());
            e.printStackTrace();

        }

    }

}
