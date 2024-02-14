package skullian.binarysearchinator.util.jar.data;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static Map<String, String> fileStorage = new HashMap<>(); // file name, full name (e.g. viaversion.jar / ViaVersion)
    public static Map<String, Object> yamlStorage = new HashMap<>(); // full name, yaml object (e.g. ViaVersion / Object)

    public static void addFileStorage(String fileName, String parsedName) {
        fileStorage.put(fileName, parsedName);
    }

    public static void addYAMLStorage(String fullName, Object data) {
        yamlStorage.put(fullName, data);
    }

    public static String queryFiles(String fileName) {
        if (fileStorage.containsKey(fileName)) {
            return fileStorage.get(fileName);
        }
        return null;
    }

    public static Object queryYAML(String fullName) {
        if (yamlStorage.containsKey(fullName)) {
            return yamlStorage.get(fullName);
        }
        return null;
    }
}
