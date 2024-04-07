package ur.anusdestroyer.etexcoreplugin.api;

import ur.anusdestroyer.etexcoreplugin.backend.ConfigFiles;
import ur.anusdestroyer.etexcoreplugin.backend.DbManager;

import java.sql.Connection;

public class API {

    public String getMrdka(){
        return ConfigFiles.getConfig().getString("database.password");
    }

    public Connection getConnection() {
        return DbManager.getConnection();
    }

}
