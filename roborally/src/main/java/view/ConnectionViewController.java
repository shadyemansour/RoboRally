package view;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import viewmodel.ConnectionViewModel;

/**
 * Controller of the ConnectionView
 */

public class ConnectionViewController {

    private Logger logger = LogManager.getLogger("org.kursivekationen.roborally.GUI");

    @FXML
    private BorderPane root_borderPane;

    @FXML
    ImageView img_Background;

    @FXML
    private CheckBox cb_AI;

    @FXML
    private Button btn_ConnectToServer;

    @FXML
    private TextField txt_Error;
    @FXML
    private Button btn_CloseError;




    private String nextScene = "gameStart";
    private ViewHandler viewHandler;
    private ConnectionViewModel connectionViewModel;

    public ObjectProperty<Boolean> connectedToServer = new SimpleObjectProperty<>();
    public Boolean connectionButtonPressed;


    final ChangeListener<Number> resizeWidthListener = new ChangeListener<Number>()
    {
        /**
         * Listener for resizing the Background gif
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
            if (connectedToServer.getValue() && connectionButtonPressed) {
                try {
                    viewHandler.openView(nextScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                connectedToServer.setValue(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connection failed");
                alert.setHeaderText("The connection to the Server failed");
                alert.setContentText("An invalid protocol version or downtime of server might be the reason");
                alert.showAndWait();
            }
        }
    };

    /**
     * init(ConnectionViewModel, ViewHandler)
     * Method initializes the variables and binds it to according ConnectionViewModel variables.
     * @param connectionViewModel
     * @param viewHandler
     * @author Thomas Richter
     */

    public void init(ConnectionViewModel connectionViewModel, ViewHandler viewHandler) {
        this.connectionViewModel = connectionViewModel;
        this.viewHandler = viewHandler;
        this.connectionButtonPressed = false;
        this.connectedToServer.bindBidirectional(connectionViewModel.connectedToServer);

        btn_CloseError.visibleProperty().bindBidirectional(connectionViewModel.showError);

        txt_Error.visibleProperty().bindBidirectional(connectionViewModel.showError);
        txt_Error.textProperty().bindBidirectional(connectionViewModel.errorText);

        connectedToServer.addListener(openNextView);
        root_borderPane.widthProperty().addListener(resizeWidthListener);

        cb_AI.selectedProperty().bindBidirectional(connectionViewModel.isAI);

    }

    /**
     * connectToServer(MouseEvent)
     * MouseEvent method which calls the ConnectionViewModel method to connect to Server
     * @param mouseEvent
     * @throws IOException
     * @author Thomas Richter
     */
    public void connectToServer(MouseEvent mouseEvent) throws IOException {
        connectionButtonPressed = true;
        connectionViewModel.connectToServer();
        }


    /**
     * closeError(MouseEvent)
     * MouseEvent method which closes the error Message and resets necessary data.
     * @param mouseEvent
     * @author Thomas Richter
     */
    public void closeError(MouseEvent mouseEvent) {
            connectionViewModel.showError.setValue(false);
            connectionViewModel.deleteError();
    }
}
