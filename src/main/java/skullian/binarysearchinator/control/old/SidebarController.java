package skullian.binarysearchinator.control.old;


import javafx.event.ActionEvent;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class SidebarController implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;
    private ErrorHandler errorHandler = new ErrorHandler();

    public static String jarDir = null;
    public static String tempDir = null;

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField dirField;
    @FXML
    private TextField tempField;
    @FXML
    private Pane jarPathInformationPane;
    @FXML
    private Pane tempPathInformationFrame;
    @FXML
    private Button contButton;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void issues_hyperlink(ActionEvent event) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(java.net.URI.create("https://github.com/Skullians/BinarySearchinator/issues"));
        } catch (IOException error) {
            ErrorHandler.error = "An error occured when trying to display a URL: \n" + error;
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occured when trying to display a URL. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void switchToAbout(MouseEvent event) {
        loadPage("about");
    }

    @FXML
    void switchToMenu(MouseEvent event) {
        loadPage("main");
    }

    @FXML
    void switchToHelp(MouseEvent event) {
        loadPage("help");
    }

    @FXML
    void promptDirChoice(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Folder Path");
        chooser.setInitialDirectory(new File("C:\\"));
        File directory = chooser.showDialog(stage);
        if (directory != null) {
            dirField.setText(directory.toString());
            tempField.setText(directory.toString() + "\\.temp");
        }
    }

    @FXML
    void promptTempChoice(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Folder Path");
        chooser.setInitialDirectory(new File("C:\\"));
        File directory = chooser.showDialog(stage);
        if (directory != null) {
            tempField.setText(directory.toString());
        }
    }


    private void loadPage(String page) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/" + page + ".fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when trying to change to page [" + page + ".fxml]: \n" + error;
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occured when trying to change to page [" + page + ".fxml]. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    @FXML
    void contInit(MouseEvent event) {
        try {
            jarDir = dirField.getText();
            tempDir = tempField.getText();
            Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/searching.fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when trying to transition to Page [binsearch/searching.fxml]: \n" + error;
            errorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occured when trying to transition to Page [binsearch/searching.fxml]: " + error);
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    // --- INFORMATION --- //

    @FXML
    void openJarDirInformation(MouseEvent event) {
        jarPathInformationPane.setOpacity(1);
        jarPathInformationPane.setVisible(true);
        jarPathInformationPane.setDisable(false);
    }

    @FXML
    void openTempDirInformation(MouseEvent event) {
        tempPathInformationFrame.setOpacity(1);
        tempPathInformationFrame.setVisible(true);
        tempPathInformationFrame.setDisable(false);
    }

    @FXML
    void closeJarDirInformation(MouseEvent event) {
        jarPathInformationPane.setOpacity(0);
        jarPathInformationPane.setVisible(false);
        jarPathInformationPane.setDisable(true);
    }

    @FXML
    void closeTempDirInformation(MouseEvent event) {
        tempPathInformationFrame.setOpacity(0);
        tempPathInformationFrame.setVisible(false);
        tempPathInformationFrame.setDisable(true);
    }
}