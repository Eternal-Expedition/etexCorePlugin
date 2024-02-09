package ur.anusdestroyer.etexcoreplugin;

import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import ur.anusdestroyer.etexcoreplugin.command.MainCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ur.anusdestroyer.etexcoreplugin.database.DbManager;

public final class etexCorePlugin extends JavaPlugin {

    private static etexCorePlugin instance;

    public DbManager database = new DbManager(instance);

    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info("Plugin is starting up!");

        // instance
        instance = this;

        // config files
        ConfigFiles.load(instance);
        getLogger().info("Config files have been loaded");



        //register commands
        getCommand("etexCorePlugin").setExecutor(new MainCommand(instance));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic



        getLogger().info("Plugin died!");
    }
}
