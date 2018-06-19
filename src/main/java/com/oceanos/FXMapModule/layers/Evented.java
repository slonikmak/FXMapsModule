package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventListener;
import com.oceanos.FXMapModule.events.MapEventType;
import java.util.*;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class Evented {
    private Map<MapEventType, List<MapEventListener>> eventListeners;

    public Evented(){
        eventListeners = new HashMap<>();
    }

    public void addEventListener(MapEventType type, MapEventListener eventListener){
        if (eventListeners.entrySet().stream().noneMatch((x)->x.getKey().equals(type))){
            eventListeners.put(type, new ArrayList<>());
        }
        eventListeners.get(type).add(eventListener);
    }

    public Optional<List<MapEventListener>> getEventListeners(MapEventType event){
        return Optional.ofNullable(eventListeners.get(event));
    }

    public void fireEvent(MapEvent event){
        getEventListeners(event.getType()).ifPresent(mapEventListeners -> {
            mapEventListeners.forEach(l->l.handle(event));
        });
    }
}
