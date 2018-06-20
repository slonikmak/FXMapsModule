package com.oceanos.FXMapModule.options;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.hildan.fxgson.FxGson;

import java.util.Optional;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class LayerOptions {
    private ObservableMap<String, Object> options = FXCollections.observableHashMap();
    private MapChangeListener<String, Object> changeListener;

    public LayerOptions(){
        init();
        options.addListener((MapChangeListener<String, Object>) change -> {
            if (changeListener!= null){
                changeListener.onChanged(change);
            }
        });
    }

    public void setOption(String option, Object value){
        options.put(option, value);

    }

    public Optional<Object> getOption(String option){
        return Optional.ofNullable(options.get(option));
    }
    public String getJson(){
        Gson fxGson = FxGson.create();
        String result = fxGson.toJson(options);
        return result;
    }

    public void setChangeListener(MapChangeListener<String, Object> changeListener){
        this.changeListener = changeListener;
    }
    abstract void init();

}
