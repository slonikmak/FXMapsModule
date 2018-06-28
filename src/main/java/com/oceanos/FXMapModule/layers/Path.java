package com.oceanos.FXMapModule.layers;


import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class Path extends Layer {
    private DoubleProperty weight = new SimpleDoubleProperty();
    private DoubleProperty opacity = new SimpleDoubleProperty();
    private BooleanProperty editable = new SimpleBooleanProperty();
    private ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private BooleanProperty fill = new SimpleBooleanProperty();
    private ObjectProperty<Color> fillColor = new SimpleObjectProperty<>();
    private DoubleProperty fillOpacity = new SimpleDoubleProperty();

    public Path(){
        //this.setOptions(new PathOptions());
    }
    abstract void redraw();

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

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
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

    public Color getFillColor() {
        return fillColor.get();
    }

    public ObjectProperty<Color> fillColorProperty() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
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

    public void setColor(Color color) {
        //getOptions().setOption("color", color.toString());
        this.color.set(color);
    }

    ChangeListener listener = ((observable, oldValue, newValue) -> {
        if (!newValue.equals(oldValue)){
            redraw();
        }
    });


}
