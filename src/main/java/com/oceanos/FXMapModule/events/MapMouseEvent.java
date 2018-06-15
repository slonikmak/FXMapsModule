package com.oceanos.FXMapModule.events;

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
}
