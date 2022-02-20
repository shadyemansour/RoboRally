package model;

import game.gameObjects.gamefield.FieldObject;
//import game.gameObjects.gamefield.Map;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;
import networking.Client;
import networking.Server;
import networking.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import viewmodel.ConnectionViewModel;
import viewmodel.GameStartViewModel;
import viewmodel.GameViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents the DataModel of the RoboRallys Client Side.
 * The Data here gets manipulated either by the Server through the Client/Server Architecture or by the various ViewModels.
 * The Data here updates the various ViewModels, thus the Views and sends Data to the Server through the Client/Server Architecture
 * @author Thomas Richter, Vincent Oeller
 * */

public class DataModelManager implements DataModel {

    private static String classname = "DataModelManager- ";
    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");

    public networking.User player;                          // gets set by Constructor

    public double protocol = 1.0;                           // doesn't change. Is sent to Server with messageType: HelloServer. Gets confirmed by Server with messageType welcome, by manipulating playerID
    public String group = "KursiveKationen";                // doesn't change. Is sent to Server with messageType: HelloServer. Gets confirmed by Server with messageType welcome, by manipulating playerID
    public Boolean isAI;                                    // gets set by ConnectionView(Model) then sent to Server with messageType: HelloServer. Gets confirmed by Server with messageType welcome, by manipulating playerID

    public ConnectionViewModel connectionViewModel;         // Is needed to realize Oberserver Pattern for updating ConnectionViewModel
    public GameStartViewModel gameStartViewModel;           // Is needed to realize Oberserver Pattern for updating GameStartViewModel
    public GameViewModel gameViewModel;                     // Is needed to realize Oberserver Pattern for updating GameViewModel

    public Boolean receivedError;                           // gets set by Server, messageType: Error.

    public int playerID;                                    // gets set by Server, messageType: Welcome. See isAI, group and protocol variables.
    public String name;                                     // gets set by Server, messageType: PlayerAdded.
    public String robot;                                    // gets set by GameStartView(Model), then sent to Server with messageType PlayerValues. Gets confirmed by Sever with messageType PlayerAdded, by manipulating PlayerAdded Variable
    public String startingBoard;                            // gets set by GameStartView(Model)
    public String racingCourse;                             // gets set by GameStartView(Model)
    public Boolean playerAdded;                             // gets set by Server through messageType PlayerAdded. See name and robot variable
    public boolean readyToPlay;                             // gets set by GameStartView(Model), then sent to Server with messageType SetStatus. Gets confirmed by Sever with messageType PlayerStatus

    public String [] availableMaps;


    public String playerNameTwonky;                         // gets set by Server through messageType: PlayerAdded
    public String playerNameHulkX90;                        // gets set by Server through messageType: PlayerAdded
    public String playerNameHammerBot;                      // gets set by Server through messageType: PlayerAdded
    public String playerNameSmashBot;                       // gets set by Server through messageType: PlayerAdded
    public String playerNameZoomBot;                        // gets set by Server through messageType: PlayerAdded
    public String playerNameSpinBot;                        // gets set by Server through messageType: PlayerAdded

    public Integer playerIdTwonky;                              // gets set by Server through messageType: PlayerAdded
    public Integer playerIdHulkX90;                             // gets set by Server through messageType: PlayerAdded
    public Integer playerIdHammerBot;                           // gets set by Server through messageType: PlayerAdded
    public Integer playerIdSmashBot;                            // gets set by Server through messageType: PlayerAdded
    public Integer playerIdZoomBot;                             // gets set by Server through messageType: PlayerAdded
    public Integer playerIdSpinBot;                             // gets set by Server through messageType: PlayerAdded

    public Boolean availableTwonky;                         // gets set by Server through messageType: PlayerAdded
    public Boolean availableHulkX90;                        // gets set by Server through messageType: PlayerAdded
    public Boolean availableHammerBot;                      // gets set by Server through messageType: PlayerAdded
    public Boolean availableSmashBot;                       // gets set by Server through messageType: PlayerAdded
    public Boolean availableZoomBot;                        // gets set by Server through messageType: PlayerAdded
    public Boolean availableSpinBot;                        // gets set by Server through messageType: PlayerAdded


    public int readyPlayers;                                // gets set by Server through messageType: PlayerAdded

    public Boolean gameStarted;

    public int figure;                                      // gets set by GameStartView(Model) and Server. Initially by V(M) then sent to Server with messageType SetStatus. Gets confirmed by Sever with messageType PlayerAdded by manipulating figure variable again

    public HashMap<Integer, String> players = new HashMap<Integer, String>();
    // gets added by Server through messageType PlayerAdded

