package me.uranusdestroyer.etexcoreplugin.commands;


import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;
import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.UUID;

/*

help:

stash:
    add [uuid] [string]
    purge [uuid]
    purgeOldItems [int days]
mail:
    sendServer



 */




public class MainCommand implements TabExecutor {

    private final etexCorePlugin instance;

    public MainCommand(etexCorePlugin instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String arg1 = args.length > 0 ? args[0].toLowerCase() : "";
        String arg2 = args.length > 1 ? args[1].toLowerCase() : "";
        String arg3 = args.length > 2 ? args[2] : "";
        String arg4 = args.length > 3 ? args[3] : "";
        String arg5 = args.length > 4 ? args[4] : "";

        if (arg1.isEmpty()) {
            commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Invalid Usage: /etex help"));
            return false;
        } else {
            switch (arg1) {
                case "help":
                    // TODO: implement help
                    commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Help is not implemented yet"));
                    break;

                case "stash":
                    switch (arg2) {
                        case "add":
                            if (!arg3.isEmpty() || !arg4.isEmpty()) {
                                UUID uuid = UUID.fromString(arg3);
                                String string = arg4;

                                Stash.add(uuid, ItemHandler.itemStackFromString(string));
                                commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Item Added Successfully"));

                            } break;
                        case "purge":
                            if (arg3 != null) {
                                UUID uuid = UUID.fromString(arg3);

                                Stash.purgeUser(uuid);
                                commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Player Purged Successfully"));
                            } break;
                        case "purgeolditems":
                            if (arg3 != null) {
                                int days = Integer.parseInt(arg3);
                                Stash.purgeOldItems(days * 24L * 60L * 60L * 1000L);
                                commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Old items were purged Successfully"));
                            } break;
                        default:
                            commandSender.sendMessage(MessageUtils.legacyMinimessageString("<red>Invalid!"));
                    } break;



                case "mail":
                    break;

            }

        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }






}