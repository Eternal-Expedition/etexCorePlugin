package me.uranusdestroyer.etexcoreplugin.backend;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    public static String replacePlaceholders(String text, Player player) {

        text = (text == null) ? "An error occurred somewhere! (messages.yml)" : text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static Component componentFromString(String text, Player player) {
        text = replacePlaceholders(text, player);

        return MiniMessage.miniMessage().deserialize(text);
    }

    public static Component componentFromString(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static String legacyMinimessageString(String text) {
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(text));
    }

    public static String legacyMinimessageString(String text, Player player) {
        text = replacePlaceholders(text, player);

        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(text));
    }

    public static List<String> legacyMinimessageStringList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for(String line : list) newList.add(legacyMinimessageString(line));

        return newList;
    }

    public static List<String> legacyMinimessageStringList(List<String> list, Player player) {
        List<String> newList = new ArrayList<>();
        for(String line : list) newList.add(legacyMinimessageString(line, player));

        return newList;
    }

}