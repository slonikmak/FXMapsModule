package com.oceanos.FXMapModule.layers.mission;

import com.google.gson.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.hildan.fxgson.FxGson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

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
        waypoint.put("rudder_range", new SimpleIntegerProperty());

        ObservableMap<String, Property> gps_fix = FXCollections.observableHashMap();
        behaviors.put("gps_fix", gps_fix);
        gps_fix.put("fix_rate", new SimpleIntegerProperty());
        gps_fix.put("finish_on_gps_fix", new SimpleIntegerProperty());
        gps_fix.put("time_out", new SimpleIntegerProperty());
        gps_fix.put("vbe_bow", new SimpleIntegerProperty());
        gps_fix.put("ballast_pitch", new SimpleIntegerProperty());
        gps_fix.put("ballast_roll", new SimpleIntegerProperty());
        gps_fix.put("vbe_aft", new SimpleIntegerProperty());

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

    Behaviors (JsonArray object){
        this();
        object.forEach(e->{
            String behaviorName = e.getAsJsonObject().get("name").getAsString();
            ObservableMap<String, Property> behavior = behaviors.get(behaviorName);
            behavior.forEach((s, property) -> {
                System.out.println(s);
                if (property instanceof DoubleProperty){
                    setProperty(behaviorName, s, e.getAsJsonObject().get(s).getAsDouble());
                } else if (property instanceof IntegerProperty){
                    setProperty(behaviorName, s, e.getAsJsonObject().get(s).getAsInt());
                } else if (property instanceof BooleanProperty){
                    setProperty(behaviorName, s, e.getAsJsonObject().get(s).getAsBoolean());
                } else if (property instanceof StringProperty){
                    setProperty(behaviorName, s, e.getAsJsonObject().get(s).getAsString());
                }
            });
        });
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

    public JsonArray getJsonObject(){
        Gson gson = FxGson.fullBuilder()
                .setPrettyPrinting()
                .create();
        JsonArray array = new JsonArray();
        behaviors.forEach((s, stringPropertyObservableMap) -> {
            JsonObject behavior = gson.toJsonTree(stringPropertyObservableMap).getAsJsonObject();
            behavior.addProperty("name", s);
            array.add(behavior);
        });
        return array;
    }
}
