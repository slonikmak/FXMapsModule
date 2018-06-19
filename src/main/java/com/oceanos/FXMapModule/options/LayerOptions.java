package com.oceanos.FXMapModule.options;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.hildan.fxgson.FxGson;

import java.util.Optional;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class LayerOptions {
    private ObservableMap<String, ObjectProperty> options = FXCollections.observableHashMap();

    public LayerOptions(){
        init();
    }

    public void setOption(String option, Object value){
        options.put(option, new SimpleObjectProperty(value));
    }
    public Optional<Object> getOptionValue(String option){
        return Optional.ofNullable(options.get(option).getValue());
    }
    public Optional<Object> getOption(String option){
        return Optional.ofNullable(options.get(option));
    }
    public String getJson(){
        Gson fxGson = FxGson.create();
        return fxGson.toJson(options);
    }
    abstract void init();

}
