package com.oceanos.FXMapModule.events;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @autor slonikmak on 18.06.2018.
 */
public class LayerEvent extends MapEvent {

    private double lat;
    private double lng;

    public LayerEvent(MapEventType type) {
        super(type);
    }

    public LayerEvent(JsonObject parser){
        super(parser);
    }

    @Override
    public void parseRawEvent(JsonObject object) {
        String typeString = object.get("type").getAsString();
        MapEventType type = MapEventType.valueOf(typeString);
        long target = object.get("target").getAsLong();
        this.type = type;
        this.target = target;
        this.lat = object.get("latLng").getAsJsonObject().get("lat").getAsDouble();
        this.lng = object.get("latLng").getAsJsonObject().get("lng").getAsDouble();
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
