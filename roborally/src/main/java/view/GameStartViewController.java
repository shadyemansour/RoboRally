package view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import viewmodel.GameStartViewModel;

/**
 * Controller of the GameStartView
 */

public class GameStartViewController {

    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");

    @FXML
    private BorderPane root_borderPane;
    @FXML
    private GridPane grid_GameStart;
    @FXML
    private GridPane grid_Chat;

    // Variables for grid_GameStart Layer
    @FXML
    ImageView img_Background;

    @FXML
    public TextField txt_userName;
    @FXML
    public Button btn_chooseUserName;

    @FXML
    public Button btn_Twonky;
    @FXML
    public ImageView img_Twonky;
    @FXML
    public ImageView img_TwonkyNotAvailable;

    @FXML
    public Button btn_HulkX90;
    @FXML
    public ImageView img_HulkX90;
    @FXML
    public ImageView img_HulkX90NotAvailable;

    @FXML
    public Button btn_HammerBot;
    @FXML
    public ImageView img_HammerBot;
    @FXML
    public ImageView img_HammerBotNotAvailable;

    @FXML
    public Button btn_SmashBot;
    @FXML
    public ImageView img_SmashBot;
    @FXML
    public ImageView img_SmashBotNotAvailable;

    @FXML
    public Button btn_ZoomBot;
    @FXML
    public ImageView img_ZoomBot;
    @FXML
    public ImageView img_ZoomBotNotAvailable;

    @FXML
    public Button btn_SpinBot;
    @FXML
    public ImageView img_SpinBot;
    @FXML
    public ImageView img_SpinBotNotAvailable;

    @FXML
    public ChoiceBox choiceb_racingCourse;
    @FXML
    public ImageView img_SubmitRacingCourse;
    @FXML
    public Button btn_SubmitRacingCourse;


    @FXML
    public Button btn_OpenChat;

    @FXML
    public Button btn_submitAndReadyToPlay;


    // Variables for grid_Chat Layer
    @FXML
    public Button btn_SendChat;
    @FXML
    public Button btn_GoBack;

    @FXML
    public TextArea txt_ChatHistory;
    @FXML
    public TextArea txt_ChatMessage;


    @FXML
    public CheckBox cb_Player1;
    @FXML
    public CheckBox cb_Player2;
    @FXML
    public CheckBox cb_Player3;
    @FXML
    public CheckBox cb_Player4;
    @FXML
    public CheckBox cb_Player5;


    // Other Variables
    private String nextScene = "game";

    private ObjectProperty <Boolean> startGame = new SimpleObjectProperty<>();

    private ViewHandler viewHandler;
    private GameStartViewModel gameStartViewModel;


