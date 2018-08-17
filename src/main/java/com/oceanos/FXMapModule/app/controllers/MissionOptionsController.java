package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.utills.ColorUtils;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.mission.Mission;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @autor slonikmak on 03.07.2018.
 */
public class MissionOptionsController implements LayerOptionsController {
    private Mission layer;

    @FXML
    private TextField name;

    @FXML
    private Label points;

    @FXML
    private Label length;

    @FXML
    private TextArea description;

    @FXML
    private TextField lineWidth;

    @FXML
    private Slider lineOpacity;

    @FXML
    private ColorPicker lineColor;

    @FXML
    private Slider pointFillOpacity;

    @FXML
    private Slider pointLineOpacity;

    @FXML
    private ColorPicker pointLinColor;

    @FXML
    private ColorPicker pointFillColor;

    @FXML
    private CheckBox fillEnabled;

    @FXML
    private TextField pointLineWidth;

    @FXML
    private VBox behaviors;

    @FXML
    private Label date;

    @FXML
    private TextField captureRadius;

    @FXML
    private CheckBox measurements;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (Mission) layer;
        fillBehaviors();
        fillOptions();
    }

    private void fillBehaviors() {
        behaviors.setAlignment(Pos.CENTER_RIGHT);
        layer.getBehaviors().getBehaviors().forEach((s, stringPropertyObservableMap) -> {
            behaviors.getChildren().add(new Separator(Orientation.HORIZONTAL));
            behaviors.getChildren().add(new Label(s));
            behaviors.getChildren().add(new Separator(Orientation.HORIZONTAL));
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setPadding(new Insets(5));
            behaviors.getChildren().add(gridPane);
            List<Map.Entry<String, Property>> entries = new ArrayList<>(stringPropertyObservableMap.entrySet());
            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<String, Property> entry = entries.get(i);
                addBevaviorProperty(entry.getKey(), entry.getValue(), gridPane, i);
            }

        });
    }

    private void addBevaviorProperty(String name, Property property, GridPane gridPane, int row){
        gridPane.add(new Label(name), 0, row);
        Node node = null;
        if (property instanceof SimpleIntegerProperty){
            node = new TextField();
            ((TextField) node).textProperty().bindBidirectional((SimpleIntegerProperty)property, new NumberStringConverter());
        } else if (property instanceof SimpleBooleanProperty){
            node = new CheckBox("");
            ((CheckBox)node).setSelected(((SimpleBooleanProperty) property).getValue());
            ((SimpleBooleanProperty)property).bindBidirectional(((CheckBox) node).selectedProperty());
        }
        gridPane.add(node, 1, row);
    }

    @Override
    public void fillOptions() {
        PathOptions pathOptions = (PathOptions) layer.getOptions();
        CircleOptions waypointOptions = layer.getWaypointOptions();

        name.textProperty().bindBidirectional(layer.nameProperty());
        description.textProperty().bindBidirectional(layer.descriptionProperty());
        date.textProperty().bindBidirectional(layer.creationDateProperty());
        points.textProperty().bindBidirectional(layer.pointsProperty(), new NumberStringConverter());
        length.textProperty().bindBidirectional(layer.lengthProperty(), new NumberStringConverter());
        lineWidth.textProperty().bindBidirectional(pathOptions.weightProperty(), new NumberStringConverter());
        lineOpacity.valueProperty().bindBidirectional(pathOptions.opacityProperty());


        //Waypoint
        captureRadius.textProperty().bindBidirectional(layer.captureRadiusProperty(), new NumberStringConverter());
        pointLineWidth.textProperty().bindBidirectional(waypointOptions.weightProperty(), new NumberStringConverter());
        pointFillOpacity.valueProperty().bindBidirectional(waypointOptions.fillOpacityProperty());
        pointLineOpacity.valueProperty().bindBidirectional(waypointOptions.opacityProperty());
        fillEnabled.selectedProperty().bindBidirectional(waypointOptions.fillProperty());
        ///Color
        lineColor.setValue(Color.web(pathOptions.colorProperty().getValue()));
        lineColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            pathOptions.colorProperty().setValue(ColorUtils.colorToHex(newValue));
        });
        pathOptions.colorProperty().addListener((observable, oldValue, newValue) -> {
            lineColor.setValue(Color.web(newValue));
        });

        pointFillColor.setValue(Color.web(waypointOptions.fillColorProperty().getValue()));
        pointFillColor.valueProperty().addListener((observable, oldValue, newValue) -> {
           waypointOptions.fillColorProperty().setValue(ColorUtils.colorToHex(newValue));
        });
        waypointOptions.fillColorProperty().addListener((observable, oldValue, newValue) -> {
            pointFillColor.setValue(Color.web(newValue));
        });
        pointLinColor.setValue(Color.web(waypointOptions.colorProperty().getValue()));
        pointLinColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            waypointOptions.colorProperty().setValue(ColorUtils.colorToHex(newValue));
        });
        waypointOptions.colorProperty().addListener((observable, oldValue, newValue) -> {
            pointLinColor.setValue(Color.web(newValue));
        });

        measurements.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                layer.showMeasurements();
            } else layer.hideMeasurements();
        });
    }
}
