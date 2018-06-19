package com.oceanos.FXMapModule.layers;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class LatLng {
    private double lat;
    private double lng;
    private double alt;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng(double lat, double lng, double alt) {
        this(lat, lng);
        this.alt = alt;
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

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}
