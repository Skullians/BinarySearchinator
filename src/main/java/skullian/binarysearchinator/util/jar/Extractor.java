package skullian.binarysearchinator.util.jar;

import javafx.scene.layout.BorderPane;
import skullian.binarysearchinator.MainApp;
import skullian.binarysearchinator.control.ErrorHandler;
import skullian.binarysearchinator.util.jar.parsing.ParseJSON;
import skullian.binarysearchinator.util.jar.parsing.ParseTOML;
import skullian.binarysearchinator.util.jar.parsing.ParseYAML;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Extractor {
    private static final Logger LOGGER = MainApp.LOGGER;

    public static String processing = "N/A";
    public static String[] dependencies;
    public static String count = "0 / 0";
    public static boolean completed = false;

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
            ZipFile jarFile = new ZipFile(jarFilePath.toFile());
            Enumeration<? extends ZipEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains("mods.toml")) {
                    return "Forge / NeoForged Mod";
                } else if (entry.getName().contains("plugin.yml")) {
                    return "Plugin";
                } else if (entry.getName().contains("fabric.mod.json")) {
                    return "Fabric Mod";
                } else if (entry.getName().contains("quilt.mod.json")) {
                    return "Quilt Mod";
                }
            }
            /*for (String fileName: toCheck) {
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
            }*/
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

    public static void extractPlugins(String input, String output) {
        createTempDirectory(output);
        int totalcount = getJarCount(input);
        count = "0 / " + totalcount;

        try {
            File folder = new File(input);
            File tempOutput = new File(output);
            File[] files = folder.listFiles();
            int aNum = 0;

            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        try (JarFile jarFile = new JarFile(file)) {
                            processing = file.getName();
                            JarEntry fileEntry = jarFile.getJarEntry("plugin.yml");

                            if (fileEntry != null) {
                                try (InputStream inputStream = jarFile.getInputStream(fileEntry);

                                    FileOutputStream outputStream = new FileOutputStream(new File(output + "/plugin.yml"))) {
                                    byte[] buffer = new byte[1024];
                                    int length;

                                    while ((length = inputStream.read(buffer)) > 0) {
                                        outputStream.write(buffer, 0, length);
                                    }

                                    ParseYAML.parseYAML(output, file.getName());
                                }
                            }
                        }
                        aNum++;
                        count = aNum + " / " + totalcount;
                    }
                }
                processing = "Done!";
                completed = true;
            }
        } catch (Exception error) {
            ErrorHandler.error = "Failed to extract plugin: \n" + error;
            ErrorHandler.setErrorMessage(pane);

            LOGGER.severe("An unexpected error occured during extracting plugins.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    public static void extractFabricQuiltMods(String input, String output, String desiredOutput) {
        createTempDirectory(output);
        int totalcount = getJarCount(input);
        count = "0 / " + totalcount;

        try {
            File folder = new File(input);
            File tempOutput = new File(output);
            File[] files = folder.listFiles();
            int aNum = 0;

            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        try (JarFile jarFile = new JarFile(file)) {
                            processing = file.getName();
                            JarEntry fileEntry = jarFile.getJarEntry(desiredOutput);
                            if (fileEntry != null) {
                                try (InputStream inputStream = jarFile.getInputStream(fileEntry);
                                     FileOutputStream outputStream = new FileOutputStream(new File(output + "/" + desiredOutput))) {
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = inputStream.read(buffer)) > 0) {
                                        outputStream.write(buffer, 0, length);
                                    }

                                    ParseJSON.parseJSON(output, file.getName(), desiredOutput);
                                }
                            }
                        }
                        aNum++;
                        count = aNum + " / " + totalcount;
                    }
                }
                processing = "Done!";
                completed = true;
            }
        } catch (Exception error) {
            ErrorHandler.error = "Failed to extract plugin: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An unexpected error occured during extracting Fabric / Quilt Mods.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    public static void extractForgeMods(String input, String output) {
        createTempDirectory(output);
        int totalcount = getJarCount(input);
        count = "0 / " + totalcount;
        System.out.println("Init");
        try {
            File folder = new File(input);
            File tempOutput = new File(output);
            File[] files = folder.listFiles();
            int aNum = 0;

            if (files != null) {
                System.out.println("Not null");
                for (File file : files) {
                    System.out.println("File " + file.getName());
                    if (file.getName().endsWith(".jar")) {
                        try (JarFile jarFile = new JarFile(file)) {
                            processing = file.getName();
                            JarEntry fileEntry = jarFile.getJarEntry("META-INF/mods.toml");
                            if (fileEntry != null) {
                                try (InputStream inputStream = jarFile.getInputStream(fileEntry);
                                     FileOutputStream outputStream = new FileOutputStream(new File(output + "/mods.toml"))) {
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while ((length = inputStream.read(buffer)) > 0) {
                                        outputStream.write(buffer, 0, length);
                                    }

                                    ParseTOML.parseTOML(output, file.getName());
                                }
                            }
                        }
                        aNum++;
                        count = aNum + " / " + totalcount;
                    }
                }
                processing = "Done!";
                completed = true;
            }
        } catch (Exception error) {
            ErrorHandler.error = "Failed to extract plugin: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An unexpected error occured during extracting mods.");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
    }

    private static void createTempDirectory(String output) {
        File directory = new File(output);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                LOGGER.info("Successfully created temporary directory at [" + output + "].");
            } else {
                LOGGER.severe("Failed to create temporary directory at [" + output + "].");
                ErrorHandler.error = "Failed to create temporary directory at [" + output + "].";
                ErrorHandler.setErrorMessage(pane);
            }
        }
    }

    private static int getJarCount(String input) {
        try {
            File directory = new File(input);
            File[] jarFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            });
            return jarFiles.length;
        } catch (Exception error) {
            ErrorHandler.error = "An error occured when trying to get the amount of jarfiles within the plugin / mod directory: \n" + error;
            ErrorHandler.setErrorMessage(pane);
            LOGGER.severe("An error occured when trying to get the amount of jarfiles within the plugin / mod directory: \n");
            LOGGER.severe(Arrays.toString(error.getStackTrace()));
        }
        return 0;
    }
}
