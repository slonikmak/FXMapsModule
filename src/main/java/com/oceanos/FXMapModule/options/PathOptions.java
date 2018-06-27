package com.oceanos.FXMapModule.options;


/**
 * @autor slonikmak on 19.06.2018.
 */
public class PathOptions extends LayerOptions{

    @Override
    void init() {
        setOption("stroke", true);
        setOption("color", "#8377ff");
        setOption("weight", 8.0);
        setOption("opacity", 1.0);
        setOption("fill", false);
        setOption("fillColor", "#3388ff");
        setOption("fillOpacity", 0.2);
        setOption("bubblingMouseEvents", false);
        setOption("editable", false);
    }
}