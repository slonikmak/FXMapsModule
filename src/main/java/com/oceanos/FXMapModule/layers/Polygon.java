package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.options.OptionsManager;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Polygon extends PolyLine {
    public static String jSController = "polygonController";
    public static JSObject jsObject;

    @Override
    public void addToMap(){
        String latlngs = gson.toJson(new ArrayList<>(getLatLngs()));
        Object value = jsObject.call("addPolygon",latlngs, OptionsManager.getOptionsJson(this));
        id = (int) value;
    }
}
