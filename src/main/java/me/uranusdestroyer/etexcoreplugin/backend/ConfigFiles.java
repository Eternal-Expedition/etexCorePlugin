package me.uranusdestroyer.etexcoreplugin.backend;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigFiles {

    @Getter
    private static YamlDocument config;
    @Getter
    private static YamlDocument messages;

    private static Map<String, YamlDocument> guiTemplates = new HashMap<>();
    private static Map<String, YamlConfiguration> tradeTemplates = new HashMap<>();

    public static void load(etexCorePlugin instance) {
        try {
            config = initializeYmlFile(instance.getDataFolder(), "config", instance.getResource("config.yml"));
            System.out.println(config.getString("currencies-bank.bank.atm-gui-template"));
            messages = initializeYmlFile(instance.getDataFolder(), "messages", instance.getResource("messages.yml"));
            guiTemplates.put(
                    config.getString("currencies-bank.bank.atm-gui-template"),
                    initializeYmlFile(
                            new File(instance.getDataFolder(), "gui-templates"),
                            config.getString("currencies-bank.bank.atm-gui-template"),
                            instance.getResource("gui-templates/" + config.getString("currencies-bank.bank.atm-gui-template") + ".yml"))
            );
        } catch (IOException e) {
            instance.getLogger().severe("Failed to load configs!!! The plugin will now disable. " + e.getMessage());
            Bukkit.getScheduler().runTask(instance, Bukkit::shutdown);
        }
    }


    private static YamlDocument initializeYmlFile(File file, String fileName, InputStream resource) throws IOException {
        YamlDocument yamlDocument = YamlDocument.create(new File(file, fileName + ".yml"),
                Objects.requireNonNull(resource),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version"))
                        .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()
        );

        yamlDocument.update();
        yamlDocument.save();

        return yamlDocument;
    }

    public static YamlDocument getGuiTemplate(String name) {
        return guiTemplates.get(name);
    }

}
