package com.oceanos.FXMapModule.layers;


import com.oceanos.FXMapModule.options.PathOptions;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class Path extends Layer {

    public Path(){
        this.setOptions(new PathOptions());
    }
    abstract void redraw();


}
