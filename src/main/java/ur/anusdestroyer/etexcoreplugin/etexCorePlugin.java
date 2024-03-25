package ur.anusdestroyer.etexcoreplugin;


import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import org.bukkit.plugin.java.JavaPlugin;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;

public final class etexCorePlugin extends JavaPlugin {

    private static etexCorePlugin instance;
    public DbManager database; // Declare here but don't initialize

    @Override
    public void onLoad() {

        // config files
        ConfigFiles.load(instance);
        getLogger().info("Config files have been loaded");


        // command api load
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).initializeNBTAPI(NBTContainer.class, NBTContainer::new ));


        new CommandAPICommand("broadcastmsg")
                .withArguments(new GreedyStringArgument("message")) // The arguments
                .withAliases("broadcast", "broadcastmessage")       // Command aliases
                .withPermission(CommandPermission.OP)               // Required permissions
                .executes((sender, args) -> {
                    String message = (String) args.get("message");


                    Bukkit.getServer().broadcastMessage(message);
                })
                .register();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin is starting up!");

        // Assign the instance before using it
        instance = this;

        // Now initialize the database manager
        database = new DbManager(instance);

        // database initialization
        database.initializeDatabase();

        //register commands
        CommandAPI.onEnable();
        // TODO: REMOVE getCommand("etexCorePlugin").setExecutor(new MainCommand(instance));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin is shutting down!");
        CommandAPI.onDisable();

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
