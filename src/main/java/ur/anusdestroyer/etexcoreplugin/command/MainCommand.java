package ur.anusdestroyer.etexcoreplugin.command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import ur.anusdestroyer.etexcoreplugin.backend.MessageUtils;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;






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