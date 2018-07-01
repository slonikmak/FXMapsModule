package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oceanos.FXMapModule.events.EditableEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.OptionsManager;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class Circle extends Path {
    public static String jSController = "circleController";
    public static JSObject jsObject;
    public static Gson gson;

    static {
        gson = new Gson();
    }

    private ObjectProperty<LatLng> latlng = new SimpleObjectProperty<>();
    private DoubleProperty radius = new SimpleDoubleProperty();
    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();



    public Circle(){
        super();
        OptionsManager.fillOptions(this);
        initHandlers();
        setName("circle");
        //this.setOptions(new CircleOptions());
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
        getOptions().editableProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue!=newValue){
                jsObject.call("setEditable", getId(), newValue);
            }
        });
        colorProperty().addListener(listener);
        weightProperty().addListener(listener);
        opacityProperty().addListener(listener);
        radiusProperty().addListener((ob, o, n)->{
            if (!o.equals(n)){
                jsObject.call("setRadius", getId(), getRadius());
            }
        });
        fillColorProperty().addListener(listener);
        fillProperty().addListener(listener);
        fillOpacityProperty().addListener(listener);

        addEventListener(MapEventType.editable_vertex_dragend, (e)->{
            System.out.println("set radius");
            radiusProperty().setValue((Number) jsObject.call("getRadius", getId()));
        });

    }

    @Override
    void redraw() {
        jsObject.call("redraw", getId(), OptionsManager.getOptionsJson(this));
    }

    @Override
    public void addToMap() {
        id = (int) jsObject.call("addCircle", gson.toJson(latlng.getValue()), OptionsManager.getOptionsJson(this));
    }

    @Override
    public void remove() {

    }

    public void setLatLng(double lat, double lng){
        latProperty().set(lat);
        lngProperty().set(lng);
        this.latlng.setValue(new LatLng(lat, lng));
    }

    public double getRadius() {
        return radius.get();
    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
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
