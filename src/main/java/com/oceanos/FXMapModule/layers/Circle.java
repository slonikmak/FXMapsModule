package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.LayerOptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class Circle extends Path {
    public static String jSController = "circleController";
    public static JSObject jsObject;
    public static Gson gson;
    private CircleOptions options;

    static {
        gson = new Gson();
    }

    private ObjectProperty<LatLng> latlng = new SimpleObjectProperty<>();
    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();



    public Circle(){
        super();
        setOptions(new CircleOptions());
        initHandlers();
        setName("circle");
    }

    @Override
    public void setOptions(LayerOptions options) {
        this.options = (CircleOptions) options;
    }

    @Override
    public LayerOptions getOptions() {
        return this.options;
    }

    public Circle(LatLng latLng, int radius){
        this.setLatLng(latLng);
        /*CircleOptions options = new CircleOptions();
        options.setOption("radius", radius);
        this.setOptions(options);*/
        this.latlng.addListener((a,b,c)->{
            if (!b.equals(c)){
                jsObject.call("setLatLng", id, gson.toJson(c));
            }
        });
    }

    //FIXME: move to Path
    //TODO: сделать что-то типа CommonPathOptions
    private void initHandlers() {
        ((CircleOptions)getOptions()).editableProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue!=newValue){
                jsObject.call("setEditable", getId(), newValue);
            }
        });
        getOptions().addListener(listener);

        options.radiusProperty().addListener((ob, o, n)->{
            if (!o.equals(n)){
                jsObject.call("setRadius", getId(), getRadius());
            }
        });

        addEventListener(MapEventType.editable_vertex_dragend, (e)->{
            System.out.println("set radius");
            radiusProperty().setValue((Number) jsObject.call("getRadius", getId()));
        });

    }

    @Override
    public void redraw() {
        if (getId() != 0) {
            jsObject.call("redraw", getId(), options.getJsonString());
            System.out.println(options.getJsonString());
        }
    }

    @Override
    public void setEditable(boolean value) {

    }

    @Override
    public void addToMap() {
        id = (int) jsObject.call("addCircle", gson.toJson(latlng.getValue()), options.getJsonString());
    }

    @Override
    public void remove() {
        jsObject.call("removeLayer", this.getId());
    }

    @Override
    public void hide() {
        jsObject.call("hide");
    }

    @Override
    public void show() {
        jsObject.call("show");
    }

    @Override
    public String convertToJson() {
        return null;
    }

    public void setLatLng(double lat, double lng){
        latProperty().set(lat);
        lngProperty().set(lng);
        this.latlng.setValue(new LatLng(lat, lng));
    }

    public double getRadius() {
        return options.getRadius();
    }

    public DoubleProperty radiusProperty() {
        return options.radiusProperty();
    }

    public void setRadius(double radius) {
        options.radiusProperty().setValue(radius);
    }

    public LatLng getLatlng() {
        return latlng.get();
    }

    public ObjectProperty<LatLng> latlngProperty() {
        return latlng;
    }

    public void setLatLng(LatLng latlng){
        this.latlngProperty().setValue(latlng);
        this.latProperty().setValue(latlng.getLat());
        this.lngProperty().setValue(latlng.getLng());
    }

    public double getLat() {
        return lat.get();
    }

    public DoubleProperty latProperty() {
        return lat;
    }

    public double getLng() {
        return lng.get();
    }

    public DoubleProperty lngProperty() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat.set(lat);
    }

    public void setLng(double lng) {
        this.lng.set(lng);
    }
}
