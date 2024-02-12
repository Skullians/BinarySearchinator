package skullian.binarysearchinator.control;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField dirField;

    @FXML
    private void issues_hyperlink(ActionEvent event) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(java.net.URI.create("https://github.com/Skullians/BinarySearchinator/issues"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        }
    }

    private void loadPage(String page) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/" + page + ".fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            LOGGER.severe("An error occured when trying to change to page [" + page + ".fxml. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

}