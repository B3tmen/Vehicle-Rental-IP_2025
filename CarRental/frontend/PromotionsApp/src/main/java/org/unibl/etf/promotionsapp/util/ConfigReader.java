package org.unibl.etf.promotionsapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_PATH = "app.properties";
    public static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_PATH)){
            properties = new Properties();
            properties.load(is);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigReader getInstance() {
        if(instance == null)
            instance = new ConfigReader();

        return instance;
    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getDatabaseURL(){
        return this.getProperty("db.url");
    }

    public String getDatabaseUsername(){
        return this.getProperty("db.username");
    }

    public String getDatabasePassword(){
        return this.getProperty("db.password");
    }

    public int getPreconnectCount(){
        return Integer.parseInt(this.getProperty("preconnectCount"));
    }

    public int getMaxIdleConnections(){
        return Integer.parseInt(this.getProperty("maxIdleConnections"));
    }

    public int getMaxConnections(){
        return Integer.parseInt(this.getProperty("maxConnections"));
    }
}
