package com.oceanos.FXMapModule.events;

import com.google.gson.JsonObject;

/**
 * @autor slonikmak on 24.07.2018.
 */
public class TileEvent extends MapEvent {

    int x;
    int y;
    int z;
    String url;


    public TileEvent(MapEventType type) {
        super(type);
    }

    public TileEvent(JsonObject object) {
        super(object);
    }

    @Override
    public void parseRawEvent(JsonObject object) {
        String typeString = object.get("type").getAsString();
        MapEventType type = MapEventType.valueOf(typeString);
        long target = object.get("target").getAsLong();
        this.type = type;
        this.target = target;
        this.x = object.get("x").getAsInt();
        this.y = object.get("y").getAsInt();
        this.z = object.get("z").getAsInt();
        this.url = object.get("url").getAsString();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getUrl() {
        return url;
    }
}
