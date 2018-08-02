package com.oceanos.FXMapModule;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor slonikmak on 01.08.2018.
 */
public class GeoJsonTest {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GeometryAdapterFactory())
                .create();

        String string = "C:/Users/Oceanos/Desktop/1.json";



        LineString lineString = LineString.of(Point.from(29.947572, 60.725013), Point.from(29.95079,60.723124));

        JsonParser parser = new JsonParser();
        Map<String, JsonElement> prop = new HashMap<>();
        prop.put("a",parser.parse("d"));

        ImmutableMap<String, JsonElement> map = ImmutableMap.copyOf(prop);
        List<Feature> features = new ArrayList<>();
        Feature feature = Feature.of(lineString).withProperties(map);
        features.add(feature);

        FeatureCollection featureCollection = new FeatureCollection(features);

        System.out.println(gson.toJson(feature));

       /* try {
            Feature collection = gson.fromJson(new FileReader(new File(string)), Feature.class);
            System.out.println(collection.properties().get("name").getAsString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }
}
