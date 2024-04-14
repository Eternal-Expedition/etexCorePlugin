package me.uranusdestroyer.etexcoreplugin.features.itemmanager;

import me.uranusdestroyer.etexcoreplugin.commands.StashCommand;
import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.uranusdestroyer.etexcoreplugin.backend.ConfigFiles;
import me.uranusdestroyer.etexcoreplugin.backend.DbManager;
import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;


public class Stash {

    private static etexCorePlugin instance;
    private static Map<UUID, Boolean> claiming = new ConcurrentHashMap<>();

    public Stash(etexCorePlugin pluginInstance) {
        instance = pluginInstance;
        instance.getCommand("stash").setExecutor(new StashCommand(instance));
        //* 24 * 60 * 60 * 1000
        Stash.purgeOldItems(ConfigFiles.getConfig().getInt("stash.delete-time")  * 24L * 60L * 60L * 1000L);
    }

    public static void add(Player player, ItemStack item) {
        if(isEnabled(player.getUniqueId(),item)) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                UUID uuid = player.getUniqueId();
                add(uuid, item);

                // TODO: messages.yml for stash
                player.sendMessage(MessageUtils.legacyMinimessageString("<red>Your inventory is full, your items are now stored in /stash for " + ConfigFiles.getConfig().getString("stash.delete-time") + " days!"));
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
                    preparedStatement.setLong(3, System.currentTimeMillis());
                    preparedStatement.executeUpdate();
                    conn.close();
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
        UUID uuid = player.getUniqueId();

        if (!claiming.containsKey(uuid)) {
            claiming.put(uuid, true);

            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

                List<StashItem> items = getPlayerStash(uuid);
                List<Integer> claimedItemIds = new ArrayList<>();

                Bukkit.getScheduler().runTask(instance, () -> {

                    for (StashItem stashItem : items) {
                        ItemStack item = ItemStack.deserializeBytes(stashItem.getItemStack());
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(item);
                            claimedItemIds.add(stashItem.getId());
                        } else {
                            break;
                        }
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
                        if (!claimedItemIds.isEmpty()) {
                            removeItemsByIds(claimedItemIds);
                        }
                        claiming.remove(uuid);

                        player.sendMessage(MessageUtils.legacyMinimessageString( "<green>" + claimedItemIds.size() + "<gray>/<green>" + items.size() + " <gray>items claimed"));

                    });
                });
            });

        } else {
            // TODO: stash message.yml if player is already claiming
            player.sendMessage(MessageUtils.legacyMinimessageString("<red>Already claiming? (try again later or report bug)"));
        }
    }

    private static void removeItemsByIds (List<Integer> list) {
        String placeholders = String.join(",", Collections.nCopies(list.size(), "?"));
        String sql = "DELETE FROM etex_core_stash WHERE id IN (" + placeholders + ")";

        Connection conn = DbManager.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : list) {
                ps.setInt(index++, id);
            }
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging this exception or handling it more gracefully.
        }
    }

    private static List<StashItem> getPlayerStash(UUID uuid) {

        List<StashItem> items = new ArrayList<>();
        String query = "SELECT * FROM etex_core_stash WHERE uuid = ?";

        Connection conn = DbManager.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        StashItem item = new StashItem(
                                rs.getInt("id"),
                                rs.getBytes("item_stack"),
                                rs.getLong("timestamp")
                        );
                        items.add(item);

                    }
            } conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        items.sort(Comparator.comparing(StashItem::getTimestamp));
        return items;

    }

    public static void purgeUser(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            Connection conn = DbManager.getConnection();
            String query = "DELETE FROM etex_core_stash WHERE uuid = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void purgeOldItems(long deleteTimeToMillis) {
        long timeStamp = System.currentTimeMillis() - deleteTimeToMillis;

        Connection conn = DbManager.getConnection();
        String query = "DELETE FROM etex_core_stash WHERE timestamp < ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setLong(1, timeStamp);
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        instance.getLogger().info("Old stash items were purged!");
    }





}
