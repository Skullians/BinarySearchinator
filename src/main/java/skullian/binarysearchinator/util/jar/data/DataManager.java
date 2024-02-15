package skullian.binarysearchinator.util.jar.data;

import com.moandjiezana.toml.Toml;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static Map<String, String> fileStorage = new HashMap<>(); // file name, full name (e.g. viaversion.jar / ViaVersion)

    public static Map<String, Map<String, Object>> yamlStorage = new HashMap<>(); // full name, yaml object (e.g. ViaVersion / Object)
    public static Map<String, JSONObject> jsonStorage = new HashMap<>();
    public static Map<String, Toml> tomlStorage = new HashMap<>();

    public static void addFileStorage(String fileName, String parsedName) {
        fileStorage.put(fileName, parsedName);
    }

    public static void addYAMLStorage(String fullName, Map<String, Object> data) {
        yamlStorage.put(fullName, data);
    }
    public static void addJSONStorage(String fullName, JSONObject data) { jsonStorage.put(fullName, data); }
    public static void addTOMLStorage(String fullName, Toml data) { tomlStorage.put(fullName, data); }

    public static String queryFiles(String fileName) {
        if (fileStorage.containsKey(fileName)) {
            return fileStorage.get(fileName);
        }
        return null;
    }

    public static Map<String, Object> queryYAML(String fullName) {
        if (yamlStorage.containsKey(fullName)) {
            return yamlStorage.get(fullName);
        }
        return null;
    }

    public static JSONObject queryJSON(String fullName) {
        if (jsonStorage.containsKey(fullName)) {
            return jsonStorage.get(fullName);
        }
        return null;
    }

    public static Toml queryTOML(String fullName) {
        if (tomlStorage.containsKey(fullName)) {
            return tomlStorage.get(fullName);
        }
        return null;
    }
}
