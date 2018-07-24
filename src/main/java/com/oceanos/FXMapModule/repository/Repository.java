package com.oceanos.FXMapModule.repository;

import com.oceanos.FXMapModule.layers.Layer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class Repository {

    private ObservableList<Layer> layers = FXCollections.observableArrayList();

    private ObjectProperty<Layer> activeLayer = new SimpleObjectProperty<>();

    public ObservableList<Layer> getLayers() {
        return layers;
    }

    public Optional<Layer> getLayerById(long id){
        System.out.println("");
        return layers.stream().filter(l->l.getId() == id).findFirst();
    }

    public void addLayer(Layer layer){
        layers.add(layer);
    }

    public void removeLayer(Layer layer){
        layers.remove(layer);
    }

    public Layer getActiveLayer() {
        return activeLayer.get();
    }

    public ObjectProperty<Layer> activeLayerProperty() {
        return activeLayer;
    }

    public void setActiveLayer(Layer activeLayer) {
        this.activeLayer.set(activeLayer);
    }
}
