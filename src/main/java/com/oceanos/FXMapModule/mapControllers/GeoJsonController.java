package com.oceanos.FXMapModule.mapControllers;

import netscape.javascript.JSObject;

public class GeoJsonController {
    public static String jSController = "geoJsonController";
    public static JSObject jsObject;

    public static void addLayerFromJso(String json){
        jsObject.call("addLayer", json);
    }
}
