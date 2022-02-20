package view;

import game.gameObjects.gamefield.ExtractJSON;
import game.gameObjects.gamefield.FieldObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import viewmodel.GameViewModel;

/**
 * Controller of the GameView
 */

public class GameViewController {

    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");

    private String nextScene = "GameEnd";
    private ViewHandler viewHandler;
    private GameViewModel gameViewModel;

    public BooleanProperty gameStarted;

    @FXML
    BorderPane root_borderPane;

    // Layers of the StackPane
    @FXML
    GridPane grid_Game;
    @FXML
    Pane pane_Robots;
    @FXML
    GridPane grid_Phase1;
    @FXML
    GridPane grid_Phase2;
    @FXML
    Pane pane_Phase3;
    @FXML
    GridPane grid_Chat;


    @FXML
    Pane pane_setStartingPoint;
    @FXML
    ImageView img_TimerGIF;
    @FXML
    ImageView img_TimerFrame;


    // grid_Game layer of StackPane
    @FXML
    ImageView img_Background;
    @FXML
    Button btn_OpenStats;
    @FXML
    Button btn_OpenChat;
    @FXML
    ImageView img_NewChat;
    @FXML
    Button btn_OpenHistory;
    @FXML
    Button btn_PlayIt;
    @FXML
    Label lbl_PlayIt;

    @FXML
    ImageView img_PlayItOn;
    @FXML
    ImageView img_PlayItOff;


    @FXML
    Pane pane_racingCourse;
    @FXML
    Pane pane_startingBoard;
    @FXML
    ImageView img_StartingBoardReboot;



    @FXML
    Button btn_startingPoint1_4;
    @FXML
    Button btn_startingPoint1_7;
    @FXML
    Button btn_startingPoint2_2;
    @FXML
    Button btn_startingPoint2_5;
    @FXML
    Button btn_startingPoint2_6;
    @FXML
    Button btn_startingPoint2_9;


    @FXML
    ImageView img_ownPlayerAvatar;
    @FXML
    ImageView img_Player1Avatar;
    @FXML
    ImageView img_Player2Avatar;
    @FXML
    ImageView img_Player3Avatar;
    @FXML
    ImageView img_Player4Avatar;
    @FXML
    ImageView img_Player5Avatar;

    @FXML
    ImageView img_OwnPlayerBackground;
    @FXML
    ImageView img_Player1Background;
    @FXML
    ImageView img_Player2Background;
    @FXML
    ImageView img_Player3Background;
    @FXML
    ImageView img_Player4Background;
    @FXML
    ImageView img_Player5Background;

    @FXML
    Label lbl_OwnName;
    @FXML
    Label lbl_Player1Name;
    @FXML
    Label lbl_Player2Name;
    @FXML
    Label lbl_Player3Name;
    @FXML
    Label lbl_Player4Name;
    @FXML
    Label lbl_Player5Name;


    // pane_Robots layer of StackPane
    @FXML
    ImageView img_Twonky;
    @FXML
    ImageView img_HulkX90;
    @FXML
    ImageView img_HammerBot;
    @FXML
    ImageView img_SmashBot;
    @FXML
    ImageView img_ZoomBot;
    @FXML
    ImageView img_SpinBot;

    @FXML
    ImageView img_TwonkyLaser;
    @FXML
    ImageView img_HulkX90Laser;
    @FXML
    ImageView img_HammerBotLaser;
    @FXML
    ImageView img_SmashBotLaser;
    @FXML
    ImageView img_ZoomBotLaser;
    @FXML
    ImageView img_SpinBotLaser;

    @FXML
    ImageView img_TwonkyDamage;
    @FXML
    ImageView img_HulkX90Damage;
    @FXML
    ImageView img_HammerBotDamage;
    @FXML
    ImageView img_SmashBotDamage;
    @FXML
    ImageView img_ZoomBotDamage;
    @FXML
    ImageView img_SpinBotDamage;



    // grid_Phase2 layer variables of StackPane Layer
    @FXML
    ImageView img_register1;
    @FXML
    ImageView img_register2;
    @FXML
    ImageView img_register3;
    @FXML
    ImageView img_register4;
    @FXML
    ImageView img_register5;


    @FXML
    ImageView img_programmingCard1;
    @FXML
    ImageView img_programmingCard2;
    @FXML
    ImageView img_programmingCard3;
    @FXML
    ImageView img_programmingCard4;
    @FXML
    ImageView img_programmingCard5;
    @FXML
    ImageView img_programmingCard6;
    @FXML
    ImageView img_programmingCard7;
    @FXML
    ImageView img_programmingCard8;
    @FXML
    ImageView img_programmingCard9;

    @FXML
    Button btn_programmingCard1;
    @FXML
    Button btn_programmingCard2;
    @FXML
    Button btn_programmingCard3;
    @FXML
    Button btn_programmingCard4;
    @FXML
    Button btn_programmingCard5;
    @FXML
    Button btn_programmingCard6;
    @FXML
    Button btn_programmingCard7;
    @FXML
    Button btn_programmingCard8;
    @FXML
    Button btn_programmingCard9;

    @FXML
    Button btn_ResetRegister;


    // Variables of grid_ShuffleCards
    @FXML
    GridPane grid_ShuffleCards;

    @FXML
    ImageView img_ShuffleGIF;




    // Variables of pane_Phase3
    @FXML
    Button btn_ClosePhase3;

    @FXML
    ImageView img_TwonkyActivationPhase;
    @FXML
    ImageView img_HulkX90ActivationPhase;
    @FXML
    ImageView img_HammerBotActivationPhase;
    @FXML
    ImageView img_SmashBotActivationPhase;
    @FXML
    ImageView img_ZoomBotActivationPhase;
    @FXML
    ImageView img_SpinBotActivationPhase;

    @FXML
    ImageView img_TwonkyRegister1;
    @FXML
    ImageView img_TwonkyRegister2;
    @FXML
    ImageView img_TwonkyRegister3;
    @FXML
    ImageView img_TwonkyRegister4;
    @FXML
    ImageView img_TwonkyRegister5;

    @FXML
    ImageView img_HulkX90Register1;
    @FXML
    ImageView img_HulkX90Register2;
    @FXML
    ImageView img_HulkX90Register3;
    @FXML
    ImageView img_HulkX90Register4;
    @FXML
    ImageView img_HulkX90Register5;

    @FXML
    ImageView img_HammerBotRegister1;
    @FXML
    ImageView img_HammerBotRegister2;
    @FXML
    ImageView img_HammerBotRegister3;
    @FXML
    ImageView img_HammerBotRegister4;
    @FXML
    ImageView img_HammerBotRegister5;

