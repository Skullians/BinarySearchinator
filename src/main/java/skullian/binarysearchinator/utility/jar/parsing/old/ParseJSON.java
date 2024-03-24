package skullian.binarysearchinator.utility.jar.parsing.old;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.jar.JarManager;
import skullian.binarysearchinator.utility.jar.data.DataManager;

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

            DependencyParsing.parseDependencies("JSON");
        } catch (IOException error) {
            ErrorHandler.error = "An unexpected error occured when parsing JSON files: \n" + error;
            ErrorHandler.setErrorMessage(JarManager.pane);

            LOGGER.severe("An unexpected error occured when parsing JSON files.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }
}
