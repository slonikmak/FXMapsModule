package com.oceanos.FXMapModule.events;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @autor slonikmak on 14.06.2018.
 */
public abstract class MapEvent {
    MapEventType type;
    long target;

    public MapEvent(MapEventType type){
        this.type = type;
    }

    public MapEvent(JsonObject object){
        parseRawEvent(object);
    }

    public MapEventType getType() {
        return type;
    }

    public void setType(MapEventType type) {
        this.type = type;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public abstract void parseRawEvent(JsonObject object);
}
