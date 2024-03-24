package skullian.binarysearchinator.utility.jar;

import javafx.scene.layout.BorderPane;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.old.ErrorHandler;
import skullian.binarysearchinator.utility.jar.parsing.old.ParseYAML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarManager {
    private static final Logger LOGGER = MainApp.LOGGER;

    public static String processing = "Waiting..."; // which jar is being processed
    public static String[] dependencies; // dependencies array
    public static String count = "0 / 0"; // jarcount
    public static boolean completed = false; // if the extractor is finished

    public static BorderPane pane = null; // for error handling

    // ------------------------ 'GETTERS' ------------------------ //

    public String getType(String jarFolder, String tempFolder) { // Get the jartype
        try {
            if (!directoryExists(jarFolder) || jarFolder.equals("") || tempFolder.equals("")) {
                ErrorHandler.error = "The mod / plugin folder, or the temp folder you specified does not exist.\nDo not make an issue for this unless you are positive the directory exists.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            } else if (!fileExists(jarFolder)) {
                ErrorHandler.error = "The tool could not find any files present inside the mod / plugin folder you specified.\nDo not make an issue for this unless you are positive there are files within the directory.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            }

            Path jarFilePath = getRandomJarFile(jarFolder);
            ZipFile jarFile = new ZipFile(jarFilePath.toFile());
            Enumeration<? extends ZipEntry> jarFileEntries = jarFile.entries();
            while (jarFileEntries.hasMoreElements()) {
                ZipEntry jarEntry = jarFileEntries.nextElement();
                switch (jarEntry.getName()) {
                    case "mods.toml":
                        return "Forge / NeoForged Mod";
                    case "plugin.yml":
                        return "Plugin";
                    case "fabric.mod.json":
                        return "Fabric Mod";
                    case "quilt.mod.json":
                        return "Quild Mod";
                }
            }

        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to get the jar type: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred when trying to get the jar type: \n" + error.getMessage());
            error.printStackTrace();
            return null;
        }

        return null;
    }

    // ------------------------ 'SUB-GETTERS' ------------------------ //

    private Path getRandomJarFile(String jarDirectoryPath) {
        try {
            Path folder = Paths.get(jarDirectoryPath);
            List<Path> jarFiles = new ArrayList<>();
            try (var stream = Files.list(folder)) {
                stream.filter(file -> file.toString().endsWith(".jar")).forEach(jarFiles::add);
            }

            if (!jarFiles.isEmpty()) {
                Random random = new Random();
                Path randomJarFile = jarFiles.get(random.nextInt(jarFiles.size()));

                if (randomJarFile == null) {
                    ErrorHandler.error = "The random jar file (for analysis) returned null.";
                    ErrorHandler.setErrorMessage(pane);
                    return null;
                }
                return randomJarFile;
            } else {
                ErrorHandler.error = "The mod / plugin folder you specified was empty, or there were no jarfiles inside.";
                ErrorHandler.setErrorMessage(pane);
                return null;
            }
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when getting a random jarfile: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred when getting a random jarfile: \n" + error.getMessage());
            error.printStackTrace();
            return null;
        }
    }

    public static int getJarCount(String jarDirectoryPath) {
        try {
            File directory = new File(jarDirectoryPath);
            File[] jarFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            });
            return jarFiles.length;
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred when trying to get the amount of jarfile within the plugin / mod directory: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred when trying to get the amount of jarfile within the plugin / mod directory: \n" + error.getMessage());
            error.printStackTrace();
            return 0;
        }
    }

    // ------------------------ SAFETY CHECKS ------------------------ //

    public boolean directoryExists(String directoryPath) {
        if (Files.exists(Paths.get(directoryPath))) {
            return true;
        }
        return false;
    }

    public boolean fileExists(String directoryPath) {
        try (var list = Files.list(Paths.get(directoryPath))) {
            if (list.findFirst().isPresent()) {
                return true;
            }
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when checking whether files existed in [" + directoryPath + "]: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            error.printStackTrace();
            return false;
        }
        return false;
    }

    // ------------------------ DEPENDENCY EXTRACTORS ------------------------ //

    public static void extractPlugins(String jarDirectoryPath, String tempDirectoryPath) {
        int totalJarCount = getJarCount(jarDirectoryPath);
        count = "0 / " + totalJarCount;

        int processedJars = 0;

        try {
            File jarFolder = new File(jarDirectoryPath);
            File[] files = jarFolder.listFiles();

            for (File file: files) {
                if (file.getName().endsWith(".jar")) {
                    try (JarFile jarFile = new JarFile(file)) {
                        processing = file.getName();
                        JarEntry jarEntry = jarFile.getJarEntry("plugin.yml");

                        if (jarEntry != null) {
                            try (InputStream inputStream = jarFile.getInputStream(jarEntry);
                                 FileOutputStream outputStream = new FileOutputStream(new File(tempDirectoryPath + "/plugin.yml"))) {

                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }

                                // ParseYAML.parseYAML(tempDirectoryPath, file.getName());
                            }
                        }
                    }

                    processedJars++;
                    count = processedJars + " / " + totalJarCount;
                }
            }

            processing = "Done!";
            completed = true;
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred during extracting plugins: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred during extracting plugins: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    public static void extractFabricOrQuiltMods(String jarDirectoryPath, String tempDirectoryPath, String outputFile) { // since fabric and quilt both use the same JSON, but different names, I just decided to do this rather than have two different functions.
        int totalJarCount = getJarCount(jarDirectoryPath);
        count = "0 / " + totalJarCount;

        int processedJars = 0;

        try {
            File jarFolder = new File(jarDirectoryPath);
            File[] files = jarFolder.listFiles();

            for (File file: files) {
                if (file.getName().endsWith(".jar")) {
                    try (JarFile jarFile = new JarFile(file)) {
                        processing = file.getName();
                        JarEntry jarEntry = jarFile.getJarEntry(outputFile);

                        if (jarEntry != null) {
                            try (InputStream inputStream = jarFile.getInputStream(jarEntry);
                                 FileOutputStream outputStream = new FileOutputStream(new File(tempDirectoryPath + "/" + outputFile))) {

                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }

                                // ParseJSON.parseJson(output, file.getName(), desiredOutput);
                            }
                        }
                    }

                    processedJars++;
                    count = processedJars + " / " + totalJarCount;
                }
            }

            processing = "Done!";
            completed = true;
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred during extracting Fabric / Quilt Mods: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred during extracting Fabric / Quilt Mods: \n" + error.getMessage());
            error.printStackTrace();
        }
    }

    public static void extractForgeMods(String jarDirectoryPath, String tempDirectoryPath) {
        int totalJarCount = getJarCount(jarDirectoryPath);
        count = "0 / " + totalJarCount;

        int processedJars = 0;

        try {
            File jarFolder = new File(jarDirectoryPath);
            File[] files = jarFolder.listFiles();

            for (File file: files) {
                if (file.getName().endsWith(".jar")) {
                    try (JarFile jarFile = new JarFile(file)) {
                        processing = file.getName();
                        JarEntry jarEntry = jarFile.getJarEntry("META-INF/mods.toml");

                        if (jarEntry != null) {
                            try (InputStream inputStream = jarFile.getInputStream(jarEntry);
                                 FileOutputStream outputStream = new FileOutputStream(new File(tempDirectoryPath + "/mods.toml"))) {

                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }

                                // ParseTOML.parseTOML(output, file.getName());
                            }
                        }
                    }

                    processedJars++;
                    count = processedJars + " / " + totalJarCount;
                }
            }

            processing = "Done!";
            completed = true;
        } catch (Exception error) {
            ErrorHandler.error = "An error occurred during extracting Forge / NeoForged Mods: \n" + error.getMessage();
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occurred during extracting plugins: \n" + error.getMessage());
            error.printStackTrace();
        }
    }



    // ------------------------ MISC ------------------------ //

    public static void createTemporaryDirectory(String temporaryDirectoryPath) {
        File directory = new File(temporaryDirectoryPath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                LOGGER.info("Successfully created temporary directory at [" + temporaryDirectoryPath + "].");
            } else {
                ErrorHandler.error = "Failed to create temporary directory at [" + temporaryDirectoryPath + "].";
                ErrorHandler.setErrorMessage(pane);
                LOGGER.severe("Failed to create the temporary directory at [" + temporaryDirectoryPath + "].");
            }
        }
    }
}
