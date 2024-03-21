package skullian.binarysearchinator.utility.jar.parsing;

import org.yaml.snakeyaml.Yaml;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;
import skullian.binarysearchinator.utility.jar.Extractor;
import skullian.binarysearchinator.utility.jar.data.DataManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class ParseYAML {
    private static Logger LOGGER = MainApp.LOGGER;

    public static void parseYAML(String output, String processed) {
        Yaml yaml = new Yaml();
        File target = new File(output, "plugin.yml");
        
        try (FileReader in = new FileReader(target, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(in)) {
            Map<String, Object> data = yaml.load(reader);
            String fullName = (String) data.get("name");

            DataManager.addFileStorage(processed, fullName);
            DataManager.addYAMLStorage(fullName, data);

            DependencyParsing.parseDependencies("YAML");
        } catch (IOException error) {
            ErrorHandler.error = "An unexpected error occured when parsing YAML files: \n" + error;
            ErrorHandler.setErrorMessage(Extractor.pane);
            LOGGER.severe("An unexpected error occured when parsing YAML files.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
            error.printStackTrace();
        }
    }
}
