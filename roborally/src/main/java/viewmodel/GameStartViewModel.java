package viewmodel;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.DataModel;
import model.DataModelManager;
import networking.Client;
import networking.User;
import networking.WriteThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.GameStartViewController;

public class GameStartViewModel {

    private static String classname = "GameStartViewModel- ";

    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");
    private static User player;
    public DataModelManager dataModel;
    public GameStartViewController gameStartViewController;

    public Boolean playerAdded;

    // Variables that are bound to the GUI

    // Variables that are bound to grid_GameStart
    public BooleanProperty disableButtonChooseUsername = new SimpleBooleanProperty();
    public BooleanProperty editableTextfieldChooseUsername = new SimpleBooleanProperty();

    public StringProperty statusTwonky = new SimpleStringProperty();
    public StringProperty statusHulkX90 = new SimpleStringProperty();
    public StringProperty statusHammerBot = new SimpleStringProperty();
    public StringProperty statusSmashBot = new SimpleStringProperty();
    public StringProperty statusZoomBot = new SimpleStringProperty();
    public StringProperty statusSpinBot = new SimpleStringProperty();

    public ObjectProperty <Paint> fillTwonky = new SimpleObjectProperty<>();
    public ObjectProperty <Paint> fillHulkX90 = new SimpleObjectProperty<>();
    public ObjectProperty <Paint> fillHammerBot = new SimpleObjectProperty<>();
    public ObjectProperty <Paint> fillSmashBot = new SimpleObjectProperty<>();
    public ObjectProperty <Paint> fillZoomBot = new SimpleObjectProperty<>();
    public ObjectProperty <Paint> fillSpinBot = new SimpleObjectProperty<>();


    public BooleanProperty disableTwonky = new SimpleBooleanProperty();
    public BooleanProperty disableHulkX90 = new SimpleBooleanProperty();
    public BooleanProperty disableHammerBot = new SimpleBooleanProperty();
    public BooleanProperty disableSmashBot = new SimpleBooleanProperty();
    public BooleanProperty disableZoomBot = new SimpleBooleanProperty();
    public BooleanProperty disableSpinBot = new SimpleBooleanProperty();

    public BooleanProperty showTwonky = new SimpleBooleanProperty();
    public BooleanProperty showHulkX90 = new SimpleBooleanProperty();
    public BooleanProperty showHammerBot = new SimpleBooleanProperty();
    public BooleanProperty showSmashBot = new SimpleBooleanProperty();
    public BooleanProperty showZoomBot = new SimpleBooleanProperty();
    public BooleanProperty showSpinBot = new SimpleBooleanProperty();

    public BooleanProperty showBlackTwonky = new SimpleBooleanProperty();
    public BooleanProperty showBlackHulkX90 = new SimpleBooleanProperty();
    public BooleanProperty showBlackHammerBot = new SimpleBooleanProperty();
    public BooleanProperty showBlackSmashBot = new SimpleBooleanProperty();
    public BooleanProperty showBlackZoomBot = new SimpleBooleanProperty();
    public BooleanProperty showBlackSpinBot = new SimpleBooleanProperty();

    public BooleanProperty disableStartingBoard = new SimpleBooleanProperty();
    public BooleanProperty disableRacingCourse = new SimpleBooleanProperty();
    public BooleanProperty visibleRacingCourseElements = new SimpleBooleanProperty();

    public BooleanProperty disableOpenChat = new SimpleBooleanProperty();

    public StringProperty statusSubmitReadyToPlay = new SimpleStringProperty();
    public BooleanProperty disableSubmitReadyToPlay = new SimpleBooleanProperty();


    // Variables that are bound to grid_Chat

    public BooleanProperty showChat = new SimpleBooleanProperty();

    public StringProperty player1Name = new SimpleStringProperty();
    public StringProperty player2Name = new SimpleStringProperty();
    public StringProperty player3Name = new SimpleStringProperty();
    public StringProperty player4Name = new SimpleStringProperty();
    public StringProperty player5Name = new SimpleStringProperty();

