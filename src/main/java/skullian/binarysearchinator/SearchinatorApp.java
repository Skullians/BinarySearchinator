package skullian.binarysearchinator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SearchinatorApp extends Application {
    double x,y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Image image = new Image("https://raw.githubusercontent.com/Skullians/BinarySearchinator/main/icon.jpg");

        stage.getIcons().add(image);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Binary Searchinator");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}