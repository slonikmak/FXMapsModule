package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

/**
 * @autor slonikmak on 27.06.2018.
 */
public class PolylineOptionsController implements LayerOptionsController {
    private PolyLine layer;

    @FXML
    private AnchorPane preferencePane;

    @FXML
    private TextField nameField;

    @FXML
    private Label lengthLabel;

    @FXML
    private CheckBox editableBox;

    @FXML
    private Label pointsLabel;

    @FXML
    private AnchorPane stylesPane;

    @FXML
    private ColorPicker colorBox;

    @FXML
    private TextField thickField;

    @FXML
    private TextField opacityField;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (PolyLine) layer;
        fillOption();
    }

    private void fillOption() {
        opacityField.textProperty().bindBidirectional(layer.opacityProperty(), new MyStringConverter());
        lengthLabel.textProperty().bindBidirectional(layer.lengthProperty(), new MyStringConverter());
        pointsLabel.textProperty().bindBidirectional(layer.pointsProperty(), new MyStringConverter());
        nameField.textProperty().bindBidirectional(layer.nameProperty());
        editableBox.selectedProperty().bindBidirectional(layer.editableProperty());
        thickField.textProperty().bindBidirectional(layer.weightProperty(), new MyStringConverter());
        colorBox.valueProperty().bindBidirectional(layer.colorProperty());
    }

    private class MyStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number object) {
            String pattern = "##0.00";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(object);

        }

        @Override
        public Double fromString(String string) {
            return Double.valueOf(string);
        }
    }
}
