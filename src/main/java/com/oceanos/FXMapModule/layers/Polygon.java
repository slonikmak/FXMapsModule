package com.oceanos.FXMapModule.layers;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.LineString;
import com.github.filosganga.geogson.model.MultiLineString;
import com.github.filosganga.geogson.model.Point;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.oceanos.FXMapModule.options.PathOptions;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public String convertToJson() {
        System.out.println("!!!!Convert");
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GeometryAdapterFactory())
                .create();

        String result = (String) jsObject.call("toGeoJson", this.getId());
        Feature feature = gson.fromJson(result, Feature.class);
        HashMap<String, JsonElement> map = Maps.newHashMap(feature.properties());
        map.put("name", new JsonPrimitive(getName()));
        feature = feature.withProperties(ImmutableMap.copyOf(map));

        System.out.println(gson.toJson(feature));
        return gson.toJson(feature);
    }

    public static Polygon getFromJson(String json){
        Polygon polygon = new Polygon();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonObject properties = object.get("properties").getAsJsonObject();
        JsonArray coords = object.getAsJsonObject("geometry").getAsJsonArray("coordinates").get(0).getAsJsonArray();
        coords.forEach(c->{
            System.out.println("coords " +c.getAsJsonArray().get(1).getAsDouble()+" "+ c.getAsJsonArray().get(0).getAsDouble());
            polygon.addLatLng(c.getAsJsonArray().get(1).getAsDouble(), c.getAsJsonArray().get(0).getAsDouble());
        });
        PathOptions options = (PathOptions) polygon.getOptions();
        options.fillOptions(properties.toString());
        polygon.setOptions(options);
        polygon.setName(properties.get("name").getAsString());
        return polygon;
    }
}
