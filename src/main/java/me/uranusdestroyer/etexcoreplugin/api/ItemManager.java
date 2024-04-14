package me.uranusdestroyer.etexcoreplugin.api;

import me.uranusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemManager {

    /*
    Give item stack to player directly to stash
     */
    public void stashAdd(UUID uuid, ItemStack stack) { Stash.add(uuid, stack);}


    /*
    Give item stack to player - if full inv -> add to stash
     */
    public void stashAdd(Player player, ItemStack stack) { Stash.add(player, stack);}

    /*
    Get item stack from etex string
     */
    public ItemStack getItemStack(String etexString) { return ItemHandler.itemStackFromString(etexString);}
}
