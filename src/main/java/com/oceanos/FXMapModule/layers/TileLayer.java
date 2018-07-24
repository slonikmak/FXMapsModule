package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.TileEvent;
import com.oceanos.FXMapModule.options.LayerOptions;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class TileLayer extends Layer {

    public static String jSController = "tileLayerController";
    public static JSObject jsObject;

    private StringProperty url = new SimpleStringProperty();
    private LayerOptions options;
    private BooleanProperty cashed = new SimpleBooleanProperty(false);

    public TileLayer(String url){
        this.url.setValue(url);

        ////FIXME: перенести кеширование в другое место
        cashed.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                Path path = ResourceManager.getInstance().getLayersCashFolder().resolve(getName());
                if (!Files.exists(path)){
                    try {
                        System.out.println("create cashe dir "+path);
                        Files.createDirectory(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        addEventListener(MapEventType.tileload, (e)->{

            if (cashed.get()){
                TileEvent event = (TileEvent) e;

            }

        });
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

    public boolean isCashed() {
        return cashed.get();
    }

    public BooleanProperty cashedProperty() {
        return cashed;
    }

    public static TileLayer getFromJson(String toString) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(toString).getAsJsonObject();
        TileLayer tileLayer = new TileLayer(object.get("url").getAsString());
        tileLayer.setName(object.get("name").getAsString());
        return tileLayer;
    }





}
