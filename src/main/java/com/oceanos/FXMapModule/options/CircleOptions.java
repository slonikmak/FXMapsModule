package com.oceanos.FXMapModule.options;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class CircleOptions extends PathOptions {
    @Override
    void init() {
        super.init();
        setOption("radius", 15.0);
    }
}