    final ChangeListener<Number> resizeWidthListener = new ChangeListener<Number>()
    {
        /**
         * Listener for resizing the Background picture
         * @param root_BorderPaneWidth
         * @param oldWidth
         * @param newWidth
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Number> root_BorderPaneWidth, Number oldWidth, Number newWidth) {
            double imageWidth;
            imageWidth = (double) newWidth + 100;
            img_Background.setFitWidth(imageWidth);
        }
    };

    final ChangeListener<Boolean> openNextView = new ChangeListener<Boolean>() {
        /**
         * Listener to determine whether nextScene can be opened
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (gameStartViewModel.startGame.getValue() && gameStartViewModel.statusSubmitReadyToPlay.getValue().equals("Withdraw Readiness")) {
                try {
                    viewHandler.openView(nextScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * Method initializes the variables and binds it to according GameStartViewModel variables.
     * @param gameStartViewModel
     * @param viewHandler
     * @author Thomas Richter
     */
    public void init(GameStartViewModel gameStartViewModel, ViewHandler viewHandler) {
        this.gameStartViewModel = gameStartViewModel;
        this.viewHandler = viewHandler;

        root_borderPane.widthProperty().addListener(resizeWidthListener);

        this.grid_GameStart.setVisible(true);
        this.grid_Chat.setVisible(false);


        btn_chooseUserName.disableProperty().bindBidirectional(gameStartViewModel.disableButtonChooseUsername);

        txt_userName.editableProperty().bindBidirectional(gameStartViewModel.editableTextfieldChooseUsername);
        txt_userName.textProperty().addListener(new ChangeListener<String>() {
                                                    /**
                                                     * Listener to determine whether the userName can be submitted to the server.
                                                     * @param observableValue
                                                     * @param s
                                                     * @param t1
                                                     * @author Thomas Richter
                                                     */
                                                    @Override
                                                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                                                        if ((txt_userName.textProperty().getValue().trim().isEmpty()) || txt_userName.textProperty().getValue().length() > 12) {
                                                            btn_chooseUserName.setDisable(true);
                                                        } else {
                                                            btn_chooseUserName.setDisable(false);
                                                        }

                                                    }
                                                });

        btn_Twonky.disableProperty().bindBidirectional(gameStartViewModel.disableTwonky);
        btn_HulkX90.disableProperty().bindBidirectional(gameStartViewModel.disableHulkX90);
        btn_HammerBot.disableProperty().bindBidirectional(gameStartViewModel.disableHammerBot);
        btn_SmashBot.disableProperty().bindBidirectional(gameStartViewModel.disableSmashBot);
        btn_ZoomBot.disableProperty().bindBidirectional(gameStartViewModel.disableZoomBot);
        btn_SpinBot.disableProperty().bindBidirectional(gameStartViewModel.disableSpinBot);

        btn_Twonky.textProperty().bindBidirectional(gameStartViewModel.statusTwonky);
        btn_HulkX90.textProperty().bindBidirectional(gameStartViewModel.statusHulkX90);
        btn_HammerBot.textProperty().bindBidirectional(gameStartViewModel.statusHammerBot);
        btn_SmashBot.textProperty().bindBidirectional(gameStartViewModel.statusSmashBot);
        btn_ZoomBot.textProperty().bindBidirectional(gameStartViewModel.statusZoomBot);
        btn_SpinBot.textProperty().bindBidirectional(gameStartViewModel.statusSpinBot);

        btn_Twonky.textFillProperty().bindBidirectional(gameStartViewModel.fillTwonky);
        btn_HulkX90.textFillProperty().bindBidirectional(gameStartViewModel.fillHulkX90);
        btn_HammerBot.textFillProperty().bindBidirectional(gameStartViewModel.fillHammerBot);
        btn_SmashBot.textFillProperty().bindBidirectional(gameStartViewModel.fillSmashBot);
        btn_ZoomBot.textFillProperty().bindBidirectional(gameStartViewModel.fillZoomBot);
        btn_SpinBot.textFillProperty().bindBidirectional(gameStartViewModel.fillSpinBot);

        img_Twonky.visibleProperty().bindBidirectional(gameStartViewModel.showTwonky);
        img_TwonkyNotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackTwonky);

