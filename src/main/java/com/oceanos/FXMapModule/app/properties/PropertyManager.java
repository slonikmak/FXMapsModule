package com.oceanos.FXMapModule.app.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class PropertyManager {

    static private PropertyManager instance;
    static private String defaultPropertyFilePath = "app.properties";

    private Properties properties;

    static public PropertyManager getInstance(){
        if (instance==null) instance = new PropertyManager();
        return instance;
    }

    private PropertyManager(){
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDefaultIconsFolder(){
        return properties.getProperty("icons.folder.default");
    }

    public String getProjectResourceFolderName(){
        return properties.getProperty("project.resource.folder");
    }

    public String getDefaultIcon(){
        return properties.getProperty("icons.icon.default");
    }


}
