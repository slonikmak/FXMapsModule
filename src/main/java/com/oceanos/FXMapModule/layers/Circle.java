package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oceanos.FXMapModule.options.CircleOptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class Circle extends Path {
    public static String jSController = "circleController";
    public static JSObject jsObject;
    public static Gson gson;

    static {
        gson = new Gson();
    }

    private ObjectProperty<LatLng> latlng = new SimpleObjectProperty<>();

    public Circle(){
        this.setOptions(new CircleOptions());
    }

    public Circle(LatLng latLng, int radius){
        this.latlng.setValue(latLng);
        CircleOptions options = new CircleOptions();
        options.setOption("radius", radius);
        this.setOptions(options);
        this.latlng.addListener((a,b,c)->{
            if (!b.equals(c)){
                jsObject.call("setLatLng", id, gson.toJson(c));
            }
        });
    }

    @Override
    void redraw() {

    }

    @Override
    public void addToMap() {
        id = (int) jsObject.call("addCircle", gson.toJson(latlng.getValue()), getOptions().getJson());
    }

    @Override
    public void remove() {

    }

    public void setLatLng(double lat, double lng){
        this.latlng.setValue(new LatLng(lat, lng));
    }
}
