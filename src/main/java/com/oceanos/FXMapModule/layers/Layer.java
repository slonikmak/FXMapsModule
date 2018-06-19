package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.PathOptions;


/**
 * @autor slonikmak on 14.06.2018.
 */
public abstract class Layer extends Evented {
    protected long id;
    private LayerOptions options;

    public long getId(){
        return id;
    }
    public void setOptions(LayerOptions options){
        this.options = options;
    }
    public LayerOptions getOptions(){
        return this.options;
    }
    public abstract void addToMap();
    public abstract void remove();

}
