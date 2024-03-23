package skullian.binarysearchinator.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.database.ConfigModel;
import skullian.binarysearchinator.utility.database.HikariDataHandler;
import skullian.binarysearchinator.utility.jar.Extractor;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {
    // ------------------------ RANDOM SHIT ------------------------ //

    private static final Logger LOGGER = MainApp.LOGGER;
    private ErrorHandler errorHandler = new ErrorHandler();

    public static String jarDirectory = null;
    public static String temporaryDirectory = null;

    // ------------------------ FXML ELEMENTS ------------------------ //
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField dirField; // Field where the path to the mods / plugins folder is displayed
    @FXML
    private TextField tempField; // Field where the temporary path to the mods / plugins folder is dispayed.

    @FXML
    private Pane jarPathInformationPane; // The information frame that comes up when you click the Information button for the jarDirectory.
    @FXML
    private Pane tempPathInformationPane; // The information frame that comes up when you click the Information button for the temporaryDirectory.

    @FXML
    private Button continueButton; // Confirmation Button

    // ------------------------ FXML INIT ------------------------ //
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Nothing here.
    }

    // ------------------------ SIDEBAR CONTROLLER ------------------------ //

    @FXML
    void switchToAbout(MouseEvent event) { loadPage("about"); } // When clicking the "about" button on the sidebar it will switch to the about page.

    @FXML
    void switchToMenu(MouseEvent event) { loadPage("main"); } // When clicking the "main" button on the sidebar it will switch to the main page.

    @FXML
    void switchToHelp(MouseEvent event) { loadPage("help"); } // When clicking the "help" button on the sidebar it will switch to the help page.

    // ------------------------ FILE SELECTION ------------------------ //

    @FXML
    void promptJarDirectoryChoice(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        DirectoryChooser selectionPopup = new DirectoryChooser(); // Set up the directory popup.
        selectionPopup.setTitle("Select Folder Path"); // Set the title of the popup window.
        // selectionPopup.setInitialDirectory(new File("C:\\")); I'm going to remove this, as this would only really work for Windows.

        File directory = selectionPopup.showDialog(stage); // Shows the popup.

        if (directory != null) { // Safety!!
            dirField.setText(directory.toString());
            tempField.setText(directory.toString() + "\\.temp"); // Automatically fill in the temporary field to it's default. This can be changed if they wish
        }
    }

    @FXML
    void promptTemporaryDirectoryChoice(MouseEvent event) {
        Node source  = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        DirectoryChooser selectionPopup = new DirectoryChooser();
        selectionPopup.setTitle("Select Folder Path");

        File directory = selectionPopup.showDialog(stage);

        if (directory != null) {
            tempField.setText(directory.toString());
        }
     }

    // ------------------------ CONFIRMATION ------------------------ //

    @FXML
    void continueConfirmation(MouseEvent event) {
        try {
            jarDirectory = dirField.getText();
            temporaryDirectory = tempField.getText();

            Extractor.createTempDirectory(temporaryDirectory);

            ConfigModel configModel = initialiseDatabase(jarDirectory, temporaryDirectory);

            if (configModel != null) {
                Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/resume_confirmation.fxml"));
                borderPane.getChildren().removeAll();
                borderPane.getChildren().setAll(root);
            } else {
                Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/searching.fxml"));
                borderPane.getChildren().removeAll();
                borderPane.getChildren().setAll(root);
            }

        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to transition to Page [binsearch/searching.fxml]: \n" + error.getMessage();
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to transition to Page [binsearch/searching.fxml]: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    private ConfigModel initialiseDatabase(String jarDirectory, String temporaryDirectory) {
        try {
            Extractor.pane = borderPane;

            MainApp.database = new HikariDataHandler();
            MainApp.database.initialiseDatabase(temporaryDirectory);

            ConfigModel configModel = MainApp.database.getConfigsIfExists(jarDirectory);
            return configModel;

        } catch (SQLException error) {
            ErrorHandler.error = "An error occurred when trying to initialise the database: \n" + error.getMessage();
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to initialise the database: \n" + error.getMessage());
            error.printStackTrace();
        }
        return null;
    }

    // ------------------------ INFORMATION PANES ------------------------ //

    @FXML
    void openJarDirectoryInformationPane(MouseEvent event) {
        jarPathInformationPane.setOpacity(1); // Self-explanatory
        jarPathInformationPane.setVisible(true);
        jarPathInformationPane.setDisable(false);
    }

    @FXML
    void openTemporaryDirectoryInformationPane(MouseEvent event) {
        tempPathInformationPane.setOpacity(1);
        tempPathInformationPane.setVisible(true);
        tempPathInformationPane.setDisable(false);
    }

    @FXML
    void closeJarDirectoryInformationPane(MouseEvent event) {
        jarPathInformationPane.setOpacity(0);
        jarPathInformationPane.setVisible(false);
        jarPathInformationPane.setDisable(true);
    }

    @FXML
    void closeTemporaryDirectoryInformationPane(MouseEvent event) {
        tempPathInformationPane.setOpacity(0);
        tempPathInformationPane.setVisible(false);
        tempPathInformationPane.setDisable(true);
    }

    // ------------------------ PAGE LOADING ------------------------ //

    private void loadPage(String pageName) {
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
