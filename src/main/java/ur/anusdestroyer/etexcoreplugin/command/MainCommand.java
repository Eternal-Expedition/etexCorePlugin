package ur.anusdestroyer.etexcoreplugin.command;


import net.Indyuce.mmoitems.manager.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import ur.anusdestroyer.etexcoreplugin.backend.MessageUtils;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;
import ur.anusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import ur.anusdestroyer.etexcoreplugin.features.itemmanager.Stash;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

/*

stash:
    add [player] [string]
    purge [uuid]
    purgeOld



 */




public class MainCommand implements TabExecutor {

    private final etexCorePlugin instance;

    public MainCommand(etexCorePlugin instance) {
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(MessageUtils.str("&7kokot"));
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "stash":
                if (args.length == 1) {
                    commandSender.sendMessage(MessageUtils.str("&7[claim]"));
                    return false;
                } else {
                    if (args[1].equalsIgnoreCase("claim")) {
                        Stash.add((Player) commandSender, ItemHandler.itemStackFromString("vl:diamond:1"));

                    }
                }


            case "mail":



            case "admin":



        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}


 /*




 */