    public  BooleanProperty  visiblePlayer1 = new SimpleBooleanProperty();
    public  BooleanProperty  visiblePlayer2 = new SimpleBooleanProperty();
    public  BooleanProperty  visiblePlayer3 = new SimpleBooleanProperty();
    public  BooleanProperty  visiblePlayer4 = new SimpleBooleanProperty();
    public  BooleanProperty  visiblePlayer5 = new SimpleBooleanProperty();

    public BooleanProperty disablePlayer1 = new SimpleBooleanProperty();
    public BooleanProperty disablePlayer2 = new SimpleBooleanProperty();
    public BooleanProperty disablePlayer3 = new SimpleBooleanProperty();
    public BooleanProperty disablePlayer4 = new SimpleBooleanProperty();
    public BooleanProperty disablePlayer5 = new SimpleBooleanProperty();

    public BooleanProperty sendToPlayer1 = new SimpleBooleanProperty();
    public BooleanProperty sendToPlayer2 = new SimpleBooleanProperty();
    public BooleanProperty sendToPlayer3 = new SimpleBooleanProperty();
    public BooleanProperty sendToPlayer4 = new SimpleBooleanProperty();
    public BooleanProperty sendToPlayer5 = new SimpleBooleanProperty();

    public StringProperty chatHistory = new SimpleStringProperty();

    public int hashMapLength;
    public ObjectProperty<Boolean> startGame = new SimpleObjectProperty<>();

    Integer [] playerIDs;
    String [] playerNames;


    /**
     * GameStartViewModel(DataModel, User)
     * Constructor of GameStartViewModel class
     * @param dataModel
     * @param player
     * @author Thomas Richter
     */
    public GameStartViewModel(DataModel dataModel, User player) {

        this.player = player;
        this.dataModel = (DataModelManager) dataModel;
        ((DataModelManager) dataModel).setGameStartViewModel(this);
        initBindVariables();
    }


