package me.uranusdestroyer.etexcoreplugin.api;

import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Bank;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Currencies;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Transaction;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CurrenciesBank {

    public static boolean executeTransaction(Player p, String currencyName, int value) { return Transaction.executeTransaction(p, currencyName, value); }

    public static void addValue(UUID uuid, String currencyName, int value) { Bank.addValue(uuid, currencyName, value); }

    public static void deductValue(UUID uuid, String currencyName, int value) { Bank.deductValue(uuid, currencyName, value); }

    public static int getValue(UUID uuid, String currencyName) { return Bank.getValue(uuid, currencyName); }

    public static int getInvValue(Player player, String currencyName) { return Currencies.getValue(player, currencyName); }
}
