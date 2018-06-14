package com.oceanos.FXMapModule.layers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Marker extends MapLayer {
    static String jSController = "MarkerController";
    static JSObject jsObject;


    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();

    public Marker(double lat, double lng){
        this.lat.setValue(lat);
        this.lng.setValue(lng);
    }

    @Override
    public void remove() {

    }

    public void addToMap() {
        id = (long) jsObject.call("createMarker", lat.get(), lng.get());
    }
}
