package skullian.binarysearchinator.util.jar.parsing;

import com.moandjiezana.toml.Toml;
import skullian.binarysearchinator.util.jar.data.DataManager;

import java.io.File;

public class ParseTOML {

    public static void parseTOML(String output, String processed) {
        Toml toml = new Toml().read(new File(output + "/mods.toml"));
        if (toml == null) {
            System.out.println("tf");
        }
        String fullName = toml.getString("displayName");
        DataManager.addFileStorage(processed, fullName);
        DataManager.addTOMLStorage(fullName, toml);
    }
}
