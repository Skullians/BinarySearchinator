package skullian.binarysearchinator;

import skullian.binarysearchinator.utility.database.HikariDataHandler;

import java.util.logging.Logger;

public class MainApp {

    public static HikariDataHandler database;

    public static Logger LOGGER = Logger.getLogger("BinarySearchinator");
    public static void main(String[] args) {

        LOGGER.info("Welcome to BinarySearchinator. Initialising SearchinatorApp.");
        SearchinatorApp.main(args);
    }
}
