package com.oceanos.FXMapModule.layers.mission;

import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MissionEvent;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Mission extends PolyLine {
    public static String jSController = "missionController";
    public static JSObject jsObject;

    private ObservableList<Waypoint> waypoints = FXCollections.observableArrayList();

    public Mission(){
        super();
        setName("mission");
        addEventListener(MapEventType.mission_waypoint_new, (e)->{
            MissionEvent event = (MissionEvent) e;
            waypoints.add(new Waypoint(((MissionEvent) e).getLayer()));
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
