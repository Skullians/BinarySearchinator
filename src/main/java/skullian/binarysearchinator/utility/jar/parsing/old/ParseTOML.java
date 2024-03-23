package skullian.binarysearchinator.utility.jar.parsing.old;

import com.moandjiezana.toml.Toml;
import skullian.binarysearchinator.utility.jar.data.DataManager;

import java.io.File;

public class ParseTOML {

    public static void parseTOML(String output, String processed) {
        Toml toml = new Toml().read(new File(output + "/mods.toml"));

        String fullName = toml.getString("displayName");

        DataManager.addFileStorage(processed, fullName);
        DataManager.addTOMLStorage(fullName, toml);

        DependencyParsing.parseDependencies("TOML");

        if (toml.containsTable("mods") && toml.getTable("mods").contains("modId")) {
            DataManager.addModID(fullName, toml.getTable("mods").getString("modId"));
        } else if (toml.contains("modId")) {
            DataManager.addModID(fullName, toml.getString("modId"));
        }
    }
}
