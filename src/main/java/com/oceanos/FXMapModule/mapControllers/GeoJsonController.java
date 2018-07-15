package com.oceanos.FXMapModule.mapControllers;

import netscape.javascript.JSObject;

public class GeoJsonController {
    public static String jSController = "geoJsonController";
    public static JSObject jsObject;

    public static void addLayerFromJson(String json){
        jsObject.call("addLayer", json);
    }

    public static void addLayerFromGpx(String gpx){

    }
}
