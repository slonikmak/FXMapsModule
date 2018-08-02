package com.oceanos.FXMapModule.options;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.oceanos.FXMapModule.app.utills.CommonUtils;
import com.oceanos.FXMapModule.app.utills.ReflectionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import org.eclipse.jetty.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
    public String getJsonString(){
        return getJsonObject().toString();
    }

    public JsonObject getJsonObject(){
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
        return object;
    }

    public Map<String, JsonElement> getOptionsMap(){
        Map<String, JsonElement> map = new HashMap<>();
        JsonParser parser = new JsonParser();
        ReflectionHelper.getAllPropertyFields(this.getClass()).forEach(f->{
            try {
                Object value = ((ReadOnlyProperty)f.get(this)).getValue();
                String type = f.getType().toString();
                String propertyName = CommonUtils.firstLetterLowerCase(f.getName().replace("Property",""));
                if (type.endsWith("IntegerProperty")){
                    map.put(propertyName, new JsonPrimitive((Integer)value));
                } else if (type.endsWith("DoubleProperty")){
                    map.put(propertyName, new JsonPrimitive((Double)value));
                } else if (type.endsWith("StringProperty")){
                    map.put(propertyName, new JsonPrimitive((String) value));
                } else if (type.endsWith("BooleanProperty")){
                    map.put(propertyName, new JsonPrimitive((Boolean)value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    public void fillOptions(String options){
        JsonParser parser = new JsonParser();
        JsonObject  object = parser.parse(options).getAsJsonObject();
        object.keySet().forEach(k->{
            //fixme: хардкод
            if (!k.toLowerCase().equals("name")){
                Method method = ReflectionHelper.getDeepMethod(this.getClass(), "set"+ CommonUtils.capitalize(k));
                try {
                    Class[] types = method.getParameterTypes();
                    Object o = object.get(k);
                    if (types[0].equals(double.class)){
                        method.invoke(this, object.get(k).getAsDouble());
                    } else if (types[0].equals(String.class)){
                        method.invoke(this, object.get(k).getAsString());
                    } else if (types[0].equals(int.class)){
                        method.invoke(this, object.get(k).getAsInt());
                    } else if (types[0].equals(boolean.class)){
                        method.invoke(this, object.get(k).getAsBoolean());
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void fillOptions(Map<String, JsonElement> options){
        JsonParser parser = new JsonParser();
        //JsonObject  object = parser.parse(options).getAsJsonObject();
        options.keySet().forEach(k->{
            //fixme: хардкод
            if (!k.toLowerCase().equals("name")){
                Method method = ReflectionHelper.getDeepMethod(this.getClass(), "set"+ CommonUtils.capitalize(k));
                try {
                    Class[] types = method.getParameterTypes();
                    if (types[0].equals(double.class)){
                        method.invoke(this, options.get(k).getAsDouble());
                    } else if (types[0].equals(String.class)){
                        method.invoke(this, options.get(k).getAsString());
                    } else if (types[0].equals(int.class)){
                        method.invoke(this, options.get(k).getAsInt());
                    } else if (types[0].equals(boolean.class)){
                        method.invoke(this, options.get(k).getAsBoolean());
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
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
