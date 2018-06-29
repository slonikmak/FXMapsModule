package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.options.OptionsManager;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Polygon extends PolyLine {
    public static String jSController = "polygonController";
    public static JSObject jsObject;

    public Polygon(){
        super();
        setName("polygon");
    }

    @Override
    public void addToMap(){
        String latlngs = gson.toJson(new ArrayList<>(getLatLngs()));
        Object value = jsObject.call("addPolygon",latlngs, OptionsManager.getOptionsJson(this));
        id = (int) value;
    }

    //FIXME: разделить как-то с родителем
    public void updateOptions() {
        System.out.println("update");
        String result = (String) jsObject.call("getOptions", getId());
        OptionsManager.fillOptions(this, result);
        String latlngsString = (String) jsObject.call("getLatLngs", id);
        System.out.println(latlngsString);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(latlngsString).getAsJsonArray();
        getLatLngs().clear();
        array.forEach(l->{
            JsonArray latlngArray = l.getAsJsonArray();
            getLatLngs().add(new LatLng(latlngArray.get(0).getAsDouble(), latlngArray.get(1).getAsDouble()));
        });
        setPoints(getLatLngs().size());
        setLength((Double) jsObject.call("getLength", id));
    }
}
