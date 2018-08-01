package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.options.PathOptions;
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
        Object value = jsObject.call("addPolygon",latlngs, getOptions().getJsonString());
        id = (int) value;
    }

    //FIXME: разделить как-то с родителем
    public void updateOptions() {
        super.updateOptions();
       /* System.out.println("update");
        String result = (String) jsObject.call("getOptions", getId());
        getOptions().fillOptions(result);
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
        setLength((Double) jsObject.call("getLength", id));*/
    }
    public static Polygon getFromJson(String json){
        Polygon polygone = new Polygon();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonObject properties = object.get("properties").getAsJsonObject();
        JsonArray coords = object.getAsJsonObject("geometry").getAsJsonArray("coordinates");
        coords.forEach(c->{
            polygone.addLatLng(c.getAsJsonArray().get(1).getAsDouble(), c.getAsJsonArray().get(0).getAsDouble());
        });
        PathOptions options = (PathOptions) polygone.getOptions();
        options.fillOptions(properties.toString());
        polygone.setOptions(options);
        polygone.setName(properties.get("name").getAsString());
        return polygone;
    }
}
