package ur.anusdestroyer.etexcoreplugin.backend;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    public static String strWithPlaceholders(String text, Player player) {
        text = (text == null) ? "An error occurred somewhere! (messages.yml)" : text;
        String str = PlaceholderAPI.setPlaceholders(player, text);
        return str(str);
    }

    public static String hex(String text, Player player) {

        text = PlaceholderAPI.setPlaceholders(player, text);

        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        Matcher match = pattern.matcher(text);
        while (match.find()){
            String color = text.substring(match.start(), match.end());
            text = text.replace(color, ChatColor.of(color) + "");
            match = pattern.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String str(String text) {
        MiniMessage parser = MiniMessage.builder().build();
        Component component = parser.deserialize(text
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underline>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>")
                .replace("§0", "<black>")
                .replace("§1", "<dark_blue>")
                .replace("§2", "<dark_green>")
                .replace("§3", "<dark_aqua>")
                .replace("§4", "<dark_red>")
                .replace("§5", "<dark_purple>")
                .replace("§6", "<gold>")
                .replace("§7", "<gray>")
                .replace("§8", "<dark_gray>")
                .replace("§9", "<blue>")
                .replace("§a", "<green>")
                .replace("§b", "<aqua>")
                .replace("§c", "<red>")
                .replace("§d", "<light_purple>")
                .replace("§e", "<yellow>")
                .replace("§f", "<white>")
                .replace("§k", "<obfuscated>")
                .replace("§l", "<bold>")
                .replace("§m", "<strikethrough>")
                .replace("§n", "<underline>")
                .replace("§o", "<italic>")
                .replace("§r", "<reset>")
        );
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

}