package me.uranusdestroyer.etexcoreplugin;

import me.uranusdestroyer.etexcoreplugin.Listeners.PlayerJoinListener;
import me.uranusdestroyer.etexcoreplugin.api.API;
import me.uranusdestroyer.etexcoreplugin.backend.ConfigFiles;
import me.uranusdestroyer.etexcoreplugin.backend.Placeholders;
import me.uranusdestroyer.etexcoreplugin.commands.ATMCommand;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Currencies;
import org.bukkit.plugin.java.JavaPlugin;
import me.uranusdestroyer.etexcoreplugin.backend.DbManager;
import me.uranusdestroyer.etexcoreplugin.commands.MainCommand;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;



public final class etexCorePlugin extends JavaPlugin {

    public API api = new API();

    private static etexCorePlugin instance;

    public DbManager database; // Declare here but don't initialize

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin is starting up!");

        // Assign the instance before using it
        instance = this;

        // config files
        ConfigFiles.load(instance);
        getLogger().info("Config files have been loaded");


        // database initialization
        DbManager.setInstance(this);
        DbManager.initializeDatabase();
        DbManager.startPoolMonitor();

        // command
        getCommand("etexcore").setExecutor(new MainCommand(instance));
        getCommand("atm").setExecutor(new ATMCommand());

        // stash initialization
        if(ConfigFiles.getConfig().getBoolean("stash.enabled")) {
            new Stash(instance);
        }

        // banks & currencies initialization
        if(ConfigFiles.getConfig().getBoolean("currencies-bank.enabled")) {
            new Currencies(instance);
        }

        new Placeholders().register();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin is shutting down!");

        // closing connection - idk if its necessary, but why not
        if (database != null) {
            database.closeDataSource();
        }

        getLogger().info("Plugin died!");
    }
    public static etexCorePlugin getInstance() {
        return instance;
    }
}
