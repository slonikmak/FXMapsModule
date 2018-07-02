package com.oceanos.FXMapModule.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.lang.reflect.InvocationTargetException;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class MarkerOptions extends LayerOptions {
    private BooleanProperty draggable;
    private DoubleProperty opacity;

    public MarkerOptions(){
    }

    @Override
    void init(){
        draggable = new SimpleBooleanProperty(false);
        opacity = new SimpleDoubleProperty(1.0);
    }


    public boolean isDraggable() {
        return draggable.get();
    }

    public BooleanProperty draggableProperty() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable.set(draggable);
    }

    public double getOpacity() {
        return opacity.get();
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity.set(opacity);
    }
}
