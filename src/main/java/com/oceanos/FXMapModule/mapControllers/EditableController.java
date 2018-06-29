package com.oceanos.FXMapModule.mapControllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.events.EditableEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.options.OptionsManager;
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
        polyLine.addEventListener(MapEventType.editable_drawing_commit,(e)->{
            polyLine.updateOptions();
        });
        polyLine.setEditable(true);
        int id =  (int) jsObject.call("startPolyline", OptionsManager.getOptionsJson(polyLine));
        polyLine.setId(id);
        mapView.addLayer(polyLine);
        return polyLine;
    }

    public static Polygon startPolygon(){
        Polygon polygon = new Polygon();
        polygon.addEventListener(MapEventType.editable_drawing_commit, (e)->{
            polygon.updateOptions();
        });
        polygon.setEditable(false);
        int id = (int) jsObject.call("startPolygon", OptionsManager.getOptionsJson(polygon));
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
        int id = (int) jsObject.call("startCircle", OptionsManager.getOptionsJson(circle));
        circle.setId(id);
        mapView.addLayer(circle);
        return circle;
    }

    public static void createTooltip(){
        jsObject.call("createTooltip");
    }
}
