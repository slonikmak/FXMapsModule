package com.oceanos.FXMapModule.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @autor slonikmak on 03.07.2018.
 */
public class WmsLayerOptions extends LayerOptions {

    private StringProperty layers;
    private StringProperty format;
    private BooleanProperty transparent;


    @Override
    void init() {
        layers = new SimpleStringProperty("izobata");
        format = new SimpleStringProperty("image/png");
        transparent = new SimpleBooleanProperty(true);
    }

    public String getLayers() {
        return layers.get();
    }

    public StringProperty layersProperty() {
        return layers;
    }

    public void setLayers(String layers) {
        this.layers.set(layers);
    }

    public String getFormat() {
        return format.get();
    }

    public StringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public boolean isTransparent() {
        return transparent.get();
    }

    public BooleanProperty transparentProperty() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent.set(transparent);
    }


}
