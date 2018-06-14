package com.oceanos.FXMapModule.JSControllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;
import java.util.Set;

/**
 * @autor slonikmak on 14.06.2018.
 */
public class EventController {
    private JsonParser jsonParser;

    public EventController(){
        jsonParser = new JsonParser();
    }

    public void fireEvent(String event){
        System.out.println("i get event");
        JsonElement element = jsonParser.parse(event);
        JsonObject object = element.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();//will return members of your object
        for (Map.Entry<String, JsonElement> entry: entries) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println(object.get("type"));
    }
}
