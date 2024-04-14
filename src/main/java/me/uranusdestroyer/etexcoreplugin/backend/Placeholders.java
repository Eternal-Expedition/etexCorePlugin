package me.uranusdestroyer.etexcoreplugin.backend;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.Bank;
import me.uranusdestroyer.etexcoreplugin.features.currenciesbank.CurrencyUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "etexcore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Uranus Destroyer";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        String[] paramsArr = params.split("_", -1);

        switch (paramsArr[0]) {
            case "bank":
                switch (paramsArr[1]) {
                    case "formatted":
                        return CurrencyUtils.formatted(Bank.getValue(player.getUniqueId(), paramsArr[2]));
                }
        }

        return null;
    }
}
