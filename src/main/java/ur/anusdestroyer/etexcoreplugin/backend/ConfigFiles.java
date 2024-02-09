package ur.anusdestroyer.etexcoreplugin.backend;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigFiles {

    private static YamlConfiguration config;
    private static YamlConfiguration messages;
    private static Map<String, YamlConfiguration> guiTemplates = new HashMap<>();
    private static Map<String, YamlConfiguration> tradeTemplates = new HashMap<>();

    public static void load(etexCorePlugin instance) {
        // Load the configuration file
        instance.saveDefaultConfig();
        instance.reloadConfig();

        // Load the config file
        File configFile = new File(instance.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            instance.getLogger().severe("Error loading messages file: " + e.getMessage());
        }

        // Load the messages file
        File messagesFile = new File(instance.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
                YamlConfiguration messages_file = YamlConfiguration.loadConfiguration(messagesFile);
                messages_file.addDefault("help-message", "&cYou might want to use /cvs help.");
                messages_file.addDefault("no-permission", "&cYou don't have permission to execute this.");
                messages_file.save(messagesFile);
            }catch (IOException e){
                System.out.println("Messages file error wtf?");}}
        messages = new YamlConfiguration();
        try {
            messages.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            instance.getLogger().severe("Error loading messages file: " + e.getMessage());
        }
    }



    public static YamlConfiguration getConfig() {
        return config;
    }
    public static YamlConfiguration getMessages() {
        return messages;
    }
}
