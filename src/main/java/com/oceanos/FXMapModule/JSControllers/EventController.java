package com.oceanos.FXMapModule.JSControllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.events.*;
import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.repository.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class EventController {
    private static Logger logger = Logger.getLogger(EventController.class.getName());
    private JsonParser jsonParser;
    private Repository repository;
    private Gson gson;

    public EventController(Repository repository){
        jsonParser = new JsonParser();
        this.repository = repository;
        this.gson = new Gson();
    }

    public void fireEvent(String event){

        MapEvent mapEvent = parseEventFromJs(event);
        Optional<Layer> layer = repository.getLayerById(mapEvent.getTarget());
        layer.ifPresent((l)->{
            l.fireEvent(mapEvent);
        });
    }

    public static MapEvent parseEventFromJs(String event){
        logger.info("Got event "+event);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(event).getAsJsonObject();
        String eventClass = object.get("eventClass").getAsString();
        MapEvent mapEvent = null;
        switch (eventClass){
            case "MouseEvent": {
                mapEvent = new MapMouseEvent(object);
                break;
            }
            case "LayerEvent": {
                mapEvent = new LayerEvent(object);
                break;
            }
            case "EditableEvent": {
                mapEvent = new EditableEvent(object);
                break;
            }case "MissionEvent": {
                mapEvent = new MissionEvent(object);
                break;
            }case "TileEvent": {
                mapEvent = new TileEvent(object);
            }

        }
        return mapEvent;
    }
}
