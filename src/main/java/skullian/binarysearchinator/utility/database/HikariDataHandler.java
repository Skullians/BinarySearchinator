package skullian.binarysearchinator.utility.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.sqlite.JDBC;
import skullian.binarysearchinator.MainApp;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class HikariDataHandler {

    private static Logger LOGGER = MainApp.LOGGER;

    private transient HikariDataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void initialiseDatabase(String tempDirectoryPath) throws SQLException {
        LOGGER.warning("Setting up SQLite...");
        createDataSource(new File(tempDirectoryPath, "data.sqlite3"));
        setupTables();
    }

    private void createDataSource(
            @NotNull File file) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.sqlite.SQLiteDataSource");
        config.addDataSourceProperty("url", JDBC.PREFIX + file.getAbsolutePath());
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("enforceForeignKeys", "true");
        config.addDataSourceProperty("synchronous", "NORMAL");
        config.addDataSourceProperty("journalMode", "WAL");
        config.setPoolName("SQLite");
        config.setMaximumPoolSize(1);

        dataSource = new HikariDataSource(config);
    }

    public void setupTables() throws SQLException {
        LOGGER.warning("Setting up SQL Tables...");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement configData = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS configData (
                     [folderPath] STRING NOT NULL,
                     [jarType] STRING NOT NULL,
                     );
                     """);) {

            configData.executeUpdate();
            configData.close();

            connection.close();
        }
    }


}
