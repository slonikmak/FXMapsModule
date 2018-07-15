package com.oceanos.FXMapModule.mapControllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.events.EditableEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.layers.mission.Mission;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 19.06.2018.
 * TODO: сделать отдельный интерфейс PlugIns
 *
 */
public class EditableController extends Evented {
    public static String jSController = "editableController";
    public static JSObject jsObject;
    public static MapView mapView;

    public static PolyLine startPolyLine(){
        PolyLine polyLine = new PolyLine();
        polyLine.setEditable(true);
        int id =  (int) jsObject.call("startPolyline", polyLine.getOptions().getJsonString());
        polyLine.setId(id);
        mapView.addLayer(polyLine);
        return polyLine;
    }

    public static Mission startMission(){
        Mission mission = new Mission(mapView);
        mission.addEventListener(MapEventType.editable_drawing_commit,(e)->{
            //mission.updateOptions();
        });
        //mission.setEditable(true);
        int id =  (int) jsObject.call("startMission", mission.getOptions().getJsonString());
        mission.setId(id);
        mapView.addLayer(mission);
        return mission;
    }

    public static Polygon startPolygon(){
        Polygon polygon = new Polygon();
        polygon.setEditable(true);
        int id = (int) jsObject.call("startPolygon", polygon.getOptions().getJsonString());
        polygon.setId(id);
        mapView.addLayer(polygon);
        return polygon;
    }

    public static Marker startMarker(){
        Marker marker = new Marker();
        marker.addEventListener(MapEventType.editable_drawing_commit, (e)->{
            marker.setLat(((EditableEvent) e).getLat());
            marker.setLng(((EditableEvent) e).getLng());
        });
        int id = (int) jsObject.call("startMarker");
        marker.setId(id);
        mapView.addLayer(marker);
        return marker;
    }

    public static Circle startCircle(){
        Circle circle = new Circle();
        circle.addEventListener(MapEventType.editable_drawing_commit, (e)->{
            circle.setLatLng(((EditableEvent) e).getLat(),((EditableEvent) e).getLng());
        });
        circle.setEditable(true);
        int id = (int) jsObject.call("startCircle", circle.getOptions().getJsonString());
        circle.setId(id);
        mapView.addLayer(circle);
        return circle;
    }



    public static void createTooltip(){
        jsObject.call("createTooltip");
    }
}
