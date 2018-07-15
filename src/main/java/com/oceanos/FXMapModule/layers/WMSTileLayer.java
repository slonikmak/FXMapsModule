package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.options.LayerOptions;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 03.07.2018.
 */
public class WMSTileLayer extends Layer {
    public static String jSController = "wmsLayerController";
    public static JSObject jsObject;

    private String url;
    private LayerOptions options;


    public WMSTileLayer(String url, LayerOptions options) {
        this.url = url;
        this.options = options;
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
        System.out.println("add tile to map");
        int a = (int) jsObject.call("addTileLayer", url, options.getJsonString());
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
