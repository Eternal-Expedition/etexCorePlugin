package me.uranusdestroyer.etexcoreplugin.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;


public class DbManager {
    private static HikariDataSource dataSource;

    private static final YamlDocument config = ConfigFiles.getConfig();

    private static etexCorePlugin instance;

    public static void setInstance(etexCorePlugin pluginInstance) {
        instance = pluginInstance;
    }

    private static void initializeDataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        System.out.println("ted by se mela delat db");

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
        hikariConfig.setMaximumPoolSize(config.getInt("database.maxPoolSize", 1500));

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        hikariConfig.setLeakDetectionThreshold(121 * 1000);
        hikariConfig.setIdleTimeout(30 * 1000);
        hikariConfig.setMaxLifetime(120 * 1000);

        hikariConfig.setPoolName("etexCore");

        try {
            dataSource = new HikariDataSource(hikariConfig);
            instance.getLogger().info("Database connected!");
        } catch (Exception e) {
            instance.getLogger().severe("Failed to initialize data source: " + e.getMessage());
            e.printStackTrace();
            Bukkit.getScheduler().runTask(instance, Bukkit::shutdown);
        }
    }

    public static void initializeDatabase() {

        initializeDataSource();



        createStashTable();
        createMailTable();
            // Add more initialization methods for other tables as needed

        instance.getLogger().info("Database initialized");

    }

    private static void createStashTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS etex_core_stash (id BIGINT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack BLOB, timestamp BIGINT)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createMailTable() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS etex_core_mail (id BIGINT PRIMARY KEY AUTO_INCREMENT, uuid VARCHAR(36), item_stack BLOB, timestamp BIGINT, message TEXT, player_name VARCHAR(255))";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCurrencyTable(String currencyName) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS etex_currency_"+ currencyName +" (uuid VARCHAR(36) PRIMARY KEY, value BIGINT)";
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

    public static void startPoolMonitor() {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (dataSource != null && dataSource.getHikariPoolMXBean() != null
                        && dataSource.getHikariPoolMXBean().getActiveConnections() >= dataSource.getMaximumPoolSize()) {
                    instance.getLogger().severe("----------------------------------------------------");
                    instance.getLogger().severe("SERVER WAS SHUTDOWN BY etexCore!!!");
                    instance.getLogger().severe("SERVER WAS SHUTDOWN BY etexCore!!!");
                    instance.getLogger().severe("SERVER WAS SHUTDOWN BY etexCore!!!");
                    instance.getLogger().severe("SERVER WAS SHUTDOWN BY etexCore!!!");
                    instance.getLogger().severe("SERVER WAS SHUTDOWN BY etexCore!!!");
                    instance.getLogger().severe("----------------------------------------------------");
                    instance.getLogger().severe("Maximum number of connections reached, increase the Pool Size in config. Recommended value is around 500. If it doesnt solve the problem, consider reporting a possible leak to plugin developer.");
                    instance.getLogger().severe("----------------------------------------------------");
                    instance.getLogger().severe("This is a protection mechanism that stops the server when the Pool Size is full, otherwise you would experience strange GAMEBREAKING bugs because I am too lazy to implement better fail safes.");
                    instance.getLogger().severe("----------------------------------------------------");

                    Bukkit.getScheduler().runTask(instance, Bukkit::shutdown);
                }
            }
        }.runTaskTimerAsynchronously(instance, 0L, 20L);
    }


}
