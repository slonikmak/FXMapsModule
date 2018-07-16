package com.oceanos.FXMapModule.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @autor slonikmak on 16.07.2018.
 */
public class GeoJsonUtils {
    public static class GeoJsonBuilder{
        JsonObject featureCollection;
        JsonArray features;

        public GeoJsonBuilder(){
            featureCollection = new JsonObject();
            featureCollection.addProperty("type", "FeatureCollection");
            features = new JsonArray();
            featureCollection.add("features", features);
        }
        public JsonObject createFeature(){
            JsonObject feature = new JsonObject();
            feature.addProperty("type", "Feature");
            feature.add("geometry", new JsonObject());
            features.add(feature);
            return feature;
        }

        public void addFeature(JsonObject feature){
            features.add(feature);
        }

        public JsonObject addPoint(double lat, double lng){
            JsonObject object = createFeature();
            JsonObject geometry = object.getAsJsonObject("geometry");
            geometry.addProperty("type", "Point");
            JsonArray coords = new JsonArray();
            coords.add(lng);
            coords.add(lat);
            geometry.add("coordinates", coords);
            features.add(object);
            return object;
        }
        public JsonObject addLineString(List<double[]> coords){
            JsonObject object = createFeature();
            JsonObject geometry = object.getAsJsonObject("geometry");
            geometry.addProperty("type", "LineString");
            JsonArray coordinates = new JsonArray();
            coords.forEach(c->{
                JsonArray coord = new JsonArray();
                coord.add(c[1]);
                coord.add(c[0]);
                coordinates.add(coord);
            });
            geometry.add("coordinates", coordinates);
            features.add(object);
            return object;
        }

        public JsonObject getFeatureCollection() {
            return featureCollection;
        }

        public JsonArray getFeatures() {
            return features;
        }
    }
}
