package com.oceanos.FXMapModule.events;

import com.google.gson.JsonObject;

/**
 * @autor slonikmak on 02.07.2018.
 */
public class MissionEvent extends MapEvent {
    private double lat;
    private double lng;
    private int layer;

    public MissionEvent(JsonObject object) {
        super(object);
    }

    @Override
    public void parseRawEvent(JsonObject object) {
        String typeString = object.get("type").getAsString().replace(":","_");
        MapEventType type = MapEventType.valueOf(typeString);
        long target = object.get("target").getAsLong();
        this.type = type;
        this.target = target;
        this.layer = object.get("layer").getAsInt();
        this.lat = object.get("latlng").getAsJsonObject().get("lat").getAsDouble();
        this.lng = object.get("latlng").getAsJsonObject().get("lng").getAsDouble();
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

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
