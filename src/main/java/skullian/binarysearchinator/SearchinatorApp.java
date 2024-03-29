package skullian.binarysearchinator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class SearchinatorApp extends Application {

    private static Logger LOGGER = MainApp.LOGGER;

    public static Stage SearchinatorStage;
    @Override
    public void start(Stage stage) throws IOException {
        LOGGER.info("Loading searching.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));

        Image image = new Image(String.valueOf(getClass().getResource("img/icon.jpg")));

        stage.getIcons().add(image);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Binary Searchinator");
        stage.show();
        LOGGER.info("FXML File loaded and displaying app");

        SearchinatorStage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}