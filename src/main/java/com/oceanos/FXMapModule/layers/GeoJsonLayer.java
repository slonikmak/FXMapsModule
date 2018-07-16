package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.options.LayerOptions;
import netscape.javascript.JSObject;

public class GeoJsonLayer extends Layer {
    public static String jSController = "geoJsonController";
    public static JSObject jsObject;

    private String string;

    public GeoJsonLayer(String geoJsonString){
        this.string = geoJsonString;
    }

    @Override
    public void setOptions(LayerOptions options) {

    }

    @Override
    public LayerOptions getOptions() {
        return null;
    }

    @Override
    public void addToMap() {
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(string).getAsJsonObject();
        JsonObject properties = object.getAsJsonObject("properties");
        if (properties != null){
            JsonElement name = properties.get("name");
            if (name!=null) {
                setName(name.getAsString());
            }
        }

        int result = (int) jsObject.call("addLayer", string);
        setId(result);
    }

    @Override
    public void remove() {
        jsObject.call("removeLayer", getId());
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public String convertToJson() {
        return null;
    }
}
