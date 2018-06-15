package com.oceanos.FXMapModule.events;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class MapEvent {
    MapEventType type;
    long target;

    public MapEvent(MapEventType type){
        this.type = type;
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
}