    @FXML
    ImageView img_SmashBotRegister1;
    @FXML
    ImageView img_SmashBotRegister2;
    @FXML
    ImageView img_SmashBotRegister3;
    @FXML
    ImageView img_SmashBotRegister4;
    @FXML
    ImageView img_SmashBotRegister5;

    @FXML
    ImageView img_ZoomBotRegister1;
    @FXML
    ImageView img_ZoomBotRegister2;
    @FXML
    ImageView img_ZoomBotRegister3;
    @FXML
    ImageView img_ZoomBotRegister4;
    @FXML
    ImageView img_ZoomBotRegister5;

    @FXML
    ImageView img_SpinBotRegister1;
    @FXML
    ImageView img_SpinBotRegister2;
    @FXML
    ImageView img_SpinBotRegister3;
    @FXML
    ImageView img_SpinBotRegister4;
    @FXML
    ImageView img_SpinBotRegister5;


    // Variables of grid_Damage
    @FXML
    GridPane grid_Damage;

    @FXML
    Button btn_SelectTrojan;
    @FXML
    Button btn_SelectWorm;
    @FXML
    Button btn_SelectVirus;

    @FXML
    Label lbl_RemainingDamages;





    // Variables of pane_Stats

    @FXML
    Pane pane_Stats;
    @FXML
    Button btn_CloseStats;

    @FXML
    ImageView img_YourStatistics;

    @FXML
    ImageView img_ownPlayerAvatar2;
    @FXML
    Button btn_nextPlayer;
    @FXML
    Button btn_previousPlayer;




    @FXML
    Label lbl_OwnName2;
    @FXML
    Label lbl_EnergyCubes;

    @FXML
    ImageView img_ControlPoint1;
    @FXML
    ImageView img_ControlPoint2;
    @FXML
    ImageView img_ControlPoint3;
    @FXML
    ImageView img_ControlPoint4;







    // Variables of pane_CardsYouGotNow
    @FXML
    Pane pane_CardsYouGotNow;

    @FXML
    ImageView img_yourCards1;
    @FXML
    ImageView img_yourCards2;
    @FXML
    ImageView img_yourCards3;
    @FXML
    ImageView img_yourCards4;
    @FXML
    ImageView img_yourCards5;

    @FXML
    Button btn_CloseCardsYouGotNow;


    // Variables of pane_DrawDamage of StackPane Layer
    @FXML
    Pane pane_DrawDamage;

    @FXML
    ImageView img_SpamHidden;
    @FXML
    ImageView img_VirusHidden;
    @FXML
    ImageView img_WormHidden;
    @FXML
    ImageView img_TrojanHidden;

    @FXML
    Label lbl_SpamCount;
    @FXML
    Label lbl_VirusCount;
    @FXML
    Label lbl_TrojanCount;
    @FXML
    Label lbl_WormCount;

    @FXML
    Button
    btn_CloseDrawDamage;



    // Variables of grid_Chat
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


    // Variables of grid_EndGame
    @FXML
    GridPane grid_GameEnd;
    @FXML
    ImageView img_gameWon;

