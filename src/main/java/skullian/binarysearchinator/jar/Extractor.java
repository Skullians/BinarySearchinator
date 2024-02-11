package skullian.binarysearchinator.jar;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Extractor {

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
            error.printStackTrace();
            return null;
        }

        try (FileReader reader = new FileReader(outputFilePath)){
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(reader);
            return data;
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }
}
