package skullian.binarysearchinator;

import org.yaml.snakeyaml.Yaml;
import skullian.binarysearchinator.jar.Extractor;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainApp {
    public static void main(String[] args) {
        /*try {
            System.out.println(Extractor.extractYML("", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        SearchinatorApp.main(args);
    }
}
