package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Path;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

/**
 * @autor slonikmak on 28.06.2018.
 */
public class PathOptionsController implements LayerOptionsController {
    Path layer;

    @FXML
    private AnchorPane stylesPane;

    @FXML
    private ColorPicker colorBox;

    @FXML
    private TextField thickField;

    @FXML
    private Slider opacityField;

    @FXML
    private AnchorPane preferencePane;

    @FXML
    private TextField nameField;

    @FXML
    private CheckBox editableBox;

    @FXML
    private Slider fillOpacityField;

    @FXML
    private ColorPicker fillColorPicker;

    @FXML
    private CheckBox fillCheckBox;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (Path)layer;
        fillOptions();
    }

    @Override
    public void fillOptions() {
        opacityField.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).opacityProperty());
        nameField.textProperty().bindBidirectional(layer.nameProperty());
        editableBox.selectedProperty().bindBidirectional(((PathOptions)layer.getOptions()).editableProperty());
        thickField.textProperty().bindBidirectional(((PathOptions)layer.getOptions()).weightProperty(), new MyStringConverter());
        colorBox.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).colorProperty());
        fillCheckBox.selectedProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillProperty());
        fillColorPicker.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillColorProperty());
        fillOpacityField.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillOpacityProperty());

    }

    class MyStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number object) {
            String pattern = "##0.00";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(object);

        }

        @Override
        public Double fromString(String string) {
            string = string.replace(",",".");
            if (string.isEmpty()) return 0.;
            return Double.valueOf(string);
        }
    }
}