    public HashMap<Integer, Boolean> readyIDs = new HashMap<Integer,Boolean>();
    // gets added by Server through messageType: PlayerStatus


    public Boolean newChat;

    public String chatHistory;
    // gets manipulated by GameStartView(Model), GameView(Model) and Server. Contains the whole ChatHistory which should be available to particular Client


    private String[] programmingCardsInitial = new String[9];
    public String[] programmingCards = new String[9];
    public String[] myRegisters = new String[5];                                        //set by Server: CardSelected AND CardsYouGotNow AND Reboot?

    public Boolean shuffleCards;


    public int currentPlayer;                                                           //set by Server: CurrentPlayer
    public int activePhase;                                                             //set by Server: ActivePhase
    public HashMap<Integer, Integer> startingPoints = new HashMap<>();
    public boolean settedStartingPoint;

    public HashMap<Integer,Integer> notYourCards = new HashMap<>();                     //set by Server: NotYourCards
    //            <playerID, cards(Anzahl)>

    public HashMap<Integer,Boolean> cardSelectedTwonky = new HashMap<>();
    public HashMap<Integer,Boolean> cardSelectedHulkX90 = new HashMap<>();
    public HashMap<Integer,Boolean> cardSelectedHammerBot = new HashMap<>();
    public HashMap<Integer,Boolean> cardSelectedSmashBot = new HashMap<>();
    public HashMap<Integer,Boolean> cardSelectedZoomBot = new HashMap<>();
    public HashMap<Integer,Boolean> cardSelectedSpinBot = new HashMap<>();
    //            <register, belegt oder nicht>

    public boolean activatePlayIt;

    public boolean timerStarted;                                                        //set by Server: TimerStarted AND TimerEnded
    public int[] timerEndedPlayers = new int[5];                                        //set by Server: TimerEnded

    public boolean shuffledCards;

    public int currentRegister;
    public boolean resetRegister;

    public HashMap<Integer,String> currentCards = new HashMap<>();                        //set by Server: CurrentCards
    //            <playerID, card>


    public HashMap<Integer,String> register1Cards = new HashMap<>();                      //set by Server: CurrentCards
    //            <playerID, card>

    public HashMap<Integer,String> register2Cards = new HashMap<>();                      //set by Server: CurrentCards
    //            <playerID, card>

    public HashMap<Integer,String> register3Cards = new HashMap<>();                      //set by Server: CurrentCards
    //            <playerID, card>

    public HashMap<Integer,String> register4Cards = new HashMap<>();                      //set by Server: CurrentCards
    //            <playerID, card>

    public HashMap<Integer,String> register5Cards = new HashMap<>();                      //set by Server: CurrentCards
    //            <playerID, card>



    public HashMap<Integer, Integer> movement = new HashMap<>();                        //set by Server: Movement
    //            <playerID, position>

    public int ownPosition;
    public Pair<Double, Double> positionTwonky;
    public Double rotationTwonky;
    public Pair<Double, Double> positionHulkX90;
    public Double rotationHulkX90;
    public Pair<Double, Double> positionHammerBot;
    public Double rotationHammerBot;
    public Pair<Double, Double> positionSmashBot;
    public Double rotationSmashBot;
    public Pair<Double, Double> positionZoomBot;
    public Double rotationZoomBot;
    public Pair<Double, Double> positionSpinBot;
    public Double rotationSpinBot;

    public int [] drawnDamageCardsCount = new int[4];
    // position 0: count of SpamCards; position 1: count of VirusCards; position 2: count of TrojanCards; position 3: count of WormCards
    public boolean showDrawDamage;

    public ArrayList<String> damageCardsTwonky = new ArrayList<>();                     //set by Server: DrawDamage
    public ArrayList<String> damageCardsHulkx90 = new ArrayList<>();
    public ArrayList<String> damageCardsHammerBot = new ArrayList<>();
    public ArrayList<String> damageCardsSmashBot = new ArrayList<>();
    public ArrayList<String> damageCardsZoomBot = new ArrayList<>();
    public ArrayList<String> damageCardsSpinBot = new ArrayList<>();

    public Boolean twonkyDamage;
    public Boolean hulkX90Damage;
    public Boolean hammerBotDamage;
    public Boolean smashBotDamage;
    public Boolean zoomBotDamage;
    public Boolean spinBotDamage;



    public String[] damageCards;
    public int pickDamage;                                                              //set by Server: PickDamage
    public int damageToPick;
    public boolean playerShooting;                                                      //set by Server: PlayerShooting

    public HashMap<Integer,Boolean> reboot = new HashMap<>();                           //set by Server: Reboot
    //            <playerID, rebooting>

