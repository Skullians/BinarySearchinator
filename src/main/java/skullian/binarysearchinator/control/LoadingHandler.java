package skullian.binarysearchinator.control;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingHandler implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void play(MouseEvent event) {
        setRotate(c1, true, 360, 5);
        setRotate(c2, true, 270, 7);
    }

    private void setRotate(Circle c, boolean reverse, int angle, int duration) {
        System.out.println("ok");
        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setByAngle(angle);
        rt.setAutoReverse(reverse);
        rt.setDelay(Duration.seconds(0));
        rt.setCycleCount(18);
        rt.play();
    }

}
