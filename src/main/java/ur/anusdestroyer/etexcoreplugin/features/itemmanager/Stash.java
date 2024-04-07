package ur.anusdestroyer.etexcoreplugin.features.itemmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;
import ur.anusdestroyer.etexcoreplugin.backend.MessageUtils;
import ur.anusdestroyer.etexcoreplugin.etexCorePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.sql.ResultSet;

public class Stash {

    private static etexCorePlugin instance;

    public static void setInstance(etexCorePlugin pluginInstance) {
        instance = pluginInstance;
    }

    public static void add(Player player, ItemStack item) {
        if(isEnabled(player.getUniqueId(),item)) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);

                // TODO: messages.yml for stash
                player.sendMessage(MessageUtils.str("&cYour inventory is full, your items are now stored in /stash for " + ConfigFiles.getConfig().getString("stash.delete-time") + " days!"));
            } else {
                UUID uuid = player.getUniqueId();
                add(uuid, item);
            }
        }

    }

    // This should add an item to stash db
    public static void add(UUID uuid, ItemStack item) {
        if(isEnabled(uuid,item)) {

            byte[] serialized_item = item.serializeAsBytes();

            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

                Connection conn = DbManager.getConnection();
                String query = "INSERT INTO etex_core_stash (uuid, item_stack, timestamp) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, String.valueOf(uuid));
                    preparedStatement.setBytes(2, serialized_item);
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(String.valueOf(System.currentTimeMillis())));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    private static boolean isEnabled(UUID uuid, ItemStack item) {
        if (!ConfigFiles.getConfig().getBoolean("stash.enabled")) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.getInventory().addItem(item);
            }
            return false;
        } else {
            return true;
        }
    }

    public static void claim(Player player) {
        List<StashItem> items = getPlayerStash(player.getUniqueId());





    }


    private static List<StashItem> getPlayerStash(UUID uuid) {

        List<StashItem> items = new ArrayList<>();
        String query = "SELECT * FROM etex_core_stash WHERE uuid = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StashItem item = new StashItem(
                            rs.getInt("id"),
                            rs.getBytes("item_stack"),
                            rs.getTimestamp("timestamp")
                    );
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        items.sort(Comparator.comparing(StashItem::getTimestamp));
        return items;

    }

    public static void purgeUser(UUID uuid) {

    }

    public static void purgeOldItems(Timestamp time) {

    }




}
