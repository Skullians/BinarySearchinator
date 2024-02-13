package skullian.binarysearchinator.util.jar;

import javafx.scene.layout.BorderPane;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Extractor {
    private static Logger LOGGER = MainApp.LOGGER;
    private ErrorHandler errorHandler = new ErrorHandler();

    public static BorderPane pane = null;

    public String getType(String input, String output) {
        try {
            if (!directoryExists(input) || input == "" || output == "") {
                ErrorHandler.error = "The mod / plugin folder you specified does not exist.\nDo not make an issue for this unless you are confident the directory exists.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            } else if (!filesExist(input)) {
                ErrorHandler.error = "Could not find any files present inside the mod / plugin folder you specified.\nDo not make an issue for this unless you are confident files exist within the directory.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            }

            Path jarFilePath = getRandomJarFile(input);
            List<String> toCheck = Arrays.asList("plugin.yml", "fabric.mod.json", "mods.toml", "quilt.mod.json");
            ZipFile jarFile = new ZipFile(jarFilePath.toFile());

            for (String fileName: toCheck) {
                ZipEntry entry = jarFile.getEntry(fileName);
                if (entry != null) {
                    if (fileName.equals("plugin.yml")) {
                        return "Plugin";
                    } else if (fileName.equals("fabric.mod.json")) {
                        return "Fabric Mod";
                    } else if (fileName.equals("mods.toml")) {
                        return "Forge / NeoForge Mod";
                    } else if (fileName.equals("quilt.mod.json")) {
                        return "Quilt Mod";
                    }
                }
            }
        } catch (Exception error) {
            ErrorHandler.error = "Failed to get jar type: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An unexpected error occured during getting the java type: \n");
            error.printStackTrace();
            return null;
        }

        return null;
    }

    private Path getRandomJarFile(String input) {
        try {
            Path folderPath = Paths.get(input);
            if (folderPath == null) {
                ErrorHandler.error = "Specified Folder Path is null.";
                ErrorHandler.setErrorMessage(pane);
                System.out.println("null");
                return null;
            }
            List<Path> jarFiles = new ArrayList<>();
            try (var stream = Files.list(folderPath)) {
                stream.filter(p -> p.toString().endsWith(".jar")).forEach(jarFiles::add);
            }
            if (!jarFiles.isEmpty()) {
                Random random = new Random();
                Path randomJarFile = jarFiles.get(random.nextInt(jarFiles.size()));

                if (randomJarFile == null) {
                    ErrorHandler.error = "Random Jar File (for analysis) returned null.";
                    ErrorHandler.setErrorMessage(pane);
                }
                return randomJarFile;
            } else {
                ErrorHandler.error = "The mod / plugin folder path specified was empty, or there were no jarfiles inside.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            }
        } catch (Exception error) {
            ErrorHandler.error = "Failed to get jar type: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An unexpected error occured during getting a random jar file for analysis: \n" + error);
            error.printStackTrace();
            return null;
        }
    }

    public boolean directoryExists(String path) {
        if (Files.exists(Paths.get(path))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean filesExist(String path) { // just a check to see if there is at least 1 file in the dir.
        try (var list = Files.list(Paths.get(path))) {
            if (list.findFirst().isPresent()) {
                return true;
            }
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when checking the mod / plugin folder's files: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            error.printStackTrace();
        }
        return false;
    }
}
