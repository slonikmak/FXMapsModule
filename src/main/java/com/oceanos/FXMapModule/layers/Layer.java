package com.oceanos.FXMapModule.layers;

import com.oceanos.FXMapModule.options.LayerOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import netscape.javascript.JSObject;


/**
 * @autor slonikmak on 14.06.2018.
 */
public abstract class Layer extends Evented {
    protected long id;
    private StringProperty name = new SimpleStringProperty("layer");

    public Layer(){
        subscribeToOptions();
    }

    public long getId(){
        return id;
    }

    public abstract void setOptions(LayerOptions options);
    //TODO: DO something
    public void subscribeToOptions(){

    }

    public abstract LayerOptions getOptions();

    public String getName(){
        return name.getValue();
    }
    public void setName(String name){
        this.name.setValue(name);
    }
    public StringProperty nameProperty(){
        return name;
    }
    public abstract void addToMap();
    public abstract void remove();
    public abstract void hide();
    public abstract void show();

    //TODO как сделать метод private
    public void setId(long id){
        this.id = id;
    }

    public abstract String convertToJson();
}
