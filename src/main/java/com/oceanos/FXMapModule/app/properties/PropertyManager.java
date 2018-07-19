package com.oceanos.FXMapModule.app.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

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


    public String getMissionStylesFile() {
        return properties.getProperty("mission.default.options");
    }

    public String getWaypointStylesFile() {
        return properties.getProperty("waypoint.default.options");
    }

    public String getDefaultStylesFolder() {
        return properties.getProperty("styles.default.folder");
    }

    public String getLayersFolder(){
        return properties.getProperty("layers.folder");
    }

    public String getLayersTileFolder(){
        return properties.getProperty("layers.tile.folder");
    }

    public String getLayersWmsFolder(){
        return properties.getProperty("layers.wms.folder");
    }

    public String getLayersWmsFile(){
        return properties.getProperty("layers.wms.file");
    }

    public String getLayersTilesFile(){
        return properties.getProperty("layers.tiles.file");
    }
}
