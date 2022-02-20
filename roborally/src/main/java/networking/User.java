package networking;

import game.gameObjects.bot.Bot;
import javafx.application.Application;
import javafx.application.Platform;
import model.DataModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.RoboRallyApplication;

import java.io.IOException;
import java.net.Socket;

/**
 * User:
 * is a representation of a server's user. Can be either a Bot
 * or a normal Client.
 */
public class User {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.User");
    // 1.) Variables
    private static int TCPPORT;
    private static String host;
    private static String userName;
    private DataModelManager dataModel;

    private Boolean isAI;
    private String group;
    private Double protocol;

    private Boolean connectionAccepted;
    private Double serverProtocol;

    private WriteThread write;
    private ReadThread read;
    private Bot bot;


    /**
     * Main Method:
     * creates a new client and launches the GUI.
     *
     * @param args: expected arguments: -u User -h Host -p Port.
     */
    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("No arguments provided. Flags: -u User -h Host -p Port.");
        if (args.length == 1) {
            if (args[0].charAt(0) == '-')
                throw new IllegalArgumentException("Expected argument after: " + args[0]);
            else
                throw new IllegalArgumentException("Illegal Argument: " + args[0]);

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
                case "-h":
                    host = nextArg;
                    i++;
                    break;
                case "-p":
                    int port = Integer.parseInt(nextArg);
                    if (port < 1024 || port > 65535)
                        throw new IllegalArgumentException("Port number " + port + " invalid");
                    TCPPORT = port;
                    i++;
                    break;
                case "-u":
                    if (nextArg.equalsIgnoreCase("ki")) {
                        Bot player = new Bot(null, false);
                        Application.launch(RoboRallyApplication.class);


                        i++;
                    } else if (nextArg.equalsIgnoreCase("player")) {
                        Client player = new Client();

                        i++;
                    } else {
                        throw new IllegalArgumentException("Invalid user option: " + nextArg + ". Please use \"player\" or \"ki\" ");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Illegal Argument: " + arg);
            }
        }


        Application.launch(RoboRallyApplication.class);

    }

    /**
     * connect():
     * Connects the client to the server and starts a write and a read thread
     * for the client
     *
     * @param isAI
     * @author Shady Mansour
     */
    public void connect(Boolean isAI) {
        try {
            Socket s = new Socket(host, TCPPORT);

            this.write = new WriteThread(this, s);
            this.read = new ReadThread(this, s);
            logger.info("Connected to Server");

            write.start();
            read.start();


        } catch (IOException oops) {
            logger.error("Connection Refused: " + oops.getMessage());
            setConnectionAccepted(false);
            dataModel.chatHistory = "Connection refused / Unable to connect to server.";
            Platform.runLater(()-> {
                dataModel.updateViewModels();
            });

        }
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public static String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
        dataModel.updateViewModels();
    }

    public WriteThread getWrite() {
        return write;
    }
    public void setWrite(WriteThread write) {
        this.write = write;
    }

    public ReadThread getRead() {
        return read;
    }
    public void setRead(ReadThread read) {
        this.read = read;
    }

    public DataModelManager getDataModel() {
        return dataModel;
    }
    public void setDataModel(DataModelManager dataModel) {
        this.dataModel = dataModel;
    }

    public Boolean getConnectionAccepted() {
        return connectionAccepted;
    }
    public void setConnectionAccepted(Boolean connectionAccepted) {
        this.connectionAccepted = connectionAccepted;
    }

    public Double getServerProtocol() {
        return serverProtocol;
    }
    public void setServerProtocol(Double serverProtocol) {
        this.serverProtocol = serverProtocol;
    }

    public Boolean getAI() {
        return isAI;
    }
    public void setAI(Boolean AI) {
        isAI = AI;
    }

    public Bot getBot() {
        return bot;
    }
    public void setBot(Bot bot) {
        this.bot = bot;
    }

}
