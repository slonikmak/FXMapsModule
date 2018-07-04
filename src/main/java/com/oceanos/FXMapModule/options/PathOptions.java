package com.oceanos.FXMapModule.options;


import javafx.beans.property.*;


/**
 * @autor slonikmak on 19.06.2018.
 */
public class PathOptions extends LayerOptions{

    private DoubleProperty weight;
    private DoubleProperty opacity ;
    private BooleanProperty editable;
    private StringProperty color;
    private BooleanProperty fill;
    private StringProperty fillColor;
    private DoubleProperty fillOpacity;

    public PathOptions(){
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

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public boolean isFill() {
        return fill.get();
    }

    public BooleanProperty fillProperty() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill.set(fill);
    }

    public String getFillColor() {
        return fillColor.get();
    }

    public StringProperty fillColorProperty() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor.set(fillColor);
    }

    public double getFillOpacity() {
        return fillOpacity.get();
    }

    public DoubleProperty fillOpacityProperty() {
        return fillOpacity;
    }

    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity.set(fillOpacity);
    }

    public void setColor(String color) {
        //getOptions().setOption("color", color.toString());
        this.color.set(color);
    }
    @Override
    void init() {
        color = new SimpleStringProperty("#8377ff");
        weight = new SimpleDoubleProperty(8.0);
        opacity = new SimpleDoubleProperty(1.0);
        fill = new SimpleBooleanProperty(false);
        fillColor = new SimpleStringProperty("#3388ff");
        fillOpacity = new SimpleDoubleProperty(0.2);
        editable = new SimpleBooleanProperty(true);
    }

}