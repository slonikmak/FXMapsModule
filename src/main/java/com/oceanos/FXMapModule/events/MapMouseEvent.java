package com.oceanos.FXMapModule.events;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class MapMouseEvent extends MapEvent {
    private double lat;
    private double lng;
    public MapMouseEvent(MapEventType type) {
        super(type);
    }

    public MapMouseEvent(MapEventType type, long target, double lat, double lng) {
        super(type);
        this.lat = lat;
        this.lng = lng;
        this.target = target;
    }
    public MapMouseEvent(JsonObject object){
        super(object);

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    @Override
    public void parseRawEvent(JsonObject object) {
        String typeString = object.get("type").getAsString();
        MapEventType type = MapEventType.valueOf(typeString);
        long target = object.get("target").getAsLong();
        this.lat = object.get("latLng").getAsJsonObject().get("lat").getAsDouble();
        this.lng = object.get("latLng").getAsJsonObject().get("lng").getAsDouble();
        this.target = target;
        this.type = type;
    }
}
