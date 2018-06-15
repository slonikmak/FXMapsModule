package com.oceanos.FXMapModule.repository;

import com.oceanos.FXMapModule.layers.Layer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * @autor slonikmak on 15.06.2018.
 */
public class Repository {

    private ObservableList<Layer> layers = FXCollections.observableArrayList();

    public ObservableList<Layer> getLayers() {
        return layers;
    }

    public Optional<Layer> getLayerById(long id){
        return layers.stream().filter(l->l.getId() == id).findFirst();
    }

    public void addLayer(Layer layer){
        layers.add(layer);
    }

}
