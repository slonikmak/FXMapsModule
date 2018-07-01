package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import com.oceanos.FXMapModule.app.utills.ColorUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import org.hildan.fxgson.FxGson;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class CustomTest {
    static ObservableMap<String, Object> people = FXCollections.observableHashMap();

    public static void main(String[] args) throws IOException {
        //String path = "C:/Users/Oceanos/mapApp/icons/marker-icon.png";
        //System.out.println(new File(path).toURI());

        //Color color = Color.web("#3388ff");
        //System.out.println("#" + Integer.toHexString(color.hashCode()).substring(0,6));

       addProperty("1", 1);
       addProperty("2","2");
       addProperty("3", 3.0);

        //people.addAll(new Person("Anton", "Doe", 12), new Person("3Anton", "Do3e", 14));
        Gson gson = FxGson.create();
        System.out.println(gson.toJson(people));

    }

    static class Person{
        StringProperty name = new SimpleStringProperty();
        ObjectProperty<String> lastName = new SimpleObjectProperty<>();
        IntegerProperty age = new SimpleIntegerProperty();

        public Person(String name, String lastName, int age) {
            this.name.setValue(name);
            this.lastName.setValue(lastName);
            this.age.setValue(age);
        }
    }
    static void addProperty(String key,Object o){
       /* Property property = null;
        if (o instanceof Integer){
            property = new SimpleIntegerProperty((Integer) o);
        } else if (o instanceof String){
            property = new SimpleStringProperty((String) o);
        } else if (o instanceof Double){
            property = new SimpleDoubleProperty((Double) o);
        }*/
        people.put(key, o);
    }

}
