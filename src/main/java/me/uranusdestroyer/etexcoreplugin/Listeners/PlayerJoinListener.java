package me.uranusdestroyer.etexcoreplugin.Listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.uranusdestroyer.etexcoreplugin.backend.ConfigFiles;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Bank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    YamlDocument config = ConfigFiles.getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(config.getBoolean("currencies-bank.enabled")) Bank.insertPlayer(player.getUniqueId());
    }
}