    final ChangeListener<Number> resizeWidthListener = new ChangeListener<Number>() {
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
            img_gameWon.setFitWidth(imageWidth);
        }
    };

    final ChangeListener<Boolean> initializeRacingCourse = new ChangeListener<Boolean>() {
        /**
         * Listener to determine whether nextScene can be opened
         * @param gameStarted
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> gameStarted, Boolean aBoolean, Boolean t1) {
            initRacingCourse();
        }
    };

    final ChangeListener<Boolean> showTimerListener = new ChangeListener<Boolean>() {
        /**
         * Listener which checks whether the Timer should be shown in the GameView.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_TimerGIF.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {

                                img_TimerGIF.setImage(new Image("/Timer.gif"));

                        }),
                        new KeyFrame(Duration.seconds(30), e -> {
                            img_TimerGIF.setVisible(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> showShuffleCardsListener = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the ShuffleCard animation should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_ShuffleGIF.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {

                            img_ShuffleGIF.setImage(new Image("/Shuffle.gif"));
                            grid_Phase2.setVisible(false);

                        }),
                        new KeyFrame(Duration.seconds(5), e -> {
                            img_ShuffleGIF.setVisible(false);
                            if (gameViewModel.getActivePhase() == 2) {
                                grid_Phase2.setVisible(true);
                            } else {
                                grid_Phase2.setVisible(false);
                            }

                        }),
                        new KeyFrame(Duration.seconds(5), e -> {
                            gameViewModel.resetShuffle();
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> twonkyShoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for Twonky should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (img_TwonkyLaser.visibleProperty().getValue()) {
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.ZERO, e -> {
                                img_TwonkyLaser.visibleProperty().setValue(true);
                            }),
                            new KeyFrame(Duration.seconds(3), e -> {
                                img_TwonkyLaser.visibleProperty().setValue(false);
                            })
                    );
                    timeline.play();
                }
        }
    };

    final ChangeListener<Boolean> hulkX90Shoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for HulkX90 should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_HulkX90Laser.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_HulkX90Laser.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_HulkX90Laser.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> hammerBotShoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for HammerBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_HammerBotLaser.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_HammerBotLaser.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_HammerBotLaser.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> smashBotShoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for SmashBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_SmashBotLaser.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_SmashBotLaser.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_SmashBotLaser.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> zoomBotShoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for ZoomBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_ZoomBotLaser.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_ZoomBotLaser.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_ZoomBotLaser.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> spinBotShoot = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the LaserShooting animation for SpinBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_SpinBotLaser.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_SpinBotLaser.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_SpinBotLaser.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
        }
    };

    final ChangeListener<Boolean> twonkyDamage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for Twonky should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_TwonkyDamage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_TwonkyDamage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_TwonkyDamage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
            }
            gameViewModel.resetRobotDamages();
        }
    };

    final ChangeListener<Boolean> hulkX90Damage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for HulkX90 should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_HulkX90Damage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_HulkX90Damage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_HulkX90Damage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
                gameViewModel.resetRobotDamages();
            }
        }

    };

    final ChangeListener<Boolean> hammerBotDamage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for HammerBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_HammerBotDamage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_HammerBotDamage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_HammerBotDamage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
                gameViewModel.resetRobotDamages();
            }
        }

    };

    final ChangeListener<Boolean> smashBotDamage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for SmashBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_SmashBotDamage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_SmashBotDamage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_SmashBotDamage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
                gameViewModel.resetRobotDamages();
            }
        }

    };

    final ChangeListener<Boolean> zoomBotDamage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for ZoomBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_ZoomBotDamage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_ZoomBotDamage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_ZoomBotDamage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
                gameViewModel.resetRobotDamages();
            }
        }

    };

    final ChangeListener<Boolean> spinBotDamage = new ChangeListener<Boolean>() {
        /**
         *
         * Listener which checks whether the Damage animation for SpinBot should be shown.
         * @param observableValue
         * @param aBoolean
         * @param t1
         * @author Thomas Richter
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (img_SpinBotDamage.visibleProperty().getValue()) {
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> {
                            img_SpinBotDamage.visibleProperty().setValue(true);
                        }),
                        new KeyFrame(Duration.seconds(3), e -> {
                            img_SpinBotDamage.visibleProperty().setValue(false);
                        })
                );
                timeline.play();
                gameViewModel.resetRobotDamages();
            }
        }

    };


    /**
     * init(GameViewModel, ViewHandler)
     * This method initializes all Variables of the GUI and binds them to the according variables of the GameView Class
     * @param gameViewModel
     * @param viewHandler
     * @throws FileNotFoundException
     * @author Thomas Richter
     *
     */
    public void init(GameViewModel gameViewModel, ViewHandler viewHandler) throws FileNotFoundException {
        this.gameViewModel = gameViewModel;
        this.viewHandler = viewHandler;

        this.gameStarted = new SimpleBooleanProperty();
        this.gameStarted.bindBidirectional(gameViewModel.gameStarted);
        this.gameStarted.addListener(initializeRacingCourse);

        grid_GameEnd.setVisible(false);

        img_StartingBoardReboot.visibleProperty().bindBidirectional(gameViewModel.visibleStartingBoardReboot);

        img_ownPlayerAvatar.imageProperty().bindBidirectional(gameViewModel.ownPlayerAvatarImage);
        lbl_OwnName.textProperty().bindBidirectional(gameViewModel.ownName);
        lbl_OwnName.textFillProperty().bindBidirectional(gameViewModel.ownNameColor);

        grid_Chat.visibleProperty().bindBidirectional(gameViewModel.visibleChat);
        img_NewChat.visibleProperty().bindBidirectional(gameViewModel.visibleNewChat);


        img_PlayItOff.visibleProperty().bindBidirectional(gameViewModel.visiblePlayItOff);
        btn_PlayIt.disableProperty().bindBidirectional(gameViewModel.visiblePlayItOff);
        lbl_PlayIt.visibleProperty().bindBidirectional(gameViewModel.visiblePlayIt);


        pane_setStartingPoint.visibleProperty().bindBidirectional(gameViewModel.setStartingPointPane);

        img_TimerGIF.imageProperty().bindBidirectional(gameViewModel.timerImage);
        img_TimerGIF.visibleProperty().bindBidirectional(gameViewModel.showTimer);
        img_TimerGIF.visibleProperty().addListener(showTimerListener);
        img_TimerFrame.visibleProperty().bindBidirectional(gameViewModel.showTimer);
        img_TimerFrame.visibleProperty().addListener(showTimerListener);

        img_Player1Avatar.imageProperty().bindBidirectional(gameViewModel.player1AvatarImage);
        img_Player2Avatar.imageProperty().bindBidirectional(gameViewModel.player2AvatarImage);
        img_Player3Avatar.imageProperty().bindBidirectional(gameViewModel.player3AvatarImage);
        img_Player4Avatar.imageProperty().bindBidirectional(gameViewModel.player4AvatarImage);
        img_Player5Avatar.imageProperty().bindBidirectional(gameViewModel.player5AvatarImage);

        img_Player1Avatar.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer1);
        img_Player2Avatar.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer2);
        img_Player3Avatar.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer3);
        img_Player4Avatar.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer4);
        img_Player5Avatar.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer5);

        img_Player1Background.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer1);
        img_Player2Background.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer2);
        img_Player3Background.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer3);
        img_Player4Background.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer4);
        img_Player5Background.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer5);

        lbl_Player1Name.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer1);
        lbl_Player2Name.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer2);
        lbl_Player3Name.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer3);
        lbl_Player4Name.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer4);
        lbl_Player5Name.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer5);

        lbl_Player1Name.textProperty().bindBidirectional(gameViewModel.player1Name);
        lbl_Player2Name.textProperty().bindBidirectional(gameViewModel.player2Name);
        lbl_Player3Name.textProperty().bindBidirectional(gameViewModel.player3Name);
        lbl_Player4Name.textProperty().bindBidirectional(gameViewModel.player4Name);
        lbl_Player5Name.textProperty().bindBidirectional(gameViewModel.player5Name);


        // Variables of pane_Robots
        btn_startingPoint1_4.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint1_4);
        btn_startingPoint1_7.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint1_7);
        btn_startingPoint2_2.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint2_2);
        btn_startingPoint2_5.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint2_5);
        btn_startingPoint2_6.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint2_6);
        btn_startingPoint2_9.disableProperty().bindBidirectional(gameViewModel.disableStartingPoint2_9);

            // Robots
        img_Twonky.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_HulkX90.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HammerBot.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_SmashBot.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_ZoomBot.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_SpinBot.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);

        img_Twonky.xProperty().bindBidirectional(gameViewModel.xTwonky);
        img_Twonky.yProperty().bindBidirectional(gameViewModel.yTwonky);
        img_Twonky.rotateProperty().bindBidirectional(gameViewModel.rotationTwonky);

        img_HulkX90.xProperty().bindBidirectional(gameViewModel.xHulkX90);
        img_HulkX90.yProperty().bindBidirectional(gameViewModel.yHulkX90);
        img_HulkX90.rotateProperty().bindBidirectional(gameViewModel.rotationHulkX90);

        img_HammerBot.xProperty().bindBidirectional(gameViewModel.xHammerBot);
        img_HammerBot.yProperty().bindBidirectional(gameViewModel.yHammerBot);
        img_HammerBot.rotateProperty().bindBidirectional(gameViewModel.rotationHammerBot);

        img_SmashBot.xProperty().bindBidirectional(gameViewModel.xSmashBot);
        img_SmashBot.yProperty().bindBidirectional(gameViewModel.ySmashBot);
        img_SmashBot.rotateProperty().bindBidirectional(gameViewModel.rotationSmashBot);

        img_ZoomBot.xProperty().bindBidirectional(gameViewModel.xZoomBot);
        img_ZoomBot.yProperty().bindBidirectional(gameViewModel.yZoomBot);
        img_ZoomBot.rotateProperty().bindBidirectional(gameViewModel.rotationZoomBot);

        img_SpinBot.xProperty().bindBidirectional(gameViewModel.xSpinBot);
        img_SpinBot.yProperty().bindBidirectional(gameViewModel.ySpinBot);
        img_SpinBot.rotateProperty().bindBidirectional(gameViewModel.rotationSpinBot);

        img_Twonky.setX(0);
        img_Twonky.setY(100);

        img_HulkX90.setX(0);
        img_HulkX90.setY(150);

        img_HammerBot.setX(0);
        img_HammerBot.setY(200);

        img_SmashBot.setX(0);
        img_SmashBot.setY(250);

        img_ZoomBot.setX(0);
        img_ZoomBot.setY(300);

        img_SpinBot.setX(0);
        img_SpinBot.setY(350);

            // Lasers

        img_TwonkyLaser.visibleProperty().bindBidirectional(gameViewModel.showTwonkyLaser);
        img_TwonkyLaser.visibleProperty().addListener(twonkyShoot);
        img_HulkX90Laser.visibleProperty().bindBidirectional(gameViewModel.showHulkX90Laser);
        img_HulkX90Laser.visibleProperty().addListener(hulkX90Shoot);
        img_HammerBotLaser.visibleProperty().bindBidirectional(gameViewModel.showHammerBotLaser);
        img_HammerBotLaser.visibleProperty().addListener(hammerBotShoot);
        img_SmashBotLaser.visibleProperty().bindBidirectional(gameViewModel.showSmashBotLaser);
        img_SmashBotLaser.visibleProperty().addListener(smashBotShoot);
        img_ZoomBotLaser.visibleProperty().bindBidirectional(gameViewModel.showZoomBotLaser);
        img_ZoomBotLaser.visibleProperty().addListener(zoomBotShoot);
        img_SpinBotLaser.visibleProperty().bindBidirectional(gameViewModel.showSpinBotLaser);
        img_SpinBotLaser.visibleProperty().addListener(spinBotShoot);

        img_TwonkyLaser.xProperty().bindBidirectional(gameViewModel.xTwonkyLaser);
        img_TwonkyLaser.yProperty().bindBidirectional(gameViewModel.yTwonkyLaser);
        img_TwonkyLaser.rotateProperty().bindBidirectional(gameViewModel.rotationTwonkyLaser);

        img_HulkX90Laser.xProperty().bindBidirectional(gameViewModel.xHulkX90Laser);
        img_HulkX90Laser.yProperty().bindBidirectional(gameViewModel.yHulkX90Laser);
        img_HulkX90Laser.rotateProperty().bindBidirectional(gameViewModel.rotationHulkX90Laser);

        img_HammerBotLaser.xProperty().bindBidirectional(gameViewModel.xHammerBotLaser);
        img_HammerBotLaser.yProperty().bindBidirectional(gameViewModel.yHammerBotLaser);
        img_HammerBotLaser.rotateProperty().bindBidirectional(gameViewModel.rotationHammerBotLaser);

        img_SmashBotLaser.xProperty().bindBidirectional(gameViewModel.xSmashBotLaser);
        img_SmashBotLaser.yProperty().bindBidirectional(gameViewModel.ySmashBotLaser);
        img_SmashBotLaser.rotateProperty().bindBidirectional(gameViewModel.rotationSmashBotLaser);

        img_ZoomBotLaser.xProperty().bindBidirectional(gameViewModel.xZoomBotLaser);
        img_ZoomBotLaser.yProperty().bindBidirectional(gameViewModel.yZoomBotLaser);
        img_ZoomBotLaser.rotateProperty().bindBidirectional(gameViewModel.rotationZoomBotLaser);

        img_SpinBotLaser.xProperty().bindBidirectional(gameViewModel.xSpinBotLaser);
        img_SpinBotLaser.yProperty().bindBidirectional(gameViewModel.ySpinBotLaser);
        img_SpinBotLaser.rotateProperty().bindBidirectional(gameViewModel.rotationSpinBotLaser);

        img_TwonkyLaser.setX(0);
        img_TwonkyLaser.setY(100);

        img_HulkX90Laser.setX(0);
        img_HulkX90Laser.setY(150);

        img_HammerBotLaser.setX(0);
        img_HammerBotLaser.setY(200);

        img_SmashBotLaser.setX(0);
        img_SmashBotLaser.setY(250);

        img_ZoomBotLaser.setX(0);
        img_ZoomBotLaser.setY(300);

        img_SpinBotLaser.setX(0);
        img_SpinBotLaser.setY(350);

            // Damage GIFs

        img_TwonkyDamage.visibleProperty().bindBidirectional(gameViewModel.showTwonkyDamage);
        img_TwonkyDamage.visibleProperty().addListener(twonkyDamage);
        img_HulkX90Damage.visibleProperty().bindBidirectional(gameViewModel.showHulkX90Damage);
        img_HulkX90Damage.visibleProperty().addListener(hulkX90Damage);
        img_HammerBotDamage.visibleProperty().bindBidirectional(gameViewModel.showHammerBotDamage);
        img_HammerBotDamage.visibleProperty().addListener(hammerBotDamage);
        img_SmashBotDamage.visibleProperty().bindBidirectional(gameViewModel.showSmashBotDamage);
        img_SmashBotDamage.visibleProperty().addListener(smashBotDamage);
        img_ZoomBotDamage.visibleProperty().bindBidirectional(gameViewModel.showZoomBotDamage);
        img_ZoomBotDamage.visibleProperty().addListener(zoomBotDamage);
        img_SpinBotDamage.visibleProperty().bindBidirectional(gameViewModel.showSpinBotDamage);
        img_SpinBotDamage.visibleProperty().addListener(spinBotDamage);

        img_TwonkyDamage.xProperty().bindBidirectional(gameViewModel.xTwonky);
        img_TwonkyDamage.yProperty().bindBidirectional(gameViewModel.yTwonky);

        img_HulkX90Damage.xProperty().bindBidirectional(gameViewModel.xHulkX90);
        img_HulkX90Damage.yProperty().bindBidirectional(gameViewModel.yHulkX90);

        img_HammerBotDamage.xProperty().bindBidirectional(gameViewModel.xHammerBot);
        img_HammerBotDamage.yProperty().bindBidirectional(gameViewModel.yHammerBot);

        img_SmashBotDamage.xProperty().bindBidirectional(gameViewModel.xSmashBot);
        img_SmashBotDamage.yProperty().bindBidirectional(gameViewModel.ySmashBot);

        img_ZoomBotDamage.xProperty().bindBidirectional(gameViewModel.xZoomBot);
        img_ZoomBotDamage.yProperty().bindBidirectional(gameViewModel.yZoomBot);

        img_SpinBotDamage.xProperty().bindBidirectional(gameViewModel.xSpinBot);
        img_SpinBotDamage.yProperty().bindBidirectional(gameViewModel.ySpinBot);

        img_TwonkyDamage.setX(0);
        img_TwonkyDamage.setY(100);

        img_HulkX90Damage.setX(0);
        img_HulkX90Damage.setY(150);

        img_HammerBotDamage.setX(0);
        img_HammerBotDamage.setY(200);

        img_SmashBotDamage.setX(0);
        img_SmashBotDamage.setY(250);

        img_ZoomBotDamage.setX(0);
        img_ZoomBotDamage.setY(300);

        img_SpinBotDamage.setX(0);
        img_SpinBotDamage.setY(350);

        disableStartingButtons();

        // Variables of grid_Phase2
        grid_Phase2.visibleProperty().bindBidirectional(gameViewModel.visiblePhase2);

        btn_ResetRegister.disableProperty().bindBidirectional(gameViewModel.disableResetRegisters);

        img_programmingCard1.imageProperty().bindBidirectional(gameViewModel.programmingCard1);
        img_programmingCard2.imageProperty().bindBidirectional(gameViewModel.programmingCard2);
        img_programmingCard3.imageProperty().bindBidirectional(gameViewModel.programmingCard3);
        img_programmingCard4.imageProperty().bindBidirectional(gameViewModel.programmingCard4);
        img_programmingCard5.imageProperty().bindBidirectional(gameViewModel.programmingCard5);
        img_programmingCard6.imageProperty().bindBidirectional(gameViewModel.programmingCard6);
        img_programmingCard7.imageProperty().bindBidirectional(gameViewModel.programmingCard7);
        img_programmingCard8.imageProperty().bindBidirectional(gameViewModel.programmingCard8);
        img_programmingCard9.imageProperty().bindBidirectional(gameViewModel.programmingCard9);


        img_register1.imageProperty().bindBidirectional(gameViewModel.register1);
        img_register2.imageProperty().bindBidirectional(gameViewModel.register2);
        img_register3.imageProperty().bindBidirectional(gameViewModel.register3);
        img_register4.imageProperty().bindBidirectional(gameViewModel.register4);
        img_register5.imageProperty().bindBidirectional(gameViewModel.register5);

        btn_programmingCard1.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard1);
        btn_programmingCard2.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard2);
        btn_programmingCard3.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard3);
        btn_programmingCard4.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard4);
        btn_programmingCard5.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard5);
        btn_programmingCard6.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard6);
        btn_programmingCard7.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard7);
        btn_programmingCard8.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard8);
        btn_programmingCard9.disableProperty().bindBidirectional(gameViewModel.disableProgrammingCard9);

        btn_programmingCard1.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard1);
        btn_programmingCard2.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard2);
        btn_programmingCard3.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard3);
        btn_programmingCard4.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard4);
        btn_programmingCard5.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard5);
        btn_programmingCard6.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard6);
        btn_programmingCard7.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard7);
        btn_programmingCard8.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard8);
        btn_programmingCard9.visibleProperty().bindBidirectional(gameViewModel.visibleProgrammingCard9);

        // Variables of grid_ShuffleCards Layer of StackPane
        grid_ShuffleCards.visibleProperty().bindBidirectional(gameViewModel.visibleShuffleCards);
        img_ShuffleGIF.visibleProperty().bindBidirectional(gameViewModel.visibleShuffleCards);
        img_ShuffleGIF.visibleProperty().addListener(showShuffleCardsListener);


        // Variables of pane_Phase3 Layer of StackPane

        pane_Phase3.visibleProperty().bindBidirectional(gameViewModel.visiblePhase3);

        img_TwonkyActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister1.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister2.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister3.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister4.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister5.visibleProperty().bindBidirectional(gameViewModel.showTwonky);
        img_TwonkyRegister1.imageProperty().bindBidirectional(gameViewModel.twonkyRegisterImage1);
        img_TwonkyRegister2.imageProperty().bindBidirectional(gameViewModel.twonkyRegisterImage2);
        img_TwonkyRegister3.imageProperty().bindBidirectional(gameViewModel.twonkyRegisterImage3);
        img_TwonkyRegister4.imageProperty().bindBidirectional(gameViewModel.twonkyRegisterImage4);
        img_TwonkyRegister5.imageProperty().bindBidirectional(gameViewModel.twonkyRegisterImage5);

        img_HulkX90ActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register1.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register2.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register3.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register4.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register5.visibleProperty().bindBidirectional(gameViewModel.showHulkX90);
        img_HulkX90Register1.imageProperty().bindBidirectional(gameViewModel.hulkX90RegisterImage1);
        img_HulkX90Register2.imageProperty().bindBidirectional(gameViewModel.hulkX90RegisterImage2);
        img_HulkX90Register3.imageProperty().bindBidirectional(gameViewModel.hulkX90RegisterImage3);
        img_HulkX90Register4.imageProperty().bindBidirectional(gameViewModel.hulkX90RegisterImage4);
        img_HulkX90Register5.imageProperty().bindBidirectional(gameViewModel.hulkX90RegisterImage5);

        img_HammerBotActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister1.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister2.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister3.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister4.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister5.visibleProperty().bindBidirectional(gameViewModel.showHammerBot);
        img_HammerBotRegister1.imageProperty().bindBidirectional(gameViewModel.hammerBotRegisterImage1);
        img_HammerBotRegister2.imageProperty().bindBidirectional(gameViewModel.hammerBotRegisterImage2);
        img_HammerBotRegister3.imageProperty().bindBidirectional(gameViewModel.hammerBotRegisterImage3);
        img_HammerBotRegister4.imageProperty().bindBidirectional(gameViewModel.hammerBotRegisterImage4);
        img_HammerBotRegister5.imageProperty().bindBidirectional(gameViewModel.hammerBotRegisterImage5);

        img_SmashBotActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister1.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister2.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister3.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister4.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister5.visibleProperty().bindBidirectional(gameViewModel.showSmashBot);
        img_SmashBotRegister1.imageProperty().bindBidirectional(gameViewModel.smashBotRegisterImage1);
        img_SmashBotRegister2.imageProperty().bindBidirectional(gameViewModel.smashBotRegisterImage2);
        img_SmashBotRegister3.imageProperty().bindBidirectional(gameViewModel.smashBotRegisterImage3);
        img_SmashBotRegister4.imageProperty().bindBidirectional(gameViewModel.smashBotRegisterImage4);
        img_SmashBotRegister5.imageProperty().bindBidirectional(gameViewModel.smashBotRegisterImage5);

        img_ZoomBotActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister1.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister2.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister3.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister4.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister5.visibleProperty().bindBidirectional(gameViewModel.showZoomBot);
        img_ZoomBotRegister1.imageProperty().bindBidirectional(gameViewModel.zoomBotRegisterImage1);
        img_ZoomBotRegister2.imageProperty().bindBidirectional(gameViewModel.zoomBotRegisterImage2);
        img_ZoomBotRegister3.imageProperty().bindBidirectional(gameViewModel.zoomBotRegisterImage3);
        img_ZoomBotRegister4.imageProperty().bindBidirectional(gameViewModel.zoomBotRegisterImage4);
        img_ZoomBotRegister5.imageProperty().bindBidirectional(gameViewModel.zoomBotRegisterImage5);

        img_SpinBotActivationPhase.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister1.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister2.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister3.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister4.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister5.visibleProperty().bindBidirectional(gameViewModel.showSpinBot);
        img_SpinBotRegister1.imageProperty().bindBidirectional(gameViewModel.spinBotRegisterImage1);
        img_SpinBotRegister2.imageProperty().bindBidirectional(gameViewModel.spinBotRegisterImage2);
        img_SpinBotRegister3.imageProperty().bindBidirectional(gameViewModel.spinBotRegisterImage3);
        img_SpinBotRegister4.imageProperty().bindBidirectional(gameViewModel.spinBotRegisterImage4);
        img_SpinBotRegister5.imageProperty().bindBidirectional(gameViewModel.spinBotRegisterImage5);

        // Grid with fields for displaying the Racing Course and the Starting Board is initialized here and not at corresponding FXML file

        root_borderPane.widthProperty().addListener(resizeWidthListener);


        // Variables of grid_Damage
        grid_Damage.setVisible(false);
        grid_Damage.visibleProperty().bindBidirectional(gameViewModel.visibleDamageLayer);

        lbl_RemainingDamages.textProperty().bindBidirectional(gameViewModel.remainingDamages);



        // Variables of pane_Stats
        pane_Stats.visibleProperty().bindBidirectional(gameViewModel.visibleStats);

        img_YourStatistics.visibleProperty().bindBidirectional(gameViewModel.visibleOwnStats);

        img_ownPlayerAvatar2.imageProperty().bindBidirectional(gameViewModel.currentPlayerAvatarImage);
        lbl_OwnName2.textProperty().bindBidirectional(gameViewModel.currentPlayerName);

        btn_nextPlayer.disableProperty().bindBidirectional(gameViewModel.disableNextPlayer);
        btn_previousPlayer.disableProperty().bindBidirectional(gameViewModel.disablePreviousPlayer);

        lbl_EnergyCubes.textProperty().bind(gameViewModel.energyCubes);
        img_ControlPoint1.imageProperty().bindBidirectional(gameViewModel.controlPoint1);
        img_ControlPoint2.imageProperty().bindBidirectional(gameViewModel.controlPoint2);
        img_ControlPoint3.imageProperty().bindBidirectional(gameViewModel.controlPoint3);
        img_ControlPoint4.imageProperty().bindBidirectional(gameViewModel.controlPoint4);


        // Variables of pane_CardsYouGotNow Layer of StackPane

        pane_CardsYouGotNow.visibleProperty().bindBidirectional(gameViewModel.visibleCardsYouGotNow);
        img_yourCards1.imageProperty().bindBidirectional(gameViewModel.register1);
        img_yourCards2.imageProperty().bindBidirectional(gameViewModel.register2);
        img_yourCards3.imageProperty().bindBidirectional(gameViewModel.register3);
        img_yourCards4.imageProperty().bindBidirectional(gameViewModel.register4);
        img_yourCards5.imageProperty().bindBidirectional(gameViewModel.register5);

        // Variables of pane_DrawDamage Layer of StackPane

        pane_DrawDamage.visibleProperty().bindBidirectional(gameViewModel.visibleDrawDamage);
        img_SpamHidden.visibleProperty().bindBidirectional(gameViewModel.visibleSpamHidden);
        img_VirusHidden.visibleProperty().bindBidirectional(gameViewModel.visibleVirusHidden);
        img_TrojanHidden.visibleProperty().bindBidirectional(gameViewModel.visibleTrojanHidden);
        img_WormHidden.visibleProperty().bindBidirectional(gameViewModel.visibleWormHidden);

        lbl_SpamCount.textProperty().bindBidirectional(gameViewModel.spamCount);
        lbl_VirusCount.textProperty().bindBidirectional(gameViewModel.virusCount);
        lbl_TrojanCount.textProperty().bindBidirectional(gameViewModel.trojanCount);
        lbl_WormCount.textProperty().bindBidirectional(gameViewModel.wormCount);



        // Variables of grid_Chat Layer of StackPane
        grid_Chat.setVisible(false);

        cb_Player1.textProperty().bindBidirectional(gameViewModel.player1Name);
        cb_Player2.textProperty().bindBidirectional(gameViewModel.player2Name);
        cb_Player3.textProperty().bindBidirectional(gameViewModel.player3Name);
        cb_Player4.textProperty().bindBidirectional(gameViewModel.player4Name);
        cb_Player5.textProperty().bindBidirectional(gameViewModel.player5Name);

        cb_Player1.disableProperty().bindBidirectional(gameViewModel.disablePlayer1);
        cb_Player2.disableProperty().bindBidirectional(gameViewModel.disablePlayer2);
        cb_Player3.disableProperty().bindBidirectional(gameViewModel.disablePlayer3);
        cb_Player4.disableProperty().bindBidirectional(gameViewModel.disablePlayer4);
        cb_Player5.disableProperty().bindBidirectional(gameViewModel.disablePlayer5);

        cb_Player1.selectedProperty().bindBidirectional(gameViewModel.sendToPlayer1);
        cb_Player2.selectedProperty().bindBidirectional(gameViewModel.sendToPlayer2);
        cb_Player3.selectedProperty().bindBidirectional(gameViewModel.sendToPlayer3);
        cb_Player4.selectedProperty().bindBidirectional(gameViewModel.sendToPlayer4);
        cb_Player5.selectedProperty().bindBidirectional(gameViewModel.sendToPlayer5);

        cb_Player1.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer1);
        cb_Player2.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer2);
        cb_Player3.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer3);
        cb_Player4.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer4);
        cb_Player5.visibleProperty().bindBidirectional(gameViewModel.visiblePlayer5);

        txt_ChatHistory.setEditable(false);
        txt_ChatHistory.textProperty().bindBidirectional(gameViewModel.chatHistory);

        txt_ChatHistory.textProperty().addListener(new ChangeListener <Object>() {
            /**
             * Listener to check whether the chat history has been changed.
             * @param observable
             * @param oldValue
             * @param newValue
             * @author Thomas Richter
             */
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                txt_ChatHistory.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
            }
        } );


        txt_ChatMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Listener to check whether the Enter key has been pressed and sends the chat message if so.
             * @param keyEvent
             * @author Thomas Richter
             */
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {
                    sendChatMessage();
                }
            }
        });

        txt_ChatMessage.textProperty().addListener(new ChangeListener<String>() {
            /**
             * Listener to check whether the current text Message is not empty or doesn't just consist of white space and can be sent to the server.
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

        grid_GameEnd.visibleProperty().bindBidirectional(gameViewModel.visibleGameEnd);
        img_gameWon.imageProperty().bindBidirectional(gameViewModel.gameEndImage);
    }

    /**
     * initRacingCourse()
     * Method is called as soon as the changeListener initializeRacingCourse is activated.
     * Method loads the field Images dynamically into the GUI, depending on the chosen racingCourse.
     * @author Thomas Richter
     */

    public void initRacingCourse() {
        ImageView[][] racingCourseFieldPictures = new ImageView[10][10];
        GridPane racingCourse = new GridPane();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                racingCourseFieldPictures[x][y] = new ImageView(gameViewModel.getRacingCourseFieldImage(x, y));
                racingCourseFieldPictures[x][y].setFitWidth(50);
                racingCourseFieldPictures[x][y].setFitHeight(50);
                racingCourseFieldPictures [x][y].setRotate(gameViewModel.getRotation(x,y));
                racingCourse.add(racingCourseFieldPictures[x][y], x * 50, y * 50);
            }
        }
        pane_racingCourse.getChildren().add(racingCourse);
    }



    // Methods from here onwards

    // Methods that are related to grid_Game layer of StackPane

    // Summary of methods which select the Starting Point of the Robot

    /**
     * setStartingPoint1_4(MouseEvent)
     * MouseEvent which selects the starting point with id 14.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint1_4(MouseEvent mouseEvent) {
        Double xValue = 50.0;
        Double yValue = 150.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }
    /**
     * setStartingPoint1_7(MouseEvent)
     * MouseEvent which selects the starting point with id 39.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint1_7(MouseEvent mouseEvent) {
        Double xValue = 50.0;
        Double yValue = 300.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }

    /**
     * setStartingPoint2_2(MouseEvent)
     * MouseEvent which selects the starting point with id 53.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint2_2(MouseEvent mouseEvent) {
        Double xValue = 100.0;
        Double yValue = 50.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }

    /**
     * setStartingPoint2_5(MouseEvent)
     * MouseEvent which selects the starting point with id 66.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint2_5(MouseEvent mouseEvent) {
        Double xValue = 100.0;
        Double yValue = 200.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }

    /**
     * setStartingPoint2_6(MouseEvent)
     * MouseEvent which selects the starting point with id 78.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint2_6(MouseEvent mouseEvent) {
        Double xValue = 100.0;
        Double yValue = 250.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }

    /**
     * setStartingPoint2_9(MouseEvent)
     * MouseEvent which selects the starting point with id 105.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void setStartingPoint2_9(MouseEvent mouseEvent) {
        Double xValue = 100.0;
        Double yValue = 400.0;
        disableStartingButtons();
        gameViewModel.setStartingPoint(xValue, yValue);
        disableStartingButtons();
    }

    /**
     * disableStartingButtons()
     * Method disables the StartingPoint Buttons.
     * @author Thomas Richter
     */
    public void disableStartingButtons() {
        btn_startingPoint1_4.setDisable(true);
        btn_startingPoint1_7.setDisable(true);
        btn_startingPoint2_2.setDisable(true);
        btn_startingPoint2_5.setDisable(true);
        btn_startingPoint2_6.setDisable(true);
        btn_startingPoint2_9.setDisable(true);
    }


    /**
     * openChat(MouseEvent)
     * MouseEvent method which activates the grid_Chat Layer of the StackPane by pressing the btn_OpenChat
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void openChat(MouseEvent mouseEvent) throws IOException {

        gameViewModel.visibleChat.setValue(true);
        gameViewModel.resetChatIndicators();
    }

    // Methods that are related to grid_Phase1 layer of StackPane


    // Methods that are related to grid_Phase2 layer of StackPane

    // Summary of methods which select and submit the programming cards in programming phase

    /**
     * selectProgrammingCard1(MouseEvent)
     * MouseEvent which submits the selected programming card of register 1 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard1(MouseEvent mouseEvent) {
        btn_programmingCard1.setVisible(false);
        btn_programmingCard1.setDisable(true);
        gameViewModel.selectCard(0);
    }

    /**
     * selectProgrammingCard2(MouseEvent)
     * MouseEvent which submits the selected programming card of register 2 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard2(MouseEvent mouseEvent) {
        btn_programmingCard2.setVisible(false);
        btn_programmingCard2.setDisable(true);
        gameViewModel.selectCard(1);
    }

    /**
     * selectProgrammingCard3(MouseEvent)
     * MouseEvent which submits the selected programming card of register 3 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard3(MouseEvent mouseEvent) {
        btn_programmingCard3.setVisible(false);
        btn_programmingCard3.setDisable(true);
        gameViewModel.selectCard(2);
    }

    /**
     * selectProgrammingCard4(MouseEvent)
     * MouseEvent which submits the selected programming card of register 4 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard4(MouseEvent mouseEvent) {
        btn_programmingCard4.setVisible(false);
        btn_programmingCard4.setDisable(true);
        gameViewModel.selectCard(3);
    }

    /**
     * selectProgrammingCard5(MouseEvent)
     * MouseEvent which submits the selected programming card of register 5 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard5(MouseEvent mouseEvent) {
        btn_programmingCard5.setVisible(false);
        btn_programmingCard5.setDisable(true);
        gameViewModel.selectCard(4);
    }

    /**
     * selectProgrammingCard6(MouseEvent)
     * MouseEvent which submits the selected programming card of register 6 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard6(MouseEvent mouseEvent) {
        btn_programmingCard6.setVisible(false);
        btn_programmingCard6.setDisable(true);
        gameViewModel.selectCard(5);
    }

    /**
     * selectProgrammingCard7(MouseEvent)
     * MouseEvent which submits the selected programming card of register 7 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard7(MouseEvent mouseEvent) {
        btn_programmingCard7.setVisible(false);
        btn_programmingCard7.setDisable(true);
        gameViewModel.selectCard(6);
    }

    /**
     * selectProgrammingCard8(MouseEvent)
     * MouseEvent which submits the selected programming card of register 8 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard8(MouseEvent mouseEvent) {
        btn_programmingCard8.setVisible(false);
        btn_programmingCard8.setDisable(true);
        gameViewModel.selectCard(7);
    }

    /**
     * selectProgrammingCard9(MouseEvent)
     * MouseEvent which submits the selected programming card of register 9 to the server.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectProgramingCard9(MouseEvent mouseEvent) {
        btn_programmingCard9.setVisible(false);
        btn_programmingCard9.setDisable(true);
        gameViewModel.selectCard(8);
    }

    /**
     * resetRegisters(MouseEvent)
     * MouseEvent which resets the chosen Programming cards for the registers 1 to 5
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void resetRegisters(MouseEvent mouseEvent) {
        gameViewModel.resetRegister();
    }



    // Methods that are related to pane_Phase3 layer of StackPane

    /**
     * closePhase3(MouseEvent)
     * MouseEvent which makes the GUI Elements of Activation Phase invisible.
     * @author Thomas Richter
     */
    public void closePhase3(MouseEvent mouseEvent) {
        gameViewModel.visiblePhase3.setValue(false);
    }


    // Methods that are related to grid_Chat Layer of the StackPane

    /**
     * closeChat(MouseEvent)
     * MouseEvent method which deactivates the grid_Chat Layer of the StackPane by pressing the btn_GoBack
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     *
     */
    public void closeChat(MouseEvent mouseEvent) throws IOException {
        gameViewModel.visibleChat.setValue(false);
        gameViewModel.resetChatIndicators();
    }

    /**
     * sendChat(MouseEvent)
     * MouseEvent initiates the sending of the chat message when the according button is pressed.
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     *
     */
    public void sendChat(MouseEvent mouseEvent) throws IOException {
        sendChatMessage();
    }


    /**
     * sencChatMessage()
     * Methods checks which players are supposed to receive the chat message and and calls the according GameViewModel method with relevant parameters
     * @author Thomas Richter
     */
    public void sendChatMessage () {
        int sendTo = 0;
        String message;
        message = txt_ChatMessage.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
        message = txt_ChatMessage.getText().replaceAll("\n", "");

        if (!message.isBlank()) {
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

            if (gameViewModel.getHashMapLength() == howMany) {
                sendTo = -1;
                gameViewModel.sendChat(message, sendTo);
            } else {
                for (int i = 0; i < 5; i++) {
                    if (sendToList[i] == true) {
                        gameViewModel.sendChat(message, i);
                    }
                }
            }
            txt_ChatMessage.setText("");
        }


    }


    /**
     * checkForValidMessage()
     * Method checks whether the chat message is valid for sending or not.
     * @return boolean
     * @author Thomas Richter
     */
    public boolean checkForValidMessage() {
        if ((txt_ChatMessage.textProperty().getValue().trim().isEmpty())) {
            btn_SendChat.setDisable(true);
            return true;
        } else {
            btn_SendChat.setDisable(false);
            return false;
        }
    }


    /**
     * openHistory(MouseEvent)
     * MouseEvent which opens the GUI Layer of the Activation Phase.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void openHistory(MouseEvent mouseEvent) {
        gameViewModel.visibleStats.setValue(false);
        gameViewModel.visibleCardsYouGotNow.setValue(false);
        gameViewModel.visibleDrawDamage.setValue(false);
        gameViewModel.visiblePhase3.setValue(true);

        gameViewModel.resetDrawDamage();
        gameViewModel.resetCardsYouGotNow();
    }

    /**
     * openHistory(MouseEvent)
     * MouseEvent which initiates the PlayIt protocoll message
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void playIt(MouseEvent mouseEvent) {
        gameViewModel.submitPlayIt();
        gameViewModel.resetCardsYouGotNow();
    }

    /**
     * openStats(MouseEvent)
     * MouseEvent which opens the GUI Layer of player stats.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void openStats(MouseEvent mouseEvent) {
        gameViewModel.visiblePhase3.setValue(false);
        gameViewModel.visibleCardsYouGotNow.setValue(false);
        gameViewModel.visibleDrawDamage.setValue(false);
        gameViewModel.visibleStats.setValue(true);

        gameViewModel.resetDrawDamage();
        gameViewModel.resetCardsYouGotNow();
    }

    /**
     * nextPlayer(MouseEvent)
     * MouseEvent which initiates the switch to get the next player stats to display them in the GUI Layer of player stats.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void nextPlayer(MouseEvent mouseEvent) {

        gameViewModel.getNextPlayerStats();


    }

    /**
     * previousPlayer(MouseEvent)
     * MouseEvent which initiates the switch to get the previous player stats to display them in the GUI Layer of player stats.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void previousPlayer(MouseEvent mouseEvent) {

        gameViewModel.getPreviousPlayerStats();

    }


    /**
     * closeStats(MouseEvent)
     * MouseEvent which closes the GUI Layer of player stats.
     * @author Thomas Richter
     * @param mouseEvent
     */
    public void closeStats(MouseEvent mouseEvent) {
        pane_Stats.setVisible(false);
    }

    /**
     * selectVirus(MouseEvent)
     * MouseEvent which initiates the selection of the virus damage card.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void selectVirus(MouseEvent mouseEvent) {
        gameViewModel.selectDamage("Virus");
    }

    /**
     * selectWorm(MouseEvent)
     * MouseEvent which initiates the selection of the Worm damage card.
     * @author Thomas Richter
     * @param mouseEvent
     */
    public void selectWorm(MouseEvent mouseEvent) {
        gameViewModel.selectDamage("Worm");
    }

    /**
     * selectTrojan(MouseEvent)
     * MouseEvent which initiates the selection of the Trojan damage card.
     * @author Thomas Richter
     * @param mouseEvent
     */
    public void selectTrojan(MouseEvent mouseEvent) {
        gameViewModel.selectDamage("Trojan");
    }

    /**
     * closeCardsYouGotNow(MouseEvent)
     * MouseEvent which closes the GUI Layer that shows your the cardsYouGotNow after the timer got the player.
     * @param mouseEvent
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void closeCardsYouGotNow(MouseEvent mouseEvent) {
        gameViewModel.visibleCardsYouGotNow.setValue(false);
        gameViewModel.resetCardsYouGotNow();
    }

    /**
     * closeDrawDamage(MouseEvent)
     * MouseEvent which closes the GUI Layer that shows the Damage Cards the player drew.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void closeDrawDamage(MouseEvent mouseEvent) {
        gameViewModel.visibleDrawDamage.setValue(false);
        gameViewModel.resetDrawDamage();
    }


}