    public HashMap<Integer,Integer> playerTurning = new HashMap<>();                    //set by Server: PlayerTurning AND Reboot
    //            <playerID, direction> (1=north, 2=east, 3=south, 4=west)

    public FieldObject [][] gameMap;
    public String [][] walls;

    public HashMap<Integer, Integer> energy = new HashMap<>();                          //set by Server: Energy
    //            <playerID, amount of Energy>


    public HashMap<Integer, Integer> checkpointReached = new HashMap<>();               //set by Server: CheckpointReached
    //            <playerID, checkpoint>


    public Boolean gameWon;
    public Boolean gameLost;
    public int gameWonPlayerID;                                                         //set by Server GameWon


    public int currentRound;

    public int statsPlayer;

    /**
     * DataModelManage(networking.User)
     * The constructor of the class initializes the DataModel of the RoboRallys Client Side.
     * @param  player the instance of Client which is calling the method
     * @author Thomas Richter
     *
     */
    DataModelManager(networking.User player) {

        this.player = player;

        this.activatePlayIt = false;

        this.availableTwonky = true;
        this.availableHulkX90 = true;
        this.availableHammerBot = true;
        this.availableSmashBot = true;
        this.availableZoomBot = true;
        this.availableSpinBot = true;

        this.isAI = false;
        this.readyToPlay = false;
        this.chatHistory = "";

        this.availableMaps = new String[2];
        availableMaps[0] = "";
        availableMaps[1] = "";

        this.readyPlayers = 0;
        this.gameStarted = false;

        this.settedStartingPoint = false;

        this.shuffleCards = false;

        this.newChat = false;

        this.programmingCards = new String[9];
        this.programmingCardsInitial = new String[9];
        this.currentRegister = 0;

        this.shuffledCards = false;

        this.myRegisters[0] = "CurrentRegister";
        this.myRegisters[1] = "LockedRegister";
        this.myRegisters[2] = "LockedRegister";
        this.myRegisters[3] = "LockedRegister";
        this.myRegisters[4] = "LockedRegister";


        this.positionTwonky = new Pair<Double, Double>(0.0,100.0);
        this.rotationTwonky = 0.0;
        this.positionHulkX90 = new Pair<Double, Double>(0.0,150.0);
        this.rotationHulkX90 = 0.0;
        this.positionHammerBot = new Pair<Double, Double>(0.0,200.0);
        this.rotationHammerBot = 0.0;
        this.positionSmashBot = new Pair<Double, Double>(0.0,250.0);
        this.rotationSmashBot = 0.0;
        this.positionZoomBot = new Pair<Double, Double>(0.0,300.0);
        this.rotationZoomBot = 0.0;
        this.positionSpinBot = new Pair<Double, Double>(0.0,350.0);
        this.rotationSpinBot = 0.0;

        this.activePhase = -1;

        this.programmingCards[0] = "NoCard";
        this.programmingCards[1] = "NoCard";
        this.programmingCards[2] = "NoCard";
        this.programmingCards[3] = "NoCard";
        this.programmingCards[4] = "NoCard";
        this.programmingCards[5] = "NoCard";
        this.programmingCards[6] = "NoCard";
        this.programmingCards[7] = "NoCard";
        this.programmingCards[8] = "NoCard";

        this.programmingCardsInitial[0] = "NoCard";
        this.programmingCardsInitial[1] = "NoCard";
        this.programmingCardsInitial[2] = "NoCard";
        this.programmingCardsInitial[3] = "NoCard";
        this.programmingCardsInitial[4] = "NoCard";
        this.programmingCardsInitial[5] = "NoCard";
        this.programmingCardsInitial[6] = "NoCard";
        this.programmingCardsInitial[7] = "NoCard";
        this.programmingCardsInitial[8] = "NoCard";

        this.gameMap = new FieldObject[10][10];
        this.walls = new String[15][12];

        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 15; x++) {
                walls[x][y] = "----";
            }
        }

        walls[2][3] = "north";
        walls[2][8] = "south";
        walls[3][5] = "east";
        walls[3][6] = "east";


        this.currentRound = 0;
        this.resetRegister = false;

        this.drawnDamageCardsCount = new int[4];
        drawnDamageCardsCount[0] = 0;
        drawnDamageCardsCount[1] = 0;
        drawnDamageCardsCount[2] = 0;
        drawnDamageCardsCount[3] = 0;

        this.showDrawDamage = false;

        this.pickDamage = 0;
        this.damageToPick = 0;
        this.damageCards = new String[74];

        this.twonkyDamage = false;
        this.hulkX90Damage = false;
        this.smashBotDamage = false;
        this.hammerBotDamage = false;
        this.spinBotDamage = false;
        this.zoomBotDamage = false;

        this.statsPlayer = 0;

        this.gameWon = false;
        this.gameLost = false;

    }

    // Business Logic:

    // Getter and Setter Classes, explicitly needed to implement Observer Patter
    public void setConnectionViewModel(ConnectionViewModel connectionViewModel) { this.connectionViewModel = connectionViewModel; }
    public void setGameStartViewModel(GameStartViewModel gameStartViewModel) { this.gameStartViewModel = gameStartViewModel; }
    public void setGameViewModel(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }

    /**
     * updateViewModels()
     * Implementation of Observer Pattern.
     * This method is called at the end of every ReadThread method
     * which implements the Server-Client communication protocol messages.
     * This method then sends the signal to the ViewModels that Data may has been changed
     * and can get fetched now.
     * @author Thomas Richter
     */
    public void updateViewModels () {
        logger.debug(classname + " updateViewModels() called.");
        connectionViewModel.updateViewModel();
        gameStartViewModel.updateViewModel();
        gameViewModel.updateViewModel();
    }


    // Business Logic Methods (GameStartView(Model))

    /**
     * submitPlayerValues()
     * Method realizes the MessageType PlayerValues.
     * Methods gets called from the GameStartView(Model)
     * @author Thomas Richter
     */
    public void submitPlayerValues() {
        logger.debug(classname + " submitPlayerValues() called.");

        int figure = 0;

        if (getRobot().equalsIgnoreCase("twonky")) {
            figure = 1;
        } else if (getRobot().equalsIgnoreCase("hulkx90")) {
            figure = 2;
        } else if (getRobot().equalsIgnoreCase("hammerbot")) {
            figure = 3;
        } else if (getRobot().equalsIgnoreCase("smashbot")) {
            figure = 4;
        } else if (getRobot().equalsIgnoreCase("zoombot")) {
            figure = 5;
        } else if (getRobot().equalsIgnoreCase("spinbot")) {
            figure = 6;
        }


        logger.debug(classname + " figure (" + figure + "), username (" + User.getUserName() + ") sent to server.");
        player.getWrite().playerValues(User.getUserName(), figure);
    }

    /**
     * submitReadyToPlay(Boolean)
     * Method realizes the MessageType SetStatus of MVVM Model part.
     * Methods gets called from the GameStartView(Model)
     * @param status
     * @author Thomas Richter
     */
    public void submitReadyToPlay(Boolean status) {
        logger.debug(classname + " submitReadyToPlay("+ status + ") called.");

        setReadyToPlay(status);
        player.getWrite().setStatus(status);


        logger.debug(classname + " readyToPlay (" + status + ")" + " sent to server.");
    }

    /**
     * submitRacingCourse(String)
     * Method realizes the MessageType SelectMap of MVVM Model part.
     * Methods gets called from the GameStartView(Model)
     * @param racingCourse
     * @author Thomas Richter
     */
    public void submitRacingCourse(String racingCourse) {
        logger.debug(classname + " submitRacingCourse(String) called.");

        player.getWrite().selectMap(racingCourse);

        logger.debug(classname + " racingCourse (" + racingCourse + ") sent to server.");
    }

    /**
     * submitStartingPoint()
     * Method realizes the MessageType SetStartingPoint of MVVM Model part.
     * Methods gets called from the GameView(Model)
     * @author Thomas Richter
     */
    public void submitStartingPoint () {
        logger.debug(classname + " submitStartingPoint() called.");

        Pair  <Double,Double> tempOwnPosition = new Pair<>(0.0,0.0);

        if (getRobot().equalsIgnoreCase("twonky")) {
            tempOwnPosition = positionTwonky;
        } else if (getRobot().equalsIgnoreCase("hulkx90")) {
            tempOwnPosition = positionHulkX90;
        } else if (getRobot().equalsIgnoreCase("hammerbot")) {
            tempOwnPosition = positionHammerBot;
        } else if (getRobot().equalsIgnoreCase("smashbot")) {
            tempOwnPosition = positionSmashBot;
        } else if (getRobot().equalsIgnoreCase("zoombot")) {
            tempOwnPosition = positionZoomBot;
        } else if (getRobot().equalsIgnoreCase("spinbot")) {
            tempOwnPosition = positionSpinBot;
        }

        Pair <Double,Double> field14 = new Pair<Double, Double>(100.0,50.0);
        Pair <Double,Double> field39 = new Pair<Double, Double>(50.0,150.0);
        Pair <Double,Double> field53 = new Pair<Double, Double>(100.0,200.0);
        Pair <Double,Double> field66 = new Pair<Double, Double>(100.0,250.0);
        Pair <Double,Double> field78 = new Pair<Double, Double>(50.0,300.0);
        Pair <Double,Double> field105 = new Pair<Double, Double>(100.0,400.0);

        int fieldID = 200;

        if (tempOwnPosition.equals(field14)) {
            fieldID = 14;
        } else if (tempOwnPosition.equals(field39)) {
            fieldID = 39;
        } else if (tempOwnPosition.equals(field53)) {
            fieldID = 53;
        } else if (tempOwnPosition.equals(field66)) {
            fieldID = 66;
        } else if (tempOwnPosition.equals(field78)) {
            fieldID = 78;
        } else if (tempOwnPosition.equals(field105)) {
            fieldID = 105;
        }
        player.getWrite().setStartingPoint(fieldID);



        logger.debug(classname + " robot( " + robot + "), fieldID (" + fieldID + ") sent to server.");
    }

    /**
     * submitRegister(int)
     * Method realizes the MessageType SelectCard of MVVM Model part.
     * Methods gets called from the GameView(Model)
     * @param programmingCard
     * @author Thomas Richter
     */
    public void submitRegister (int programmingCard) {
        logger.debug(classname + " submitRegister(" + programmingCard + ") called.");

        String card = programmingCards[programmingCard];
        int register = currentRegister;

        player.getWrite().selectCard(card, register);

        logger.debug(classname + " card( " + card + "), register( " + currentRegister + ") sent to server.");
    }

    /**
     * submitPlayIt()
     * Method realizes the MessageType PlayIt of MVVM Model part.
     * Methods gets called from the GameView(Model)
     * @author Thomas Richter
     */
    public void submitPlayIt () {
        logger.debug(classname + " submitPlayIt called.");
        player.getWrite().playIt();

    }


    /**
     * submitDamage(String)
     * Method realizes the MessageType SelectDamage of MVVM Model part.
     * Methods gets called from the GameView(Model)
     * @param damageCard
     * @author Thomas Richter
     */
    public void submitDamage (String damageCard) {
        logger.debug(classname + " submitDamage(" + damageCard + ") called.");

        int arrayPosition = damageToPick - pickDamage;
        damageCards [arrayPosition] = damageCard;

        pickDamage = pickDamage - 1;

        logger.debug(classname + "Remaining damageCards to pick: " + pickDamage);

        if(pickDamage == 0) {
            String [] chosenDamageCards = new String[damageToPick];
            for (int i = 0; i<damageToPick; i++) {
                chosenDamageCards [i] = damageCards [i];
                damageCards [i] = "";
                logger.debug(classname + " chosenDamageCards [" + i + "]: " + chosenDamageCards[i]);
            }
            pickDamage = 0;
            damageToPick = 0;
            player.getWrite().selectDamage(chosenDamageCards);
            logger.debug(classname + " chosenDamageCards sent to server.");
        }


        Platform.runLater(()-> {
            updateViewModels();
        });
    }


    /**
     * sendChat(String, int)
     * Method realizes the MessageType SendChat of MVVM Model part.
     * Methods gets called either from the GameStartView(Model) or GameView(Model)
     * @param message
     * @param toPlayerID
     * @author Thomas Richter
     */
    public void sendChat(String message, int toPlayerID) {
        logger.debug(classname + " sendChat(" + message + ", " + toPlayerID + ") called.");
        player.getWrite().sendChat(message, toPlayerID);
    }



    // Methods which are used by server to manipulate data

    /**
     * updateForActivePhase()
     * Method checks which what the current phase is and calls accordingly methods to prepare data of MVVM Model to prepare for the current phase.
     * Methods gets called from the ReadThread
     * @author Thomas Richter
     */
    public void updateForActivePhase () {
        logger.debug(classname + " updateForActivePhase() called.");
        if (activePhase == 2) {
            logger.debug(classname + " activePhase: " + activePhase);
            prepareProgrammingPhase();
        }
    }

    /**
     * prepareProgrammingPhase()
     * Method prepares data of MVVM Model for programming phase.
     * Method gets called from updateForActivePhase()
     * @author Thomas Richter
     */
    public void prepareProgrammingPhase() {
        logger.debug(classname + " prepareProgrammingPhase() called.");

        this.myRegisters[0] = "CurrentRegister";
        this.myRegisters[1] = "LockedRegister";
        this.myRegisters[2] = "LockedRegister";
        this.myRegisters[3] = "LockedRegister";
        this.myRegisters[4] = "LockedRegister";

        cardSelectedTwonky.clear();
        cardSelectedHulkX90.clear();
        cardSelectedHammerBot.clear();
        cardSelectedSmashBot.clear();
        cardSelectedSpinBot.clear();
        cardSelectedZoomBot.clear();

        register1Cards.clear();
        register2Cards.clear();
        register3Cards.clear();
        register4Cards.clear();
        register5Cards.clear();

        reboot.clear();
    }


    /**
     * updatePositions(int, int)
     * Method updates the robot positions of the robots chosen for the game.
     * Method gets called from ReadThread, method movement(JSONObject).
     * @param playerID
     * @param to
     * @author Thomas Richter
     */
    public void updatePositions (int playerID, int to) {
        logger.debug(classname + " updatePositions(" + playerID + ", " + to + ") called.");

        if (playerIdTwonky!= null && playerID == playerIdTwonky) {
            positionTwonky = moveToField(to);
            logger.debug(classname + " Twonky (" + playerID +  ") moved to: " + positionTwonky);
        } else if (playerIdHulkX90 != null && playerID == playerIdHulkX90) {
            positionHulkX90 = moveToField(to);
            logger.debug(classname + " HulkX90 (" + playerID + ") moved to: " + positionHulkX90);
        } else if (playerIdHammerBot != null && playerID == playerIdHammerBot) {
            positionHammerBot = moveToField(to);
            logger.debug(classname + " HammerBot (" + playerID + ") moved to: " + positionHammerBot);
        } else if (playerIdSmashBot != null && playerID == playerIdSmashBot) {
            positionSmashBot = moveToField(to);
            logger.debug(classname + " SmashBot (" + playerID + ") moved to: " + positionSmashBot);
        } else if (playerIdZoomBot != null && playerID == playerIdZoomBot) {
            positionZoomBot = moveToField(to);
            logger.debug(classname + " ZoomBot (" + playerID + ") moved to: " + positionZoomBot);
        } else if (playerIdSpinBot != null && playerID == playerIdSpinBot) {
            positionSpinBot = moveToField(to);
            logger.debug(classname + " SpinBot (" + playerID + ") moved to: " + positionSpinBot);
        }
    }

    /**
     * moveToFiel(int)
     * Method translates the field position id of the chosen robot to pixel values so that it can get consumed by the GameView(Model).
     * Method gets called by updatePositions(int, int)
     * @param field
     * @return coordinates
     * @author Thomas Richter
     */

    public Pair<Double, Double> moveToField (int field) {
        logger.debug(classname + " moveToField(" + field + ") called.");

        Double xValue = 0.0;
        Double yValue = 0.0;


        // Row 0
        if (0 <= field && field < 13) {
              xValue = field * 50.0 + 50.0;
              yValue = 0.0;
        }
        // Row 1
        else if (13 <= field && field < 26) {
            xValue = (field - 13) * 50.0 + 50.0;
            yValue = 50.0;
        }
        // Row 2
        else if (26 <= field && field < 39) {
            xValue = (field - 26) * 50.0 + 50.0;
            yValue = 100.0;
        }
        // Row 3
        else if (39 <= field && field < 52) {
            xValue = (field - 39) * 50.0 + 50.0;
            yValue = 150.0;
        }
        // Row 4
        else if (52 <= field && field < 65) {
            xValue = (field - 52) * 50.0 + 50.0;
            yValue = 200.0;
        }
        // Row 5
        else if (65 <= field && field < 78) {
            xValue = (field - 65) * 50.0 + 50.0;
            yValue = 250.0;
        }
        // Row 6
        else if (78 <= field && field < 91) {
            xValue = (field - 78) * 50.0 + 50.0;
            yValue = 300.0;
        }
        // Row 7
        else if (91 <= field && field < 104) {
            xValue = (field - 91) * 50.0 + 50.0;
            yValue = 350.0;
        }
        // Row 8
        else if (104 <= field && field < 117) {
            xValue = (field - 104) * 50.0 + 50.0;
            yValue = 400.0;
        }
        // Row 9
        else if (117 <= field && field < 130) {
            xValue = (field - 117) * 50.0 + 50.0;
            yValue = 450.0;
        }

        Pair <Double, Double> coordinates = new Pair<>(xValue, yValue);
        logger.debug(classname +  field + " was translated to: xValue(" + coordinates.getKey() + "), yValue(" + coordinates.getValue() + ")");

        return coordinates;
    }


    /**
     * updateRotations(int, String)
     * Method updates the orientation of the robots chosen for the game.
     * Method gets called from ReadThread, method playerTurning(JSONObject).
     * @param playerID
     * @param direction
     * @author Thomas Richter
     */
    public void updateRotations ( int playerID, String direction) {
        logger.debug(classname + " updateRotations(" + playerID + ", " + direction + ") called.");


            if (playerIdTwonky!= null && playerID == playerIdTwonky) {
                Double robotDirection = rotationTwonky;
                logger.debug(classname + " Twonky (" + playerID +  ") rotated from: " + rotationTwonky);
                rotationTwonky = rotateRobot(direction, robotDirection);
                logger.debug(classname + " Twonky (" + playerID +  ") to: " + rotationTwonky);
            } else if (playerIdHulkX90 != null && playerID == playerIdHulkX90) {
                Double robotDirection = rotationHulkX90;
                logger.debug(classname + " HulkX90 (" + playerID +  ") rotated from: " + rotationHulkX90);
                rotationHulkX90 = rotateRobot(direction, robotDirection);
                logger.debug(classname + " HulkX90 (" + playerID +  ") to: " + rotationHulkX90);
            } else if (playerIdHammerBot != null && playerID == playerIdHammerBot) {
                Double robotDirection = rotationHammerBot;
                logger.debug(classname + " HammerBot (" + playerID +  ") rotated from: " + rotationHammerBot);
                rotationHammerBot = rotateRobot(direction, robotDirection);
                logger.debug(classname + " HammerBot (" + playerID +  ") to: " + rotationHammerBot);
            } else if (playerIdSmashBot != null && playerID == playerIdSmashBot) {
                Double robotDirection = rotationSmashBot;
                logger.debug(classname + " SmashBot (" + playerID +  ") rotated from: " + rotationSmashBot);
                rotationSmashBot = rotateRobot(direction, robotDirection);
                logger.debug(classname + " SmashBot (" + playerID +  ") to: " + rotationSmashBot);
            } else if (playerIdZoomBot != null && playerID == playerIdZoomBot) {
                Double robotDirection = rotationZoomBot;
                logger.debug(classname + " ZoomBot (" + playerID +  ") rotated from: " + rotationZoomBot);
                rotationZoomBot = rotateRobot(direction, robotDirection);
                logger.debug(classname + " ZoomBot (" + playerID +  ") to: " + rotationZoomBot);
            } else if (playerIdSpinBot != null && playerID == playerIdSpinBot) {
                Double robotDirection = rotationSpinBot;
                logger.debug(classname + " SpinBot (" + playerID +  ") rotated from: " + rotationSpinBot);
                rotationSpinBot = rotateRobot(direction, robotDirection);
                logger.debug(classname + " SpinBot (" + playerID +  ") to: " + rotationSpinBot);
            }
    }


    /**
     * rotateRobot(String, Double)
     * Method translates the turning value and old orientation value from chosen robot to the new robot orientation so that it can get consumed by the GameView(Model).
     * Method gets called by updateRotations(int, String).
     * @param direction
     * @param robotDirection
     * @return Double
     * @author Thomas Richter
     */
    public Double rotateRobot (String direction, Double robotDirection) {
        Double newRobotDirection = 0.0;
        if (direction.equalsIgnoreCase("clockwise")) {
            if (robotDirection == 0.0) {
                newRobotDirection = 90.0;
            } else if (robotDirection == 90.0) {
                newRobotDirection = 180.0;
            } else if (robotDirection == 180.0) {
                newRobotDirection = 270.0;
            } else {
                newRobotDirection = 0.0;
            }
        } else {
            if (robotDirection == 0.0) {
                newRobotDirection = 270.0;
            } else if (robotDirection == 270.0) {
                newRobotDirection = 180.0;
            } else if (robotDirection == 180.0) {
                newRobotDirection = 90.0;
            } else {
                newRobotDirection = 0.0;
            }
        }
        return newRobotDirection;
    }


    /**
     * saveCurrentCards()
     * Method saves the played cards from the current round.
     * Method gets called by ReadThread method currentCards(JSON Object).
     * @author Thomas Richter
     */
    public void saveCurrentCards() {
        logger.debug(classname + " saveCurrentCards() called.");

        activatePlayIt = true;
        currentRound = currentRound + 1;
        if (currentRound == 1) {
            register1Cards.putAll(currentCards);
        } else if (currentRound == 2) {
            register2Cards.putAll(currentCards);
        } else if (currentRound == 3) {
            register3Cards.putAll(currentCards);
        } else if (currentRound == 4) {
            register4Cards.putAll(currentCards);
        } else if (currentRound == 5) {
            register5Cards.putAll(currentCards);
            currentRound = 0;
            resetRegister = true;
        } else {
            register1Cards.clear();
            register2Cards.clear();
            register3Cards.clear();
            register4Cards.clear();
            register5Cards.clear();
        }

        logger.debug(classname + " currentCards: " + currentCards);
    }

    /**
     * setRobotDamage(int)
     * Method registers when a robot got hit by a Laser.
     * Method gets called from ReadThread method damaged(JSONObject).
     * Method sets variables so that the damage animation in the GameView can be shown.
     * @param playerID
     * @author Thomas Richter
     */
    public void setRobotDamage(int playerID) {
        logger.debug(classname + " setRobotDamage(" + playerID + ") called.");
        int id = playerID;

        if (!(playerIdTwonky == null) && id == playerIdTwonky) {
            twonkyDamage = true;
            logger.debug(classname + " twonkyDamage: " + twonkyDamage);
        }

        if (!(playerIdHulkX90 == null) && id == playerIdHulkX90) {
            hulkX90Damage = true;
            logger.debug(classname + " hulkX90Damage: " + hulkX90Damage);
        }

        if (!(playerIdHammerBot == null) && id == playerIdHammerBot) {
            hammerBotDamage = true;
            logger.debug(classname + " hammerBotDamage: " + hammerBotDamage);
        }

        if (!(playerIdSmashBot == null) && id == playerIdSmashBot) {
            smashBotDamage = true;
            logger.debug(classname + " smashBotDamage: " + smashBotDamage);
        }

        if (!(playerIdZoomBot == null) && id == playerIdZoomBot) {
            zoomBotDamage = true;
            logger.debug(classname + " zoomBotDamage: " + zoomBotDamage);
        }

        if (!(playerIdSpinBot == null) && id == playerIdSpinBot) {
            spinBotDamage = true;
            logger.debug(classname + " spinBotDamage: " + spinBotDamage);
        }
    }







    // Getter and Setter Classes
    public void setRobot(String robot) { this.robot = robot; }
    public String getRobot() { return robot; }

    public String getPlayerNameTwonky() { return playerNameTwonky; }
    public String getPlayerNameHulkX90() { return playerNameHulkX90; }
    public String getPlayerNameHammerBot() { return playerNameHammerBot; }
    public String getPlayerNameSmashBot() { return playerNameSmashBot; }
    public String getPlayerNameZoomBot() { return playerNameZoomBot; }
    public String getPlayerNameSpinBot() { return playerNameSpinBot; }

    public Boolean getAvailableTwonky() { return availableTwonky; }
    public Boolean getAvailableHulkX90() { return availableHulkX90; }
    public Boolean getAvailableHammerBot() { return availableHammerBot; }
    public Boolean getAvailableSmashBot() { return availableSmashBot; }
    public Boolean getAvailableZoomBot() { return availableZoomBot; }
    public Boolean getAvailableSpinBot() { return availableSpinBot; }

    public Boolean getPlayerAdded() { return playerAdded; }
    public void setPlayerAdded (Boolean added) { this.playerAdded = added; }

    public void setStartingBoard(String startingBoard) { this.startingBoard = startingBoard; }
    public void setRacingCourse(String racingCourse) { this.racingCourse = racingCourse; }

    public void setReadyToPlay (Boolean isReadyToPlay) {this.readyToPlay = isReadyToPlay; }


    public String[] getProgrammingCardsInitial() { return programmingCardsInitial; }
    public void setProgrammingCardsInitial(String[] programmingCardsInitial) { this.programmingCardsInitial = programmingCardsInitial.clone(); }
    public String[] getProgrammingCards() { return programmingCards; }
    public void setProgrammingCards(String[] programmingCards) { this.programmingCards = programmingCards.clone(); }
    public String[] getMyRegisters() { return myRegisters; }
    public void setMyRegisters(String[] registers) { this.myRegisters = registers; }

    public Pair<Double, Double> getPositionTwonky() { return positionTwonky; }
    public void setPositionTwonky(Pair<Double, Double> positionTwonky) { this.positionTwonky = positionTwonky; }
    public Pair<Double, Double> getPositionHulkX90() { return positionHulkX90; }
    public void setPositionHulkX90(Pair<Double, Double> positionHulkX90) { this.positionHulkX90 = positionHulkX90; }
    public Pair<Double, Double> getPositionHammerBot() { return positionHammerBot; }
    public void setPositionHammerBot(Pair<Double, Double> positionHammerBot) { this.positionHammerBot = positionHammerBot; }
    public Pair<Double, Double> getPositionSmashBot() { return positionSmashBot; }
    public void setPositionSmashBot(Pair<Double, Double> positionSmashBot) { this.positionSmashBot = positionSmashBot; }
    public Pair<Double, Double> getPositionZoomBot() { return positionZoomBot; }
    public void setPositionZoomBot(Pair<Double, Double> positionZoomBot) { this.positionZoomBot = positionZoomBot; }
    public Pair<Double, Double> getPositionSpinBot() { return positionSpinBot; }
    public void setPositionSpinBot(Pair<Double, Double> positionSpinBot) { this.positionSpinBot = positionSpinBot; }


}

