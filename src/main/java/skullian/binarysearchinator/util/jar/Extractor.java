package skullian.binarysearchinator.util.jar;

import org.yaml.snakeyaml.Yaml;
import skullian.binarysearchinator.MainApp;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class Extractor {
    private static Logger LOGGER = MainApp.LOGGER;

    public static Map<String, Object> extractYML(String jarFilePath, String outputFilePath) {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            JarEntry pluginYmlEntry = jarFile.getJarEntry("plugin.yml");
            if (pluginYmlEntry != null) {
                try (InputStream inputStream = jarFile.getInputStream(pluginYmlEntry);
                     FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath))) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            }
        } catch (Exception error) {
            LOGGER.severe("An unexpected error occured when trying to extract the jarfile. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
            return null;
        }

        try (FileReader reader = new FileReader(outputFilePath)){
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(reader);
            File file = new File(outputFilePath);
            file.delete(); // Delete the file so that the next one can start
            return data;
        } catch (Exception error) {
            LOGGER.severe("An unexpected error occured when trying to parse the jarfile's plugin.yml. Please report this to https://github.com/Skullians/BinarySearchinator/issues.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
            return null;
        }
    }
}
