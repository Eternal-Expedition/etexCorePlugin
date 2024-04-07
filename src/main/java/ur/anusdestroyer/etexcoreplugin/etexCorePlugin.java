package ur.anusdestroyer.etexcoreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.checkerframework.checker.units.qual.A;
import ur.anusdestroyer.etexcoreplugin.api.API;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import org.bukkit.plugin.java.JavaPlugin;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;
import ur.anusdestroyer.etexcoreplugin.command.MainCommand;
import ur.anusdestroyer.etexcoreplugin.features.itemmanager.Stash;


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

        // stash initialization
        if(ConfigFiles.getConfig().getBoolean("stash.enabled")) {
            Stash.setInstance(instance);
        }


        // commands
        getCommand("etex").setExecutor(new MainCommand(instance));

        // API

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
