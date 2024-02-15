package skullian.binarysearchinator.util.jar.parsing;

import org.json.JSONObject;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;
import skullian.binarysearchinator.util.jar.Extractor;
import skullian.binarysearchinator.util.jar.data.DataManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class ParseJSON {
    private static Logger LOGGER = MainApp.LOGGER;

    public static void parseJSON(String output, String processed, String filename) {
        try {
            FileReader reader = new FileReader(output + "\\" + filename);
            JSONObject jsonObject = new JSONObject(reader);

            String fullName = jsonObject.getString("name");

            DataManager.addFileStorage(processed, fullName);
            DataManager.addJSONStorage(fullName, jsonObject);
        } catch (IOException error) {
            ErrorHandler.error = "An unexpected error occured when parsing JSON files: \n" + error;
            ErrorHandler.setErrorMessage(Extractor.pane);
            LOGGER.severe("An unexpected error occured when parsing JSON files.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }
}
