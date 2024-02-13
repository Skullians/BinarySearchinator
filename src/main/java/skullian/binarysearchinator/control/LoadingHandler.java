package skullian.binarysearchinator.control;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.util.jar.Extractor;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoadingHandler implements Initializable {
    private static Logger LOGGER = MainApp.LOGGER;
    private ConfirmationHandler confirmationHandler = new ConfirmationHandler();

    @FXML
    private BorderPane borderPane;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Extractor.pane = borderPane;
        setRotate(c1, true, 360, 5);
        setRotate(c2, true, 270, 7);

        confirmationHandler.userConfirmation(borderPane);
    }

    private void setRotate(Circle c, boolean reverse, int angle, int duration) {
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setByAngle(angle);
        rt.setAutoReverse(reverse);
        rt.setDelay(Duration.seconds(0));
        rt.setCycleCount(18);
        rt.play();
    }
}
