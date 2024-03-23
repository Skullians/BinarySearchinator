package skullian.binarysearchinator.utility.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariDataHandler {

    private transient HikariDataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void initialiseDatabase(String tempDirectoryPath) throws SQLException {
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement configData = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS configData (
                     [folderPath] STRING PRIMARY KEY NOT NULL,
                     [jarType] STRING NOT NULL
                     );
                     """);

             PreparedStatement dependenciesData = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS dependenciesData (
                     [modName] STRING PRIMARY KEY NOT NULL,
                     [jarName] STRING NOT NULL,
                     [presentDependencies] BLOB NOT NULL,
                     [missingDependencies] BLOB NOT NULL
                     );
                     """)) {

            configData.executeUpdate();
            configData.close();

            dependenciesData.executeUpdate();
            dependenciesData.close();

            connection.close();
        }
    }

    // ------------------------ CONFIG STUFFS ------------------------ //
    public void registerConfigs(String folderPath, String jarType) throws SQLException{
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO configData(folderPath, jarType) VALUES (?, ?)")) {

            statement.setString(1, folderPath);
            statement.setString(2, jarType);

            statement.executeUpdate();
            statement.close();

            connection.close();
        }
    }

    public void updateJarType(String folderPath, String jarType) throws SQLException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE configData SET jarType = ? WHERE folderPath = ?")) {

            statement.setString(1, jarType);
            statement.setString(2, folderPath);

            statement.executeUpdate();
            statement.close();

            connection.close();
        }
    }

    // ------------------------ 'GETTERS' ------------------------ //



    // Basically check if there is already data for this folder.
    public ConfigModel getConfigsIfExists(String folderPath) throws SQLException {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM configData WHERE folderPath = ?")) {

            statement.setString(1, folderPath);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new ConfigModel(resultSet.getString("folderPath"), resultSet.getString("jarType"));
            }

            statement.close();

            connection.close();
        }

        return null;
    }


}
