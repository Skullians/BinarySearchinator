package skullian.binarysearchinator.control;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.old.ConfirmationHandler;
import skullian.binarysearchinator.utility.jar.Extractor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JarAnalysisController implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;
    private ConfigConfirmationHandler confirmationHandler = new ConfigConfirmationHandler();

    @FXML
    private BorderPane borderPane;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Extractor.pane = borderPane;
        rotateAnimation(c1, true, 360, 5);
        rotateAnimation(c2, true, 270, 7);

        confirmationHandler.userConfirmation(borderPane);
    }

    private void rotateAnimation(Circle circle, boolean reverse, int angle, int duration) {
        RotateTransition animation = new RotateTransition(Duration.seconds(duration), circle);

        animation.setByAngle(angle);
        animation.setAutoReverse(reverse);
        animation.setDelay(Duration.seconds(0));
        animation.setCycleCount(18);
        animation.play();
    }
}
