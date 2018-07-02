package com.oceanos.FXMapModule.options;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class CircleOptions extends PathOptions {
    private DoubleProperty radius;


    @Override
    void init() {
        super.init();
        radius = new SimpleDoubleProperty(15.0);
    }

    public double getRadius() {
        return radius.get();
    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }
}
