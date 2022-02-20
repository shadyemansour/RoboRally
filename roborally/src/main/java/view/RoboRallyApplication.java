package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import model.ModelFactory;
import viewmodel.ViewModelFactory;
import networking.Client;

/**
 *  The RoboRallyApplication Class is responsible for launching the view, by starting the ViewHandler, and instantiation of the DataModel and ViewModels.
 */



public class RoboRallyApplication extends Application {

    public static networking.User player;


    /**
     * start(Stage)
     * The start Method instantiates
     *  - the DataModel, through the ModelFactory and hands over the reference to the player of type Client
     *  - the ViewModels, through the ViewModelFactory and hands over the reference to the player of type Client and the DataModel by referencing the ModelFactory
     *  - the ViewHandler, which gets a reference to the player of type Client and the ViewModels by referencing the ModelFactory
     *
     *  Additionally the ViewHandler, thus the GUI gets launched.
     *
     * @param stage
     * @author Thomas Richter
    */

    @Override
    public void start (Stage stage) throws Exception {

        ModelFactory modelFactory = new ModelFactory(player);
        ViewModelFactory viewModelFactory = new ViewModelFactory(modelFactory, player);
        ViewHandler viewHandler = new ViewHandler(stage, viewModelFactory, player);
        Image icon = new Image(getClass().getResourceAsStream("/GameStartView-Images/RoboRally.gif"));
        stage.getIcons().add(icon);
        viewHandler.start(stage);
    }
}