    /**
     * initBindVariables()
     * This method initializes all the variables that are bound to the GameStartViewController
     * @author Thomas Richter
     */
    public void initBindVariables() {
        logger.debug(classname + " initBindVariables() called.");

        // Variables of grid_GameStart
        this.playerAdded = false;

        this.disableButtonChooseUsername.setValue(true);
        this.editableTextfieldChooseUsername.setValue(true);

        this.disableOpenChat.setValue(true);


        // Checks if Twonky should be able to be chosen
        if (dataModel.getAvailableTwonky()) {
            this.statusTwonky.setValue("AVAILABLE");
            this.disableTwonky.setValue(false);
            this.showTwonky.setValue(true);
            this.showBlackTwonky.setValue(false);
            this.fillTwonky.setValue(Color.WHITE);
        } else {
            this.statusTwonky.setValue(dataModel.getPlayerNameTwonky());
            this.disableTwonky.setValue(true);
            this.showTwonky.setValue(false);
            this.showBlackTwonky.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdTwonky)) {
                this.fillTwonky.setValue(Color.LIMEGREEN);
            } else {
                this.fillTwonky.setValue(Color.WHITE);
            }
        }

        // Checks if HulkX90 should be able to be chosen
        if (dataModel.getAvailableHulkX90()) {
            this.statusHulkX90.setValue("AVAILABLE");
            this.disableHulkX90.setValue(false);
            this.showHulkX90.setValue(true);
            this.showBlackHulkX90.setValue(false);
            this.fillHulkX90.setValue(Color.WHITE);
        } else {
            this.statusHulkX90.setValue(dataModel.getPlayerNameHulkX90());
            this.disableHulkX90.setValue(true);
            this.showHulkX90.setValue(false);
            this.showBlackHulkX90.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdHulkX90)) {
                this.fillHulkX90.setValue(Color.LIMEGREEN);
            } else {
                this.fillHulkX90.setValue(Color.WHITE);
            }
        }

        // Checks if HammerBot should be able to be chosen
        if (dataModel.getAvailableHammerBot()) {
            this.statusHammerBot.setValue("AVAILABLE");
            this.disableHammerBot.setValue(false);
            this.showHammerBot.setValue(true);
            this.showBlackHammerBot.setValue(false);
            this.fillHammerBot.setValue(Color.WHITE);
        } else {
            this.statusHammerBot.setValue(dataModel.getPlayerNameHammerBot());
            this.disableHammerBot.setValue(true);
            this.showHammerBot.setValue(false);
            this.showBlackHammerBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdHammerBot)) {
                this.fillHammerBot.setValue(Color.LIMEGREEN);
            } else {
                this.fillHammerBot.setValue(Color.WHITE);
            }
        }

        // Checks if SmashBot should be able to be chosen
        if (dataModel.getAvailableSmashBot()) {
            this.statusSmashBot.setValue("AVAILABLE");
            this.disableSmashBot.setValue(false);
            this.showSmashBot.setValue(true);
            this.showBlackSmashBot.setValue(false);
            this.fillSmashBot.setValue(Color.WHITE);
        } else {
            this.statusSmashBot.setValue(dataModel.getPlayerNameSmashBot());
            this.disableSmashBot.setValue(true);
            this.showSmashBot.setValue(false);
            this.showBlackSmashBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdSmashBot)) {
                this.fillSmashBot.setValue(Color.LIMEGREEN);
            } else {
                this.fillSmashBot.setValue(Color.WHITE);
            }
        }

        // Checks if ZoomBot should be able to be chosen
        if (dataModel.getAvailableZoomBot()) {
            this.statusZoomBot.setValue("AVAILABLE");
            this.disableZoomBot.setValue(false);
            this.showZoomBot.setValue(true);
            this.showBlackZoomBot.setValue(false);
            this.fillZoomBot.setValue(Color.WHITE);
        } else {
            this.statusZoomBot.setValue(dataModel.getPlayerNameZoomBot());
            this.disableZoomBot.setValue(true);
            this.showZoomBot.setValue(false);
            this.showBlackZoomBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdZoomBot)) {
                this.fillZoomBot.setValue(Color.LIMEGREEN);
            } else {
                this.fillZoomBot.setValue(Color.WHITE);
            }
        }

        // Checks if SpinBot should be able to be chosen
        if (dataModel.getAvailableSpinBot()) {
            this.statusSpinBot.setValue("AVAILABLE");
            this.disableSpinBot.setValue(false);
            this.showSpinBot.setValue(true);
            this.showBlackSpinBot.setValue(false);
            this.fillSpinBot.setValue(Color.WHITE);
        } else {
            this.statusSpinBot.setValue(dataModel.getPlayerNameSpinBot());
            this.disableSpinBot.setValue(true);
            this.showSpinBot.setValue(false);
            this.showBlackSpinBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdSpinBot)) {
                this.fillSpinBot.setValue(Color.LIMEGREEN);
            } else {
                this.fillSpinBot.setValue(Color.WHITE);
            }
        }

        this.disableStartingBoard.setValue(true);
        this.disableRacingCourse.setValue(true);

        this.visibleRacingCourseElements.setValue(false);

        this.statusSubmitReadyToPlay.setValue("Submit Selections");
        this.disableSubmitReadyToPlay.setValue(true);


        // Variables of grid_Chat
        this.showChat.setValue(false);

        this.hashMapLength = dataModel.players.size();

        this.player1Name.setValue("");
        this.player2Name.setValue("");
        this.player3Name.setValue("");
        this.player4Name.setValue("");
        this.player5Name.setValue("");

        this.disablePlayer1.setValue(true);
        this.disablePlayer2.setValue(true);
        this.disablePlayer3.setValue(true);
        this.disablePlayer4.setValue(true);
        this.disablePlayer5.setValue(true);

        this.visiblePlayer1.setValue(false);
        this.visiblePlayer2.setValue(false);
        this.visiblePlayer3.setValue(false);
        this.visiblePlayer4.setValue(false);
        this.visiblePlayer5.setValue(false);

        this.sendToPlayer1.setValue(false);
        this.sendToPlayer2.setValue(false);
        this.sendToPlayer3.setValue(false);
        this.sendToPlayer4.setValue(false);
        this.sendToPlayer5.setValue(false);

        this.chatHistory.setValue(dataModel.chatHistory);

        this.startGame.setValue(false);

        this.playerIDs = new Integer [6];
        this.playerNames = new String [6];

        logger.info(startGame);

    }

    /**
     * updateViewModel()
     * This method is called at the end of every ReadThread method
     * which implements the Server-Client communication protocol messages,
     * by calling the updateViewModels method of the DataModelManager
     * These methods of ReadThread manipulate the Data of the DataModelManager.
     * Therefore after the manipulation the GameStartViewModel has to fetch the updates.
     * This is realized with the updateViewModel method.
     * @author Thomas Richter
     */
    public void updateViewModel () {
        logger.debug(classname + " updateViewModel() called.");


        // Updating of grid_GameStart

        if (!(User.getUserName() == null) && !(dataModel.getRobot() == null)) {
            disableSubmitReadyToPlay.setValue(false);
        } else if ((Client.getUserName() == null) || (dataModel.getRobot() == null)) {
            disableSubmitReadyToPlay.setValue(true);
        }

        logger.debug(classname + " btn_submitAndReadyToPlay disabled: " + disableSubmitReadyToPlay.getValue());

        if (!(dataModel.getPlayerAdded() == null)) {
            if (dataModel.getPlayerAdded() == true) {
                playerAdded = true;

                disableOpenChat.setValue(false);
                disableTwonky.setValue(true);
                disableHulkX90.setValue(true);
                disableHammerBot.setValue(true);
                disableSmashBot.setValue(true);
                disableZoomBot.setValue(true);
                disableSpinBot.setValue(true);


                logger.debug(classname + " playerAdded: " + playerAdded + ", all robot buttons disabled and chatfunction activated.");
                playerAdded();
            }
        };

        // Checks whether and which robots are available
        if (!dataModel.availableTwonky) {
            disableTwonky.setValue(true);
            statusTwonky.setValue(dataModel.getPlayerNameTwonky());
            showTwonky.setValue(false);
            showBlackTwonky.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdTwonky)) {
                fillTwonky.setValue(Color.GREEN);
            } else {
                fillTwonky.setValue(Color.WHITE);
            }
        }else if(dataModel.availableTwonky && !(dataModel.getRobot() == null) && !dataModel.robot.equals("Twonky")){
            disableTwonky.setValue(false);
            statusTwonky.setValue("AVAILABLE");
            showTwonky.setValue(true);
            showBlackTwonky.setValue(false);
            fillTwonky.setValue(Color.WHITE);
        }

        if (!dataModel.availableHulkX90) {
            disableHulkX90.setValue(true);
            statusHulkX90.setValue(dataModel.getPlayerNameHulkX90());
            showHulkX90.setValue(false);
            showBlackHulkX90.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdHulkX90)) {
                fillHulkX90.setValue(Color.GREEN);
            } else {
                fillHulkX90.setValue(Color.WHITE);
            }
        }else if(dataModel.availableHulkX90 && !(dataModel.getRobot() == null) && !dataModel.robot.equals("HulkX90")){
            disableHulkX90.setValue(false);
            statusHulkX90.setValue("AVAILABLE");
            showHulkX90.setValue(true);
            showBlackHulkX90.setValue(false);
            fillHulkX90.setValue(Color.WHITE);
        }

        if (!dataModel.availableHammerBot) {
            disableHammerBot.setValue(true);
            statusHammerBot.setValue(dataModel.getPlayerNameHammerBot());
            showHammerBot.setValue(false);
            showBlackHammerBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdHammerBot)) {
                fillHammerBot.setValue(Color.GREEN);
            } else {
                fillHammerBot.setValue(Color.WHITE);
            }
        }else if(dataModel.availableHammerBot && !(dataModel.getRobot() == null) && !dataModel.robot.equals("HammerBot")){
            disableHammerBot.setValue(false);
            statusHammerBot.setValue("AVAILABLE");
            showHammerBot.setValue(true);
            showBlackHammerBot.setValue(false);
            fillHammerBot.setValue(Color.WHITE);
        }

        if (!dataModel.availableSmashBot) {
            disableSmashBot.setValue(true);
            statusSmashBot.setValue(dataModel.getPlayerNameSmashBot());
            showSmashBot.setValue(false);
            showBlackSmashBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdSmashBot)) {
                fillSmashBot.setValue(Color.GREEN);
            } else {
                fillSmashBot.setValue(Color.WHITE);
            }
        }else if(dataModel.availableSmashBot  && !(dataModel.getRobot() == null) && !dataModel.robot.equals("SmashBot")){
            disableSmashBot.setValue(false);
            statusSmashBot.setValue("AVAILABLE");
            showSmashBot.setValue(true);
            showBlackSmashBot.setValue(false);
            fillSmashBot.setValue(Color.WHITE);
        }
        if (!dataModel.availableZoomBot) {
            disableZoomBot.setValue(true);
            statusZoomBot.setValue(dataModel.getPlayerNameZoomBot());
            showZoomBot.setValue(false);
            showBlackZoomBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdZoomBot)) {
                fillZoomBot.setValue(Color.GREEN);
            } else {
                fillZoomBot.setValue(Color.WHITE);
            }
        }else if(dataModel.availableZoomBot && !(dataModel.getRobot() == null) && !dataModel.robot.equals("ZoomBot")){
            disableZoomBot.setValue(false);
            statusZoomBot.setValue("AVAILABLE");
            showZoomBot.setValue(true);
            showBlackZoomBot.setValue(false);
            fillZoomBot.setValue(Color.WHITE);
        }
        if (!dataModel.availableSpinBot) {
            disableSpinBot.setValue(true);
            statusSpinBot.setValue(dataModel.getPlayerNameSpinBot());
            showSpinBot.setValue(false);
            showBlackSpinBot.setValue(true);
            if (dataModel.readyIDs.containsKey(dataModel.playerIdSpinBot)) {
                fillSpinBot.setValue(Color.GREEN);
            } else {
                fillSpinBot.setValue(Color.WHITE);
            }
        }else if(dataModel.availableSpinBot && !(dataModel.getRobot() == null) && !dataModel.robot.equals("SpinBot")){
            disableSpinBot.setValue(false);
            statusSpinBot.setValue("AVAILABLE");
            showSpinBot.setValue(true);
            showBlackSpinBot.setValue(false);
            fillSpinBot.setValue(Color.WHITE);
        }
        logger.debug(classname + " Available robots: Twonky(" + dataModel.availableTwonky + "), " + "HulkX90(" + dataModel.availableHulkX90 + "), " + "HammerBot(" + dataModel.availableHammerBot + "), " + "SmashBot(" + dataModel.availableSmashBot + "), " + "ZoomBot(" + dataModel.availableZoomBot + "), " + "SpinBot(" + dataModel.availableSpinBot + ")");


        //
        int numAvailableMaps = dataModel.availableMaps.length;
        boolean makeMapsVisible = true;
        for (int i = 0; i<numAvailableMaps; i++) {
            if (dataModel.availableMaps[i] != "") {
                makeMapsVisible = makeMapsVisible && true;
            } else {
                makeMapsVisible = makeMapsVisible && false;
            }
        }
        if (makeMapsVisible) {
            disableRacingCourse.setValue(false);
            visibleRacingCourseElements.setValue(true);
            logger.debug(classname + " racingCourse can be chosen.");
        } else {
            disableRacingCourse.setValue(true);
            visibleRacingCourseElements.setValue(false);
            logger.debug(classname + " racingCourse can not be chosen.");
        }



        // Sets the ability of GUI to switch to GameView Scene if enough players are ready to play
        if (dataModel.gameStarted) {
            logger.debug(classname + " dataModel.gameStarted: " + dataModel.gameStarted);
            logger.debug(classname + " GameView can be started");
            startGame.setValue(true);
        }

        // Updating of grid_Chat layer


        hashMapLength = dataModel.players.size();

        if (hashMapLength > 0) {
            logger.debug(classname + " Other players joined the lobby and submitted playerValues. Chat messages can be sent between players");

            for (int i = 0; i < hashMapLength; i++) {
                if (!(dataModel.players.keySet().toArray()[i] == null)) {
                    playerIDs [i] = (Integer) dataModel.players.keySet().toArray()[i];
                    playerNames [i] = (dataModel.players.get(playerIDs [i]));
                }
            }
            if (hashMapLength == 1) {
                player1Name.setValue(playerNames[0] + " (" +  String.valueOf(playerIDs[0]) + ")");
                disablePlayer1.setValue(false);
                visiblePlayer1.setValue(true);
                sendToPlayer1.setValue(true);
            } else if (hashMapLength == 2) {
                player1Name.setValue(playerNames[0] + " (" +  String.valueOf(playerIDs[0]) + ")");
                disablePlayer1.setValue(false);
                visiblePlayer1.setValue(true);
                sendToPlayer1.setValue(true);

                player2Name.setValue(playerNames[1] + " (" +  String.valueOf(playerIDs[1]) + ")");
                disablePlayer2.setValue(false);
                visiblePlayer2.setValue(true);
                sendToPlayer2.setValue(true);
            } else if (hashMapLength == 3) {
                player1Name.setValue(playerNames[0] + " (" +  String.valueOf(playerIDs[0]) + ")");
                disablePlayer1.setValue(false);
                visiblePlayer1.setValue(true);
                sendToPlayer1.setValue(true);

                player2Name.setValue(playerNames[1] + " (" +  String.valueOf(playerIDs[1]) + ")");
                disablePlayer2.setValue(false);
                visiblePlayer2.setValue(true);
                sendToPlayer2.setValue(true);

                player3Name.setValue(playerNames[2] + " (" +  String.valueOf(playerIDs[2]) + ")");
                disablePlayer3.setValue(false);
                visiblePlayer3.setValue(true);
                sendToPlayer3.setValue(true);
            } else if (hashMapLength == 4) {
                player1Name.setValue(playerNames[0] + " (" +  String.valueOf(playerIDs[0]) + ")");
                disablePlayer1.setValue(false);
                visiblePlayer1.setValue(true);
                sendToPlayer1.setValue(true);

                player2Name.setValue(playerNames[1] + " (" +  String.valueOf(playerIDs[1]) + ")");
                disablePlayer2.setValue(false);
                visiblePlayer2.setValue(true);
                sendToPlayer2.setValue(true);

                player3Name.setValue(playerNames[2] + " (" +  String.valueOf(playerIDs[2]) + ")");
                disablePlayer3.setValue(false);
                visiblePlayer3.setValue(true);
                sendToPlayer3.setValue(true);

                player4Name.setValue(playerNames[3] + " (" +  String.valueOf(playerIDs[3]) + ")");
                disablePlayer4.setValue(false);
                visiblePlayer4.setValue(true);
                sendToPlayer4.setValue(true);

            } else if (hashMapLength == 5) {
                player1Name.setValue(playerNames[0] + " (" +  String.valueOf(playerIDs[0]) + ")");
                disablePlayer1.setValue(false);
                visiblePlayer1.setValue(true);
                sendToPlayer1.setValue(true);

                player2Name.setValue(playerNames[1] + " (" +  String.valueOf(playerIDs[1]) + ")");
                disablePlayer2.setValue(false);
                visiblePlayer2.setValue(true);
                sendToPlayer2.setValue(true);

                player3Name.setValue(playerNames[2] + " (" +  String.valueOf(playerIDs[2]) + ")");
                disablePlayer3.setValue(false);
                visiblePlayer3.setValue(true);
                sendToPlayer3.setValue(true);

                player4Name.setValue(playerNames[3] + " (" +  String.valueOf(playerIDs[3]) + ")");
                disablePlayer4.setValue(false);
                visiblePlayer4.setValue(true);
                sendToPlayer4.setValue(true);

                player5Name.setValue(playerNames[4] + " (" +  String.valueOf(playerIDs[4]) + ")");
                disablePlayer5.setValue(false);
                visiblePlayer5.setValue(true);
                sendToPlayer5.setValue(true);
            }
        }
        chatHistory.setValue(dataModel.chatHistory);

        if (!(dataModel.receivedError == null)) {
            if (dataModel.receivedError) {
                logger.debug(classname + " dataModel.receivedError: " + dataModel.receivedError);
                logger.debug(classname + " Error is displayed to GUI");
                showChat.setValue(true);
            }
        }

    }

    /**
     * playerAdded()
     * this method is an outsourced part of the updateViewModel method.
     * @author Thomas Richter
     */
    public void playerAdded () {
        logger.debug(classname + " playerAdded() is called");
        if (dataModel.getPlayerAdded()) {
            logger.debug(classname + " dataModel.getPlayerAdded(): " + dataModel.getPlayerAdded());
            if (!this.statusSubmitReadyToPlay.getValue().toLowerCase().equals("withdraw readiness")) {
                this.statusSubmitReadyToPlay.setValue("Ready to play!");
            }
        }
        logger.debug(classname + " statusSubmitAndReadyToPlay: "+ statusSubmitReadyToPlay.getValue());
    }


    /**
     * chooseUserName(String)
     * This method gets called by the GUI and saves the Username in the DataModel
     * @param userName
     * @author Thomas Richter
     */
    public void chooseUserName(String userName) {
        logger.debug(classname + " chooseUserName("+ userName + ") is called");
        player.setUserName(userName);
        this.disableButtonChooseUsername.setValue(true);
        this.editableTextfieldChooseUsername.setValue(false);
    }

    /**
     * selectRobot(String)
     * This method gets called by the GUI and saves the chosen Robot in the DataModel
     * @param robot
     * @author Thomas Richter
     */
    public void selectRobot(String robot) {
        logger.debug(classname + " selectRobot("+ robot + ") is called from GUI. Gets validated through server!");
        dataModel.setRobot(robot);
        updateViewModel();
    }


    /**
     * submitPlayerValues()
     * This method launches the submitPlayerValues method of the DataModel which sends the chosen username and robot to the server for verification
     * @author Thomas Richter
     */
    public void submitPlayerValues() {
        logger.debug(classname + " submitPlayerValues() is called from GUI. Gets validated through server!");
        dataModel.submitPlayerValues();
    }


    /**
     * sendChat(String, int)
     * This method launches sendChat method of the DataModel which sends chat messages to the Server
     * @param message
     * @param sendTo
     * @author Thomas Richter
     */
    public void sendChat (String message, int sendTo) {
        logger.debug(classname + " sendChat("+ message +", " + sendTo + ") is called from GUI.");
        int sendToID;
        if (!(sendTo == -1)) {
            sendToID = playerIDs[sendTo];
        } else {
            sendToID = sendTo;
        }
        dataModel.sendChat(message, sendToID);
    }


    /**
     * resetRacingCourseElements()
     * This methods resets the variables in the DataModel after Map is chosen
     * @author Thomas Richter
     */

    public void resetRacingCourseElements () {
        logger.debug(classname + " resetRacingCourseElements() is called");
        dataModel.availableMaps[0] = "";
        dataModel.updateViewModels();
    }


    /**
     * submitReadyToPlay(Boolean)
     * This method launches submitReadyToPlay method of the DataModel which sends the signal to Server, that the Client is ready to play
     * @param status
     * @author Thomas Richter
     *
     */
    public void submitReadyToPlay (Boolean status) {
        logger.debug(classname + " submitReadyToPlay("+ status + ") is called from GUI. Gets validated through server!");
        dataModel.submitReadyToPlay(status);
    }


    /**
     * submitRacingCourse(String)
     * Method initiates the submission the selected racingCourse to the server
     * @param racingCourse
     * @author Thomas Richter
     */

    public void submitRacingCourse (String racingCourse) {
        logger.debug(classname + " submitRacingCourse("+ racingCourse + ") is called from GUI. Gets validated through server!");
        dataModel.submitRacingCourse(racingCourse);
        visibleRacingCourseElements.setValue(false);
        disableRacingCourse.setValue(true);
    }



    // Getter and Setter Method(s)
    public int getHashMapLength() { return hashMapLength; }
}
