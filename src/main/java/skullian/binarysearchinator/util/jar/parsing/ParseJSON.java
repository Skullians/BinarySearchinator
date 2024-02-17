package skullian.binarysearchinator.util.jar.parsing;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;
import skullian.binarysearchinator.util.jar.Extractor;
import skullian.binarysearchinator.util.jar.data.DataManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class ParseJSON {
    private static Logger LOGGER = MainApp.LOGGER;

    public static void parseJSON(String output, String processed, String filename) {
        File target = new File(output, filename);
        try (FileReader in = new FileReader(target, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(in)) {

            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            String fullName = jsonObject.get("name").getAsString();

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
