package skullian.binarysearchinator.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.util.jar.Extractor;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ConfirmationHandler implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;
    Extractor extractor = new Extractor();

    public String jtype = "n/a";

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField dirField;
    @FXML
    private TextField tempField;
    @FXML
    private TextField jarTypeField;

    // -- types -- //
    @FXML
    private Button pluginType;
    @FXML
    private Button forgeType;
    @FXML
    private Button fabricType;
    @FXML
    private Button quiltType;
    @FXML
    private Pane jarTypeInformationPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dirField.setText(SidebarController.jarDir);
        tempField.setText(SidebarController.tempDir);

        jtype = extractor.getType(SidebarController.jarDir, SidebarController.tempDir);
        if (jtype == null) {
            ErrorHandler.setErrorMessage(borderPane);
            return;
        }
        jarTypeField.setText(jtype);

        switch (jtype) {
            case "Plugin":
                pluginType.setStyle("-fx-background-radius: 1em");
                pluginType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Fabric Mod":
                fabricType.setStyle("-fx-background-radius: 1em");
                fabricType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Forge / NeoForged Mod":
                forgeType.setStyle("-fx-background-radius: 1em");
                forgeType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Quilt Mod":
                quiltType.setStyle("-fx-background-radius: 1em");
                quiltType.setStyle("-fx-background-color: #165DDB");
                break;
        }
    }

    public void userConfirmation(BorderPane borderPane1) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/typeconf.fxml"));
            borderPane1.getChildren().removeAll();
            borderPane1.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when trying to change to page [binsearch/typeconf.fmxl]: \n" + error;
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An unexpected error occured when trying to change to page [binsearch/typeconf.fxml]. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    @FXML
    void confirmChoices(MouseEvent event) {

    }

    @FXML
    void rejectChoices(MouseEvent event) {

    }

    @FXML
    void openTypePane(MouseEvent event) {
        jarTypeInformationPane.setOpacity(1);
        jarTypeInformationPane.setVisible(true);
        jarTypeInformationPane.setDisable(false);
    }

    @FXML
    void closeTypePane(MouseEvent event) {
        jarTypeInformationPane.setOpacity(0);
        jarTypeInformationPane.setVisible(false);
        jarTypeInformationPane.setDisable(true);
    }
}
