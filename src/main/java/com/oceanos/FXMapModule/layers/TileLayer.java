package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.options.LayerOptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class TileLayer extends Layer {

    public static String jSController = "tileLayerController";
    public static JSObject jsObject;

    private StringProperty url = new SimpleStringProperty();
    private LayerOptions options;

    public TileLayer(String url){
        this.url.setValue(url);
    }

    public TileLayer(String url, LayerOptions options){
        this(url);
        this.options = options;
    }



    @Override
    public void setOptions(LayerOptions options) {

    }

    @Override
    public LayerOptions getOptions() {
        return null;
    }

    public void addToMap(){
        System.out.println("add tile to map");
        int a = (int) jsObject.call("addTileLayer", url.get(), options);
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
        object.addProperty("url", getUrl());
        return object;
    }

    public StringProperty urlProperty(){
        return url;
    }

    public String getUrl(){
        return url.getValue();
    }

    public static TileLayer getFromJson(String toString) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(toString).getAsJsonObject();
        TileLayer tileLayer = new TileLayer(object.get("url").getAsString());
        tileLayer.setName(object.get("name").getAsString());
        return tileLayer;
    }



}
