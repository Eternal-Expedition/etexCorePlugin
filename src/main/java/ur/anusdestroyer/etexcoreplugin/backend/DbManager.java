package ur.anusdestroyer.etexcoreplugin.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;




public class DbManager {
    private static HikariDataSource dataSource;

    private static final YamlConfiguration config = ConfigFiles.getConfig();

    private static etexCorePlugin instance;

    public static void setInstance(etexCorePlugin pluginInstance) {
        instance = pluginInstance;
    }

    private static void initializeDataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        String ip = config.getString("database.ip");
        int port = config.getInt("database.port");
        String databaseName = config.getString("database.databaseName");

        // Ensure you're handling potential null values from the configuration
        if (ip == null || databaseName == null ) {
            instance.getLogger().severe("Database configuration is missing. Please check your config file.");
            return;
        }

        hikariConfig.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + databaseName);
        hikariConfig.setUsername(config.getString("database.username"));
        hikariConfig.setPassword(config.getString("database.password"));
        // Make sure to provide a default value in case the configuration does not specify one
        hikariConfig.setMaximumPoolSize(config.getInt("database.maxPoolSize", 200));

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setLeakDetectionThreshold(60000);

        hikariConfig.setPoolName("etexCore");

        try {
            dataSource = new HikariDataSource(hikariConfig);
            instance.getLogger().info("Database connected!");
        } catch (Exception e) {
            instance.getLogger().severe("Failed to initialize data source: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {

        initializeDataSource();

        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

            createStashTable();
            createMailTable();
            // Add more initialization methods for other tables as needed

            instance.getLogger().info("Database initialized");
        });
    }

    private static void createStashTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS etex_core_stash (id INT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack BLOB, timestamp BIGINT)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createMailTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS etex_core_mail (id INT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack BLOB, timestamp BIGINT, message TEXT, player_name VARCHAR(255))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to obtain database connection", e);
        }
    }

    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
