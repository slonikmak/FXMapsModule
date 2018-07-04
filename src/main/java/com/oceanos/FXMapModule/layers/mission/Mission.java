package com.oceanos.FXMapModule.layers.mission;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MissionEvent;
import com.oceanos.FXMapModule.layers.PolyLine;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Mission extends PolyLine {
    public static String jSController = "missionController";
    public static JSObject jsObject;

    private MapView mapView;

    private ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
    private Behaviors behaviors;
    private CircleOptions waypointOptions;

    private StringProperty description = new SimpleStringProperty("new mission");
    private StringProperty creationDate = new SimpleStringProperty();
    private IntegerProperty captureRadius = new SimpleIntegerProperty();

    public Mission(MapView mapView){
        super();
        PathOptions options = new PathOptions();
        options.fillOptions(ResourceManager.getInstance().getDefaultMissionOptions());
        setOptions(options);
        this.mapView = mapView;
        setName("mission");
        setCreationDate();
        behaviors = new Behaviors();
        waypointOptions = new CircleOptions();
        waypointOptions.fillOptions(ResourceManager.getInstance().getDefaultWaypointOptions());
        captureRadius.bindBidirectional(waypointOptions.radiusProperty());
        addEventListener(MapEventType.mission_waypoint_new, (e)->{
            MissionEvent event = (MissionEvent) e;
            Waypoint waypoint = new Waypoint(((MissionEvent) e).getLayer());
            waypoint.setOptions(waypointOptions);
            updateWaypoints();
            waypoint.setName("waypoint");
            waypoint.setId(event.getLayer());
            waypoint.setLat(((MissionEvent) e).getLat());
            waypoint.setLng(((MissionEvent) e).getLng());
            waypoints.add(waypoint);
            mapView.addLayer(waypoint);
        });

        addEventListener(MapEventType.mission_waypoint_move, (event1)->{
            waypoints.stream().filter((waypoint -> waypoint.getId() == ((MissionEvent)event1).getLayer())).findFirst().ifPresent((w)->{
                w.setLat(((MissionEvent) event1).getLat());
                w.setLng(((MissionEvent) event1).getLng());
            });
        });

        waypointOptions.addListener(observable -> {
            System.out.println("update waypoints");
            jsObject.call("updateWaypoints",getId(), waypointOptions.getJson());
        });

        captureRadius.addListener((o)->{
            waypointOptions.radiusProperty().setValue(captureRadius.getValue());
        });
    }

    private void updateWaypoints(){
        System.out.println("update waypoints");
        jsObject.call("updateWaypoints",getId(), waypointOptions.getJson());
    }

    public ObservableList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ObservableList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Behaviors getBehaviors() {
        return behaviors;
    }

    public CircleOptions getWaypointOptions(){
        return this.waypointOptions;
    }

    @Override
    public void addToMap(){
        String latlngs = gson.toJson(new ArrayList<>(getLatLngs()));
        Object value = jsObject.call("addMission",latlngs, getOptions().getJson());
        id = (int) value;
    }

    @Override
    public void hide() {
        jsObject.call("hide");
    }

    @Override
    public void show() {
        jsObject.call("show");
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCreationDate() {
        return creationDate.get();
    }

    public StringProperty creationDateProperty() {
        return creationDate;
    }

    public int getCaptureRadius() {
        return captureRadius.get();
    }

    public IntegerProperty captureRadiusProperty() {
        return captureRadius;
    }

    public void setCaptureRadius(int captureRadius) {
        this.captureRadius.set(captureRadius);
    }

    public void setCreationDate() {
        Date localDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy:HH:mm", Locale.getDefault());
        String date = format.format(localDate);
        this.creationDate.set(date);
    }

    @Override
    public void remove(){
        int size = waypoints.size();
        for (int i = 0; i < size; i++) {
            mapView.getLayers().remove(waypoints.get(i));
        }
        super.remove();

    }

    //FIXME: разделить как-то с родителем
    public void updateOptions() {
        super.updateOptions();
    }

    @Override
    public String convertToJson() {

        JsonObject object = new JsonObject();
        object.addProperty("name", getName());
        object.addProperty("id", getId());
        object.addProperty("description", getDescription());
        JsonArray waypointsArray = new JsonArray();
        waypoints.forEach((w)->{
            waypointsArray.add(w.getJsonObject());
        });
        object.add("waypoints", waypointsArray);
        object.add("behaviors", behaviors.getJsonObject());
        return gson.toJson(object);
    }
}