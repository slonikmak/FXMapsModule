package com.oceanos.FXMapModule.layers;


import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class Path extends Layer {


    public Path(){
        //this.setOptions(new PathOptions());
    }
    abstract void redraw();

    public abstract void setEditable(boolean value);


    InvalidationListener listener = observable -> redraw();


}
