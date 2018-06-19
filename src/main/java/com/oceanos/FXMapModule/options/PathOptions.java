package com.oceanos.FXMapModule.options;


/**
 * @autor slonikmak on 19.06.2018.
 */
public class PathOptions extends LayerOptions{

    @Override
    void init() {
        setOption("stroke", false);
        setOption("color", "#3388ff");
        setOption("weight", 3);
        setOption("opacity", 1.0);
        setOption("fill", false);
        setOption("fillColor", "#3388ff");
        setOption("fillOpacity", 0.2);
    }
}