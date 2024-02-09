package ur.anusdestroyer.etexcoreplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbManager {
    private HikariDataSource dataSource;

    private YamlConfiguration config = ConfigFiles.getConfig();

    private final etexCorePlugin instance;

    public DbManager(etexCorePlugin instance) {
        this.instance = instance;
    }

    public void DatabaseManager() {
        initializeDataSource();
    }

    private void initializeDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        String ip = config.getString("database.mysql.ip");
        int port = config.getInt("database.mysql.port");
        String databaseName = config.getString("database.mysql.databaseName");

        hikariConfig.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + databaseName);
        hikariConfig.setUsername(config.getString("database.mysql.username"));
        hikariConfig.setPassword(config.getString("database.mysql.password"));
        hikariConfig.setMaximumPoolSize(config.getInt("database.mysql.maxPoolSize"));

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hikariConfig);
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

    public void testdb() {
        // You can add a method for testing database connections or running sample queries
    }

    public void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}