package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EventListener;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @autor slonikmak on 14.06.2018.
 */
class EventedTest {

    static MapEvent mapEvent;
    static MapEventListener eventListener;
    static Evented evented;

    @BeforeAll
    static void setup(){
        mapEvent = new MapEvent();
        eventListener = new MapEventListener() {
            @Override
            public void handle(MapEvent event) {
                System.out.println("event");
            }
        };
        evented = new Evented();
        evented.addEventListener(mapEvent, eventListener);
    }

    @Test
    void getEventListeners() {
        Assertions.assertEquals(eventListener, evented.getEventListeners(mapEvent).get().get(0));
    }

    @Test
    void getNotUsedEventListener(){
        MapEvent e = new MapEvent();
        Assertions.assertEquals(Optional.empty(), evented.getEventListeners(e));
    }


}