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
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.jar.JarManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ConfigConfirmationHandler implements Initializable {
    // ------------------------ RANDOM SHIT ------------------------ //

    private final static Logger LOGGER = MainApp.LOGGER;
    JarManager jarManager = new JarManager();

    String jarType = null;
    public Button selectedButton;
    public Button jarTypeModificationButton;

    // ------------------------ FXML ELEMENTS ------------------------ //

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField dirField;
    @FXML
    private TextField tempField;
    @FXML
    private TextField jarTypeField;

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

    // ------------------------ INIT ------------------------ //

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dirField.setText(MainController.jarDirectory);
        tempField.setText(MainController.temporaryDirectory);

        jarType = jarManager.getType(MainController.jarDirectory, MainController.temporaryDirectory);
        if (jarType == null) {
            ErrorHandler.error = "Jar Type returned null\n\nAre you sure you specified the correct mod / plugin directory?";
            ErrorHandler.setErrorMessage(borderPane);
            return;
        }
        jarTypeField.setText(jarType);

        switch (jarType) {
            case "Plugin":
                selectedButton = pluginType;
                jarType = "Plugin";
                pluginType.setStyle("-fx-background-radius: 1em");
                pluginType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Fabric Mod":
                jarType = "Fabric Mod";
                selectedButton = fabricType;
                fabricType.setStyle("-fx-background-radius: 1em");
                fabricType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Forge / NeoForged Mod":
                selectedButton = forgeType;
                jarType = "Forge";
                forgeType.setStyle("-fx-background-radius: 1em");
                forgeType.setStyle("-fx-background-color: #165DDB");
                break;
            case "Quilt Mod":
                jarType = "Quilt";
                selectedButton = quiltType;
                quiltType.setStyle("-fx-background-radius: 1em");
                quiltType.setStyle("-fx-background-color: #165DDB");
                break;
        }
    }

    public void userConfirmation(BorderPane bp) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/binsearch/typeconf.fxml"));
            bp.getChildren().removeAll();
            bp.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to transition to Page [binsearch/typeconf.fxml]: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(bp);
            LOGGER.severe("An error occurred when trying to transition to Page [binsearch/typeconf.fxml]: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    // ------------------------ ELEMENT HANDLING ------------------------ //

    @FXML
    void confirmChoices(MouseEvent event) {
        try {
            MainApp.database.registerConfigs(dirField.getText(), jarType);

            loadPage("binsearch/decom");
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to register configs in the database: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to register configs in the database: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    @FXML
    void rejectChoices(MouseEvent event) { loadPage("main"); }

    @FXML
    void openJarTypeSelectionPane(MouseEvent event) {
        jarTypeInformationPane.setOpacity(1);
        jarTypeInformationPane.setVisible(true);
        jarTypeInformationPane.setDisable(false);
    }

    @FXML
    void closeJarTypeSelectionPane(MouseEvent event) {
        jarTypeInformationPane.setOpacity(0);
        jarTypeInformationPane.setVisible(false);
        jarTypeInformationPane.setDisable(true);
    }

    @FXML
    void updateJarType(MouseEvent event) {
        if (event.getSource() != selectedButton) {
            selectedButton.setStyle("-fx-background-radius: 0.5em");
            selectedButton.setStyle("-fx-background-color: #282828");
        } else {
            selectedButton.setStyle("-fx-background-radius: 1em");
            selectedButton.setStyle("-fx-background-color: #165DDB");
        }
        jarTypeModificationButton = (Button) event.getSource();
        switch (jarTypeModificationButton.getText()) {

            case "Forge²":
                jarTypeField.setText("Forge / NeoForged Mod");
                jarType = "Forge / NeoForged Mod";
                break;
            case "Plugin¹":
                jarTypeField.setText("Plugin");
                jarType = "Plugin";
                break;
            case "Fabric":
                jarTypeField.setText("Fabric Mod");
                jarType = "Fabric Mod";
                break;
            case "QuiltMC":
                jarTypeField.setText("Quilt Mod");
                jarType = "Quilt Mod";
                break;
        }
    }

    // ------------------------ FXML PAGE LOADING ------------------------ //

    private void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/skullian/binarysearchinator/fxml/" + page + ".fxml"));
            borderPane.getChildren().removeAll();
            borderPane.getChildren().setAll(root);
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to change to page [" + page + ".fxml]: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when trying to change to page [" + page + ".fxml]: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

}
