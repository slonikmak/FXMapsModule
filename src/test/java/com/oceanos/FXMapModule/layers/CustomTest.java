package com.oceanos.FXMapModule.layers;

import com.google.gson.Gson;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.hildan.fxgson.FxGson;

/**
 * @autor slonikmak on 19.06.2018.
 */
public class CustomTest {
    static ObservableMap<String, Object> people = FXCollections.observableHashMap();

    public static void main(String[] args) {

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
