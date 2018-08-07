package com.oceanos.FXMapModule.layers.mission;

import com.google.gson.*;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MissionEvent;
import com.oceanos.FXMapModule.layers.LatLng;
import com.oceanos.FXMapModule.layers.PolyLine;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.MissionOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import com.oceanos.FXMapModule.utils.GeoJsonUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /*public Mission(MapView mapView){
        PathOptions options = new PathOptions();
        options.fillOptions(ResourceManager.getInstance().getDefaultMissionOptions());
        CircleOptions waypointOptions = new CircleOptions();
        waypointOptions.fillOptions(ResourceManager.getInstance().getDefaultWaypointOptions());
        this(mapView, options, waypointOptions);
    }*/

    public Mission(MapView mapView, PathOptions missionOptions, CircleOptions waypointOptions){
        super();
        /*PathOptions options = new PathOptions();
        options.fillOptions(ResourceManager.getInstance().getDefaultMissionOptions());*/
        setOptions(missionOptions);
        this.mapView = mapView;
        setName("mission");
        setCreationDate();
        behaviors = new Behaviors();
       /* waypointOptions = new CircleOptions();
        waypointOptions.fillOptions(ResourceManager.getInstance().getDefaultWaypointOptions());*/
       this.waypointOptions = waypointOptions;
        captureRadius.bindBidirectional(waypointOptions.radiusProperty());
        addEventListener(MapEventType.mission_waypoint_new, (e)->{
            System.out.println("new waypoint");
            //FIXME: перенести в метод addWaypoint()
            MissionEvent event = (MissionEvent) e;
            Waypoint waypoint = new Waypoint(((MissionEvent) e).getLayer());
            waypoint.setMission(this);
            waypoint.setOptions(waypointOptions);

            waypoint.setName("Waypoint");
            waypoint.setId(event.getLayer());
            waypoint.setLat(((MissionEvent) e).getLat());
            waypoint.setLng(((MissionEvent) e).getLng());
            waypoints.add(waypoint);
            mapView.addLayer(waypoint);
            updateWaypoints();
        });
        addEventListener(MapEventType.mission_waypoint_deleted, (e)->{
            long id = ((MissionEvent)e).getLayer();
            Optional<Waypoint> waypoint = waypoints.stream().filter((waypoint1 -> waypoint1.getId()==id)).findFirst();
            waypoint.ifPresent((waypoint1 -> {
                waypoints.remove(waypoint1);
                mapView.removeLayer(waypoint1);
            }));
            updateWaypoints();
        });

        addEventListener(MapEventType.mission_waypoint_move, (event1)->{
            waypoints.stream().filter((waypoint -> waypoint.getId() == ((MissionEvent)event1).getLayer())).findFirst().ifPresent((w)->{
                w.setLat(((MissionEvent) event1).getLat());
                w.setLng(((MissionEvent) event1).getLng());
            });
        });

        waypointOptions.addListener(observable -> {
            System.out.println("update waypoints");
            jsObject.call("updateWaypoints",getId(), waypointOptions.getJsonString());
        });

        captureRadius.addListener((o)->{
            waypointOptions.radiusProperty().setValue(captureRadius.getValue());
        });
    }

    private void addWaypoint(Waypoint waypoint){
        waypoint.setOptions(waypointOptions);
        waypoint.setMission(this);
        //updateWaypoints();
        waypoints.add(waypoint);
    }

    void setWaypointId(Waypoint waypoint){
        Object result = jsObject.call("getWaypointId",this.getId(), waypoint.getLat(), waypoint.getLng());
       Integer waypointId = (Integer) result;
       waypoint.setId(waypointId);
    }

    private void updateWaypoints(){
        //fixme:hardcode
        if (waypointOptions != null){
            System.out.println("update waypoints");
            jsObject.call("updateWaypoints",getId(), waypointOptions.getJsonString());
            waypoints.forEach(w->{
                int number = (int) jsObject.call("getWaypointIndex", this.getId(), w.getId());
                System.out.println("layer "+this.getId()+" waypoint "+w.getId()+"number "+number);
                w.setIndex(number);
            });
        }
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
        String latlngs = gson.toJson(waypoints.stream().map(w->new LatLng(w.getLat(), w.getLng())).collect(Collectors.toList()));
        Object value = jsObject.call("addMission",latlngs, getOptions().getJsonString());
        id = (int)value;
        System.out.println("id "+id);
        waypoints.forEach(w->{
            setWaypointId(w);
            mapView.addLayer(w);
        });
        updateWaypoints();
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
        String jsonResult = super.convertToJson();
        JsonParser parser = new JsonParser();
        JsonObject lineObj = parser.parse(jsonResult).getAsJsonObject();
        GeoJsonUtils.GeoJsonBuilder geoJsonBuilder = new GeoJsonUtils.GeoJsonBuilder();
        geoJsonBuilder.addFeature(lineObj);
        waypoints.forEach(w->{
            geoJsonBuilder.addPoint(w.getLat(),w.getLng());
        });
        JsonObject properties = new JsonObject();
        geoJsonBuilder.getFeatureCollection().add("properties", properties);
        properties.addProperty("name", getName());
        properties.addProperty("description", getDescription());

        return geoJsonBuilder.getFeatureCollection().toString();
    }

    public String convertToMissionJson(){
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
        //FIXME: lng lon hardcode
        return gson.toJson(object).replaceAll("lng", "lon");
    }

    //todo: hardcode
    public static Mission getFromJson(String content, MapView mapView, PathOptions missionOptions, CircleOptions waypointOptions) {
        content = content.replaceAll("lon", "lng");
        Mission mission = new Mission(mapView, missionOptions, waypointOptions);
        JsonObject contentObject = new JsonParser().parse(content).getAsJsonObject();
        mission.setName(contentObject.get("name").getAsString());
        mission.setDescription(contentObject.get("description").getAsString());
        JsonArray waypointsArr = contentObject.getAsJsonArray("waypoints");
        waypointsArr.forEach(w->{
            Waypoint waypoint = Waypoint.getFromJson(w.getAsJsonObject());
            mission.addWaypoint(waypoint);
            //mission.waypoints.add(waypoint);
        });
        JsonArray behaviorsArr = contentObject.get("behaviors").getAsJsonArray();
        Behaviors behaviors = new Behaviors(behaviorsArr);
        mission.behaviors = behaviors;

        return mission;
    }
}
