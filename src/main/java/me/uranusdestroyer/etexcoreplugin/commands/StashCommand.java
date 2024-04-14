package me.uranusdestroyer.etexcoreplugin.commands;

import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;

import java.util.List;

public class StashCommand implements TabExecutor {

    private final etexCorePlugin instance;

    public StashCommand(etexCorePlugin instance) { this.instance = instance; }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission("etex.core.stash.use")) {
                Stash.claim(player);
            } else {
                // TODO: add in messages.yml
                player.sendMessage(MessageUtils.legacyMinimessageString("<red>Missing permissions"));
            }
        } else {
            commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Console does not have a stash"));
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
