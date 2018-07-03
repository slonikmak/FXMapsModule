package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.options.CircleOptions;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

/**
 * @autor slonikmak on 03.07.2018.
 */
public class WaypointController implements LayerOptionsController {
    private Waypoint layer;

    @FXML
    private Label lat;

    @FXML
    private Label lng;

    @FXML
    private Label azimuth;

    @FXML
    private Label distance;

    @FXML
    private Label index;

    @FXML
    private TextField name;

    @FXML
    private Label radius;

    @FXML
    private CheckBox gpsFix;

    @FXML
    private Label lineColor;

    @FXML
    private Label fillColor;

    @FXML
    private Label width;

    @FXML
    private Label lineOpacyti;

    @FXML
    private Label fillOpacity;

    @FXML
    private TextField targetDepth;

    @FXML
    private TextField targetAltitude;

    @FXML
    private TextField depth;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (Waypoint) layer;
        fillOptions();
    }

    @Override
    public void fillOptions() {
        CircleOptions options = (CircleOptions)layer.getOptions();
        fillOpacity.textProperty().bindBidirectional(options.fillOpacityProperty(), new NumberStringConverter());
        lineOpacyti.textProperty().bindBidirectional(options.opacityProperty(), new NumberStringConverter());
        width.textProperty().bindBidirectional(options.weightProperty(), new NumberStringConverter());
        fillColor.textProperty().bindBidirectional(options.fillColorProperty());
        lineColor.textProperty().bindBidirectional(options.colorProperty());

        lat.textProperty().bindBidirectional(layer.latProperty(), new NumberStringConverter());
        lng.textProperty().bindBidirectional(layer.lngProperty(), new NumberStringConverter());
        azimuth.textProperty().bindBidirectional(layer.azimuthProperty(), new NumberStringConverter());
        distance.textProperty().bindBidirectional(layer.distanceProperty(), new NumberStringConverter());

        targetAltitude.textProperty().bindBidirectional(layer.targetAltitudeProperty(), new NumberStringConverter());
        targetDepth.textProperty().bindBidirectional(layer.targetDepthProperty(), new NumberStringConverter());
        depth.textProperty().bindBidirectional(layer.depthProperty(), new NumberStringConverter());
        gpsFix.selectedProperty().bindBidirectional(layer.fixGpsProperty());
        name.textProperty().bindBidirectional(layer.nameProperty());
        radius.textProperty().bindBidirectional(layer.radiusProperty(), new NumberStringConverter());
        index.textProperty().bindBidirectional(layer.indexProperty(), new NumberStringConverter());
    }
}
