package com.oceanos.FXMapModule.layers;

import com.google.gson.*;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.OptionsManager;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class PolyLine extends Path {
    public static String jSController = "polyLineController";
    public static JSObject jsObject;
    private PathOptions options;
    public Gson gson;
    private ObservableList<LatLng> latLngs = FXCollections.observableArrayList();
    private DoubleProperty length = new SimpleDoubleProperty();
    private LongProperty points = new SimpleLongProperty();

    static {

    }

    public PolyLine(){
        super();
        gson = new Gson();
        //OptionsManager.fillOptions(this);
        setOptions(new PathOptions());
        initHandlers();
        setName("polyline");
    }

    @Override
    public void setOptions(LayerOptions options) {
        this.options = (PathOptions) options;
    }

    @Override
    public LayerOptions getOptions() {
        return this.options;
    }


    public PolyLine(List<LatLng> latLngs){
        this();
        this.latLngs.addAll(latLngs);
    }

    private void initHandlers() {
        ((PathOptions)getOptions()).editableProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue!=newValue){
                jsObject.call("setEditable", getId(), newValue);
            }
        });
        this.addEventListener(MapEventType.editable_drawing_commit,(e)->{
            this.updateOptions();
        });
        addEventListener(MapEventType.editable_editing, (e)->{
            this.updateOptions();
        });

        getOptions().addListener(listener);
    }



    public void addLatLng(double lat, double lng){
        latLngs.add(new LatLng(lat, lng));
    }

    public ObservableList<LatLng> getLatLngs(){
        return this.latLngs;
    }

    /*public void updateLatLng(){
        String latLngString = (String) jsObject.call("getLatLngs", id);
        List<LatLng> list = gson.fromJson(latLngString, List.class);
    }*/


    public double getLength() {
        return length.get();
    }

    public DoubleProperty lengthProperty() {
        return length;
    }

    public void setLength(double length) {
        this.length.set(length);
    }

    public long getPoints() {
        return points.get();
    }

    public LongProperty pointsProperty() {
        return points;
    }

    public void setPoints(long points) {
        this.points.set(points);
    }


    @Override
    void redraw() {
        //FIXME: hardcode
        if (getId() != 0){
            jsObject.call("redraw", getId(), getOptions().getJson());
        }

    }

    @Override
    public void setEditable(boolean value) {
        ((PathOptions)getOptions()).setEditable(value);
    }

    @Override
    public void remove() {
        jsObject.call("remove", getId());
    }

    @Override
    public void addToMap() {
        String latlngs = gson.toJson(new ArrayList<>(this.latLngs));
        Object value = jsObject.call("addPolyline",latlngs,getOptions().getJson());
        id = (int) value;
    }

    public void updateOptions() {
        //System.out.println("update options");
        /*String result = (String) jsObject.call("getOptions", getId());
        getOptions().fillOptions(result);*/
        String latlngsString = (String) jsObject.call("getLatLngs", id);
        //System.out.println(latlngsString);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(latlngsString).getAsJsonArray();
        latLngs.clear();
        array.forEach(l->{
            JsonArray latlngArray = l.getAsJsonArray();
            latLngs.add(new LatLng(latlngArray.get(0).getAsDouble(), latlngArray.get(1).getAsDouble()));
        });
        setPoints(latLngs.size());
        setLength((Double) jsObject.call("getLength", id));
    }

}
