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
import skullian.binarysearchinator.util.jar.Extractor;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DecomManager implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField extractingField;
    @FXML
    private TextField dependenciesField;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Label scannedCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Extractor.pane = borderPane;
        setRotate(c1, true, 360, 5);
        setRotate(c2, true, 270, 7);

        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            beginExtraction();
        });

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            Platform.runLater(() -> {
                scannedCount.setText(Extractor.count);
                extractingField.setText(Extractor.processing);
                dependenciesField.setText(Arrays.toString(Extractor.dependencies));
                if (Extractor.completed) { executorService.shutdown(); }
            });
        };
        executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.MILLISECONDS);
    }

    private void setRotate(Circle c, boolean reverse, int angle, int duration) {
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setByAngle(angle);
        rt.setAutoReverse(reverse);
        rt.setDelay(Duration.seconds(0));
        rt.setCycleCount(10000);
        rt.play();
    }

    private void beginExtraction() {
        Extractor.pane = borderPane;
        switch (ConfirmationHandler.jtype) {

            case "Forge":
                LOGGER.warning("Extracting Forge / NeoForge Mods.");
                Extractor.extractForgeMods(ConfirmationHandler.input, ConfirmationHandler.output);
                break;
            case "Plugin":
                LOGGER.warning("Extracting Spigot / Paper Plugins.");
                Extractor.extractPlugins(ConfirmationHandler.input, ConfirmationHandler.output);
                break;
            case "Fabric":
                LOGGER.warning("Extracting Fabric Mods.");
                Extractor.extractFabricQuiltMods(ConfirmationHandler.input, ConfirmationHandler.output, "fabric.mod.json");
                break;
            case "Quilt":
                LOGGER.warning("Extracting Quilt Mods.");
                Extractor.extractFabricQuiltMods(ConfirmationHandler.input, ConfirmationHandler.output, "quilt.mod.json");
                break;
        }
    }
}
