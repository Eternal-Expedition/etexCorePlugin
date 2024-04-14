package me.uranusdestroyer.etexcoreplugin.api;

import me.uranusdestroyer.etexcoreplugin.backend.DbManager;

import java.sql.Connection;

public class DatabaseManager {

    /*
    Get database connection
     */
    public Connection getConnection() { return DbManager.getConnection();}




}
