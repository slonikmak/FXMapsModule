package com.oceanos.FXMapModule.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.controllers.PolylineOptionsController;
import com.oceanos.FXMapModule.app.utills.ColorUtils;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.scene.paint.Color;

/**
 * @autor slonikmak on 27.06.2018.
 */
public class OptionsManager {
    static public void fillOptions(Layer layer){
        if (layer instanceof Marker){
            fillMarkerOptions(layer);
        } else if (layer instanceof PolyLine){
            fillPolyLineOptions(layer);
        }
    }

    static public void fillOptions(Layer layer, String options){
        if (layer instanceof Marker){
            fillMarkerOptions(layer, options);
        } else if (layer instanceof PolyLine){
            fillPolyLineOptions(layer, options);
        }
    }

    private static void fillPolyLineOptions(Layer layer, String options) {
        PolyLine line = (PolyLine)layer;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(options).getAsJsonObject();
        line.setWeight(jsonObject.get("weight").getAsDouble());
        line.setColor(Color.web(jsonObject.get("color").getAsString()));
        line.setOpacity(jsonObject.get("opacity").getAsDouble());
        line.setEditable(jsonObject.get("editable").getAsBoolean());
    }

    private static void fillMarkerOptions(Layer layer, String options) {

    }

    public static String getOptionsJson(Layer layer){
        if (layer instanceof Marker){
            return getMarkerOptionsJson(layer);
        } else if (layer instanceof PolyLine){
            return getPolylineOptionsJson(layer);
        }
        return null;
    }

    private static String getPolylineOptionsJson(Layer layer) {
        PathOptions options = new PathOptions();
        PolyLine line = (PolyLine)layer;
        System.out.println(line.getColor());
        options.setOption("color", ColorUtils.colorToHex(line.getColor()));
        options.setOption("weight", line.getWeight());
        options.setOption("opacity", line.getOpacity());
        options.setOption("editable", line.isEditable());
        return options.getJson();
    }

    private static String getMarkerOptionsJson(Layer layer) {
        return null;
    }

    private static void fillPolyLineOptions(Layer layer) {
        PathOptions options = new PathOptions();
        PolyLine line = (PolyLine)layer;
        line.setEditable((Boolean) options.getOption("editable").orElse(false));
        line.setOpacity((Double) options.getOption("opacity").orElse(1.0));
        line.setColor(Color.web((String) options.getOption("color").orElse("#3388ff")));
        line.setWeight((Double) options.getOption("weight").orElse(3));
    }

    private static void fillMarkerOptions(Layer layer) {

    }
}
