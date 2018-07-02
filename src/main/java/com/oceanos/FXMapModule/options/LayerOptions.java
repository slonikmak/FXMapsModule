package com.oceanos.FXMapModule.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.utills.CommonUtils;
import com.oceanos.FXMapModule.app.utills.ReflectionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import org.eclipse.jetty.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @autor slonikmak on 19.06.2018.
 */
public abstract class LayerOptions implements Observable {
    private InvalidationListener invalidationListener;

    public LayerOptions(){
        init();
    }

   /* public void setOption(String option, Object value){
        try {
            ((Property)ReflectionHelper.getDeepField(this.getClass(), option).get(this)).setValue(value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //options.put(option, value);
    }*/
/*
    public Optional<Object> getOption(String option){
        return Optional.ofNullable(options.get(option));
    }*/
    public String getJson(){
        JsonObject object = new JsonObject();
        ReflectionHelper.getAllPropertyFields(this.getClass()).forEach(f->{
            try {
                Object value = ((ReadOnlyProperty)f.get(this)).getValue();
                String type = f.getType().toString();
                String propertyName = CommonUtils.firstLetterLowerCase(f.getName().replace("Property",""));
                if (type.endsWith("IntegerProperty")){
                    object.addProperty(propertyName, (Integer)value);
                } else if (type.endsWith("DoubleProperty")){
                    object.addProperty(propertyName, (Double)value);
                } else if (type.endsWith("StringProperty")){
                    object.addProperty(propertyName, (String) value);
                } else if (type.endsWith("BooleanProperty")){
                    object.addProperty(propertyName, (Boolean)value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return object.toString();
    }

    public void fillOptions(String options){
        JsonParser parser = new JsonParser();
        JsonObject  object = parser.parse(options).getAsJsonObject();
        object.keySet().forEach(k->{
            Method method = ReflectionHelper.getDeepMethod(this.getClass(), "set"+ CommonUtils.capitalize(k));
            try {
                method.invoke(this, object.get(k));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    abstract void init();


    @Override
    public void addListener(InvalidationListener listener) {
        this.invalidationListener = listener;
        ReflectionHelper.getAllPropertyFields(this.getClass()).forEach(f->{
            try {
                ((Observable)f.get(this)).addListener(invalidationListener);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        this.invalidationListener = null;
    }
}
