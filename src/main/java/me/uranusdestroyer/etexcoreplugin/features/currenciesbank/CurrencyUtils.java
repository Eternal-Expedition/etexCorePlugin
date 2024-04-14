package me.uranusdestroyer.etexcoreplugin.features.currenciesbank;

import java.util.ArrayList;
import java.util.List;

public class CurrencyUtils {

    public static String formatted(int currency){
        return String.valueOf(currency);
    }

    public static List<String> replaceCustomPlaceholders(List<String> lore, String placeholder, String replacement){
        List<String> newLore = new ArrayList<>();
        for(String line : lore) newLore.add(line.replace(placeholder, replacement));

        return newLore;
    }

}
