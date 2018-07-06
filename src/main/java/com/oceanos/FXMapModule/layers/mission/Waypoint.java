package com.oceanos.FXMapModule.layers.mission;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.LayerOptions;
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

    public Waypoint(){
        super();
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


    public Waypoint(int id){
        this();
        setId(id);

    }

    @Override
    public void addToMap(){

    }

    public void updateProperties(){

    }

    public JsonObject getJsonObject(){
        JsonObject object = new JsonObject();
        object.addProperty("lat", getLat());
        object.addProperty("lng", getLng());
        object.addProperty("azimuth", getAzimuth());
        object.addProperty("distance", getDistance());
        object.addProperty("target_depth", getTargetDepth());
        object.addProperty("target_altitude", getTargetAltitude());
        object.addProperty("depth", getDepth());
        object.addProperty("capture_radius", getCaptureRadius());
        object.addProperty("index", getIndex());
        object.addProperty("id", getId());
        object.addProperty("fix_gps", isFixGps());
        object.add("tasks", new JsonArray());
        return object;
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

    public static Waypoint getFromJson(JsonObject jsonObject){
        Waypoint waypoint = new Waypoint();
        waypoint.setLat(jsonObject.get("lat").getAsDouble());
        waypoint.setLng(jsonObject.get("lng").getAsDouble());
        waypoint.setAzimuth(jsonObject.get("azimuth").getAsDouble());
        waypoint.setDistance(jsonObject.get("distance").getAsDouble());
        waypoint.setTargetDepth(jsonObject.get("target_depth").getAsDouble());
        waypoint.setTargetAltitude(jsonObject.get("target_altitude").getAsDouble());
        waypoint.setDepth(jsonObject.get("depth").getAsDouble());
        waypoint.setCaptureRadius(jsonObject.get("capture_radius").getAsDouble());
        waypoint.setIndex(jsonObject.get("index").getAsInt());
        waypoint.setFixGps(jsonObject.get("fix_gps").getAsBoolean());
        return waypoint;
    }
}
