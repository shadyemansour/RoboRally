package viewmodel;

import javafx.beans.property.*;
import model.DataModel;
import model.DataModelManager;
import networking.Server;
import networking.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ConnectionViewController;

public class ConnectionViewModel {

    private static String classname = "ConnectionViewModel- ";

    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");
    private static User player;
    public DataModelManager dataModel;
    public BooleanProperty isAI;

    public ObjectProperty<Boolean> connectedToServer = new SimpleObjectProperty<>();

    public BooleanProperty showError = new SimpleBooleanProperty();
    public StringProperty errorText = new SimpleStringProperty();

    private ConnectionViewController connectionViewController;


    /**
     * ConnectionViewModel(DataModel, User)
     * Constructor of ConnectionViewModel
     * @param dataModel
     * @param player
     * @author Thomas Richter
     */
    public ConnectionViewModel(DataModel dataModel, User player) {
        this.player = player;
        this.dataModel = (DataModelManager) dataModel;
        ((DataModelManager) dataModel).setConnectionViewModel(this);
        isAI = new SimpleBooleanProperty(false);
        connectedToServer = new SimpleObjectProperty<>();
        this.connectedToServer = new SimpleObjectProperty<>(false);
        this.errorText = new SimpleStringProperty("");
    }

    /**
     * connectToServer()
     * This method initiates the connection to Server
     * @author Thomas Richter
     */
    public void connectToServer() {
        logger.debug(classname + " connectToServer() called.");
        dataModel.isAI = isAI.getValue();
        player.connect(isAI.getValue());
    }

    /**
     * updateViewModel()
     * This method is called at the end of every ReadThread method
     * which implements the Server-Client communication protocol messages,
     * by calling the updateViewModels method of the DataModelManager
     * These methods of ReadThread manipulate the Data of the DataModelManager.
     * Therefore after the manipulation the ConnectionViewModel has to fetch the updates.
     * This is realized with the updateViewModel method.
     * @author Thomas Richter
     */
    public void updateViewModel () {
        logger.debug(classname + " updateViewModel() called.");
        if (!(player.getConnectionAccepted() == null)) {
            if (player.getConnectionAccepted()) {
                connectedToServer.setValue(true);
            } else {
                connectedToServer.setValue(false);
            }
        }

        errorText.setValue(dataModel.chatHistory);
        if (!(errorText.getValue().isEmpty())) {
            showError.setValue(true);
        } else if (errorText.getValue().isEmpty()) {
            showError.setValue(false);
        }
    }

    /**
     * deleteError()
     * This method resets the error Message by resetting the chat History. At this stage of the app the only value of the chat History can be an error Message, therefore this action is valid.
     * @author Thomas Richter
     */
    public void deleteError() {
        dataModel.chatHistory = "";
        dataModel.updateViewModels();
    }

}
