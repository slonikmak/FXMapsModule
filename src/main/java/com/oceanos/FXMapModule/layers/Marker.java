package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Marker extends Layer {
    public static String jSController = "markerController";
    public static JSObject jsObject;

    private Map<String,String> options;
    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();

    public Marker(double lat, double lng){
        this.lat.setValue(lat);
        this.lng.setValue(lng);
        options = new HashMap<>();
    }
    public Marker(double lat, double lng, Map<String, String> options){
       this(lat, lng);
        this.options = options;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void addOption(String key, String value) {
        options.put(key, value);
    }

    @Override
    public void remove() {

    }

    public void addToMap() {
        Gson gson = new Gson();
        int value = (int) jsObject.call("addMarker", lat.get(), lng.get(), gson.toJson(options));
        id = value;
    }


}
