package ur.anusdestroyer.etexcoreplugin;

import ur.anusdestroyer.etexcoreplugin.api.API;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import org.bukkit.plugin.java.JavaPlugin;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;
import ur.anusdestroyer.etexcoreplugin.command.MainCommand;
import ur.anusdestroyer.etexcoreplugin.command.StashCommand;
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

        // command
        getCommand("etex").setExecutor(new MainCommand(instance));

        // stash initialization
        if(ConfigFiles.getConfig().getBoolean("stash.enabled")) {
            Stash.setInstance(instance);
            getCommand("stash").setExecutor(new StashCommand(instance));
            //* 24 * 60 * 60 * 1000
            Stash.purgeOldItems(ConfigFiles.getConfig().getInt("stash.delete-time")  * 1000L);
        }

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
