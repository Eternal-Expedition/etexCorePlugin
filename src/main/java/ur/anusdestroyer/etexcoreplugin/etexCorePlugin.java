package ur.anusdestroyer.etexcoreplugin;

import ur.anusdestroyer.etexcoreplugin.command.MainCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class etexCorePlugin extends JavaPlugin {

    private static etexCorePlugin instance;
    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info("Plugin is starting up!");

        // instance
        instance = this;


        //register commands
        getCommand("etexCorePlugin").setExecutor(new MainCommand(instance));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic



        getLogger().info("Plugin died!");
    }
}
