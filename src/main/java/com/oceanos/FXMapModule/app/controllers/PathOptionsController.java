package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.utills.ColorUtils;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Path;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
        //((PathOptions)layer.getOptions()).colorProperty().bindBidirectional(colorBox.valueProperty(), new ColorUtils.ColorStringConverter());
        fillCheckBox.selectedProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillProperty());
        //((PathOptions)layer.getOptions()).fillColorProperty().bindBidirectional(fillColorPicker.valueProperty(), new ColorUtils.ColorStringConverter());
        //fillColorPicker.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillColorProperty());
        fillOpacityField.valueProperty().bindBidirectional(((PathOptions)layer.getOptions()).fillOpacityProperty());

        //FIXME: что-то сделать с bindings
        colorBox.setValue(Color.web(((PathOptions)layer.getOptions()).colorProperty().getValue()));
        colorBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            ((PathOptions)layer.getOptions()).colorProperty().setValue(ColorUtils.colorToHex(newValue));
        });
        ((PathOptions)layer.getOptions()).colorProperty().addListener((observable, oldValue, newValue) -> {
            colorBox.setValue(Color.web(newValue));
        });

        fillColorPicker.setValue(Color.web(((PathOptions)layer.getOptions()).fillColorProperty().getValue()));
        fillColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            ((PathOptions)layer.getOptions()).fillColorProperty().setValue(ColorUtils.colorToHex(newValue));
        });
        ((PathOptions)layer.getOptions()).fillColorProperty().addListener((observable, oldValue, newValue) -> {
            fillColorPicker.setValue(Color.web(newValue));
        });

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