        img_HulkX90.visibleProperty().bindBidirectional(gameStartViewModel.showHulkX90);
        img_HulkX90NotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackHulkX90);

        img_HammerBot.visibleProperty().bindBidirectional(gameStartViewModel.showHammerBot);
        img_HammerBotNotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackHammerBot);

        img_SmashBot.visibleProperty().bindBidirectional(gameStartViewModel.showSmashBot);
        img_SmashBotNotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackSmashBot);

        img_ZoomBot.visibleProperty().bindBidirectional(gameStartViewModel.showZoomBot);
        img_ZoomBotNotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackZoomBot);

        img_SpinBot.visibleProperty().bindBidirectional(gameStartViewModel.showSpinBot);
        img_SpinBotNotAvailable.visibleProperty().bindBidirectional(gameStartViewModel.showBlackSpinBot);



        choiceb_racingCourse.disableProperty().bindBidirectional(gameStartViewModel.disableRacingCourse);
        btn_SubmitRacingCourse.disableProperty().bindBidirectional(gameStartViewModel.disableRacingCourse);

        img_SubmitRacingCourse.visibleProperty().bindBidirectional(gameStartViewModel.visibleRacingCourseElements);
        btn_SubmitRacingCourse.visibleProperty().bindBidirectional(gameStartViewModel.visibleRacingCourseElements);

        choiceb_racingCourse.getItems().add("DizzyHighway");
        choiceb_racingCourse.getItems().add("ExtraCrispy");


        btn_OpenChat.disableProperty().bindBidirectional(gameStartViewModel.disableOpenChat);

        btn_submitAndReadyToPlay.textProperty().bindBidirectional(gameStartViewModel.statusSubmitReadyToPlay);
        btn_submitAndReadyToPlay.disableProperty().bindBidirectional(gameStartViewModel.disableSubmitReadyToPlay);

        startGame.bindBidirectional(gameStartViewModel.startGame);
        startGame.addListener(openNextView);

        // Variables for grid_Chat Layer
        grid_Chat.visibleProperty().bindBidirectional(gameStartViewModel.showChat);

        cb_Player1.textProperty().bindBidirectional(gameStartViewModel.player1Name);
        cb_Player2.textProperty().bindBidirectional(gameStartViewModel.player2Name);
        cb_Player3.textProperty().bindBidirectional(gameStartViewModel.player3Name);
        cb_Player4.textProperty().bindBidirectional(gameStartViewModel.player4Name);
        cb_Player5.textProperty().bindBidirectional(gameStartViewModel.player5Name);

        cb_Player1.disableProperty().bindBidirectional(gameStartViewModel.disablePlayer1);
        cb_Player2.disableProperty().bindBidirectional(gameStartViewModel.disablePlayer2);
        cb_Player3.disableProperty().bindBidirectional(gameStartViewModel.disablePlayer3);
        cb_Player4.disableProperty().bindBidirectional(gameStartViewModel.disablePlayer4);
        cb_Player5.disableProperty().bindBidirectional(gameStartViewModel.disablePlayer5);

        cb_Player1.selectedProperty().bindBidirectional(gameStartViewModel.sendToPlayer1);
        cb_Player2.selectedProperty().bindBidirectional(gameStartViewModel.sendToPlayer2);
        cb_Player3.selectedProperty().bindBidirectional(gameStartViewModel.sendToPlayer3);
        cb_Player4.selectedProperty().bindBidirectional(gameStartViewModel.sendToPlayer4);
        cb_Player5.selectedProperty().bindBidirectional(gameStartViewModel.sendToPlayer5);

        cb_Player1.visibleProperty().bindBidirectional(gameStartViewModel.visiblePlayer1);
        cb_Player2.visibleProperty().bindBidirectional(gameStartViewModel.visiblePlayer2);
        cb_Player3.visibleProperty().bindBidirectional(gameStartViewModel.visiblePlayer3);
        cb_Player4.visibleProperty().bindBidirectional(gameStartViewModel.visiblePlayer4);
        cb_Player5.visibleProperty().bindBidirectional(gameStartViewModel.visiblePlayer5);

        txt_ChatHistory.setEditable(false);
        txt_ChatHistory.textProperty().bindBidirectional(gameStartViewModel.chatHistory);

        txt_ChatMessage.textProperty().addListener(new ChangeListener<String>() {
            /**
             * Listener to determine whether the chat message can be sent to the server.
             * @param observableValue
             * @param s
             * @param t1
             * @author Thomas Richter
             */
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                checkForValidMessage();
            }
        });

    }


    /**
     * chooseUserName(ActionEvent)
     * Action Event which gets launched after Pressing btn_ChooseUserName.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void chooseUserName(ActionEvent actionEvent) {
        gameStartViewModel.chooseUserName(txt_userName.getText());
    }

    /**
     * selectTwonky(ActionEvent)
     * Action Event which gets launched after choosing Twonky after pressing btn_Twonky.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectTwonky(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_Twonky.setText("SELECTED");
            gameStartViewModel.selectRobot("Twonky");
        }

    }

    /**
     * selectHulkX90(ActionEvent)
     * Action Event which gets launched after choosing HulkX90 after pressing btn_HulkX90.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectHulkX90(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_HulkX90.setText("SELECTED");
            gameStartViewModel.selectRobot("HulkX90");
        }

    }

    /**
     * selectHammerBot(ActionEvent)
     * Action Event which gets launched after choosing HammerBot after pressing btn_HammerBot.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectHammerBot(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_HammerBot.setText("SELECTED");
            gameStartViewModel.selectRobot("HammerBot");
        }

    }

    /**
     * selectSmashBot(ActionEvent)
     * Action Event which gets launched after choosing SmashBot after pressing btn_SmashBot.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectSmashBot(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_SmashBot.setText("SELECTED");
            gameStartViewModel.selectRobot("SmashBot");
        }

    }

    /**
     * selectZoomBot(ActionEvent)
     * Action Event which gets launched after choosing ZoomBot after pressing btn_ZoomBot.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectZoomBot(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_ZoomBot.setText("SELECTED");
            gameStartViewModel.selectRobot("ZoomBot");
        }

    }

    /**
     * selectSpinBot(ActionEvent)
     * Action Event which gets launched after choosing SpinBot after pressing btn_SpinBot.
     * Method calls related GameStartViewModel method.
     * @param actionEvent
     * @author Thomas Richter
     */
    public void selectSpinBot(ActionEvent actionEvent) {
        resetButtonText();
        if (!gameStartViewModel.playerAdded) {
            btn_SpinBot.setText("SELECTED");
            gameStartViewModel.selectRobot("SpinBot");
        }

    }

    /**
     * resetButtonText()
     * This method gets called to change the Text of the Robot Buttons back to Available after changing the selection of the robot.
     * @author Thomas Richter
     */
    public void resetButtonText() {
        if (btn_Twonky.getText().toLowerCase().equals("selected")) {
            btn_Twonky.setText("AVAILABLE");
        } else if (btn_HulkX90.getText().toLowerCase().equals("selected")) {
            btn_HulkX90.setText("AVAILABLE");
        } else if (btn_HammerBot.getText().toLowerCase().equals("selected")) {
            btn_HammerBot.setText("AVAILABLE");
        } else if (btn_SmashBot.getText().toLowerCase().equals("selected")) {
            btn_SmashBot.setText("AVAILABLE");
        } else if (btn_ZoomBot.getText().toLowerCase().equals("selected")) {
            btn_ZoomBot.setText("AVAILABLE");
        } else if (btn_SpinBot.getText().toLowerCase().equals("selected")) {
            btn_SpinBot.setText("AVAILABLE");
        }
    }

    /**
     * showReadiness(MouseEvent)
     * Mouse Event which either:
     * - Submits the Clients selections of Robot and Username and manipulates the GUI accordingly
     * - Signals in the end to the Server, that Client is ready to play
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void showReadiness(MouseEvent mouseEvent) throws IOException {
    if (btn_submitAndReadyToPlay.textProperty().getValue().toLowerCase().equals("submit selections")) {

        submitPlayerValues();
        lockRobotButtons();

        } else if (btn_submitAndReadyToPlay.textProperty().getValue().toLowerCase().equals("ready to play!")) {
            gameStartViewModel.submitReadyToPlay(true);
            if (gameStartViewModel.startGame.getValue()) {
                viewHandler.openView(nextScene);
            }
            else {
                gameStartViewModel.statusSubmitReadyToPlay.setValue("Withdraw Readiness");
            }
        } else if (btn_submitAndReadyToPlay.textProperty().getValue().toLowerCase().equals("withdraw readiness")) {
            gameStartViewModel.submitReadyToPlay(false);
            if (gameStartViewModel.visibleRacingCourseElements.getValue()) {
                gameStartViewModel.visibleRacingCourseElements.setValue(false);
                gameStartViewModel.resetRacingCourseElements();
            }
            gameStartViewModel.statusSubmitReadyToPlay.setValue("Ready to play!");
    }
    }


    /**
     * submitPlayerValues()
     * This method submits the PlayerValues to the Server through MVVM Pattern.
     * Gets called by showReadiness event
     * @author Thomas Richter
     */
    public void submitPlayerValues() {
        gameStartViewModel.submitPlayerValues();
    }

    /**
     * lockRobotButtons()
     * This method locks the Robot Buttons of GUI, so that player can't select different robot after submitting his selections.
     * Gets called by showReadiness event
     * @author Thomas Richter
     */
    public void lockRobotButtons () {
        gameStartViewModel.disableTwonky.setValue(true);
        gameStartViewModel.disableHulkX90.setValue(true);
        gameStartViewModel.disableHammerBot.setValue(true);
        gameStartViewModel.disableSmashBot.setValue(true);
        gameStartViewModel.disableSpinBot.setValue(true);
        gameStartViewModel.disableZoomBot.setValue(true);
    }



    // Methods that are related to pane_Chat Layer of the StackPane
    /**
     * openChat(MouseEvent)
     * MouseEvent method which activates the grid_Chat Layer of the StackPane by pressing the btn_OpenChat
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void openChat(MouseEvent mouseEvent) throws IOException {
        grid_Chat.setVisible(true);
    }

    /**
     * closeChat(MouseEvent)
     * MouseEvent method which deactivates the grid_Chat Layer of the StackPane by pressing the btn_GoBack
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void closeChat(MouseEvent mouseEvent) throws IOException {
        grid_Chat.setVisible(false);
    }

    /**
     * sendChat(MouseEvent)
     * Method sends the chat message to selected other players by calling according GameStartViewModel method with relevant parameters
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void sendChat(MouseEvent mouseEvent) throws IOException {
        int sendTo = 0;
        String message;
        message = txt_ChatMessage.getText().replaceAll("(?m)^[ \t]*\r?\n", "");;
        Boolean sendToList[] = new Boolean[5];
        for (int i = 0; i < 5; i++) {
            sendToList[i] = false;
        }

        int howMany = 0;
        if (cb_Player1.isSelected()) {
            howMany = howMany + 1;
            sendToList[0] = true;
        }
        if (cb_Player2.isSelected()) {
            howMany = howMany + 1;
            sendToList[1] = true;
        }
        if (cb_Player3.isSelected()) {
            howMany = howMany + 1;
            sendToList[2] = true;
        }
        if (cb_Player4.isSelected()) {
            howMany = howMany + 1;
            sendToList[3] = true;
        }
        if (cb_Player5.isSelected()) {
            howMany = howMany + 1;
            sendToList[4] = true;
        }

        if (gameStartViewModel.getHashMapLength() == howMany) {
            sendTo = -1;
            gameStartViewModel.sendChat(message, sendTo);
        } else {
            for (int i = 0; i < 5; i++ ) {
                if (sendToList[i] == true) {
                    gameStartViewModel.sendChat(message, i);
                }
            }
        }

        txt_ChatMessage.setText("");
    }

    /**
     * checkForValidMessage()
     * Method checks whether the chat message is empty and sets the ability to press the send button accordingly.
     * @author Thomas Richter
     */
    public void checkForValidMessage() {
        if ((txt_ChatMessage.textProperty().getValue().trim().isEmpty())) {
            btn_SendChat.setDisable(true);
        } else {
            btn_SendChat.setDisable(false);
        }
    }

    /**
     * submitRacingCourse(MouseEvent)
     * MouseEvent which submits the chosen RacingCourse to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void submitRacingCourse(MouseEvent mouseEvent) {
        gameStartViewModel.submitRacingCourse((String) choiceb_racingCourse.getValue());
        gameStartViewModel.visibleRacingCourseElements.setValue(false);
        gameStartViewModel.resetRacingCourseElements();
    }



    // Getter and Setter Methods
    public ViewHandler getViewHandler() { return viewHandler; }

    public String getNextScene() { return nextScene; }


}
