package com.oceanos.FXMapModule.events;

import com.google.gson.JsonObject;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class EditableEvent extends MapEvent {
    private double lat;
    private double lng;
    public EditableEvent(MapEventType type) {
        super(type);
    }

    public EditableEvent(JsonObject object) {
        super(object);
    }

    @Override
    public void parseRawEvent(JsonObject object) {
        String typeString = object.get("type").getAsString().replace(":","_");
        //System.out.println(typeString);
        MapEventType type = MapEventType.valueOf(typeString);
        this.target = object.get("target").getAsLong();
        this.type = type;
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
