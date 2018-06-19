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
    public static void main(String[] args) {
        ObservableMap<String, ObjectProperty> people = FXCollections.observableHashMap();
        people.put("Anton", new SimpleObjectProperty(3));
        people.put("Anton3",new SimpleObjectProperty("aaaa"));

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
}
