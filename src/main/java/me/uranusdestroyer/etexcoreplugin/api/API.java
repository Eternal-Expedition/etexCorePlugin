package me.uranusdestroyer.etexcoreplugin.api;

import lombok.Getter;
import me.uranusdestroyer.etexcoreplugin.backend.MessageUtils;

public class API {

    /*
    Just a function to make sure API is working
     */
    public String ping() { return "pong";}


    @Getter
    DatabaseManager databaseManager = new DatabaseManager();
    @Getter
    ItemManager itemManager = new ItemManager();
    @Getter
    CurrenciesBank currenciesBank = new CurrenciesBank();

    @Getter
    MessageUtils messageUtils = new MessageUtils();

}
