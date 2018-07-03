package com.oceanos.FXMapModule.layers.mission;

import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.options.CircleOptions;
import javafx.beans.property.*;

public class Waypoint extends Circle {
    private DoubleProperty azimuth;
    private DoubleProperty distance;
    private DoubleProperty targetDepth;
    private DoubleProperty targetAltitude;
    private DoubleProperty depth;
    private DoubleProperty captureRadius;
    private IntegerProperty index;
    private BooleanProperty fixGps;


    public Waypoint(int id){
        super();
        setId(id);

        azimuth = new SimpleDoubleProperty();
        distance = new SimpleDoubleProperty();
        targetDepth = new SimpleDoubleProperty();
        targetAltitude = new SimpleDoubleProperty();
        depth = new SimpleDoubleProperty();
        captureRadius = new SimpleDoubleProperty();
        index = new SimpleIntegerProperty();
        fixGps = new SimpleBooleanProperty();

        captureRadius.bindBidirectional(((CircleOptions)getOptions()).radiusProperty());
    }

    public void updateProperties(){

    }

    public double getAzimuth() {
        return azimuth.get();
    }

    public DoubleProperty azimuthProperty() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth.set(azimuth);
    }

    public double getDistance() {
        return distance.get();
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public double getTargetDepth() {
        return targetDepth.get();
    }

    public DoubleProperty targetDepthProperty() {
        return targetDepth;
    }

    public void setTargetDepth(double targetDepth) {
        this.targetDepth.set(targetDepth);
    }

    public double getTargetAltitude() {
        return targetAltitude.get();
    }

    public DoubleProperty targetAltitudeProperty() {
        return targetAltitude;
    }

    public void setTargetAltitude(double targetAltitude) {
        this.targetAltitude.set(targetAltitude);
    }

    public double getDepth() {
        return depth.get();
    }

    public DoubleProperty depthProperty() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth.set(depth);
    }

    public double getCaptureRadius() {
        return captureRadius.get();
    }

    public DoubleProperty captureRadiusProperty() {
        return captureRadius;
    }

    public void setCaptureRadius(double captureRadius) {
        this.captureRadius.set(captureRadius);
    }

    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public boolean isFixGps() {
        return fixGps.get();
    }

    public BooleanProperty fixGpsProperty() {
        return fixGps;
    }

    public void setFixGps(boolean fixGps) {
        this.fixGps.set(fixGps);
    }
}
