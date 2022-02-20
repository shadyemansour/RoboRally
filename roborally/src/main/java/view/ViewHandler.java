package view;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import viewmodel.ViewModelFactory;
import networking.User;

import java.io.IOException;

/**
 *  The ViewHandler is responsible for displaying the Data through the various Views to the GUI.
 *  The Class additionally deals with displaying the different Views to the GUI.
 */

public class ViewHandler extends Application {

    private ViewModelFactory viewModelFactory;
    private User player;
    private Stage stage;

    /**
     * ViewHandler(Stage, ViewModelFactory, User)
     * Constructor of ViewHandler
     * @param stage
     * @param viewModelFactory
     * @param player
     * @author Thomas Richter
     */
    public ViewHandler(Stage stage, ViewModelFactory viewModelFactory, User player) {
        this.stage = stage;
        this.viewModelFactory = viewModelFactory;
        this.player = player;
    }


    /**
     * start(Stage)
     * Opens the first view, the connectionView
     * @param stage
     * @throws Exception
     * @author Thomas Richter
     */
    @Override
    public void start(Stage stage) throws Exception {
        openView("connection");
    }

    /**
     * openView(String)
     * Which View should be displayed to the player is determined here based on the value of the viewToOpen String
     * @param viewToOpen
     * @throws IOException
     * @author Thomas Richter
     */
    public void openView(String viewToOpen) throws IOException {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;

        loader.setLocation(getClass().getResource("/" + viewToOpen + "View.fxml"));
        root = loader.load();

        if (viewToOpen.toLowerCase().equals("connection")) {
            ConnectionViewController viewController = loader.getController();
            viewController.init(viewModelFactory.getConnectionViewModel(), this);
            stage.setTitle("Welcome to RoboRally!");
        } else if (viewToOpen.toLowerCase().equals("gamestart")) {
            GameStartViewController viewController = loader.getController();
            viewController.init(viewModelFactory.getGameStartViewModel(), this);
            stage.setTitle("RoboRally");
        } else if (viewToOpen.toLowerCase().equals("game")) {
            GameViewController viewController = loader.getController();
            viewController.init(viewModelFactory.getGameViewModel(), this);
            stage.setTitle("RoboRally");
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(1248);
        stage.setMinHeight(702);
        stage.setWidth(1290);
        stage.setHeight(720);
        stage.show();
    }

    // Getter and Setter methods
    public Stage getStage() { return stage; }
}








