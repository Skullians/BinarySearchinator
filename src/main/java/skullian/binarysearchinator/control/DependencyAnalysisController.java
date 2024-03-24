package skullian.binarysearchinator.control;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.SearchinatorApp;
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.database.ConfigModel;
import skullian.binarysearchinator.utility.jar.JarManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DependencyAnalysisController implements Initializable {
    private static final Logger LOGGER = MainApp.LOGGER;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField extractingField; // Display of what jarfile is being extracted
    @FXML
    private TextField dependenciesField; // Display of analysed dependencies

    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Label jarCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JarManager.pane = borderPane; // This is to provide the current borderPane so that if an error occurrs in the Extractor the error can be displayed.

        rotateAnimation(c1, true, 360, 5);
        rotateAnimation(c2, true, 270, 7);

        ExecutorService dependencyService = Executors.newSingleThreadExecutor();
        dependencyService.submit(() -> {
            beginDependencyAnalysis();
        });

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            Platform.runLater(() -> {
                jarCount.setText(JarManager.count);
                extractingField.setText(JarManager.processing);
                dependenciesField.setText(Arrays.toString(JarManager.dependencies));
                if (JarManager.completed) {
                    executorService.shutdown();
                }
            });
        }; // Update the fields every 1ms
        executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.MILLISECONDS);

        SearchinatorApp.SearchinatorStage.setOnCloseRequest(event -> {
            executorService.shutdown(); // Shutdown the services so it can close down.
            dependencyService.shutdown();
        });
    }

    private void rotateAnimation(Circle circle, boolean reverse, int angle, int duration) {
        RotateTransition animation = new RotateTransition(Duration.seconds(duration), circle);

        animation.setByAngle(angle);
        animation.setAutoReverse(reverse);
        animation.setDelay(Duration.seconds(0));
        animation.setCycleCount(18);
        animation.play();
    }

    private void beginDependencyAnalysis() {
        try {
            ConfigModel configModel = MainApp.database.getConfigsIfExists(MainController.jarDirectory);
            if (configModel == null) {
                ErrorHandler.error = "No configs were found in the database.";
                ErrorHandler.setErrorMessage(borderPane);
                return;
            }
            System.out.println(configModel.getJarType());
            switch (configModel.getJarType()) {
                case "Forge / NeoForged Mod":
                    LOGGER.info("Extracting Forge / NeoForged Mods");
                    JarManager.extractForgeMods(MainController.jarDirectory, MainController.temporaryDirectory);
                    break;
                case "Plugin":
                    LOGGER.info("Extracting Spigot / Paper Plugins");
                    JarManager.extractPlugins(MainController.jarDirectory, MainController.temporaryDirectory);
                    break;
                case "Fabric Mod":
                    LOGGER.info("Extracting Fabric Mods");
                    JarManager.extractFabricOrQuiltMods(MainController.jarDirectory, MainController.temporaryDirectory, "fabric.mod.json");
                    break;
                case "Quilt Mod":
                    LOGGER.info("Extracting Quilt Mods");
                    JarManager.extractFabricOrQuiltMods(MainController.jarDirectory, MainController.temporaryDirectory, "quilt.mod.json");
                    break;
            }
        } catch (SQLException error) {
            ErrorHandler.error = "An error occurred when querying the database: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(borderPane);
            LOGGER.severe("An error occurred when querying the database: \n" + error.getMessage());
            error.printStackTrace();
        }
    }
}
