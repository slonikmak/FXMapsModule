package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.options.LayerOptions;
import javafx.beans.property.DoubleProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class TileLayer extends Layer {

    public static String jSController = "tileLayerController";
    public static JSObject jsObject;

    private String url;
    private LayerOptions options;

    public TileLayer(String url){
        this.url = url;
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
        int a = (int) jsObject.call("addTileLayer", url, options);
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
        return null;
    }

}
