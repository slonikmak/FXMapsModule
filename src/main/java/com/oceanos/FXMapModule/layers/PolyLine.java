package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class PolyLine extends Path {
    public static String jSController = "polyLineController";
    public static JSObject jsObject;
    public static Gson gson;

    static {
        gson = new Gson();
    }

    private ObservableList<LatLng> latLngs = FXCollections.observableArrayList();

    public PolyLine(){
    }

    public PolyLine(List<LatLng> latLngs){
        this.latLngs.addAll(latLngs);
    }

    public void addLatLng(double lat, double lng){
        latLngs.add(new LatLng(lat, lng));
    }

    public void updateLatLng(){
        String latLngString = (String) jsObject.call("getLatLngs", id);
        List<LatLng> list = gson.fromJson(latLngString, List.class);
    }

    @Override
    void redraw() {
        jsObject.call("redraw", this.id);
    }

    @Override
    public void remove() {
        jsObject.call("remove", this.id);
    }

    @Override
    public void addToMap() {
        Gson gson = new Gson();
        String latlngs = gson.toJson(new ArrayList<>(this.latLngs));
        Object value = jsObject.call("addPolyline",latlngs,getOptions().getJson());
        id = (int) value;
    }
}
