package skullian.binarysearchinator.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.database.ConfigModel;
import skullian.binarysearchinator.utility.jar.Extractor;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class PreviousDataConfirmationController implements Initializable {

    private static final Logger LOGGER = MainApp.LOGGER;
    private ErrorHandler errorHandler = new ErrorHandler();

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField dirField;
    @FXML
    private TextField tempField;
    @FXML
    private TextField jarType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ConfigModel configModel = MainApp.database.getConfigsIfExists(MainController.jarDirectory);

            dirField.setText(MainController.jarDirectory);
            tempField.setText(MainController.temporaryDirectory);

            jarType.setText(configModel.getJarType());
        } catch (SQLException error) {
            ErrorHandler.error = "An error occurred when trying to query the database: \n" + error.getMessage();
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to query the database: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    @FXML
    void confirmContinuation(MouseEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/searching.fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);

        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to transition to Page [binsearch/searching.fxml]: \n" + error.getMessage();
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to transition to Page [binsearch/searching.fxml]: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    @FXML
    void restartSelection(MouseEvent event) { switchPage("main"); }

    private void switchPage(String pageName) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/" + pageName + ".fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to transition to Page [" + pageName + ".fxml]: \n" + error.getMessage();
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to transition to Page [" + pageName + ".fxml]: \n" + error.getMessage());
            error.printStackTrace();
        }
    }
}
