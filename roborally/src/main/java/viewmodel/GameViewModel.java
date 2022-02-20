package viewmodel;

import game.gameObjects.gamefield.FieldObject;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import model.DataModel;
import model.DataModelManager;
import networking.Server;
import networking.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class GameViewModel {

    private static String classname = "GameViewModel- ";

    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");
    private static User player;
    public DataModelManager dataModel;

    public int currentStatPlayerID;
    public boolean softupdate;
    // Variables that are bound to the GUI

    // Variables that display the various StackPane layers

        // StackPane Layers:
        /*  - grid_Game
            - pane_Robots:
                - Images: robots, robot lasers, robot damage GIFs
                - StartingPoint buttons
            - pane_setStartingPoint
            - grid_Phase1
            - grid_Phase2
            - grid_Damage
            - grid_ShuffleCards: Animation Layer for shuffle effect
            - pane_Phase3
            - pane_Stats
            - pane_CardsYouGotNow
            - pane_DrawDamage
            - grid_Chat
            - grid_GameEnd
         */


    // Variables that are bound to grid_Game
    public BooleanProperty gameStarted = new SimpleBooleanProperty();

    public BooleanProperty disableStartingPoint1_4 = new SimpleBooleanProperty();
    public BooleanProperty disableStartingPoint1_7 = new SimpleBooleanProperty();
    public BooleanProperty disableStartingPoint2_2 = new SimpleBooleanProperty();
    public BooleanProperty disableStartingPoint2_5 = new SimpleBooleanProperty();
    public BooleanProperty disableStartingPoint2_6 = new SimpleBooleanProperty();
    public BooleanProperty disableStartingPoint2_9 = new SimpleBooleanProperty();

    public BooleanProperty visibleStartingBoardReboot = new SimpleBooleanProperty();

    public BooleanProperty visiblePlayItOff = new SimpleBooleanProperty();
    public BooleanProperty visiblePlayIt = new SimpleBooleanProperty();

    public BooleanProperty visibleNewChat = new SimpleBooleanProperty();
    public BooleanProperty visibleChat = new SimpleBooleanProperty();

    public BooleanProperty setStartingPointPane = new SimpleBooleanProperty();
    public BooleanProperty showTimer = new SimpleBooleanProperty();
    public ObjectProperty<Image> timerImage = new SimpleObjectProperty<>();
    public Image timerGIF;
    public Image timerFrame;

    public ObjectProperty <Paint> ownNameColor= new SimpleObjectProperty<>();
    public StringProperty ownName = new SimpleStringProperty();
    public ObjectProperty<Image> ownPlayerAvatarImage = new SimpleObjectProperty<>();
    public ObjectProperty<Image> player1AvatarImage = new SimpleObjectProperty<>();
    public ObjectProperty<Image> player2AvatarImage = new SimpleObjectProperty<>();
    public ObjectProperty<Image> player3AvatarImage = new SimpleObjectProperty<>();
    public ObjectProperty<Image> player4AvatarImage = new SimpleObjectProperty<>();
    public ObjectProperty<Image> player5AvatarImage = new SimpleObjectProperty<>();

        // Robots
    public Image twonkyAvatar = new Image("/TwonkyAvatar.png");
    public Image hulkX90Avatar = new Image("/HulkX90Avatar.png");
    public Image hammerBotAvatar = new Image("/HammerBotAvatar.png");
    public Image smashBotAvatar = new Image("/SmashBotAvatar.png");
    public Image zoomBotAvatar = new Image("/ZoomBotAvatar.png");
    public Image spinBotAvatar = new Image("/SpinBotAvatar.png");



    public BooleanProperty showTwonky = new SimpleBooleanProperty();
    public BooleanProperty showHulkX90 = new SimpleBooleanProperty();
    public BooleanProperty showHammerBot = new SimpleBooleanProperty();
    public BooleanProperty showSmashBot = new SimpleBooleanProperty();
    public BooleanProperty showZoomBot = new SimpleBooleanProperty();
    public BooleanProperty showSpinBot = new SimpleBooleanProperty();

    public DoubleProperty xTwonky = new SimpleDoubleProperty();
    public DoubleProperty yTwonky = new SimpleDoubleProperty();
    public DoubleProperty rotationTwonky = new SimpleDoubleProperty();

    public DoubleProperty xHulkX90 = new SimpleDoubleProperty();
    public DoubleProperty yHulkX90 = new SimpleDoubleProperty();
    public DoubleProperty rotationHulkX90 = new SimpleDoubleProperty();

    public DoubleProperty xHammerBot = new SimpleDoubleProperty();
    public DoubleProperty yHammerBot= new SimpleDoubleProperty();
    public DoubleProperty rotationHammerBot = new SimpleDoubleProperty();

    public DoubleProperty xSmashBot = new SimpleDoubleProperty();
    public DoubleProperty ySmashBot = new SimpleDoubleProperty();
    public DoubleProperty rotationSmashBot = new SimpleDoubleProperty();

    public DoubleProperty xZoomBot = new SimpleDoubleProperty();
    public DoubleProperty yZoomBot = new SimpleDoubleProperty();
    public DoubleProperty rotationZoomBot = new SimpleDoubleProperty();

    public DoubleProperty xSpinBot = new SimpleDoubleProperty();
    public DoubleProperty ySpinBot = new SimpleDoubleProperty();
    public DoubleProperty rotationSpinBot= new SimpleDoubleProperty();

        // Lasers of robots
    public BooleanProperty showTwonkyLaser = new SimpleBooleanProperty();
    public BooleanProperty showHulkX90Laser = new SimpleBooleanProperty();
    public BooleanProperty showHammerBotLaser = new SimpleBooleanProperty();
    public BooleanProperty showSmashBotLaser = new SimpleBooleanProperty();
    public BooleanProperty showZoomBotLaser = new SimpleBooleanProperty();
    public BooleanProperty showSpinBotLaser = new SimpleBooleanProperty();

    public DoubleProperty xTwonkyLaser = new SimpleDoubleProperty();
    public DoubleProperty yTwonkyLaser = new SimpleDoubleProperty();
    public DoubleProperty rotationTwonkyLaser = new SimpleDoubleProperty();

    public DoubleProperty xHulkX90Laser = new SimpleDoubleProperty();
    public DoubleProperty yHulkX90Laser = new SimpleDoubleProperty();
    public DoubleProperty rotationHulkX90Laser = new SimpleDoubleProperty();

    public DoubleProperty xHammerBotLaser = new SimpleDoubleProperty();
    public DoubleProperty yHammerBotLaser = new SimpleDoubleProperty();
    public DoubleProperty rotationHammerBotLaser = new SimpleDoubleProperty();

    public DoubleProperty xSmashBotLaser = new SimpleDoubleProperty();
    public DoubleProperty ySmashBotLaser = new SimpleDoubleProperty();
    public DoubleProperty rotationSmashBotLaser = new SimpleDoubleProperty();

    public DoubleProperty xZoomBotLaser = new SimpleDoubleProperty();
    public DoubleProperty yZoomBotLaser = new SimpleDoubleProperty();
    public DoubleProperty rotationZoomBotLaser = new SimpleDoubleProperty();

    public DoubleProperty xSpinBotLaser = new SimpleDoubleProperty();
    public DoubleProperty ySpinBotLaser = new SimpleDoubleProperty();
    public DoubleProperty rotationSpinBotLaser = new SimpleDoubleProperty();

        // Damage GIFs of robots
    public BooleanProperty showTwonkyDamage = new SimpleBooleanProperty();
    public BooleanProperty showHulkX90Damage = new SimpleBooleanProperty();
    public BooleanProperty showHammerBotDamage = new SimpleBooleanProperty();
    public BooleanProperty showSmashBotDamage = new SimpleBooleanProperty();
    public BooleanProperty showZoomBotDamage = new SimpleBooleanProperty();
    public BooleanProperty showSpinBotDamage = new SimpleBooleanProperty();


    // Variables of grid_Phase2 layer of StackPane
    public BooleanProperty visiblePhase2 = new SimpleBooleanProperty();

    public BooleanProperty disableResetRegisters = new SimpleBooleanProperty();

    public String [] programmingCardsPath = new String[9];
    public String [] registersPath = new String[5];
    public Image [] programmingCardImages = new Image[9];
    public Image [] myRegisterCardImages = new Image[5];

    public boolean againFlags [] = new boolean[9];

    public ObjectProperty<Image> programmingCard1 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard2 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard3 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard4 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard5 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard6 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard7 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard8 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> programmingCard9 = new SimpleObjectProperty<Image>();

    public ObjectProperty<Image> register1 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> register2 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> register3 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> register4 = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> register5 = new SimpleObjectProperty<Image>();

    public BooleanProperty disableProgrammingCard1 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard2 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard3 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard4 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard5 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard6 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard7 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard8 = new SimpleBooleanProperty();
    public BooleanProperty disableProgrammingCard9 = new SimpleBooleanProperty();

    public BooleanProperty visibleProgrammingCard1 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard2 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard3 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard4 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard5 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard6 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard7 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard8 = new SimpleBooleanProperty();
    public BooleanProperty visibleProgrammingCard9 = new SimpleBooleanProperty();

    // Variables of grid_ShuffleCards
    public BooleanProperty visibleShuffleCards = new SimpleBooleanProperty();


    // Variables of pane_Phase3 layer of StackPane
    public BooleanProperty visiblePhase3 = new SimpleBooleanProperty();

    public Image activationPhaseEmptyRegister = new Image("/EmptyRegister.png");

    public String [] twonkyRegistersPath = new String[5];
    public Image [] twonkyRegistersImage = new Image[5];

    public ObjectProperty<Image> twonkyRegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> twonkyRegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> twonkyRegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> twonkyRegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> twonkyRegisterImage5 = new SimpleObjectProperty<>();

    public String [] hulkX90RegistersPath = new String[5];
    public Image [] hulkX90RegistersImage = new Image[5];

    public ObjectProperty<Image> hulkX90RegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hulkX90RegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hulkX90RegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hulkX90RegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hulkX90RegisterImage5 = new SimpleObjectProperty<>();

    public String [] hammerBotRegistersPath = new String[5];
    public Image [] hammerBotRegistersImage = new Image[5];

    public ObjectProperty<Image> hammerBotRegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hammerBotRegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hammerBotRegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hammerBotRegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> hammerBotRegisterImage5 = new SimpleObjectProperty<>();

    public String [] smashBotRegistersPath = new String[5];
    public Image [] smashBotRegistersImage = new Image[5];

    public ObjectProperty<Image> smashBotRegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> smashBotRegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> smashBotRegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> smashBotRegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> smashBotRegisterImage5 = new SimpleObjectProperty<>();

    public String [] zoomBotRegistersPath = new String[5];
    public Image [] zoomBotRegistersImage = new Image[5];

    public ObjectProperty<Image> zoomBotRegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> zoomBotRegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> zoomBotRegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> zoomBotRegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> zoomBotRegisterImage5 = new SimpleObjectProperty<>();

    public String [] spinBotRegistersPath = new String[5];
    public Image [] spinBotRegistersImage = new Image[5];

    public ObjectProperty<Image> spinBotRegisterImage1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> spinBotRegisterImage2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> spinBotRegisterImage3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> spinBotRegisterImage4 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> spinBotRegisterImage5 = new SimpleObjectProperty<>();


    // Variables of grid_DrawDamage
    public BooleanProperty visibleDamageLayer = new SimpleBooleanProperty();
    public StringProperty remainingDamages = new SimpleStringProperty();


    // Variables of grid_Stats layer of StackPane
    public BooleanProperty visibleStats = new SimpleBooleanProperty();
    public BooleanProperty visibleOwnStats = new SimpleBooleanProperty();


    public StringProperty energyCubes = new SimpleStringProperty();

    public ObjectProperty<Image> currentPlayerAvatarImage = new SimpleObjectProperty<>();
    public StringProperty currentPlayerName = new SimpleStringProperty();

    public BooleanProperty disableNextPlayer = new SimpleBooleanProperty();
    public BooleanProperty disablePreviousPlayer = new SimpleBooleanProperty();

    public Image openControlPoint1Image = new Image("/OpenControlPoint1.png");
    public Image openControlPoint2Image = new Image("/OpenControlPoint2.png");
    public Image openControlPoint3Image = new Image("/OpenControlPoint3.png");
    public Image openControlPoint4Image = new Image("/OpenControlPoint4.png");

    public Image arrivedControlPoint1Image = new Image("/ArrivedControlPoint1.png");
    public Image arrivedControlPoint2Image = new Image("/ArrivedControlPoint2.png");
    public Image arrivedControlPoint3Image = new Image("/ArrivedControlPoint3.png");
    public Image arrivedControlPoint4Image = new Image("/ArrivedControlPoint4.png");

    public ObjectProperty<Image> controlPoint1 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> controlPoint2 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> controlPoint3 = new SimpleObjectProperty<>();
    public ObjectProperty<Image> controlPoint4 = new SimpleObjectProperty<>();

    // Variables that are bound to pane_CardsYouGotNow
    public BooleanProperty visibleCardsYouGotNow = new SimpleBooleanProperty();

    // Variables that are bound to pane_DrawDamage
    public BooleanProperty visibleDrawDamage = new SimpleBooleanProperty();

    public BooleanProperty visibleSpamHidden = new SimpleBooleanProperty();
    public BooleanProperty visibleVirusHidden = new SimpleBooleanProperty();
    public BooleanProperty visibleTrojanHidden = new SimpleBooleanProperty();
    public BooleanProperty visibleWormHidden = new SimpleBooleanProperty();

    public StringProperty spamCount = new SimpleStringProperty();
    public StringProperty virusCount = new SimpleStringProperty();
    public StringProperty trojanCount = new SimpleStringProperty();
    public StringProperty wormCount = new SimpleStringProperty();


    // Variables that are bound to grid_Chat
    public StringProperty player1Name = new SimpleStringProperty();
    public StringProperty player2Name = new SimpleStringProperty();
    public StringProperty player3Name = new SimpleStringProperty();
    public StringProperty player4Name = new SimpleStringProperty();
    public StringProperty player5Name = new SimpleStringProperty();

    public BooleanProperty visiblePlayer1 = new SimpleBooleanProperty();
    public BooleanProperty visiblePlayer2 = new SimpleBooleanProperty();
    public BooleanProperty visiblePlayer3 = new SimpleBooleanProperty();
    public BooleanProperty visiblePlayer4 = new SimpleBooleanProperty();
    public BooleanProperty visiblePlayer5 = new SimpleBooleanProperty();

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
    public ObjectProperty<Boolean> readyPlayers = new SimpleObjectProperty<>();

    Integer [] playerIDs;
    String [] playerNames;

    // Variables bound to grid_GameEnd of StackPane
    public BooleanProperty visibleGameEnd = new SimpleBooleanProperty();
    public ObjectProperty<Image> gameEndImage = new SimpleObjectProperty<>();

    public Image gameLostImage = new Image("/GameLost.png");
    public Image gameWonImage = new Image("/GameWon.png");


    /**
     * GameViewModel(DataModel, User)
     * Constructor of GameViewModel class
     * @param dataModel
     * @param player
     * @author Thomas Richter
     */
    public GameViewModel(DataModel dataModel, User player) {
        this.player = player;
        this.dataModel = (DataModelManager) dataModel;
        ((DataModelManager) dataModel).setGameViewModel(this);
        initBindVariables();
    }

    /**
     * initBindVariables()
     * This method initializes all the variables that are bound to the GameViewController
     * @author Thomas Richter
     */
    private void initBindVariables() {
        // Variables of grid_Game StackPane Layer
        this.gameStarted = new SimpleBooleanProperty(false);


        this.ownName = new SimpleStringProperty();
        this.visibleStartingBoardReboot = new SimpleBooleanProperty(false);
        this.setStartingPointPane = new SimpleBooleanProperty(false);

        this.visiblePlayItOff = new SimpleBooleanProperty(true);
        this.visibleNewChat = new SimpleBooleanProperty(false);
        this.visibleChat = new SimpleBooleanProperty(false);

        this.showTimer = new SimpleBooleanProperty(false);
        this.timerGIF = new Image("/Timer.gif");
        this.timerFrame = new Image("/TimerFrame.png");

        this.timerImage = new SimpleObjectProperty<>(timerFrame);

        this.visibleCardsYouGotNow = new SimpleBooleanProperty(false);

        this.showTwonky = new SimpleBooleanProperty(false);
        this.showHulkX90 = new SimpleBooleanProperty(false);
        this.showHammerBot = new SimpleBooleanProperty(false);
        this.showSmashBot = new SimpleBooleanProperty(false);
        this.showZoomBot = new SimpleBooleanProperty(false);
        this.showSpinBot = new SimpleBooleanProperty(false);

        if (!(dataModel.getPlayerNameTwonky() == null)) {
            showTwonky.setValue(true);
        }
        if (!(dataModel.getPlayerNameHulkX90() == null)) {
            showHulkX90.setValue(true);
        }
        if (!(dataModel.getPlayerNameHammerBot() == null)) {
            showHammerBot.setValue(true);
        }
        if (!(dataModel.getPlayerNameSmashBot() == null)) {
            showSmashBot.setValue(true);
        }
        if (!(dataModel.getPlayerNameZoomBot() == null)) {
            showZoomBot.setValue(true);
        }
        if (!(dataModel.getPlayerNameSpinBot() == null)) {
            showSpinBot.setValue(true);
        }


        // Variables of grid_Phase2 StackPane Layer
        this.visiblePhase2.setValue(false);

        this.disableResetRegisters.setValue(false);

        this.programmingCardsPath = new String[9];
        this.registersPath = new String[5];

        initRegisters();
        initProgrammingCards();

        // Variables of grid_ShuffleCards StackPane Layer
        if (dataModel.shuffleCards) {
            visibleShuffleCards.setValue(true);
        } else {
            visibleShuffleCards.setValue(false);
        }

        // Variables of pane_Phase3 StackPane Layer
        this.visiblePhase3.setValue(false);

        initRegisterCards();

        // Variables of gird_Damage StackPane Layer
        // Variables of grid_Damages

        this.visibleDamageLayer = new SimpleBooleanProperty(false);
        this.remainingDamages = new SimpleStringProperty("");

        // Variables of pane_Stats StackPane Layer
        this.currentStatPlayerID = 0;
        this.softupdate = false;

        this.visibleStats.setValue(false);
        this.visibleOwnStats.setValue(false);

        this.currentPlayerAvatarImage = new SimpleObjectProperty<>();
        if (!(dataModel.getRobot() == null)) {
            if (dataModel.getRobot().toLowerCase().equals("twonky")) {
                currentPlayerAvatarImage.setValue(twonkyAvatar);
            } else if (dataModel.getRobot().toLowerCase().equals("hulkx90")) {
                currentPlayerAvatarImage.setValue(hulkX90Avatar);
            } else if (dataModel.getRobot().toLowerCase().equals("hammerbot")) {
                currentPlayerAvatarImage.setValue(hammerBotAvatar);
            } else if (dataModel.getRobot().toLowerCase().equals("smashbot")) {
                currentPlayerAvatarImage.setValue(smashBotAvatar);
            } else if (dataModel.getRobot().toLowerCase().equals("zoombot")) {
                currentPlayerAvatarImage.setValue(zoomBotAvatar);
            } else if (dataModel.getRobot().toLowerCase().equals("spinbot")) {
                currentPlayerAvatarImage.setValue(spinBotAvatar);
            }
        }

        this.currentPlayerName = new SimpleStringProperty(dataModel.name);

        this.disableNextPlayer.setValue(false);
        this.disablePreviousPlayer.setValue(false);

        this.energyCubes.setValue("5");

        this.controlPoint1 = new SimpleObjectProperty<>(new Image("/OpenControlPoint1.png"));
        this.controlPoint2 = new SimpleObjectProperty<>(new Image("/OpenControlPoint2.png"));
        this.controlPoint3 = new SimpleObjectProperty<>(new Image("/OpenControlPoint3.png"));
        this.controlPoint4 = new SimpleObjectProperty<>(new Image("/OpenControlPoint4.png"));

        // Variables of pane_DrawDamage StackPane Layer

        this.visibleDrawDamage.setValue(false);
        this.spamCount.setValue("0");
        this.virusCount.setValue("0");
        this.trojanCount.setValue("0");
        this.wormCount.setValue("0");


        // Variables of grid_Chat StackPane Layer
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

        this.readyPlayers.setValue(false);

        this.playerIDs = new Integer [6];
        this.playerNames = new String [6];

        getRobotInformation();

        // Variables of grid_GameEnd
        this.visibleGameEnd.set(false);
        this.gameEndImage = new SimpleObjectProperty<>(gameWonImage);


        // Locks all the GUI elements which should not be displayed when using the AI
        if (dataModel.isAI) {
            lockGUIforAI();
        }
    }


    /**
     * updateViewModel()
     * This method is called at the end of every ReadThread method
     * which implements the Server-Client communication protocol messages,
     * by calling the updateViewModels method of the DataModelManager
     * These methods of ReadThread manipulate the Data of the DataModelManager.
     * Therefore after the manipulation the GameStartViewModel has to fetch the updates.
     * Additionally some methods from the GUI manipulate and consume data from the DataModel,
     * therefore they can also initiate the calling of updateViewModel()
     * @author Thomas Richter
     */
    public void updateViewModel () {

        logger.debug("GameViewModel is updated:");

        // Updating grid_Game Layer of StackPane
        if (dataModel.gameStarted && !(gameStarted.getValue())) {
            gameStarted.setValue(true);
        }

        if (dataModel.activePhase == 0) {

            ownName.setValue(dataModel.name);

            if (dataModel.getRobot() == "Twonky") {
                ownNameColor.setValue(Color.DARKORANGE);
            } else if (dataModel.getRobot() == "HulkX90") {
                ownNameColor.setValue(Color.RED);
            } else if (dataModel.getRobot() == "HammerBot") {
                ownNameColor.setValue(Color.BLUE);
            } else if (dataModel.getRobot() == "SmashBot") {
                ownNameColor.setValue(Color.ORANGE);
            } else if (dataModel.getRobot() == "ZoomBot") {
                ownNameColor.setValue(Color.GREEN);
            } else if (dataModel.getRobot() == "SpinBot") {
                ownNameColor.setValue(Color.CYAN);
            }

            if (!(dataModel.getRobot() == null)) {
                if (dataModel.getRobot().toLowerCase().equals("twonky")) {
                    ownPlayerAvatarImage.setValue(twonkyAvatar);
                } else if (dataModel.getRobot().toLowerCase().equals("hulkx90")) {
                    ownPlayerAvatarImage.setValue(hulkX90Avatar);
                } else if (dataModel.getRobot().toLowerCase().equals("hammerbot")) {
                    ownPlayerAvatarImage.setValue(hammerBotAvatar);
                } else if (dataModel.getRobot().toLowerCase().equals("smashbot")) {
                    ownPlayerAvatarImage.setValue(smashBotAvatar);
                } else if (dataModel.getRobot().toLowerCase().equals("zoombot")) {
                    ownPlayerAvatarImage.setValue(zoomBotAvatar);
                } else if (dataModel.getRobot().toLowerCase().equals("spinbot")) {
                    ownPlayerAvatarImage.setValue(spinBotAvatar);
                }
            }

            if (!(dataModel.racingCourse == null) && dataModel.racingCourse.equals("ExtraCrispy")) {
                visibleStartingBoardReboot.setValue(true);
            } else {
                visibleStartingBoardReboot.setValue(false);
            }

            checkForStartingPointTurn();
        }


        getRobotInformation();
        getRobotPositions();

        // Updating grid_Phase 2 Layer of StackPane
        if (dataModel.activePhase == 2) {

            updateProgrammingCards(); // updates the paths of the Programming Cards
            updateRegister(); // updates the paths of the selected register Cards

            if (dataModel.timerStarted) {
                showTimer.setValue(true);
            } else {
                showTimer.setValue(false);
            }
            if (!dataModel.shuffleCards) {
                visiblePhase2.setValue(true);
            } else {
                visiblePhase2.setValue(false);
            }
            if (dataModel.resetRegister && registersPath[0].equals("/CurrentRegister.png")) {
                dataModel.resetRegister = false;
                resetRegister();
            }

            resetActivationPhaseRegisters();

        } else {
            visiblePhase2.setValue(false);
        }

        // Variables of pane_CardsYouGotNow StackPane Layer
        if (dataModel.shuffledCards) {
            visiblePhase3.setValue(false);
            visibleStats.setValue(false);
            visibleCardsYouGotNow.setValue(true);
        } else {
            visibleCardsYouGotNow.setValue(false);
        }


        // Variables of grid_ShuffleCards StackPane Layer
        if (dataModel.shuffleCards) {
            visibleShuffleCards.setValue(true);
        } else {
            visibleShuffleCards.setValue(false);
        }

        // Updating pane_Phase3 Layer of StackPane
        updateRegisterCards();

        // Variables of pane_DrawDamage StackPane Layer

        if (dataModel.showDrawDamage) {
            visibleStats.setValue(false);
            visiblePhase3.setValue(false);

            if (dataModel.drawnDamageCardsCount[0] != 0) {
                visibleSpamHidden.setValue(false);
                spamCount.setValue(String.valueOf(dataModel.drawnDamageCardsCount[0]) + "x");
            } else {
                spamCount.setValue("0x");
                visibleSpamHidden.setValue(true);
            }

            if (dataModel.drawnDamageCardsCount[1] != 0) {
                visibleVirusHidden.setValue(false);
                virusCount.setValue(String.valueOf(dataModel.drawnDamageCardsCount[1])+ "x");
            } else {
                virusCount.setValue("0x");
                visibleVirusHidden.setValue(true);
            }

            if (dataModel.drawnDamageCardsCount[2] != 0) {
                visibleTrojanHidden.setValue(false);
                trojanCount.setValue(String.valueOf(dataModel.drawnDamageCardsCount[2])+ "x");
            } else {
                trojanCount.setValue("0x");
                visibleTrojanHidden.setValue(true);
            }


            if (dataModel.drawnDamageCardsCount[3] != 0) {
                visibleWormHidden.setValue(false);
                wormCount.setValue(String.valueOf(dataModel.drawnDamageCardsCount[3])+ "x");
            } else {
                wormCount.setValue("0x");
                visibleWormHidden.setValue(true);
            }

            visibleDrawDamage.setValue(true);
        }

        // Updates only needed when activation phase is set

        if (dataModel.activePhase == 3) {

            // PlayIt Button
            if (dataModel.activatePlayIt) {
                visiblePlayItOff.setValue(false);
                visiblePlayIt.setValue(true);
            } else {
                visiblePlayItOff.setValue(true);
                visiblePlayIt.setValue(false);
            }

            disableResetRegisters.setValue(false);

            // Turns off timer
            showTimer.setValue(false);
        }

        // Updating of grid_Damage StackPane Layer
        if (dataModel.pickDamage == 0) {
            visibleDamageLayer.setValue(false);
            if (dataModel.activePhase == 3 && dataModel.activatePlayIt) {
                visiblePlayItOff.setValue(false);
                visiblePlayIt.setValue(true);
            }
        } else {
            visibleDamageLayer.setValue(true);
            visiblePlayItOff.setValue(true);
            visiblePlayIt.setValue(false);
        }

        remainingDamages.setValue("Remaining: " + dataModel.pickDamage);

        // Updating of pane_Chat layer

        if (dataModel.receivedError != null && dataModel.receivedError) {
            visibleChat.setValue(true);
        } else {
            if (dataModel.newChat && !visibleChat.getValue()) {
                visibleNewChat.setValue(true);
            } else {
                visibleNewChat.setValue(false);
            }
        }

        hashMapLength = dataModel.players.size();

        if (hashMapLength > 0) {
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

        // Locks GUI Elements in case User is an AI
        if(dataModel.isAI) {
            lockGUIforAI();
        }

        // updating of grid_GameEnd StackPane Layer
        if (dataModel.gameLost || dataModel.gameWon) {
            if(dataModel.gameLost) {
                gameEndImage.setValue(gameLostImage);
            } else if (dataModel.gameWon) {
                gameEndImage.setValue(gameWonImage);
            }
            visibleGameEnd.setValue(true);
        }
    }

    // Outsourced initialization methods

    /**
     * getRacingCourseFieldImage():
     * looks for the given Values which Images needs to be returned
     *
     * @param x: xPosition in the FieldObject[][]
     * @param y: yPosition in the FieldObject[][]

     * @return String of the path of the image
     * @author Franziska Leitmeir
     */
    public String getRacingCourseFieldImage(int x, int y)  {
        FieldObject fields [][] = dataModel.gameMap;
        String path = null;

        FieldObject currentField = fields[x][y];

        if (currentField == null) {
            path = "/Empty.png";
        } else {
            String type = currentField.getType();
            int speed = currentField.getSpeed();
            ArrayList orientations = currentField.getOrientations();
            double orientation = currentField.getOrientation();
            boolean isCrossing = currentField.getIsCrossing();
            int count = currentField.getCounter();

            switch (type) {

                //Empty
                case "Empty":
                    path = "/Empty.png";
                    break;

                //Belts (blue or green)
                case "Belt":
                    if (speed == 1) {
                        path = "/Belt1.gif";
                    } else
                        path = "/Belt2.gif";
                    break;

                //EnergySpaces
                case "EnergySpace":
                    path = "/EnergySpace.png";
                    break;

                //RotaitingBelts (blue or green? right or left? Crossing or Curve?)
                case "RotatingBelt":
                    switch (speed) {
                        case 1:
                            if (orientations.get(0).equals("down") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("left") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("right") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("up") && orientations.get(1).equals("left")) {

                               if (isCrossing) { path = "/RotatingCrossingBeltLeft1.png";}
                               else path = "/RotatingBeltLeft1.png";
                            } else
                                if (isCrossing) {path = "/RotatingCrossingBeltRight1.png";}
                                else path = "/RotatingBeltRight1.png";

                            break;

                        case 2:
                            if (orientations.get(0).equals("down") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("left") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("right") && orientations.get(1).equals("left") ||
                                    orientations.get(0).equals("up") && orientations.get(1).equals("left")) {

                                if (isCrossing) { path = "/RotatingCrossingBeltLeft2.png";}
                                else path = "/RotatingBeltLeft2.png";
                            } else
                            if (isCrossing) {path = "/RotatingCrossingBeltRight2.png";}
                            else path = "/RotatingBeltRight2.png";

                            break;
                    }
                    break;

                //Wall
                case "Wall":
                    path = "/Wall2.png";
                    break;

                //Empty with Laser
                case "L":
                    path = "/Laser.png";
                    break;

                //RestartPoint
                case "RestartPoint":
                    path = "/RestartPoint.png";
                    break;

                //ControlPoint
                case "ControlPoint":
                    path = "/ControlPoint1.png";
                    break;

                //Pit
                case "Pit":
                    path = "/Pit.png";
                    break;

                //Gear (right or left)
                case "Gear":
                    if (orientation == 180) {
                        path = "/GearLeft.png";
                    }
                    else path = "/GearRight.png";
                    break;

                //Wall with Laser or Laser ends at Wall
                case "LaserWall":
                    path = "/LaserWall.png";
                    break;
                case "LWall":
                    path = "/LWall.png";
                    break;

                //Wall and EnergySpace on the same field
                case"EnergySpaceWall":
                    path = "/EnergySpaceWall.png";
                    break;

                //Laser shoots over Belt
                case"BeltL":
                    path = "/BeltLaser.gif";
                    break;

                //ControlPoint with Laser on a Wall (which counter?)
                case"ControlPointWallLaser":
                    switch (count) {
                        case 1:  path = "/ControlPointLaserWall1.png";
                        break;
                        case 2:  path = "/ControlPointLaserWall2.png";
                        break;
                        case 3:  path = "/ControlPointLaserWall3.png";
                        break;
                        case 4:  path = "/ControlPointLaserWall4.png";
                        break;
                    }

                    break;
            }
        }
        return path;
    }

    public String getType (int x, int y) {
        String type = "Fehler";
        FieldObject field = dataModel.gameMap [x][y];
        type = field.getType();
        return type;
    }

    public double getRotation (int x, int y) {
        FieldObject fields [][] = dataModel.gameMap;
        double orientation = 0;


        FieldObject currentField = fields[x][y];
        if (currentField == null) {
            orientation = 0;
        } else {
            orientation = currentField.getOrientation();


        }
        return orientation;
    }

    /**
     * initRegisters()
     * Method is part of initBindvariables() and initializes the register images.
     * @author Thomas Richter
     */
    public void initRegisters () {
        this.register1 = new SimpleObjectProperty<>(new Image("/CurrentRegister.png"));
        this.register2 = new SimpleObjectProperty<>(new Image("/LockedRegister.png"));
        this.register3 = new SimpleObjectProperty<>(new Image("/LockedRegister.png"));
        this.register4 = new SimpleObjectProperty<>(new Image("/LockedRegister.png"));
        this.register5 = new SimpleObjectProperty<>(new Image("/LockedRegister.png"));
    }

    /**
     * initProgrammingCards()
     * Method is part of initBindvariables() and initializes the programmingCard images and buttons.
     * @author Thomas Richter
     */
    public void initProgrammingCards () {
        this.disableProgrammingCard1.setValue(false);
        this.disableProgrammingCard2.setValue(false);
        this.disableProgrammingCard3.setValue(false);
        this.disableProgrammingCard4.setValue(false);
        this.disableProgrammingCard5.setValue(false);
        this.disableProgrammingCard6.setValue(false);
        this.disableProgrammingCard7.setValue(false);
        this.disableProgrammingCard8.setValue(false);
        this.disableProgrammingCard9.setValue(false);

        this.visibleProgrammingCard1.setValue(true);
        this.visibleProgrammingCard2.setValue(true);
        this.visibleProgrammingCard3.setValue(true);
        this.visibleProgrammingCard4.setValue(true);
        this.visibleProgrammingCard5.setValue(true);
        this.visibleProgrammingCard6.setValue(true);
        this.visibleProgrammingCard7.setValue(true);
        this.visibleProgrammingCard8.setValue(true);
        this.visibleProgrammingCard9.setValue(true);

        String [] programmingCards = dataModel.getProgrammingCards();
        this.programmingCardImages = new Image[9];

        for (int i = 0; i < 9; i++) {
            programmingCardsPath[i] = "/" + programmingCards[i] + ".png";
            programmingCardImages[i] = new Image(programmingCardsPath[i]);
            if (programmingCards[i].equals("Again")) {
                againFlags [i] = true;
            } else {
                againFlags [i] = false;
            }

        }
        this.programmingCard1 = new SimpleObjectProperty<>(programmingCardImages[0]);
        this.programmingCard2 = new SimpleObjectProperty<>(programmingCardImages[1]);
        this.programmingCard3 = new SimpleObjectProperty<>(programmingCardImages[2]);
        this.programmingCard4 = new SimpleObjectProperty<>(programmingCardImages[3]);
        this.programmingCard5 = new SimpleObjectProperty<>(programmingCardImages[4]);
        this.programmingCard6 = new SimpleObjectProperty<>(programmingCardImages[5]);
        this.programmingCard7 = new SimpleObjectProperty<>(programmingCardImages[6]);
        this.programmingCard8 = new SimpleObjectProperty<>(programmingCardImages[7]);
        this.programmingCard9 = new SimpleObjectProperty<>(programmingCardImages[8]);

        lockAgainCards();
    }

    /**
     * initRegisterCards()
     * Method is part of initBindvariables() and initializes the register images, which are
     * shown in the ActivationPhase layer, where the player can see which cards are played by which
     * player/robot.
     * @author Thomas Richter
     */
    public void initRegisterCards () {
        this.twonkyRegistersPath = new String[5];
        this.twonkyRegistersImage = new Image[5];
        this.twonkyRegisterImage1 = new SimpleObjectProperty<>();
        this.twonkyRegisterImage2 = new SimpleObjectProperty<>();
        this.twonkyRegisterImage3 = new SimpleObjectProperty<>();
        this.twonkyRegisterImage4 = new SimpleObjectProperty<>();
        this.twonkyRegisterImage5 = new SimpleObjectProperty<>();

        this.hulkX90RegistersPath = new String[5];
        this.hulkX90RegistersImage = new Image[5];
        this.hulkX90RegisterImage1 = new SimpleObjectProperty<>();
        this.hulkX90RegisterImage2 = new SimpleObjectProperty<>();
        this.hulkX90RegisterImage3 = new SimpleObjectProperty<>();
        this.hulkX90RegisterImage4 = new SimpleObjectProperty<>();
        this.hulkX90RegisterImage5 = new SimpleObjectProperty<>();

        this.hammerBotRegistersPath = new String[5];
        this.hammerBotRegistersImage = new Image[5];
        this.hammerBotRegisterImage1 = new SimpleObjectProperty<>();
        this.hammerBotRegisterImage2 = new SimpleObjectProperty<>();
        this.hammerBotRegisterImage3 = new SimpleObjectProperty<>();
        this.hammerBotRegisterImage4 = new SimpleObjectProperty<>();
        this.hammerBotRegisterImage5 = new SimpleObjectProperty<>();

        this.smashBotRegistersPath = new String[5];
        this.smashBotRegistersImage = new Image[5];
        this.smashBotRegisterImage1 = new SimpleObjectProperty<>();
        this.smashBotRegisterImage2 = new SimpleObjectProperty<>();
        this.smashBotRegisterImage3 = new SimpleObjectProperty<>();
        this.smashBotRegisterImage4 = new SimpleObjectProperty<>();
        this.smashBotRegisterImage5 = new SimpleObjectProperty<>();

        this.zoomBotRegistersPath = new String[5];
        this.zoomBotRegistersImage = new Image[5];
        this.zoomBotRegisterImage1 = new SimpleObjectProperty<>();
        this.zoomBotRegisterImage2 = new SimpleObjectProperty<>();
        this.zoomBotRegisterImage3 = new SimpleObjectProperty<>();
        this.zoomBotRegisterImage4 = new SimpleObjectProperty<>();
        this.zoomBotRegisterImage5 = new SimpleObjectProperty<>();

        this.spinBotRegistersPath = new String[5];
        this.spinBotRegistersImage = new Image[5];
        this.spinBotRegisterImage1 = new SimpleObjectProperty<>();
        this.spinBotRegisterImage2 = new SimpleObjectProperty<>();
        this.spinBotRegisterImage3 = new SimpleObjectProperty<>();
        this.spinBotRegisterImage4 = new SimpleObjectProperty<>();
        this.spinBotRegisterImage5 = new SimpleObjectProperty<>();

    }




    // Outsourced updating methods

    /**
     * getRobotInformation()
     * Method is part of the updateViewModel() and initBindVariables() methods and collects and updates the robot data, like:
     * - which robot should be displayed
     * - which player uses which robot
     * - what are the robot stats, like EnergyCubes, reached ControlPoints which can be seen in the Stats layer
     * @author Thomas Richter
     */
    public void getRobotInformation () {

        if (dataModel.statsPlayer == 0) {
            if (!(dataModel.getRobot() == null)) {
                if (dataModel.getRobot().toLowerCase().equals("twonky")) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    if (dataModel.playerIdTwonky != null) {
                        currentStatPlayerID = dataModel.playerIdTwonky;
                    }

                } else if (dataModel.getRobot().toLowerCase().equals("hulkx90")) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    if (dataModel.playerIdHulkX90 != null) {
                        currentStatPlayerID = dataModel.playerIdHulkX90;
                    }
                } else if (dataModel.getRobot().toLowerCase().equals("hammerbot")) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    if (dataModel.playerIdHammerBot != null) {
                        currentStatPlayerID = dataModel.playerIdHammerBot;
                    }
                } else if (dataModel.getRobot().toLowerCase().equals("smashbot")) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    if (dataModel.playerIdSmashBot != null) {
                        currentStatPlayerID = dataModel.playerIdSmashBot;
                    }
                } else if (dataModel.getRobot().toLowerCase().equals("zoombot")) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    if (dataModel.playerIdZoomBot != null) {
                        currentStatPlayerID = dataModel.playerIdZoomBot;
                    }
                } else if (dataModel.getRobot().toLowerCase().equals("spinbot")) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    if (dataModel.playerIdSpinBot != null) {
                        currentStatPlayerID = dataModel.playerIdSpinBot;
                    }
                }
            }
            currentPlayerName.setValue(dataModel.name);
            visibleOwnStats.setValue(true);
        } else {
            visibleOwnStats.setValue(false);
        }


        // Checks if a player uses the robot Twonky and sets the correct AvatarImage
        if (!(dataModel.playerIdTwonky == null)) {
            if ((dataModel.playerNameTwonky + " (" + dataModel.playerIdTwonky + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(twonkyAvatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    currentPlayerName.setValue(dataModel.playerNameTwonky);
                    currentStatPlayerID = dataModel.playerIdTwonky;
                }
            } else if ((dataModel.playerNameTwonky + " (" + dataModel.playerIdTwonky + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(twonkyAvatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    currentPlayerName.setValue(dataModel.playerNameTwonky);
                    currentStatPlayerID = dataModel.playerIdTwonky;
                }
            } else if ((dataModel.playerNameTwonky + " (" + dataModel.playerIdTwonky + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(twonkyAvatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    currentPlayerName.setValue(dataModel.playerNameTwonky);
                    currentStatPlayerID = dataModel.playerIdTwonky;
                }
            } else if ((dataModel.playerNameTwonky + " (" + dataModel.playerIdTwonky + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(twonkyAvatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    currentPlayerName.setValue(dataModel.playerNameTwonky);
                    currentStatPlayerID = dataModel.playerIdTwonky;
                }
            } else if ((dataModel.playerNameTwonky + " (" + dataModel.playerIdTwonky + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(twonkyAvatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(twonkyAvatar);
                    currentPlayerName.setValue(dataModel.playerNameTwonky);
                    currentStatPlayerID = dataModel.playerIdTwonky;
                }
            }
        }

        // Checks if a player uses the robot HulkX90 and sets the correct AvatarImage
        if (!(dataModel.playerIdHulkX90 == null)) {
            if ((dataModel.playerNameHulkX90 + " (" + dataModel.playerIdHulkX90 + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(hulkX90Avatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    currentPlayerName.setValue(dataModel.playerNameHulkX90);
                    currentStatPlayerID = dataModel.playerIdHulkX90;
                }
            } else if ((dataModel.playerNameHulkX90 + " (" + dataModel.playerIdHulkX90 + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(hulkX90Avatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    currentPlayerName.setValue(dataModel.playerNameHulkX90);
                    currentStatPlayerID = dataModel.playerIdHulkX90;
                }
            } else if ((dataModel.playerNameHulkX90 + " (" + dataModel.playerIdHulkX90 + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(hulkX90Avatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    currentPlayerName.setValue(dataModel.playerNameHulkX90);
                    currentStatPlayerID = dataModel.playerIdHulkX90;
                }
            } else if ((dataModel.playerNameHulkX90 + " (" + dataModel.playerIdHulkX90 + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(hulkX90Avatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    currentPlayerName.setValue(dataModel.playerNameHulkX90);
                    currentStatPlayerID = dataModel.playerIdHulkX90;
                }
            } else if ((dataModel.playerNameHulkX90 + " (" + dataModel.playerIdHulkX90 + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(hulkX90Avatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(hulkX90Avatar);
                    currentPlayerName.setValue(dataModel.playerNameHulkX90);
                    currentStatPlayerID = dataModel.playerIdHulkX90;
                }
            }
        }

        // Checks if a player uses the robot HammerBot and sets the correct AvatarImage
        if (!(dataModel.playerIdHammerBot == null)) {
            if ((dataModel.playerNameHammerBot + " (" + dataModel.playerIdHammerBot + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(hammerBotAvatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameHammerBot);
                    currentStatPlayerID = dataModel.playerIdHammerBot;
                }
            } else if ((dataModel.playerNameHammerBot + " (" + dataModel.playerIdHammerBot + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(hammerBotAvatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameHammerBot);
                    currentStatPlayerID = dataModel.playerIdHammerBot;
                }
            } else if ((dataModel.playerNameHammerBot + " (" + dataModel.playerIdHammerBot + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(hammerBotAvatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameHammerBot);
                    currentStatPlayerID = dataModel.playerIdHammerBot;
                }
            } else if ((dataModel.playerNameHammerBot + " (" + dataModel.playerIdHammerBot + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(hammerBotAvatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameHammerBot);
                    currentStatPlayerID = dataModel.playerIdHammerBot;
                }
            } else if ((dataModel.playerNameHammerBot + " (" + dataModel.playerIdHammerBot + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(hammerBotAvatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(hammerBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameHammerBot);
                    currentStatPlayerID = dataModel.playerIdHammerBot;
                }
            }
        }

        // Checks if a player uses the robot SmashBot and sets the correct AvatarImage
        if (!(dataModel.playerIdSmashBot == null)) {
            if ((dataModel.playerNameSmashBot + " (" + dataModel.playerIdSmashBot + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(smashBotAvatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSmashBot);
                    currentStatPlayerID = dataModel.playerIdSmashBot;
                }
            } else if ((dataModel.playerNameSmashBot + " (" + dataModel.playerIdSmashBot + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(smashBotAvatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSmashBot);
                    currentStatPlayerID = dataModel.playerIdSmashBot;
                }
            } else if ((dataModel.playerNameSmashBot + " (" + dataModel.playerIdSmashBot + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(smashBotAvatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSmashBot);
                    currentStatPlayerID = dataModel.playerIdSmashBot;
                }
            } else if ((dataModel.playerNameSmashBot + " (" + dataModel.playerIdSmashBot + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(smashBotAvatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSmashBot);
                    currentStatPlayerID = dataModel.playerIdSmashBot;
                }
            } else if ((dataModel.playerNameSmashBot + " (" + dataModel.playerIdSmashBot + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(smashBotAvatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(smashBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSmashBot);
                    currentStatPlayerID = dataModel.playerIdSmashBot;
                }
            }
        }

        // Checks if a player uses the robot ZoomBot and sets the correct AvatarImage
        if (!(dataModel.playerIdZoomBot == null)) {
            if ((dataModel.playerNameZoomBot + " (" + dataModel.playerIdZoomBot + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(zoomBotAvatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameZoomBot);
                    currentStatPlayerID = dataModel.playerIdZoomBot;
                }
            } else if ((dataModel.playerNameZoomBot + " (" + dataModel.playerIdZoomBot + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(zoomBotAvatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameZoomBot);
                    currentStatPlayerID = dataModel.playerIdZoomBot;
                }
            } else if ((dataModel.playerNameZoomBot + " (" + dataModel.playerIdZoomBot + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(zoomBotAvatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameZoomBot);
                    currentStatPlayerID = dataModel.playerIdZoomBot;
                }
            } else if ((dataModel.playerNameZoomBot + " (" + dataModel.playerIdZoomBot + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(zoomBotAvatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameZoomBot);
                    currentStatPlayerID = dataModel.playerIdZoomBot;
                }
            } else if ((dataModel.playerNameZoomBot + " (" + dataModel.playerIdZoomBot + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(zoomBotAvatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(zoomBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameZoomBot);
                    currentStatPlayerID = dataModel.playerIdZoomBot;
                }
            }
        }

        // Checks if a player uses the robot SpinBot and sets the correct AvatarImage
        if (!(dataModel.playerIdSpinBot == null)) {
            if ((dataModel.playerNameSpinBot + " (" + dataModel.playerIdSpinBot + ")").equals(player1Name.getValue())) {
                player1AvatarImage.setValue(spinBotAvatar);
                if (dataModel.statsPlayer == 1) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSpinBot);
                    currentStatPlayerID = dataModel.playerIdSpinBot;
                }
            } else if ((dataModel.playerNameSpinBot + " (" + dataModel.playerIdSpinBot + ")").equals(player2Name.getValue())) {
                player2AvatarImage.setValue(spinBotAvatar);
                if (dataModel.statsPlayer == 2) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSpinBot);
                    currentStatPlayerID = dataModel.playerIdSpinBot;
                }
            } else if ((dataModel.playerNameSpinBot + " (" + dataModel.playerIdSpinBot + ")").equals(player3Name.getValue())) {
                player3AvatarImage.setValue(spinBotAvatar);
                if (dataModel.statsPlayer == 3) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSpinBot);
                    currentStatPlayerID = dataModel.playerIdSpinBot;
                }
            } else if ((dataModel.playerNameSpinBot + " (" + dataModel.playerIdSpinBot + ")").equals(player4Name.getValue())) {
                player4AvatarImage.setValue(spinBotAvatar);
                if (dataModel.statsPlayer == 4) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSpinBot);
                    currentStatPlayerID = dataModel.playerIdSpinBot;
                }
            } else if ((dataModel.playerNameSpinBot + " (" + dataModel.playerIdSpinBot + ")").equals(player5Name.getValue())) {
                player5AvatarImage.setValue(spinBotAvatar);
                if (dataModel.statsPlayer == 5) {
                    currentPlayerAvatarImage.setValue(spinBotAvatar);
                    currentPlayerName.setValue(dataModel.playerNameSpinBot);
                    currentStatPlayerID = dataModel.playerIdSpinBot;
                }
            }
        }

        // Updating missing pane_Stats Layer information of StackPane

        if (dataModel.energy.get(currentStatPlayerID) != null) {
            energyCubes.setValue("");
            String energyValue = String.valueOf((int) (dataModel.energy.get(currentStatPlayerID)));
            energyCubes.setValue(energyValue);
        } else {
            energyCubes.setValue("5");
        }

        if (dataModel.checkpointReached.get(currentStatPlayerID) == null) {
            controlPoint1.setValue(openControlPoint1Image);
            controlPoint2.setValue(openControlPoint2Image);
            controlPoint3.setValue(openControlPoint3Image);
            controlPoint4.setValue(openControlPoint4Image);
        } else if (dataModel.checkpointReached.get(currentStatPlayerID) == 1) {
            controlPoint1.setValue(arrivedControlPoint1Image);
            controlPoint2.setValue(openControlPoint2Image);
            controlPoint3.setValue(openControlPoint3Image);
            controlPoint4.setValue(openControlPoint4Image);
        } else if (dataModel.checkpointReached.get(currentStatPlayerID) == 2) {
            controlPoint1.setValue(arrivedControlPoint1Image);
            controlPoint2.setValue(arrivedControlPoint2Image);
            controlPoint3.setValue(openControlPoint3Image);
            controlPoint4.setValue(openControlPoint4Image);
        } else if (dataModel.checkpointReached.get(currentStatPlayerID) == 3) {
            controlPoint1.setValue(arrivedControlPoint1Image);
            controlPoint2.setValue(arrivedControlPoint2Image);
            controlPoint3.setValue(arrivedControlPoint3Image);
            controlPoint4.setValue(openControlPoint4Image);
        } else if (dataModel.checkpointReached.get(currentStatPlayerID) == 4) {
            controlPoint1.setValue(arrivedControlPoint1Image);
            controlPoint2.setValue(arrivedControlPoint2Image);
            controlPoint3.setValue(arrivedControlPoint3Image);
            controlPoint4.setValue(arrivedControlPoint4Image);
        }
    }


    /**
     * getRobotPositions()
     * Method is part of the updateViewModel() and initBindVariables() methods, and gets robot information like:
     * - x,y position of robot in pixel, so that it can be consumed by the GUI
     * - orientation of the robot
     * - x,y position of robot laser (image is always in front of robot, therefore depends on orientation) in pixel,
     *   so that it can be consumed by the GUI
     * - orientation of the robot laser
     * - x,y position of damage effect image (identical to robot)
     * - whether damage animation and laser should be shown in the GUI
     * @author Thomas Richter
     */
    public void getRobotPositions () {
        logger.debug(classname + "getRobotPositions() is called from updateViewModel() of GameViewModel");

        // Updates the positions of robots
        xTwonky.setValue(dataModel.positionTwonky.getKey());
        yTwonky.setValue(dataModel.positionTwonky.getValue());
        rotationTwonky.setValue(dataModel.rotationTwonky);
        rotationTwonkyLaser.setValue(dataModel.rotationTwonky);

        xHulkX90.setValue(dataModel.positionHulkX90.getKey());
        yHulkX90.setValue(dataModel.positionHulkX90.getValue());
        rotationHulkX90.setValue(dataModel.rotationHulkX90);
        rotationHulkX90Laser.setValue(dataModel.rotationHulkX90);

        xHammerBot.setValue(dataModel.positionHammerBot.getKey());
        yHammerBot.setValue(dataModel.positionHammerBot.getValue());
        rotationHammerBot.setValue(dataModel.rotationHammerBot);
        rotationHammerBotLaser.setValue(dataModel.rotationHammerBot);

        xSmashBot.setValue(dataModel.positionSmashBot.getKey());
        ySmashBot.setValue(dataModel.positionSmashBot.getValue());
        rotationSmashBot.setValue(dataModel.rotationSmashBot);
        rotationSmashBotLaser.setValue(dataModel.rotationSmashBot);

        xZoomBot.setValue(dataModel.positionZoomBot.getKey());
        yZoomBot.setValue(dataModel.positionZoomBot.getValue());
        rotationZoomBot.setValue(dataModel.rotationZoomBot);
        rotationZoomBotLaser.setValue(dataModel.rotationZoomBot);

        xSpinBot.setValue(dataModel.positionSpinBot.getKey());
        ySpinBot.setValue(dataModel.positionSpinBot.getValue());
        rotationSpinBot.setValue(dataModel.rotationSpinBot);
        rotationSpinBotLaser.setValue(dataModel.rotationSpinBot);

        // Lasers
        if (dataModel.rotationTwonky == 0.0) {
            xTwonkyLaser.setValue(dataModel.positionTwonky.getKey());
            yTwonkyLaser.setValue(dataModel.positionTwonky.getValue());
        } else if (dataModel.rotationTwonky == 90.0) {
            xTwonkyLaser.setValue(dataModel.positionTwonky.getKey() - 25.0);
            yTwonkyLaser.setValue(dataModel.positionTwonky.getValue() + 25.0);
        } else if (dataModel.rotationTwonky == 180.0) {
            xTwonkyLaser.setValue(dataModel.positionTwonky.getKey() - 50.0);
            yTwonkyLaser.setValue(dataModel.positionTwonky.getValue() + 0.0);
        } else if (dataModel.rotationTwonky == 270.0) {
            xTwonkyLaser.setValue(dataModel.positionTwonky.getKey() - 25.0);
            yTwonkyLaser.setValue(dataModel.positionTwonky.getValue() - 25.0);
        }

        if (dataModel.rotationHulkX90 == 0.0) {
            xHulkX90Laser.setValue(dataModel.positionHulkX90.getKey());
            yHulkX90Laser.setValue(dataModel.positionHulkX90.getValue());
        } else if (dataModel.rotationHulkX90 == 90.0) {
            xHulkX90Laser.setValue(dataModel.positionHulkX90.getKey() - 25.0);
            yHulkX90Laser.setValue(dataModel.positionHulkX90.getValue() + 25.0);
        } else if (dataModel.rotationHulkX90 == 180.0) {
            xHulkX90Laser.setValue(dataModel.positionHulkX90.getKey() - 50.0);
            yHulkX90Laser.setValue(dataModel.positionHulkX90.getValue() + 0.0);
        } else if (dataModel.rotationHulkX90 == 270.0) {
            xHulkX90Laser.setValue(dataModel.positionHulkX90.getKey() - 25.0);
            yHulkX90Laser.setValue(dataModel.positionHulkX90.getValue() - 25.0);
        }


        if (dataModel.rotationHammerBot == 0.0) {
            xHammerBotLaser.setValue(dataModel.positionHammerBot.getKey());
            yHammerBotLaser.setValue(dataModel.positionHammerBot.getValue());
        } else if (dataModel.rotationHammerBot == 90.0) {
            xHammerBotLaser.setValue(dataModel.positionHammerBot.getKey() - 25.0);
            yHammerBotLaser.setValue(dataModel.positionHammerBot.getValue() + 25.0);
        } else if (dataModel.rotationHammerBot == 180.0) {
            xHammerBotLaser.setValue(dataModel.positionHammerBot.getKey() - 50.0);
            yHammerBotLaser.setValue(dataModel.positionHammerBot.getValue() + 0.0);
        } else if (dataModel.rotationHammerBot == 270.0) {
            xHammerBotLaser.setValue(dataModel.positionHammerBot.getKey() - 25.0);
            yHammerBotLaser.setValue(dataModel.positionHammerBot.getValue() - 25.0);
        }

        if (dataModel.rotationSmashBot == 0.0) {
            xSmashBotLaser.setValue(dataModel.positionSmashBot.getKey());
            ySmashBotLaser.setValue(dataModel.positionSmashBot.getValue());
        } else if (dataModel.rotationSmashBot == 90.0) {
            xSmashBotLaser.setValue(dataModel.positionSmashBot.getKey() - 25.0);
            ySmashBotLaser.setValue(dataModel.positionSmashBot.getValue() + 25.0);
        } else if (dataModel.rotationSmashBot == 180.0) {
            xSmashBotLaser.setValue(dataModel.positionSmashBot.getKey() - 50.0);
            ySmashBotLaser.setValue(dataModel.positionSmashBot.getValue() + 0.0);
        } else if (dataModel.rotationSmashBot == 270.0) {
            xSmashBotLaser.setValue(dataModel.positionSmashBot.getKey() - 25.0);
            ySmashBotLaser.setValue(dataModel.positionSmashBot.getValue() - 25.0);
        }

        if (dataModel.rotationSpinBot == 0.0) {
            xSpinBotLaser.setValue(dataModel.positionSpinBot.getKey());
            ySpinBotLaser.setValue(dataModel.positionSpinBot.getValue());
        } else if (dataModel.rotationSpinBot == 90.0) {
            xSpinBotLaser.setValue(dataModel.positionSpinBot.getKey() - 25.0);
            ySpinBotLaser.setValue(dataModel.positionSpinBot.getValue() + 25.0);
        } else if (dataModel.rotationSpinBot == 180.0) {
            xSpinBotLaser.setValue(dataModel.positionSpinBot.getKey() - 50.0);
            ySpinBotLaser.setValue(dataModel.positionSpinBot.getValue() + 0.0);
        } else if (dataModel.rotationSpinBot == 270.0) {
            xSpinBotLaser.setValue(dataModel.positionSpinBot.getKey() - 25.0);
            ySpinBotLaser.setValue(dataModel.positionSpinBot.getValue() - 25.0);
        }

        if (dataModel.rotationZoomBot == 0.0) {
            xZoomBotLaser.setValue(dataModel.positionZoomBot.getKey());
            yZoomBotLaser.setValue(dataModel.positionZoomBot.getValue());
        } else if (dataModel.rotationZoomBot == 90.0) {
            xZoomBotLaser.setValue(dataModel.positionZoomBot.getKey() - 25.0);
            yZoomBotLaser.setValue(dataModel.positionZoomBot.getValue() + 25.0);
        } else if (dataModel.rotationZoomBot == 180.0) {
            xZoomBotLaser.setValue(dataModel.positionZoomBot.getKey() - 50.0);
            yZoomBotLaser.setValue(dataModel.positionZoomBot.getValue() + 0.0);
        } else if (dataModel.rotationZoomBot == 270.0) {
            xZoomBotLaser.setValue(dataModel.positionZoomBot.getKey() - 25.0);
            yZoomBotLaser.setValue(dataModel.positionZoomBot.getValue() - 25.0);
        }

        if (!(dataModel.getPlayerNameTwonky() == null)) {
            showTwonky.setValue(true);
            double x = dataModel.positionTwonky.getKey();
            double y = dataModel.positionTwonky.getValue();
            double orientation = dataModel.rotationTwonky;
            if (dataModel.reboot.containsKey(dataModel.playerIdTwonky)) {
                showTwonkyLaser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation) && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showTwonkyLaser.setValue(true);
                    softupdate = false;
                } else {
                    showTwonkyLaser.setValue(false);
                }
            }

        }

        if (!(dataModel.getPlayerNameHulkX90() == null)) {
            showHulkX90.setValue(true);
            double x = dataModel.positionHulkX90.getKey();
            double y = dataModel.positionHulkX90.getValue();
            double orientation = dataModel.rotationHulkX90;
            if (dataModel.reboot.containsKey(dataModel.playerIdHulkX90)) {
                showHulkX90Laser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation)  && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showHulkX90Laser.setValue(true);
                } else {
                    showHulkX90Laser.setValue(false);
                    softupdate = false;
                }
            }

        }

        if (!(dataModel.getPlayerNameHammerBot() == null)) {
            showHammerBot.setValue(true);
            double x = dataModel.positionHammerBot.getKey();
            double y = dataModel.positionHammerBot.getValue();
            double orientation = dataModel.rotationHammerBot;
            if (dataModel.reboot.containsKey(dataModel.playerIdHammerBot)) {
                showHammerBotLaser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation)  && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showHammerBotLaser.setValue(true);
                } else {
                    showHammerBotLaser.setValue(false);
                    softupdate = false;
                }
            }
        }

        if (!(dataModel.getPlayerNameSmashBot() == null)) {
            showSmashBot.setValue(true);
            double x = dataModel.positionSmashBot.getKey();
            double y = dataModel.positionSmashBot.getValue();
            double orientation = dataModel.rotationSmashBot;
            if (dataModel.reboot.containsKey(dataModel.playerIdSmashBot)) {
                showSmashBotLaser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation)  && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showSmashBotLaser.setValue(true);
                } else {
                    showSmashBotLaser.setValue(false);
                    softupdate = false;
                }
            }
        }

        if (!(dataModel.getPlayerNameZoomBot() == null)) {
            showZoomBot.setValue(true);
            double x = dataModel.positionZoomBot.getKey();
            double y = dataModel.positionZoomBot.getValue();
            double orientation = dataModel.rotationZoomBot;
            if (dataModel.reboot.containsKey(dataModel.playerIdZoomBot)) {
                showZoomBotLaser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation) && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showZoomBotLaser.setValue(true);
                } else {
                    showZoomBotLaser.setValue(false);
                    softupdate = false;
                }
            }
        }

        if (!(dataModel.getPlayerNameSpinBot() == null)) {
            showSpinBot.setValue(true);
            double x = dataModel.positionSpinBot.getKey();
            double y = dataModel.positionSpinBot.getValue();
            double orientation = dataModel.rotationSpinBot;
            if (dataModel.reboot.containsKey(dataModel.playerIdSpinBot)) {
                showSpinBotLaser.setValue(false);
            } else {
                if (checkForLaserShooting(x,y,orientation) && dataModel.activePhase == 3 && dataModel.currentRound != 1 && !softupdate) {
                    showSpinBotLaser.setValue(true);
                } else {
                    showSpinBotLaser.setValue(false);
                    softupdate = false;
                }
            }
        }

        // Evaluates whether DamageAnimation should be shown for robots
        if (dataModel.twonkyDamage) {
            showTwonkyDamage.setValue(true);
        } else {
            showTwonkyDamage.setValue(false);
        }

        if (dataModel.hulkX90Damage) {
            showHulkX90Damage.setValue(true);
        } else {
            showHulkX90Damage.setValue(false);
        }

        if (dataModel.hammerBotDamage) {
            showHammerBotDamage.setValue(true);
        } else {
            showHammerBotDamage.setValue(false);
        }

        if (dataModel.smashBotDamage) {
            showSmashBotDamage.setValue(true);
        } else {
            showSmashBotDamage.setValue(false);
        }

        if (dataModel.zoomBotDamage) {
            showZoomBotDamage.setValue(true);
        } else {
            showZoomBotDamage.setValue(false);
        }

        if (dataModel.spinBotDamage) {
            showSpinBotDamage.setValue(true);
        } else {
            showSpinBotDamage.setValue(false);
        }
    }


    /**
     * checkForLaserShooting(double, double, double)
     * This methods checks whether the robot is facing a wall or is rebooting to determine whether the laser shooting
     * animation should be displayed to the GUI.
     * Method is called from getRobotPositions() method.
     * @param x
     * @param y
     * @param orientation
     * @return boolean
     * @author Thomas Richter
     */
    public boolean checkForLaserShooting (double x, double y, double orientation) {
        logger.debug(classname + " checkForLaserShooting(" + x + ", " + y + ", " + orientation + ") is called from updateViewModel of GameViewModel");

        int arrayX = (int) ((x /50.0));
        int arrayY = (int) ((y/50.0) + 1.0);

        logger.debug(classname + "x: " + arrayX + ", y: " + arrayY);

            if (orientation == 0.0) {
                if (dataModel.walls[arrayX][arrayY] == "east" || dataModel.walls[arrayX + 1][arrayY] == "west") {
                    logger.debug(classname + "No wall was found in direction east. Laser can be shot.");
                    return false;
                } else {
                    logger.debug(classname + "Wall was found in direction east. Laser can not be shot.");
                    return true;
                }
            }
            if (orientation == 90.0) {
                if (dataModel.walls[arrayX][arrayY] == "south" || dataModel.walls[arrayX][arrayY + 1] == "north") {
                    logger.debug(classname + "No wall was found in direction south. Laser can be shot.");
                    return false;
                } else {
                    logger.debug(classname + "Wall was found in direction south. Laser can not be shot.");
                    return true;
                }
            }
            if (orientation == 180.0) {
                if (dataModel.walls[arrayX][arrayY] == "west" || dataModel.walls[arrayX - 1][arrayY] == "east") {
                    logger.debug(classname + "No wall was found in direction west. Laser can be shot.");
                    return false;
                } else {
                    logger.debug(classname + "Wall was found in direction west. Laser can not be shot.");
                    return true;
                }
            }
            if (orientation == 270.0) {
                if (dataModel.walls[arrayX][arrayY] == "north" || dataModel.walls[arrayX][arrayY - 1] == "south") {
                    logger.debug(classname + "No wall was found in direction north. Laser can be shot.");
                    return false;
                } else {
                    logger.debug(classname + "Wall was found in direction north. Laser can not be shot.");
                    return true;
                }
            }

            return false;
    }


    /**
     * checkForStartingPointTurn()
     * Method is part of the updateViewModel() and initBindVaribales() methods and
     * checks whether the starting point can be chosen and manipulates the GUI accordingly.
     * @author Thomas Richter
     */
    public void checkForStartingPointTurn() {
        logger.debug(classname + " checkForStartingPointTurn() is called from updateViewModel of GameViewModel");
        if ((dataModel.currentPlayer == dataModel.playerID) && dataModel.activePhase == 0 &&(!dataModel.settedStartingPoint)) {
            logger.debug(classname + " It's not the players turn.");
            disableStartingPoint1_4.setValue(false);
            disableStartingPoint1_7.setValue(false);
            disableStartingPoint2_2.setValue(false);
            disableStartingPoint2_5.setValue(false);
            disableStartingPoint2_6.setValue(false);
            disableStartingPoint2_9.setValue(false);
            if(!dataModel.isAI){
                setStartingPointPane.setValue(true);
            }else{
                setStartingPointPane.setValue(false);}
        } else {
            disableStartingPoint1_4.setValue(true);
            disableStartingPoint1_7.setValue(true);
            disableStartingPoint2_2.setValue(true);
            disableStartingPoint2_5.setValue(true);
            disableStartingPoint2_6.setValue(true);
            disableStartingPoint2_9.setValue(true);
            setStartingPointPane.setValue(false);
            logger.debug(classname + " It's the players turn.");
        }
    }

    /**
     * updateRegister()
     * Method is called by updateViewModel() method and updates the images of the registers.
     * @author Thomas Richter
     */
    public void updateRegister () {
        logger.debug(classname + " updateRegister() is called from updateViewModel of GameViewModel");
        String [] registerCards = dataModel.getMyRegisters();
        Arrays.fill(myRegisterCardImages, null);
        logger.debug(classname + " registerCards[]: [" + registerCards[0] + ", " + registerCards[1] + ", " + registerCards[2] + ", " + registerCards[3] + ", " + registerCards[4] + "] ");
        for (int i = 0; i < 5; i++) {
            registersPath[i] = "/" + registerCards[i] + ".png";
            myRegisterCardImages[i] = new Image(registersPath[i]);
        }
        register1.setValue(myRegisterCardImages[0]);
        register2.setValue(myRegisterCardImages[1]);
        register3.setValue(myRegisterCardImages[2]);
        register4.setValue(myRegisterCardImages[3]);
        register5.setValue(myRegisterCardImages[4]);
    }

    /**
     * updateProgrammingCards()
     * Method is called by updateViewModel() method and updates the images and buttons of the programming cards.
     * @author Thomas Richter
     */
    public void updateProgrammingCards () {
        logger.debug(classname + " updateProgrammingCards() is called from updateViewModel of GameViewModel");
        String [] programmingCards = new String[9];
        for (int i = 0; i < 9; i++) {
            programmingCards[i] = dataModel.programmingCards[i];
        }
        logger.debug(classname + " programmingCards[]: [" + programmingCards[0] + ", " + programmingCards[1] + ", " + programmingCards[2] + ", " + programmingCards[3] + ", " + programmingCards[4] + ", " + programmingCards[5] + ", " + programmingCards[6] + ", " + programmingCards[7] + ", " + programmingCards[8] + "]");

        for (int i = 0; i < 9; i++) {
            programmingCardsPath[i] = "/" + programmingCards[i] + ".png";
            programmingCardImages[i] = new Image(programmingCardsPath[i]);
            if (programmingCards[i].equals("Again")) {
                againFlags [i] = true;
            } else {
                againFlags [i] = false;
            }
        }
        programmingCard1.setValue(programmingCardImages[0]);
        programmingCard2.setValue(programmingCardImages[1]);
        programmingCard3.setValue(programmingCardImages[2]);
        programmingCard4.setValue(programmingCardImages[3]);
        programmingCard5.setValue(programmingCardImages[4]);
        programmingCard6.setValue(programmingCardImages[5]);
        programmingCard7.setValue(programmingCardImages[6]);
        programmingCard8.setValue(programmingCardImages[7]);
        programmingCard9.setValue(programmingCardImages[8]);

        lockAgainCards();
    }

    /**
     * lockAgainCards()
     * Method is called by updateProgrammingCards() and initProgrammingCards() methods and locks the Again card in case
     * the current register is register 1, where it is not allowed to play the Again Card.
     * @author Thomas Richter
     */
    public void lockAgainCards() {
        logger.debug(classname + " lockAgainCards() is called either from initProgrammingCards() or updateProgrammingCards().");
        logger.debug(classname + " dataModel.currentRegister: " + dataModel.currentRegister);
        logger.debug(classname + " if "+ dataModel.currentRegister + " == 0, then cards that must be locked from dataModel.programmingCards are: 1("+ againFlags[0] + "), 2(" + againFlags[1] + "), 3(" + againFlags[2]  + "), 4(" + againFlags[3] + "), 5(" + againFlags[4] + "), 6(" + againFlags[5] + "), 7(" + againFlags[6] + "), 8(" + againFlags[7] + "), 9(" + againFlags[8] + ")");

        if (dataModel.currentRegister == 0 && againFlags [0]) {
            disableProgrammingCard1.setValue(true);
            visibleProgrammingCard1.setValue(false);
            logger.debug(classname + " programmingCard1 locked: " + disableProgrammingCard1.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [0]){
            disableProgrammingCard1.setValue(false);
            visibleProgrammingCard1.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [1]) {
            disableProgrammingCard2.setValue(true);
            visibleProgrammingCard2.setValue(false);
            logger.debug(classname + " programmingCard2 locked: " + disableProgrammingCard2.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [1]){
            disableProgrammingCard2.setValue(false);
            visibleProgrammingCard2.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [2]) {
            disableProgrammingCard3.setValue(true);
            visibleProgrammingCard3.setValue(false);
            logger.debug(classname + " programmingCard3 locked: " + disableProgrammingCard3.getValue());

        } else if (!(dataModel.currentRegister == 5) && againFlags [2]) {
            disableProgrammingCard3.setValue(false);
            visibleProgrammingCard3.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [3]) {
            disableProgrammingCard4.setValue(true);
            visibleProgrammingCard4.setValue(false);
            logger.debug(classname + " programmingCard4 locked: " + disableProgrammingCard4.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [3]) {
            disableProgrammingCard4.setValue(false);
            visibleProgrammingCard4.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [4]) {
            disableProgrammingCard5.setValue(true);
            visibleProgrammingCard5.setValue(false);
            logger.debug(classname + " programmingCard5 locked: " + disableProgrammingCard5.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [4]) {
            disableProgrammingCard5.setValue(false);
            visibleProgrammingCard5.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [5]) {
            disableProgrammingCard6.setValue(true);
            visibleProgrammingCard6.setValue(false);
            logger.debug(classname + " programmingCard6 locked: " + disableProgrammingCard6.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [5]) {
            disableProgrammingCard6.setValue(false);
            visibleProgrammingCard6.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [6]) {
            disableProgrammingCard7.setValue(true);
            visibleProgrammingCard7.setValue(false);
            logger.debug(classname + " programmingCard7 locked: " + disableProgrammingCard7.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [6]) {
            disableProgrammingCard7.setValue(false);
            visibleProgrammingCard7.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [7]) {
            disableProgrammingCard8.setValue(true);
            visibleProgrammingCard8.setValue(false);
            logger.debug(classname + " programmingCard8 locked: " + disableProgrammingCard8.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [7]) {
            disableProgrammingCard8.setValue(false);
            visibleProgrammingCard8.setValue(true);
        }
        if (dataModel.currentRegister == 0 && againFlags [8]) {
            disableProgrammingCard9.setValue(true);
            visibleProgrammingCard9.setValue(false);
            logger.debug(classname + " programmingCard9 locked: " + disableProgrammingCard9.getValue());
        } else if (!(dataModel.currentRegister == 5) && againFlags [8]) {
            disableProgrammingCard9.setValue(false);
            visibleProgrammingCard9.setValue(true);
        }
    }

    /**
     * updateRegisterCards()
     * Method is called by updateViewModel() method and updates the programming cards images with the played cards of
     * current activation phase.
     * @author Thomas Richter
     */
    public void updateRegisterCards () {
        logger.debug(classname + " updateRegisterCards() is called from updateViewModel of GameViewModel.");

        for (int i = 0; i<5; i++) {
            twonkyRegistersPath[i] = "/EmptyRegister.png";
            hulkX90RegistersPath[i] = "/EmptyRegister.png";
            hammerBotRegistersPath[i] = "/EmptyRegister.png";
            smashBotRegistersPath[i] = "/EmptyRegister.png";
            zoomBotRegistersPath[i] = "/EmptyRegister.png";
            spinBotRegistersPath[i] = "/EmptyRegister.png";
        }

        // gets all played cards from round 1 of robots that are playing
        if (dataModel.register1Cards.get(dataModel.playerIdTwonky) != null) {
            twonkyRegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdTwonky) + "Phase3.png";
        }
        if (dataModel.register1Cards.get(dataModel.playerIdHulkX90) != null) {
            hulkX90RegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdHulkX90) + "Phase3.png";
        }
        if (dataModel.register1Cards.get(dataModel.playerIdHammerBot) != null) {
            hammerBotRegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdHammerBot) + "Phase3.png";
        }
        if (dataModel.register1Cards.get(dataModel.playerIdSmashBot) != null) {
            smashBotRegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdSmashBot) + "Phase3.png";
        }
        if (dataModel.register1Cards.get(dataModel.playerIdZoomBot) != null) {
            zoomBotRegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdZoomBot) + "Phase3.png";
        }
        if (dataModel.register1Cards.get(dataModel.playerIdSpinBot) != null) {
            spinBotRegistersPath[0] = "/" + dataModel.register1Cards.get(dataModel.playerIdSpinBot) + "Phase3.png";
        }

        // gets all played cards from round 2 of robots that are playing
        if (dataModel.register2Cards.get(dataModel.playerIdTwonky) != null) {
            twonkyRegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdTwonky) + "Phase3.png";
        }
        if (dataModel.register2Cards.get(dataModel.playerIdHulkX90) != null) {
            hulkX90RegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdHulkX90) + "Phase3.png";
        }
        if (dataModel.register2Cards.get(dataModel.playerIdHammerBot) != null) {
            hammerBotRegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdHammerBot) + "Phase3.png";
        }
        if (dataModel.register2Cards.get(dataModel.playerIdSmashBot) != null) {
            smashBotRegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdSmashBot) + "Phase3.png";
        }
        if (dataModel.register2Cards.get(dataModel.playerIdZoomBot) != null) {
            zoomBotRegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdZoomBot) + "Phase3.png";
        }
        if (dataModel.register2Cards.get(dataModel.playerIdSpinBot) != null) {
            spinBotRegistersPath[1] = "/" + dataModel.register2Cards.get(dataModel.playerIdSpinBot) + "Phase3.png";
        }

        // gets all played cards from round 3 of robots that are playing
        if (dataModel.register3Cards.get(dataModel.playerIdTwonky) != null) {
            twonkyRegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdTwonky) + "Phase3.png";
        }
        if (dataModel.register3Cards.get(dataModel.playerIdHulkX90) != null) {
            hulkX90RegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdHulkX90) + "Phase3.png";
        }
        if (dataModel.register3Cards.get(dataModel.playerIdHammerBot) != null) {
            hammerBotRegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdHammerBot) + "Phase3.png";
        }
        if (dataModel.register3Cards.get(dataModel.playerIdSmashBot) != null) {
            smashBotRegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdSmashBot) + "Phase3.png";
        }
        if (dataModel.register3Cards.get(dataModel.playerIdZoomBot) != null) {
            zoomBotRegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdZoomBot)+ "Phase3.png";
        }
        if (dataModel.register3Cards.get(dataModel.playerIdSpinBot) != null) {
            spinBotRegistersPath[2] = "/" + dataModel.register3Cards.get(dataModel.playerIdSpinBot)+ "Phase3.png";
        }

        // gets all played cards from round 4 of robots that are playing
        if (dataModel.register4Cards.get(dataModel.playerIdTwonky) != null) {
            twonkyRegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdTwonky)+ "Phase3.png";
        }
        if (dataModel.register4Cards.get(dataModel.playerIdHulkX90) != null) {
            hulkX90RegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdHulkX90)+ "Phase3.png";
        }
        if (dataModel.register4Cards.get(dataModel.playerIdHammerBot) != null) {
            hammerBotRegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdHammerBot)+ "Phase3.png";
        }
        if (dataModel.register4Cards.get(dataModel.playerIdSmashBot) != null) {
            smashBotRegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdSmashBot)+ "Phase3.png";
        }
        if (dataModel.register4Cards.get(dataModel.playerIdZoomBot) != null) {
            zoomBotRegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdZoomBot)+ "Phase3.png";
        }
        if (dataModel.register4Cards.get(dataModel.playerIdSpinBot) != null) {
            spinBotRegistersPath[3] = "/" + dataModel.register4Cards.get(dataModel.playerIdSpinBot)+ "Phase3.png";
        }

        // gets all played cards from round 5 of robots that are playing
        if (dataModel.register5Cards.get(dataModel.playerIdTwonky) != null) {
            twonkyRegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdTwonky)+ "Phase3.png";
        }
        if (dataModel.register5Cards.get(dataModel.playerIdHulkX90) != null) {
            hulkX90RegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdHulkX90)+ "Phase3.png";
        }
        if (dataModel.register5Cards.get(dataModel.playerIdHammerBot) != null) {
            hammerBotRegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdHammerBot)+ "Phase3.png";
        }
        if (dataModel.register5Cards.get(dataModel.playerIdSmashBot) != null) {
            smashBotRegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdSmashBot)+ "Phase3.png";
        }
        if (dataModel.register5Cards.get(dataModel.playerIdZoomBot) != null) {
            zoomBotRegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdZoomBot)+ "Phase3.png";
        }
        if (dataModel.register5Cards.get(dataModel.playerIdSpinBot) != null) {
            spinBotRegistersPath[4] = "/" + dataModel.register5Cards.get(dataModel.playerIdSpinBot)+ "Phase3.png";
        }

        for (int i = 0; i<5; i++) {
            twonkyRegistersImage[i] = new Image(twonkyRegistersPath[i]);
            hulkX90RegistersImage[i] = new Image(hulkX90RegistersPath[i]);
            hammerBotRegistersImage[i] = new Image(hammerBotRegistersPath[i]);
            smashBotRegistersImage[i] = new Image(smashBotRegistersPath[i]);
            zoomBotRegistersImage[i] = new Image(zoomBotRegistersPath[i]);
            spinBotRegistersImage[i] = new Image(spinBotRegistersPath[i]);
        }

        twonkyRegisterImage1.setValue(twonkyRegistersImage[0]);
        twonkyRegisterImage2.setValue(twonkyRegistersImage[1]);
        twonkyRegisterImage3.setValue(twonkyRegistersImage[2]);
        twonkyRegisterImage4.setValue(twonkyRegistersImage[3]);
        twonkyRegisterImage5.setValue(twonkyRegistersImage[4]);

        hulkX90RegisterImage1.setValue(hulkX90RegistersImage[0]);
        hulkX90RegisterImage2.setValue(hulkX90RegistersImage[1]);
        hulkX90RegisterImage3.setValue(hulkX90RegistersImage[2]);
        hulkX90RegisterImage4.setValue(hulkX90RegistersImage[3]);
        hulkX90RegisterImage5.setValue(hulkX90RegistersImage[4]);

        hammerBotRegisterImage1.setValue(hammerBotRegistersImage[0]);
        hammerBotRegisterImage2.setValue(hammerBotRegistersImage[1]);
        hammerBotRegisterImage3.setValue(hammerBotRegistersImage[2]);
        hammerBotRegisterImage4.setValue(hammerBotRegistersImage[3]);
        hammerBotRegisterImage5.setValue(hammerBotRegistersImage[4]);

        smashBotRegisterImage1.setValue(smashBotRegistersImage[0]);
        smashBotRegisterImage2.setValue(smashBotRegistersImage[1]);
        smashBotRegisterImage3.setValue(smashBotRegistersImage[2]);
        smashBotRegisterImage4.setValue(smashBotRegistersImage[3]);
        smashBotRegisterImage5.setValue(smashBotRegistersImage[4]);

        zoomBotRegisterImage1.setValue(zoomBotRegistersImage[0]);
        zoomBotRegisterImage2.setValue(zoomBotRegistersImage[1]);
        zoomBotRegisterImage3.setValue(zoomBotRegistersImage[2]);
        zoomBotRegisterImage4.setValue(zoomBotRegistersImage[3]);
        zoomBotRegisterImage5.setValue(zoomBotRegistersImage[4]);

        spinBotRegisterImage1.setValue(spinBotRegistersImage[0]);
        spinBotRegisterImage2.setValue(spinBotRegistersImage[1]);
        spinBotRegisterImage3.setValue(spinBotRegistersImage[2]);
        spinBotRegisterImage4.setValue(spinBotRegistersImage[3]);
        spinBotRegisterImage5.setValue(spinBotRegistersImage[4]);
    }






    // Methods of grid_Game StackPane layer

    /**
     * setStartingPoint(double,double)
     * Method is called from the GUI, and submits the chosen starting point to the DataModel.
     * @param x
     * @param y
     * @author Thomas Richter
     */
    public void setStartingPoint (Double x, Double y) {
        logger.debug(classname + " setStartingPoint(" + x + ", "+ y +") is called from GUI.");
        setStartingPointPane.setValue(false);

        String robot = dataModel.getRobot();
        logger.debug(classname + " dataModel.getRobot(): " + dataModel.getRobot());
        Pair<Double, Double> positions = new Pair<Double,Double>(x,y);

        if (robot.toLowerCase().equals("twonky")) {
            dataModel.setPositionTwonky(positions);
        } else if (robot.toLowerCase().equals("hulkx90")) {
            dataModel.setPositionHulkX90(positions);
        } else if (robot.toLowerCase().equals("hammerbot")) {
            dataModel.setPositionHammerBot(positions);
        } else if (robot.toLowerCase().equals("smashbot")) {
            dataModel.setPositionSmashBot(positions);
        } else if (robot.toLowerCase().equals("zoombot")) {
            dataModel.setPositionZoomBot(positions);
        } else if (robot.toLowerCase().equals("spinbot")) {
            dataModel.setPositionSpinBot(positions);
        }
        dataModel.submitStartingPoint();
    }


    /**
     * submitPlayIt()
     * Method realizes the VM part of PlayIt protocol message.
     * @author Thomas Richter
     */
    public void submitPlayIt() {
        logger.debug(classname + " submitPlayIt() is called from GUI.");
        dataModel.submitPlayIt();
        dataModel.activatePlayIt = false;
        visiblePlayItOff.setValue(true);
        visiblePlayIt.setValue(false);
    }

    /**
     * resetRobotDamages()
     * Method resets the variables which are responsible for showing the damage animation
     * after the damage animation of the GUI is finished.
     * @author Thomas Richter
     */
    public void resetRobotDamages() {
        logger.debug(classname + " resetRobotDamages() is called from GUI (ChangeListener).");
        dataModel.twonkyDamage = false;
        dataModel.hulkX90Damage = false;
        dataModel.hammerBotDamage = false;
        dataModel.smashBotDamage = false;
        dataModel.zoomBotDamage = false;
        dataModel.spinBotDamage = false;
    }


    // Methods of grid_Phase1 StackPane Layer
    // Methods of grid_Phase2 StackPane Layer

    /**
     * selectCard(int)
     * Method submits the chosen programming card for the current register to the DataModel
     * @param programmingCard
     * @author Thomas Richter
     */
    public void selectCard (int programmingCard) {
        logger.debug(classname + " selectCard(" + programmingCard + ") is called from GUI.");

        dataModel.submitRegister(programmingCard);

        dataModel.myRegisters[dataModel.currentRegister] = dataModel.programmingCards[programmingCard];
        dataModel.programmingCards[programmingCard] = "NoCard";

        if (dataModel.currentRegister < 4) {
            dataModel.myRegisters[dataModel.currentRegister + 1] = "CurrentRegister";
        } else {
            visibleProgrammingCard1.setValue(false);
            visibleProgrammingCard2.setValue(false);
            visibleProgrammingCard3.setValue(false);
            visibleProgrammingCard4.setValue(false);
            visibleProgrammingCard5.setValue(false);
            visibleProgrammingCard6.setValue(false);
            visibleProgrammingCard7.setValue(false);
            visibleProgrammingCard8.setValue(false);
            visibleProgrammingCard9.setValue(false);

            disableProgrammingCard1.setValue(true);
            disableProgrammingCard2.setValue(true);
            disableProgrammingCard3.setValue(true);
            disableProgrammingCard4.setValue(true);
            disableProgrammingCard5.setValue(true);
            disableProgrammingCard6.setValue(true);
            disableProgrammingCard7.setValue(true);
            disableProgrammingCard8.setValue(true);
            disableProgrammingCard9.setValue(true);

            disableResetRegisters.setValue(true);
        }
        dataModel.currentRegister = dataModel.currentRegister + 1;

    }

    /**
     * resetRegister()
     * Method resets the current chosen registers in case the player wants to use other programming cards then those
     * which are currently selected.
     * @author Thomas Richter
     */
    public void resetRegister () {
        logger.debug(classname + " resetRegister() is called.");
        dataModel.setProgrammingCards(dataModel.getProgrammingCardsInitial());


        dataModel.currentRegister = 0;

        dataModel.myRegisters[0] = "CurrentRegister";
        dataModel.myRegisters[1] = "LockedRegister";
        dataModel.myRegisters[2] = "LockedRegister";
        dataModel.myRegisters[3] = "LockedRegister";
        dataModel.myRegisters[4] = "LockedRegister";

        visibleProgrammingCard1.setValue(true);
        visibleProgrammingCard2.setValue(true);
        visibleProgrammingCard3.setValue(true);
        visibleProgrammingCard4.setValue(true);
        visibleProgrammingCard5.setValue(true);
        visibleProgrammingCard6.setValue(true);
        visibleProgrammingCard7.setValue(true);
        visibleProgrammingCard8.setValue(true);
        visibleProgrammingCard9.setValue(true);

        disableProgrammingCard1.setValue(false);
        disableProgrammingCard2.setValue(false);
        disableProgrammingCard3.setValue(false);
        disableProgrammingCard4.setValue(false);
        disableProgrammingCard5.setValue(false);
        disableProgrammingCard6.setValue(false);
        disableProgrammingCard7.setValue(false);
        disableProgrammingCard8.setValue(false);
        disableProgrammingCard9.setValue(false);


        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }

    /**
     * resetActivationPhaseRegisters()
     * Method resets the images of played cards once the activation phase is over and a new round starts.
     * @author Thomas Richter
     */
    public void resetActivationPhaseRegisters() {
        logger.debug(classname + " resetActivationPhaseRegisters() is called.");
        twonkyRegisterImage1.setValue(activationPhaseEmptyRegister);
        twonkyRegisterImage2.setValue(activationPhaseEmptyRegister);
        twonkyRegisterImage3.setValue(activationPhaseEmptyRegister);
        twonkyRegisterImage4.setValue(activationPhaseEmptyRegister);
        twonkyRegisterImage5.setValue(activationPhaseEmptyRegister);

        hulkX90RegisterImage1.setValue(activationPhaseEmptyRegister);
        hulkX90RegisterImage2.setValue(activationPhaseEmptyRegister);
        hulkX90RegisterImage3.setValue(activationPhaseEmptyRegister);
        hulkX90RegisterImage4.setValue(activationPhaseEmptyRegister);
        hulkX90RegisterImage5.setValue(activationPhaseEmptyRegister);

        hammerBotRegisterImage1.setValue(activationPhaseEmptyRegister);
        hammerBotRegisterImage2.setValue(activationPhaseEmptyRegister);
        hammerBotRegisterImage3.setValue(activationPhaseEmptyRegister);
        hammerBotRegisterImage4.setValue(activationPhaseEmptyRegister);
        hammerBotRegisterImage5.setValue(activationPhaseEmptyRegister);

        smashBotRegisterImage1.setValue(activationPhaseEmptyRegister);
        smashBotRegisterImage2.setValue(activationPhaseEmptyRegister);
        smashBotRegisterImage3.setValue(activationPhaseEmptyRegister);
        smashBotRegisterImage4.setValue(activationPhaseEmptyRegister);
        smashBotRegisterImage5.setValue(activationPhaseEmptyRegister);

        zoomBotRegisterImage1.setValue(activationPhaseEmptyRegister);
        zoomBotRegisterImage2.setValue(activationPhaseEmptyRegister);
        zoomBotRegisterImage3.setValue(activationPhaseEmptyRegister);
        zoomBotRegisterImage4.setValue(activationPhaseEmptyRegister);
        zoomBotRegisterImage5.setValue(activationPhaseEmptyRegister);

        spinBotRegisterImage1.setValue(activationPhaseEmptyRegister);
        spinBotRegisterImage2.setValue(activationPhaseEmptyRegister);
        spinBotRegisterImage3.setValue(activationPhaseEmptyRegister);
        spinBotRegisterImage4.setValue(activationPhaseEmptyRegister);
        spinBotRegisterImage5.setValue(activationPhaseEmptyRegister);
    }

    // Methods of pane_DrawDamage

    /**
     * resetDrawDamage()
     * Method resets the drawDamage variables after all damage cards have been selected
     * by realization of PickDamage and SelectDamage protocol message.
     * @author Thomas Richter
     */
    public void resetDrawDamage() {
        logger.debug(classname + " resetDrawDamage() is called from GUI.");
        dataModel.showDrawDamage = false;
        dataModel.drawnDamageCardsCount[0] = 0;
        dataModel.drawnDamageCardsCount[1] = 0;
        dataModel.drawnDamageCardsCount[2] = 0;
        dataModel.drawnDamageCardsCount[3] = 0;
    }


    // Methods of pane_CardsYouGotNow

    /**
     * resetCardsYouGotNow()
     * Method resets the cards the player got after the timer got him once he
     * closes that information.
     * @author Thomas Richter
     */
    public void resetCardsYouGotNow() {
        logger.debug(classname + " resetCardsYouGotNow() is called from GUI.");
        dataModel.shuffledCards = false;
    }


    // Methods of grid_ShuffleCards

    /**
     * resetShuffle()
     * Method resets the variables which are responsible for showing the shuffle Effect in GUI
     * @author Thomas Richter
     */
    public void resetShuffle () {
        logger.debug(classname + " resetShuffle() is called from GUI (ChangeListener).");
        dataModel.shuffleCards = false;
    }




    // Methods of grid_DamageLayer

    /**
     * selectDamage()
     * Method realizes the VM part of the selectDamage protocol message, which
     * chooses the desired damage cards after spam cards deck is empty
     * @param damageCard
     * @author Thomas Richter
     */
    public void selectDamage(String damageCard) {
        logger.debug(classname + " damageCard("+ damageCard + ") is called from GUI.");
        dataModel.submitDamage(damageCard);
    }


    // Methods of grid_Chat StackPane Layer
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
     * resetChatIndicators()
     * Once a new message is received this is displayed in the GUI by notifying the player.
     * After the player opened the chatroom layer this indication is resetted by this method.
     * @author Thomas Richter
     */
    public void resetChatIndicators() {
        dataModel.newChat = false;
        dataModel.receivedError = false;
        softupdate = true;

        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }


    /**
     * getNextPlayerStats()
     * Method loops through the players and fetches their stat information.
     * @author Thomas Richter
     */
    public void getNextPlayerStats () {
        int lastPlayer = dataModel.readyIDs.size()- 1;
        int currentStatPlayer = dataModel.statsPlayer;
        softupdate = true;

        if (lastPlayer == currentStatPlayer) {
            dataModel.statsPlayer = 0;
        } else {
            dataModel.statsPlayer++;
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });
    }

    /**
     * getPreviousPlayerStats()
     * Method loops through the players and fetches their stat information.
     * @author Thomas Richter
     */
    public void getPreviousPlayerStats() {
        int lastPlayer = dataModel.readyIDs.size()-1;
        int currentStatPlayer = dataModel.statsPlayer;
        softupdate = true;

        if (currentStatPlayer == 0) {
            dataModel.statsPlayer = lastPlayer;
        } else {
            dataModel.statsPlayer--;
        }
        Platform.runLater(()-> {
            dataModel.updateViewModels();
        });

    }


    /**
     * lockGUIforAI()
     * This methods locks the GUI elements which should not be displayed if the connected player
     * is using an AI.
     * @author Thomas Richter
     */
    public void lockGUIforAI() {
        disableStartingPoint1_4.setValue(true);
        disableStartingPoint1_7.setValue(true);
        disableStartingPoint2_2.setValue(true);
        disableStartingPoint2_5.setValue(true);
        disableStartingPoint2_6.setValue(true);
        disableStartingPoint2_9.setValue(true);

        visiblePlayItOff.setValue(true);
        visiblePlayIt.setValue(false);

        visiblePhase2.setValue(false);
        visibleDamageLayer.setValue(false);
    }


    // Getter and Setter
    public int getHashMapLength() { return hashMapLength;}

    public int getActivePhase () {
        return dataModel.activePhase;
    }

}
