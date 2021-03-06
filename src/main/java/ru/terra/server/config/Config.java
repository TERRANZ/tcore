package ru.terra.server.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Vadim Korostelev
 * Date: 09.09.13
 * Time: 15:46
 */
public class Config {

    private PropertiesConfiguration configuration;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static Config instance = new Config();

    public static Config getConfig() {
        return instance;
    }

    private Config() {
        try {
            configuration = new PropertiesConfiguration("tm.properties");
            configuration.setEncoding("UTF-8");
            configuration.setAutoSave(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getValue(String key, String defVal) {
        if (configuration == null)
            return defVal;
        return configuration.getString(key, defVal);
    }


    public void setConfig(String key, String val) {
        if (configuration != null)
            configuration.setProperty(key, val);
    }
}
