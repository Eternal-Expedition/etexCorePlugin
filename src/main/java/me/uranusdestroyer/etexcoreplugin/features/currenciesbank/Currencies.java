package me.uranusdestroyer.etexcoreplugin.features.currenciesbank;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.uranusdestroyer.etexcoreplugin.backend.ConfigFiles;
import me.uranusdestroyer.etexcoreplugin.backend.DbManager;
import me.uranusdestroyer.etexcoreplugin.etexCorePlugin;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.ItemHandler;
import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class Currencies {

    private final etexCorePlugin instance;

    public Currencies(etexCorePlugin pluginInstance) {
        instance = pluginInstance;

        Section currencyList = ConfigFiles.getConfig().getSection("currencies-bank.currency-list");
        if (currencyList != null) {
            Set<Object> currencyNames = currencyList.getKeys();
            for (Object currencyName : currencyNames) {
                DbManager.createCurrencyTable((String) currencyName);
                instance.getLogger().info("Currency table created: " + currencyName);
            }
        } else {
            instance.getLogger().info("Currency list doesn't exist or is empty!");
        }

    }

    public static String getCurrencyString(String currencyName) {
        return ConfigFiles.getConfig().getString("currencies-bank.currency-list" + currencyName);
    }

    public static Set<Object> getCurrencies() {
        return ConfigFiles.getConfig().getSection("currencies-bank.currency-list").getKeys();
    }

    public static int getValue( Player p, String currencyName ) {
        return 0;
    }

    public static void addValue( Player p, String currencyName, int value ) {
        ItemStack item = ItemHandler.itemStackFromString(currencyName);
        List<ItemStack> list = ItemHandler.listOfItemStacks(item, value);
        for (ItemStack stack : list) {
            Stash.add(p, stack);
        }

    }

    public static void deductValue( Player p, String currencyName, int value ) {
        ItemHandler.hasItem(getCurrencyString(currencyName), p, true);
    }

    public static boolean hasValue(Player p, String currencyName, int value) {
        return ItemHandler.hasItem(getCurrencyString(currencyName), p, false);
    }
}
