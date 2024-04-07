package ur.anusdestroyer.etexcoreplugin.api;

import net.Indyuce.mmoitems.manager.ItemManager;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;
import ur.anusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import ur.anusdestroyer.etexcoreplugin.features.itemmanager.Stash;

import java.sql.Connection;
import java.util.UUID;

public class API {

    /*
    Just a test function
     */
    public String ping(){ return "pong";}


    /*
    Get database connection
     */
    public Connection getConnection() { return DbManager.getConnection();}


    /*
    Get item stack from etex string
     */
    public ItemStack getItemStack(String etex_string) { return ItemHandler.itemStackFromString(etex_string);}


    /*
    Give item stack to player directly to stash
     */
    public void stashAdd(UUID uuid, ItemStack stack) { Stash.add(uuid, stack);}

    /*
    Give item stack to player - if full inv -> add to stash
     */
    public void stashAdd(Player player, ItemStack stack) { Stash.add(player, stack);}



}
