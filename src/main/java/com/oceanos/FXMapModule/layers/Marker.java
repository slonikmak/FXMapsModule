package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.events.LayerEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.MarkerOptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Marker extends Layer {
    public static String jSController = "markerController";
    public static JSObject jsObject;
    private static Gson gson;
    private MarkerOptions options;

    static {
        gson = new Gson();
    }

    private Icon icon;
    private DoubleProperty lat = new SimpleDoubleProperty();
    private DoubleProperty lng = new SimpleDoubleProperty();

    public Marker(){
        this.setOptions(new MarkerOptions());
        setName("marker");
        initHandlers();
    }



    @Override
    public void setOptions(LayerOptions options) {
        this.options = (MarkerOptions) options;
    }

    @Override
    public LayerOptions getOptions() {
        return this.options;
    }

    public Marker(double lat, double lng){
        this();
        this.lat.setValue(lat);
        this.lng.setValue(lng);

    }


    @Override
    public void remove() {
        jsObject.call("removeLayer", this.getId());
    }

    @Override
    public void hide() {
        jsObject.call("hide", getId());
    }

    @Override
    public void show() {
        jsObject.call("show", getId());
    }

    @Override
    public String convertToJson() {
        String jsonString = (String) jsObject.call("toGeoJson", this.getId());
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        JsonObject propObj = jsonObject.getAsJsonObject("properties");
        propObj.addProperty("name", getName());
        return jsonObject.toString();
    }

    public void addToMap() {
        Gson gson = new Gson();
        int value = (int) jsObject.call("addMarker", lat.get(), lng.get(), getOptions().getJsonString(), gson.toJson(icon));
        id = value;
    }

    private void initHandlers(){
        options.addListener(observable -> update());
        addEventListener(MapEventType.move, (event -> {
            lat.setValue(((LayerEvent)event).getLat());
            lng.setValue(((LayerEvent)event).getLng());
        }));

        latProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("update");
            update();
        });
        lngProperty().addListener(observable -> update());
    }

    public void setIcon(String src){
        //fixme: перенести куда-либо в подходящее место
        this.icon = new Icon(src.replace("file:/", "myapp:///"));
        String json =  gson.toJson(icon);
        System.out.println(json);
        if (isOnMap()) jsObject.call("setIcon", id, json);
    }

    public Icon getIcon(){
        return icon;
    }

    public double getLat() {
        return lat.get();
    }

    public DoubleProperty latProperty() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat.set(lat);
    }

    public double getLng() {
        return lng.get();
    }

    public DoubleProperty lngProperty() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng.set(lng);
    }

    private void update(){
        System.out.println("update marker");
        if (!isOnMap()) return;
        jsObject.call("update", id, lat.get(), lng.get(), getOptions().getJsonString());
    }

    private boolean isOnMap(){
        return id != 0;
    }

    public static Marker getFromJson(String content) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(content).getAsJsonObject();
        JsonObject geometry = jsonObject.getAsJsonObject("geometry");
        JsonArray coords = geometry.getAsJsonArray("coordinates");
        Double lng = coords.get(0).getAsDouble();
        Double lat = coords.get(1).getAsDouble();
        JsonObject properties = jsonObject.getAsJsonObject("properties");
        String name = properties.get("name").getAsString();
        Marker marker = new Marker(lat, lng);
        marker.setName(name);
        return marker;
    }

}
