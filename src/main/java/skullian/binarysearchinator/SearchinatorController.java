package skullian.binarysearchinator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchinatorController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane anchorPane;

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

    private void loadPage(String page) {
        try {
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

}