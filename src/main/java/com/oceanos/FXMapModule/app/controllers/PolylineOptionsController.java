package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

/**
 * @autor slonikmak on 27.06.2018.
 */
public class PolylineOptionsController extends PathOptionsController {
    //private PolyLine layer;

    @FXML
    private AnchorPane preferencePane;

    @FXML
    private TextField nameField;

    @FXML
    private Label lengthLabel;


    @FXML
    private Label pointsLabel;

    @FXML
    private AnchorPane stylesPane;




    @Override
    public void setLayer(Layer layer) {
        super.setLayer(layer);
        /*this.layer = (PolyLine) layer;
        fillOptions();*/
    }

    @Override
    public void fillOptions() {
        super.fillOptions();
        lengthLabel.textProperty().bindBidirectional(((PolyLine)layer).lengthProperty(), new MyStringConverter());
        pointsLabel.textProperty().bindBidirectional(((PolyLine)layer).pointsProperty(), new MyStringConverter());
        nameField.textProperty().bindBidirectional(((PolyLine)layer).nameProperty());

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
            string = string.replace(",",".");
            if (string.isEmpty()) return 0.;
            return Double.valueOf(string);
        }
    }
}
