package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.TileLayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;



/**
 * @autor slonikmak on 24.07.2018.
 */
public class TileLayerOptionsController implements LayerOptionsController {
    private TileLayer layer;

    @FXML
    private Text name;

    @FXML
    private Text url;

    @FXML
    private CheckBox cashBox;

    @FXML
    private CheckBox fromCache;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (TileLayer) layer;
        fillOptions();
    }

    @Override
    public void fillOptions() {
        name.textProperty().bind(layer.nameProperty());
        url.textProperty().bind(layer.urlProperty());
        cashBox.selectedProperty().bindBidirectional(layer.cashedProperty());
        fromCache.selectedProperty().bindBidirectional(layer.loadFromCacheProperty());

        fromCache.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                cashBox.setSelected(false);
            }
        });

        cashBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                fromCache.setSelected(false);
            }
        });


    }

    public void initialize(){
        //fillOptions();

    }


}
