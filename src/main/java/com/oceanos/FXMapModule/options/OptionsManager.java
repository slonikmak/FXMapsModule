package com.oceanos.FXMapModule.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.controllers.PolylineOptionsController;
import com.oceanos.FXMapModule.app.utills.ColorUtils;
import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.PolyLine;
import javafx.scene.paint.Color;

/**
 * @autor slonikmak on 27.06.2018.
 */
public class OptionsManager {
   /* static public void fillOptions(Layer layer){
        if (layer instanceof Marker){
            fillMarkerOptions(layer);
        } else if (layer instanceof PolyLine){
            fillPolyLineOptions(layer);
        } else if (layer instanceof Circle){
            fillCircleOptions(layer);
        }

    }


    static public void fillOptions(Layer layer, String options){
        if (layer instanceof Marker){
            fillMarkerOptions(layer, options);
        } else if (layer instanceof PolyLine){
            fillPolyLineOptions(layer, options);
        } else if (layer instanceof Circle){
            fillCircleOptions(layer, options);
        }
    }


    public static String getOptionsJson(Layer layer){
        if (layer instanceof Marker){
            return getMarkerOptionsJson(layer);
        } else if (layer instanceof PolyLine){
            return getPolylineOptionsJson(layer);
        } else if (layer instanceof Circle){
            return getCircleOptionsJson(layer);
        }
        return null;
    }

    private static void fillCircleOptions(Layer layer, String options) {
        Circle circle = (Circle) layer;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(options).getAsJsonObject();
        circle.setWeight(jsonObject.get("weight").getAsDouble());
        circle.setColor(Color.web(jsonObject.get("color").getAsString()));
        circle.setOpacity(jsonObject.get("opacity").getAsDouble());
        circle.setEditable(jsonObject.get("editable").getAsBoolean());
        circle.setRadius(jsonObject.get("radius").getAsDouble());
        circle.setFill(jsonObject.get("fill").getAsBoolean());
        circle.setFillColor(Color.web(jsonObject.get("fillColor").getAsString()));
        circle.setFillOpacity(jsonObject.get("fillOpacity").getAsDouble());
    }

    private static void fillCircleOptions(Layer layer) {
        Circle circle = (Circle) layer;
        CircleOptions options = new CircleOptions();
        circle.setEditable((Boolean) options.getOption("editable").orElse(false));
        circle.setOpacity((Double) options.getOption("opacity").orElse(1.0));
        circle.setColor(Color.web((String) options.getOption("color").orElse("#3388ff")));
        circle.setWeight((Double) options.getOption("weight").orElse(3));
        circle.setRadius((Double) options.getOption("radius").orElse(15));
        circle.setFillOpacity((Double)options.getOption("fillOpacity").orElse(1.0));
        circle.setFill((Boolean)options.getOption("fill").orElse(false));
        circle.setFillColor(Color.web((String) options.getOption("fillColor").orElse("#3388ff")));
    }


    private static void fillPolyLineOptions(Layer layer, String options) {
        PolyLine line = (PolyLine)layer;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(options).getAsJsonObject();
        line.setWeight(jsonObject.get("weight").getAsDouble());
        line.setColor(Color.web(jsonObject.get("color").getAsString()));
        line.setOpacity(jsonObject.get("opacity").getAsDouble());
        line.setEditable(jsonObject.get("editable").getAsBoolean());
        line.setFill(jsonObject.get("fill").getAsBoolean());
        line.setFillColor(Color.web(jsonObject.get("fillColor").getAsString()));
        line.setFillOpacity(jsonObject.get("fillOpacity").getAsDouble());

    }

    private static void fillMarkerOptions(Layer layer, String options) {

    }


    private static String getCircleOptionsJson(Layer layer) {
        CircleOptions options = new CircleOptions();
        Circle circle = (Circle)layer;
        options.setOption("color", ColorUtils.colorToHex(circle.getColor()));
        options.setOption("weight", circle.getWeight());
        options.setOption("opacity", circle.getOpacity());
        options.setOption("editable", circle.isEditable());
        options.setOption("radius", circle.getRadius());
        options.setOption("fill", circle.isFill());
        options.setOption("fillColor", ColorUtils.colorToHex(circle.getFillColor()));
        options.setOption("fillOpacity", circle.getFillOpacity());
        return options.getJson();
    }

    private static String getPolylineOptionsJson(Layer layer) {
        PathOptions options = new PathOptions();
        PolyLine line = (PolyLine)layer;
        //System.out.println(line.getColor());
        options.setOption("color", ColorUtils.colorToHex(line.getColor()));
        options.setOption("weight", line.getWeight());
        options.setOption("opacity", line.getOpacity());
        options.setOption("editable", line.isEditable());
        options.setOption("fill", line.isFill());
        options.setOption("fillColor", ColorUtils.colorToHex(line.getFillColor()));
        options.setOption("fillOpacity", line.getFillOpacity());
        return options.getJson();
    }

    private static String getMarkerOptionsJson(Layer layer) {
        return null;
    }

    private static void fillPolyLineOptions(Layer layer) {
       *//* PathOptions options = new PathOptions();
        PolyLine line = (PolyLine)layer;
        line.setEditable((Boolean) options.getOption("editable").orElse(false));
        line.setOpacity((Double) options.getOption("opacity").orElse(1.0));
        line.setColor(Color.web((String) options.getOption("color").orElse("#3388ff")));
        line.setWeight((Double) options.getOption("weight").orElse(3));
        line.setFillOpacity((Double)options.getOption("fillOpacity").orElse(1.0));
        line.setFill((Boolean)options.getOption("fill").orElse(false));
        line.setFillColor(Color.web((String) options.getOption("fillColor").orElse("#3388ff")));*//*

    }

    private static void fillMarkerOptions(Layer layer) {

    }*/
}
