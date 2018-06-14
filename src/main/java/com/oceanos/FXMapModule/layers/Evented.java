package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Evented {
    private Map<MapEvent, List<MapEventListener>> eventListeners;

    public Evented(){
        eventListeners = new HashMap<>();
    }

    public void addEventListener(MapEvent event, MapEventListener eventListener){
        if (eventListeners.entrySet().stream().noneMatch((x)->x.getKey().equals(event))){
            eventListeners.put(event, new ArrayList<>());
        }
        eventListeners.get(event).add(eventListener);
    }

    public Optional<List<MapEventListener>> getEventListeners(MapEvent event){
        return Optional.ofNullable(eventListeners.get(event));
    }

    private void fireEvent(MapEvent event){
        eventListeners.get(event).forEach(l->{
            l.handle(event);
        });
    }
}
