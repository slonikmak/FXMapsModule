package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.utills.GeoUtils;
import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.layers.Layer;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * @autor slonikmak on 28.06.2018.
 */
public class CircleOptionsController extends PathOptionsController {
    //private Circle layer;
    @FXML
    private TextField latField;

    @FXML
    private TextField lngField;

    @FXML
    private TextField radiusLabel;

    @Override
    public void setLayer(Layer layer) {
        super.setLayer(layer);
        /*this.layer = (Circle)layer;
        fillOptions();*/
    }

    @Override
    public void fillOptions() {
        super.fillOptions();
        radiusLabel.textProperty().bindBidirectional((((Circle)layer).radiusProperty()), new MyStringConverter());
        latField.textProperty().bindBidirectional(((Circle)layer).latProperty(), new GeoUtils.CoordsToStringConverter());
        lngField.textProperty().bindBidirectional(((Circle)layer).lngProperty(), new GeoUtils.CoordsToStringConverter());
    }
}
