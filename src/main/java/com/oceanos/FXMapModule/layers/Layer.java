package com.oceanos.FXMapModule.layers;

import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 14.06.2018.
 */
public abstract class Layer extends Evented {
    protected long id;

    public abstract void remove();
    public long getId(){
        return id;
    }

    public abstract void addToMap();
}
