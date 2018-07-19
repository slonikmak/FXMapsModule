package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.WmsLayerOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 03.07.2018.
 */
public class WMSTileLayer extends Layer {
    public static String jSController = "wmsLayerController";
    public static JSObject jsObject;

    private StringProperty url = new SimpleStringProperty();
    private LayerOptions options;


    public WMSTileLayer(String url, LayerOptions options) {
        this.url.setValue(url);
        this.options = options;
    }

    @Override
    public void setOptions(LayerOptions options) {

    }

    @Override
    public LayerOptions getOptions() {
        return options;
    }

    @Override
    public void addToMap() {
        System.out.println("add tile to map");
        int a = (int) jsObject.call("addTileLayer", url.get(), options.getJsonString());
        id = a;
    }

    @Override
    public void remove() {
        jsObject.call("removeLayer", this.getId());
    }

    @Override
    public void hide() {
        jsObject.call("hide");
    }

    @Override
    public void show() {
        jsObject.call("show");
    }

    @Override
    public String convertToJson() {
       return convertToRawJsonObject().toString();
    }

    public JsonObject convertToRawJsonObject(){
        JsonObject object = new JsonObject();
        object.addProperty("name", getName());
        object.addProperty("url", url.getValue());
        JsonObject options = getOptions().getJsonObject();
        object.add("options", options);
        return object;
    }

    public String getUrl() {
        return url.getValue();
    }

    public void setUrl(String url) {
        this.url.setValue(url);
    }

    public static WMSTileLayer getFromJson(String json){
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        WmsLayerOptions options = new WmsLayerOptions();
        options.fillOptions(object.get("options").getAsJsonObject().toString());
        WMSTileLayer layer = new WMSTileLayer(object.get("url").getAsString(), options);
        layer.setName(object.get("name").getAsString());
        return layer;
    }

    public StringProperty urlProperty(){
        return url;
    }
}
