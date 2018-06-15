package com.oceanos.FXMapModule.layers;

import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class TileLayer extends Layer {

    public static String jSController = "tileLayerController";
    public static JSObject jsObject;

    private String url;
    private String options;

    public TileLayer(String url, String options){
        this.url = url;
        this.options = options;
    }

    public void addToMap(){
        System.out.println("add tile to map");
        int a = (int) jsObject.call("addTileLayer", url, options);
        id = a;
    }

    @Override
    public void remove() {

    }
}
