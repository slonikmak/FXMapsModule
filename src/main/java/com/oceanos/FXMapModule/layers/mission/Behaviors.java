package com.oceanos.FXMapModule.layers.mission;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Behaviors {
    private ObservableMap<String, ObservableMap<String, Property>> behaviors = FXCollections.observableHashMap();

    Behaviors(){
        ObservableMap<String, Property> dive =FXCollections.observableHashMap();
        behaviors.put("dive", dive);
        dive.put("vbe_bow", new SimpleIntegerProperty());
        dive.put("ballast_pitch", new SimpleIntegerProperty());
        dive.put("ballast_roll", new SimpleIntegerProperty());
        dive.put("vbe_aft", new SimpleIntegerProperty());
        dive.put("time_out", new SimpleIntegerProperty());

        ObservableMap<String, Property> waypoint = FXCollections.observableHashMap();
        behaviors.put("waypoint", waypoint);
        waypoint.put("lock_rudder", new SimpleBooleanProperty());
        waypoint.put("time_out", new SimpleIntegerProperty());

        ObservableMap<String, Property> gps_fix = FXCollections.observableHashMap();
        behaviors.put("gps_fix", gps_fix);
        gps_fix.put("fix_rate", new SimpleIntegerProperty());
        gps_fix.put("finish_on_gps_fix", new SimpleIntegerProperty());
        gps_fix.put("time_out", new SimpleIntegerProperty());

        ObservableMap<String, Property> surface = FXCollections.observableHashMap();
        behaviors.put("surface", surface);
        surface.put("vbe_bow", new SimpleIntegerProperty());
        surface.put("ballast_pitch", new SimpleIntegerProperty());
        surface.put("ballast_roll", new SimpleIntegerProperty());
        surface.put("vbe_aft", new SimpleIntegerProperty());
        surface.put("time_out", new SimpleIntegerProperty());

        ObservableMap<String, Property> obstacle_detection = FXCollections.observableHashMap();
        behaviors.put("obstacle_detection", obstacle_detection);
        obstacle_detection.put("enabled", new SimpleBooleanProperty());
        obstacle_detection.put("time_period", new SimpleIntegerProperty());
        obstacle_detection.put("distance_length", new SimpleIntegerProperty());
    }

    public Property getProperty(String behaviorName, String parametrName){
        return behaviors.get(behaviorName).get(parametrName);
    }
    public void setProperty(String behaviorName, String parametrName, Object value){
        behaviors.get(behaviorName).get(parametrName).setValue(value);
    }

    public ObservableMap<String, ObservableMap<String, Property>> getBehaviors(){
        return this.behaviors;
    }
}
