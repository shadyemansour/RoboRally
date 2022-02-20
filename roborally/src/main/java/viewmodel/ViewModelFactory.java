package viewmodel;

import model.ModelFactory;
import networking.User;


public class ViewModelFactory {

    public ConnectionViewModel connectionViewModel;
    public GameStartViewModel gameStartViewModel;
    public GameViewModel gameViewModel;

    public User player;

    /**
     * ViewModelFactory(ModelFactory, User)
     * This Constructor creates the ViewModelFactory which creates the various ViewModel instances.
     * @param modelFactory
     * @param player
     * @author Thomas Richter
     */
    public ViewModelFactory(ModelFactory modelFactory, User player) {
        connectionViewModel = new ConnectionViewModel(modelFactory.getDataModel(), player);
        gameStartViewModel = new GameStartViewModel(modelFactory.getDataModel(), player);
        gameViewModel = new GameViewModel(modelFactory.getDataModel(), player);
    }

    public ConnectionViewModel getConnectionViewModel () {
        return connectionViewModel;
    }

    public GameStartViewModel getGameStartViewModel () {
        return gameStartViewModel;
    }

    public GameViewModel getGameViewModel () {
        return gameViewModel;
    }

}
