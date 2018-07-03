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
}
