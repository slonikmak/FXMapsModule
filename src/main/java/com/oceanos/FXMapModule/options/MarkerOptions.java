package com.oceanos.FXMapModule.options;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class MarkerOptions extends LayerOptions {
    @Override
    void init() {
        setOption("draggable", false);
        setOption("opacity", 1.0);
        setOption("bubblingMouseEvents", false);
    }
}
