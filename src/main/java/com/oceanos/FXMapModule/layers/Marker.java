package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.oceanos.FXMapModule.events.EditableEvent;
import com.oceanos.FXMapModule.events.LayerEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.options.MarkerOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import netscape.javascript.JSObject;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Marker extends Layer {
    public static String jSController = "markerController";
    public static JSObject jsObject;
    private static Gson gson;

    static {
        gson = new Gson();
    }

    private Icon icon;
    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();

    public Marker(){
        this.setOptions(new MarkerOptions());
        getOptions().setChangeListener((c)->{
            update();
        });
        addEventListener(MapEventType.move, (event -> {
            lat.setValue(((LayerEvent)event).getLat());
            lng.setValue(((LayerEvent)event).getLng());
        }));

    }

    public Marker(double lat, double lng){
        this();
        this.lat.setValue(lat);
        this.lng.setValue(lng);
    }


    @Override
    public void remove() {

    }

    public void addToMap() {
        Gson gson = new Gson();
        int value = (int) jsObject.call("addMarker", lat.get(), lng.get(), getOptions().getJson(), gson.toJson(icon));
        id = value;
    }

    public void setIcon(String src){
        this.icon = new Icon(src);
        String json =  gson.toJson(icon);
        if (isOnMap()) jsObject.call("setIcon", id, json);
    }

    public Icon getIcon(){
        return icon;
    }

    public double getLat() {
        return lat.get();
    }

    public DoubleProperty latProperty() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat.set(lat);
    }

    public double getLng() {
        return lng.get();
    }

    public DoubleProperty lngProperty() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng.set(lng);
    }

    private void update(){
        jsObject.call("update", id, getOptions().getJson());
    }

    private boolean isOnMap(){
        return id != 0;
    }

}
