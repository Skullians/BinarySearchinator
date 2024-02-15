package skullian.binarysearchinator.util.jar.parsing;

import org.yaml.snakeyaml.Yaml;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;
import skullian.binarysearchinator.util.jar.Extractor;
import skullian.binarysearchinator.util.jar.data.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class ParseYAML {
    private static Logger LOGGER = MainApp.LOGGER;

    public static void parseYAML(String output, String processed) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = ParseYAML.class.getClassLoader().getResourceAsStream(output + "\\plugin.yml")) {
            Map<String, Object> data = yaml.load(inputStream);
            String fullName = (String) data.get("name");

            DataManager.addFileStorage(processed, fullName);
            DataManager.addYAMLStorage(fullName, data);
        } catch (IOException error) {
            ErrorHandler.error = "An unexpected error occured when parsing YAML files: \n" + error;
            ErrorHandler.setErrorMessage(Extractor.pane);
            LOGGER.severe("An unexpected error occured when parsing YAML files.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }
}
