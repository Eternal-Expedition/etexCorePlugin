package me.uranusdestroyer.etexcoreplugin.features.currenciesbank;

import me.uranusdestroyer.etexcoreplugin.features.itemmanager.Stash;
import org.bukkit.entity.Player;

public class Transaction {

    //private static HashMap<UUID, Boolean> activeTransactions = new HashMap<>();

    public static void currToBank(Player p, String currencyName, int value) {
        if (Currencies.hasValue(p, currencyName, value)) {
            Currencies.deductValue(p, currencyName, value);
            Bank.addValue(p.getUniqueId(), currencyName, value);
        }
    }

    public static void bankToCurr(Player p, String currencyName, int value) {
        if (Bank.hasValue(p.getUniqueId(), currencyName, value)) {
            Bank.deductValue(p.getUniqueId(), currencyName, value);
            Currencies.addValue(p, currencyName, value);
        }
    }

    public static boolean executeTransaction(Player p, String currencyName, int value) {
        if (Currencies.hasValue(p, currencyName, value)) {
            Currencies.deductValue(p, currencyName, value);
            return true;
        } else {
            if (Bank.hasValue(p.getUniqueId(), currencyName, value)) {
                Bank.addValue(p.getUniqueId(), currencyName, value);
                return true;
            } else {
                return false;
            }
        }
    }

    //int value = Currencies.getValue(p, currencyName);

}
