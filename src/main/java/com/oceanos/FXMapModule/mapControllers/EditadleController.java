package com.oceanos.FXMapModule.mapControllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.events.EditableEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.layers.Evented;
import com.oceanos.FXMapModule.layers.Marker;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 19.06.2018.
 * TODO: сделать отдельный интерфейс PlugIns
 *
 */
public class EditadleController extends Evented {
    public static String jSController = "editableController";
    public static JSObject jsObject;
    public static MapView mapView;

    public static long startPolyLine(){

        return (int) jsObject.call("startPolyline");
    }

    public static long startMarker(){
        Marker marker = new Marker();
        marker.addEventListener(MapEventType.editable_drawing_commit, (e)->{
            marker.setLat(((EditableEvent) e).getLat());
            marker.setLng(((EditableEvent) e).getLng());
        });
        int id = (int) jsObject.call("startMarker");
        marker.setId(id);
        mapView.addLayer(marker);
        return id;
    }
}
