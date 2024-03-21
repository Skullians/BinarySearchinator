package skullian.binarysearchinator.utility.jar.parsing;

import com.google.gson.JsonObject;
import com.moandjiezana.toml.Toml;
import skullian.binarysearchinator.utility.jar.data.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyParsing {

    public static void parseDependencies(String type) {
        System.out.println("triggered");
        System.out.println(type);
        for (Map.Entry<String, String> entry : DataManager.fileStorage.entrySet()) {
            String jarName = entry.getKey();
            String parsedName = entry.getValue();

            if (type == "YAML") {
                dependenciesFromYAML(parsedName);
            } else if (type == "JSON") {

            } else if (type == "TOML") {

            }
        }
    }

    public static String[] dependenciesFromYAML(String name) {
        Map<String, Object> yamlStorage = DataManager.queryYAML(name);

        if (yamlStorage != null) {
            List<String> missingDependencies = new ArrayList<>();
            List<String> presentDependencies = new ArrayList<>();

            List<String> softDependencies = (List<String>) yamlStorage.get("softdepend");
            List<String> forceDependencies = (List<String>) yamlStorage.get("depend");

            if (forceDependencies != null) {
                for (String forceDependency : forceDependencies) {

                    Map<String, String> fileStorage = DataManager.fileStorage;

                    if (fileStoragePresent(forceDependency)) {
                        presentDependencies.add(forceDependency);
                    } else {
                        missingDependencies.add(forceDependency);
                    }
                }
            }

            if (softDependencies != null) {
                for (String softDependency : softDependencies) {
                    System.out.println(softDependency);
                    if (DataManager.queryFiles(softDependency) != null) {
                        presentDependencies.add(softDependency);
                    }
                }
            }

            DataManager.addMissingDependencies(name, missingDependencies);
            DataManager.addDependencies(name, presentDependencies);
        }
        return null;
    }

    public static String[] dependenciesFromJSON(String name) {
        JsonObject jsonStorage = DataManager.queryJSON(name);

        if (jsonStorage != null) {
            List<String> missingDependencies = new ArrayList<>();
            List<String> presentDependencies = new ArrayList<>();
        }
        return null;
    }

    public static String[] dependenciesFromTOML(String name) {
        Toml tomlStorage = DataManager.queryTOML(name);

        if (tomlStorage != null) {
            List<String> missingDependencies = new ArrayList<>();
            List<String> presentDependencies = new ArrayList<>();

            String modID = null;
            String displayName = null;

            if (tomlStorage.contains("modId") && tomlStorage.contains("displayName")) {
                modID = tomlStorage.getString("modId");
                displayName = tomlStorage.getString("displayName");
            } else if (tomlStorage.containsTable("mods") && tomlStorage.getTables("mods").contains("modId") && tomlStorage.getTables("mods").contains("displayName")) {
                modID = tomlStorage.getTable("mods").getString("modId");
                displayName = tomlStorage.getTable("mods").getString("displayName");
            }

            // developers can either do dependencies.ctov (assuming the modId was "ctov"), or do dependencies.${modId} which achieves the same result
            // we have to use these else-ifs to account for that
            if (tomlStorage.containsTable("dependencies." + modID)) {

                String finalModID = modID;
                tomlStorage.getTables("dependencies." + modID).forEach(table -> {
                    if (fileStoragePresent(tomlStorage.getTable("dependencies." + finalModID).getString("modId"))) {

                    }
                });

            } else if (tomlStorage.containsTable("dependencies.${modId}")) {

            }
         }
        return null;
    }

    public static boolean fileStoragePresent(String dependency) {
        Map<String, String> fileStorage = DataManager.fileStorage;

        for (Map.Entry<String, String> entry : fileStorage.entrySet()) {
            if (entry.getValue().toLowerCase() == dependency.toLowerCase()) {
                return true;
            }
        }

        return false;
    }
}
