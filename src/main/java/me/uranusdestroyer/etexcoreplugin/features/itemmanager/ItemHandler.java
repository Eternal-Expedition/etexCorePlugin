package me.uranusdestroyer.etexcoreplugin.features.itemmanager;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    /*
    String types:

           TYPE:CATEGORY:ID:COUNT                                  // input & output
           PLACEHOLDER:DESCRIPTION:VALUE:OPERATION:COMPAREtoVALUE       // input
           COMMAND:PLAYER:DESCRIPTION:COMMAND                           // output

     */


    public static ItemStack itemStackFromString (String item) {
        ItemStack stack;

        String[] itemArray = item.split(":");

        String arg1 = itemArray[0].toLowerCase();
        String arg2 = (itemArray.length > 1) ? itemArray[1] : null;
        String arg3 = (itemArray.length > 2) ? itemArray[2] : null;
        String arg4 = (itemArray.length > 3) ? itemArray[3] : null;
        String arg5 = (itemArray.length > 4) ? itemArray[4] : null;

        ItemMeta itemMeta;


        // TODO: make this better - minimizing dmg caused by improper configuration
        try {
            switch (arg1) {
                case "vanilla":
                case "vl":
                    stack = new ItemStack(Material.getMaterial(arg2.toUpperCase()));
                    if (arg3 != null) {
                        stack.setAmount(Integer.parseInt(arg3));
                    }

                    if (arg4 != null) {
                        itemMeta = stack.getItemMeta();
                        itemMeta.setDisplayName(arg4);
                        itemMeta.setCustomModelData(Integer.parseInt(arg5));
                    }

                    break;

                case "slimefun":
                case "sf":

                    SlimefunItem sfItem = SlimefunItem.getById(arg2);
                    stack = new ItemStack(sfItem.getItem());
                    if (arg3 != null) stack.setAmount(Integer.parseInt(arg3));

                    break;

                case "itemsadder":
                case "ia":

                    CustomStack iaItem = CustomStack.getInstance(arg2 + ":" + arg3);
                    stack = new ItemStack(iaItem.getItemStack());
                    if (arg4 != null) {
                        stack.setAmount(Integer.parseInt(arg4));
                    }
                    break;

                case "mmoitems":
                case "mmoitem":
                case "mi":

                    MMOItem mmoItem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(arg2), arg3 );
                    stack = new ItemStack(mmoItem.newBuilder().build());
                    if (arg4 != null) {
                        stack.setAmount(Integer.parseInt(arg4));
                    }
                    break;
                default:
                    throw new RuntimeException("Item error: " + item);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return stack;
    }

    public static List<ItemStack> listOfItemStacks (ItemStack itemStack, int value) {
        final int MAX_STACK_SIZE = 64;
        List<ItemStack> stacks = new ArrayList<>();

        while (value > 0) {
            int stackSize = Math.min(value, MAX_STACK_SIZE);

            ItemStack stack = itemStack.clone();
            stack.setAmount(stackSize);
            stacks.add(stack);

            value -= stackSize;
        }

        return stacks;
    }

    public static boolean hasItem (String item, Player player, boolean remove) {

        String[] itemArray = item.split(":");

        String arg1 = itemArray[0].toLowerCase();
        String arg2 = (itemArray.length > 1) ? itemArray[1] : null;
        String arg3 = (itemArray.length > 2) ? itemArray[2] : null;
        String arg4 = (itemArray.length > 3) ? itemArray[3] : null;
        String arg5 = (itemArray.length > 4) ? itemArray[4] : null;

        int count = 0;
        int needed;

        switch (arg1) {
            case "vanilla":
            case "vl":

                needed = Integer.parseInt(arg3);

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem != null && checkedItem.getType().equals(Material.getMaterial(arg2.toUpperCase())) && !checkedItem.getItemMeta().hasDisplayName()) {

                        count += checkedItem.getAmount();

                        if (remove) {
                            int amountToRemove = Math.min(checkedItem.getAmount(), needed);
                            checkedItem.setAmount(checkedItem.getAmount() - amountToRemove);

                            if (checkedItem.getAmount() <= 0) {
                                player.getInventory().removeItem(checkedItem);
                            }

                            needed -= amountToRemove;
                            if (needed <= 0) {
                                break;
                            }
                        }
                    }
                }

                return count >= Integer.parseInt(arg3);

            case "slimefun":
            case "sf":

                needed = Integer.parseInt(arg3);

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem sfitem = new NBTItem(checkedItem);
                    NBTCompound tag = sfitem.getCompound("PublicBukkitValues");
                    if (tag != null && tag.getString("slimefun:slimefun_item").equals(arg2)) {
                        count += checkedItem.getAmount();

                        if (remove) {
                            int amountToRemove = Math.min(checkedItem.getAmount(), needed);
                            checkedItem.setAmount(checkedItem.getAmount() - amountToRemove);

                            if (checkedItem.getAmount() <= 0) {
                                player.getInventory().removeItem(checkedItem);
                            }

                            needed -= amountToRemove;
                            if (needed <= 0) {
                                break;
                            }
                        }
                    }
                    return count >= needed;
                }

            case "itemadder":
            case "ia":
                needed = Integer.parseInt(arg4);

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem IAitem = new NBTItem(checkedItem);
                    NBTCompound tag = IAitem.getCompound("itemsadder");
                    if (tag != null && tag.getString("id").equals(arg3) && tag.getString("namespace").equals(arg2)) {
                        count += checkedItem.getAmount();

                        if (remove) {
                            int amountToRemove = Math.min(checkedItem.getAmount(), needed);
                            checkedItem.setAmount(checkedItem.getAmount() - amountToRemove);

                            if (checkedItem.getAmount() <= 0) {
                                player.getInventory().removeItem(checkedItem);
                            }

                            needed -= amountToRemove;
                            if (needed <= 0) {
                                break;
                            }
                        }
                    }
                }

                return count >= needed;

            case "mmoitems":
            case "mmoitem":
            case "mi":
                needed = Integer.parseInt(arg4);

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem MIitem = new NBTItem(checkedItem);
                    if (MIitem.getString("MMOITEMS_ITEM_ID").equals(arg3) && MIitem.getString("MMOITEMS_ITEM_TYPE").equals(arg2)) {
                        count += checkedItem.getAmount();
                        if (remove) {
                            int amountToRemove = Math.min(checkedItem.getAmount(), needed);
                            checkedItem.setAmount(checkedItem.getAmount() - amountToRemove);
                            if (checkedItem.getAmount() <= 0) {
                                player.getInventory().removeItem(checkedItem);
                            }

                            needed -= amountToRemove;
                            if (needed <= 0) {
                                break;
                            }
                        }
                    }
                }
                return count >= needed;

            case "placeholder":
            case "papi":
                String firstvalue = PlaceholderAPI.setPlaceholders(player, arg2);
                String operation = arg3;
                String secondvalue = PlaceholderAPI.setPlaceholders(player, arg4);

                switch (operation) {
                    case "==":
                        return firstvalue.equalsIgnoreCase(secondvalue);
                    case "<":
                        return Integer.parseInt(firstvalue) < Integer.parseInt(secondvalue);
                    case "<=":
                        return Integer.parseInt(firstvalue) <= Integer.parseInt(secondvalue);
                    case ">":
                        return Integer.parseInt(firstvalue) > Integer.parseInt(secondvalue);
                    case ">=":
                        return Integer.parseInt(firstvalue) >= Integer.parseInt(secondvalue);
                }
                break;
        } return false;
    }
    public static int getItemCount (String item, Player player) {

        String[] itemArray = item.split(":");

        String arg1 = itemArray[0].toLowerCase();
        String arg2 = (itemArray.length > 1) ? itemArray[1] : null;
        String arg3 = (itemArray.length > 2) ? itemArray[2] : null;
        String arg4 = (itemArray.length > 3) ? itemArray[3] : null;
        String arg5 = (itemArray.length > 4) ? itemArray[4] : null;

        int count = 0;
        switch (arg1) {
            case "vanilla":
            case "vl":

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem != null && checkedItem.getType().equals(Material.getMaterial(arg2.toUpperCase())) && !checkedItem.getItemMeta().hasDisplayName()) {

                        count += checkedItem.getAmount();

                    }
                }
                return count;

            case "slimefun":
            case "sf":

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem sfitem = new NBTItem(checkedItem);
                    NBTCompound tag = sfitem.getCompound("PublicBukkitValues");
                    if (tag != null && tag.getString("slimefun:slimefun_item").equals(arg2)) {
                        count += checkedItem.getAmount();

                    }

                    return count;
                }

            case "itemadder":
            case "ia":

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem IAitem = new NBTItem(checkedItem);
                    NBTCompound tag = IAitem.getCompound("itemsadder");
                    if (tag != null && tag.getString("id").equals(arg3) && tag.getString("namespace").equals(arg2)) {
                        count += checkedItem.getAmount();

                    }
                }

                return count;

            case "mmoitems":
            case "mmoitem":
            case "mi":

                for (ItemStack checkedItem : player.getInventory().getContents()) {
                    if (checkedItem == null) continue;
                    NBTItem MIitem = new NBTItem(checkedItem);
                    if (MIitem.getString("MMOITEMS_ITEM_ID").equals(arg3) && MIitem.getString("MMOITEMS_ITEM_TYPE").equals(arg2)) {
                        count += checkedItem.getAmount();
                    }
                }
                return count;
        }
        return 0;
    }


    public static void giveItem (String item, Player player) {
        ItemStack giveItem = itemStackFromString(item);
        giveItem(giveItem, player);
    }

    public static void giveItem (ItemStack item, Player player) {
        player.getInventory().addItem(item);
    }


}