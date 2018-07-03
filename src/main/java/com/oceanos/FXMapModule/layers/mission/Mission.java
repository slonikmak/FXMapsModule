package com.oceanos.FXMapModule.layers.mission;

import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MissionEvent;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Mission extends PolyLine {
    public static String jSController = "missionController";
    public static JSObject jsObject;

    private ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();
    private Behaviors behaviors;

    public Mission(){
        super();
        setName("mission");
        behaviors = new Behaviors();
        addEventListener(MapEventType.mission_waypoint_new, (e)->{
            MissionEvent event = (MissionEvent) e;
            Waypoint waypoint = new Waypoint(((MissionEvent) e).getLayer());
            waypoint.setName("waypoint");
            waypoints.add(waypoint);
        });
    }

    public ObservableList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ObservableList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public void addToMap(){
        String latlngs = gson.toJson(new ArrayList<>(getLatLngs()));
        Object value = jsObject.call("addMission",latlngs, getOptions().getJson());
        id = (int) value;
    }

    //FIXME: разделить как-то с родителем
    public void updateOptions() {
        super.updateOptions();
    }
}
