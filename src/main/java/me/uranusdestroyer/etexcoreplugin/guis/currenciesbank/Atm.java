package me.uranusdestroyer.etexcoreplugin.guis.currenciesbank;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.uranusdestroyer.etexcoreplugin.backend.ConfigFiles;
import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Currencies;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.CurrencyUtils;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Atm {

    YamlDocument config = ConfigFiles.getConfig();
    YamlDocument atm = ConfigFiles.getGuiTemplate(config.getString("currencies-bank.bank.atm-gui-template"));


    private Gui createGui(Player player){
        Gui gui = Gui.gui()
                .title(MessageUtils.componentFromString(atm.getString("title")))
                .type(GuiType.valueOf(atm.getString("inventory-type")))
                .rows(atm.getInt("rows"))
                .create();

        Section currencyList = config.getSection("currencies-bank.currency-list");

        int index = 0;
        for(Object key : currencyList.getKeys()){

            int slot = atm.getIntList("currencies.slots").get(index);
            ItemStack currencyItem = ItemHandler.itemStackFromString(config.getString("currencies-bank.currency-list." + key));
            ItemMeta currencyMeta = currencyItem.getItemMeta();
            currencyMeta.setLore(
                    MessageUtils.legacyMinimessageStringList(
                            CurrencyUtils.replaceCustomPlaceholders(atm.getStringList("currencies.lore"), "{currency_name}", (String) key), player
                    )
            );
            currencyItem.setItemMeta(currencyMeta);

            GuiItem guiItem = ItemBuilder.from(currencyItem).asGuiItem(event -> event.setCancelled(true));

            gui.setItem(slot,guiItem);

            index++;
        }

        if(atm.getBoolean("fill-item.enabled")){
            ItemStack fillItemStack = ItemHandler.itemStackFromString(atm.getString("fill-item.etexString"));
            GuiItem guiItem = ItemBuilder.from(fillItemStack).asGuiItem(event -> event.setCancelled(true));
            gui.getFiller().fill(guiItem);
        }

        if(atm.getBoolean("deposit-all-button.enabled")){
            ItemStack depositItemStack = ItemHandler.itemStackFromString(atm.getString("deposit-all-button.etexString"));
            GuiItem guiItem = ItemBuilder.from(depositItemStack).asGuiItem(event -> event.setCancelled(true));
            for(int slot : atm.getIntList("deposit-all-button.slots")){
                gui.setItem(slot, guiItem);
            }
        }


        return gui;
    }

    public void openAtm(Player player){
        createGui(player).open(player);
    }

}
