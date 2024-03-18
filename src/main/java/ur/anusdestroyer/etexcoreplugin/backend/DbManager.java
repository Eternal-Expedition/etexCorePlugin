package ur.anusdestroyer.etexcoreplugin.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbManager {
    private HikariDataSource dataSource;

    private final YamlConfiguration config = ConfigFiles.getConfig();

    private final etexCorePlugin instance;

    public DbManager(etexCorePlugin instance) {
        this.instance = instance;
        initializeDataSource(); // Ensure the data source is initialized here
    }

    private void initializeDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        String ip = config.getString("database.ip");
        int port = config.getInt("database.port");
        String databaseName = config.getString("database.databaseName");

        // Ensure you're handling potential null values from the configuration
        if (ip == null || databaseName == null ) {
            Bukkit.getLogger().severe("Database configuration is missing. Please check your config file.");
            return;
        }

        hikariConfig.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + databaseName);
        hikariConfig.setUsername(config.getString("database.username"));
        hikariConfig.setPassword(config.getString("database.password"));
        // Make sure to provide a default value in case the configuration does not specify one
        hikariConfig.setMaximumPoolSize(config.getInt("database.maxPoolSize", 10));

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to initialize data source: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeDatabase() {
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            createStashTable();
            createMailTable();
            // Add more initialization methods for other tables as needed

            Bukkit.getLogger().info("Database initialized");
        });
    }

    private void createStashTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS stash (id INT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack TEXT, timestamp TIMESTAMP)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createMailTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS mail (id INT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack TEXT, timestamp TIMESTAMP, message TEXT, player_name VARCHAR(255))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
