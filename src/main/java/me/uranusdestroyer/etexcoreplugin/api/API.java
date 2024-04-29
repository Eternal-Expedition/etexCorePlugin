package me.uranusdestroyer.etexcoreplugin.api;

import lombok.Getter;

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

}
