package me.uranusdestroyer.etexcoreplugin.api;

import lombok.Getter;

public class API {

    /*
    Just a test function
     */
    public String ping() { return "pong";}


    @Getter
    DatabaseManager databaseManager = new DatabaseManager();
    @Getter
    ItemManager itemManager = new ItemManager();

}
