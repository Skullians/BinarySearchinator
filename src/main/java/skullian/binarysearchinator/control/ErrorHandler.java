package skullian.binarysearchinator.control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import skullian.binarysearchinator.MainApp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ErrorHandler implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField errorField;
    @FXML
    private Hyperlink issues_hyperlink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void issues_hyperlink(ActionEvent event) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(java.net.URI.create("https://github.com/Skullians/BinarySearchinator/issues"));
        } catch (IOException error) {
            LOGGER.severe("An error occured when trying to display a URL. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    public void setErrorMessage(BorderPane pane) {
        try {
            Parent root;
            root = FXMLLoader.load(ErrorHandler.class.getResource("/skullian/binarysearchinator/fxml/error.fxml"));
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            LOGGER.severe("COPY AND PASTE THE ENTIRE LOG");
        } catch (Exception error){
            LOGGER.severe("An error occured when trying to display the error page [" + error + "]. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }
}
