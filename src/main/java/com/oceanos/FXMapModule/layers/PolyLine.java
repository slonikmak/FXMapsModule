package com.oceanos.FXMapModule.layers;

import com.google.gson.*;
import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.OptionsManager;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class PolyLine extends Path {
    public static String jSController = "polyLineController";
    public static JSObject jsObject;
    public static Gson gson;

    private ObservableList<LatLng> latLngs = FXCollections.observableArrayList();
    private DoubleProperty length = new SimpleDoubleProperty();
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private LongProperty points = new SimpleLongProperty();
    private DoubleProperty weight = new SimpleDoubleProperty();
    private DoubleProperty opacity = new SimpleDoubleProperty();
    private BooleanProperty editable = new SimpleBooleanProperty();


    static {
        gson = new Gson();
    }

    public PolyLine(){
        super();
        OptionsManager.fillOptions(this);
        initHandlers();
    }

    private void initHandlers() {
        editableProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue!=newValue){
                jsObject.call("setEditable", getId(), newValue);
            }
        });
        colorProperty().addListener((observable, oldValue, newValue)->{
            if (!newValue.equals(oldValue)){
                redraw();
            }
        });
    }

    public PolyLine(List<LatLng> latLngs){
        this();
        this.latLngs.addAll(latLngs);
    }


    public void addLatLng(double lat, double lng){
        latLngs.add(new LatLng(lat, lng));
    }

    public void updateLatLng(){
        String latLngString = (String) jsObject.call("getLatLngs", id);
        List<LatLng> list = gson.fromJson(latLngString, List.class);
    }


    public double getLength() {
        return length.get();
    }

    public DoubleProperty lengthProperty() {
        return length;
    }

    public void setLength(double length) {
        this.length.set(length);
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        getOptions().setOption("color", color.toString());
        this.color.set(color);
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

    public double getWeight() {
        return weight.get();
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public double getOpacity() {
        return opacity.get();
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity.set(opacity);
    }

    public boolean isEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    @Override
    void redraw() {
        jsObject.call("redraw", getId(), OptionsManager.getOptionsJson(this));
    }

    @Override
    public void remove() {
        jsObject.call("remove", getId());
    }

    @Override
    public void addToMap() {
        String latlngs = gson.toJson(new ArrayList<>(this.latLngs));
        Object value = jsObject.call("addPolyline",latlngs,OptionsManager.getOptionsJson(this));
        id = (int) value;
    }

    public void updateOptions() {
        String result = (String) jsObject.call("getOptions", getId());
        OptionsManager.fillOptions(this, result);
        String latlngsString = (String) jsObject.call("getLatLngs", id);
        System.out.println(latlngsString);
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
