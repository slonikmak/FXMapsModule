package com.oceanos.FXMapModule.layers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.app.properties.ResourceManager;

import com.oceanos.FXMapModule.options.LayerOptions;
import javafx.beans.property.*;

import netscape.javascript.JSObject;


/**
 * @autor slonikmak on 15.06.2018.
 */
public class TileLayer extends Layer {

    public static String jSController = "tileLayerController";
    public static JSObject jsObject;

    private StringProperty url = new SimpleStringProperty();
    private StringProperty cachedUrl = new SimpleStringProperty();
    private LayerOptions options;
    private BooleanProperty cashed = new SimpleBooleanProperty(false);
    private BooleanProperty loadFromCache = new SimpleBooleanProperty(false);
    private ObjectProperty<String[]> domains = new SimpleObjectProperty<>();


    public TileLayer(String url){
        this.url.setValue(url);
        //createCachedUrl();

        loadFromCache.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                setUrl(cachedUrl.getValue());
            } else {
                setUrl(this.url.getValue());
            }
        });
    }

    public TileLayer(String url, LayerOptions options){
        this(url);
        this.options = options;
    }



    @Override
    public void setOptions(LayerOptions options) {

    }

    @Override
    public LayerOptions getOptions() {
        return null;
    }

    public void addToMap(){
        System.out.println("add tile to map");
        int a = (int) jsObject.call("addTileLayer", url.get(), options);
        id = a;
    }

    public void setUrl(String url){
        jsObject.call("setUrl", getId(), url);
    }

    @Override
    public void remove() {
        jsObject.call("removeLayer", this.getId());
    }

    @Override
    public void hide() {
        jsObject.call("hide");
    }

    @Override
    public void show() {
        jsObject.call("show");
    }

    @Override
    public String convertToJson() {
        return convertToRawJsonObject().toString();
    }

    @Override
    public void setName(String name){
        super.setName(name);
        //createCachedUrl();
    }

    public JsonObject convertToRawJsonObject(){
        JsonObject object = new JsonObject();
        object.addProperty("name", getName());
        object.addProperty("url", getUrl());
        return object;
    }

    public StringProperty urlProperty(){
        return url;
    }

    public String getUrl(){
        return url.getValue();
    }

    public boolean isCashed() {
        return cashed.get();
    }

    public BooleanProperty cashedProperty() {
        return cashed;
    }

    public boolean isLoadFromCache() {
        return loadFromCache.get();
    }

    public BooleanProperty loadFromCacheProperty() {
        return loadFromCache;
    }

    public void setLoadFromCache(boolean loadFromCache) {
        this.loadFromCache.set(loadFromCache);
    }

    public String[] getDomains() {
        return domains.get();
    }

    public ObjectProperty<String[]> domainsProperty() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains.set(domains);
    }

    public static TileLayer getFromJson(String toString) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(toString).getAsJsonObject();
        TileLayer tileLayer = new TileLayer(object.get("url").getAsString());
        tileLayer.setName(object.get("name").getAsString());
        return tileLayer;
    }

    public void createCachedUrl(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("myapp:///").append(ResourceManager.getInstance().getLayersCacheFolder().resolve(getName()).resolve("{z}").resolve("{x}").resolve("{y}.png"));
        cachedUrl.setValue(stringBuilder.toString().replace("\\", "/"));
        System.out.println("Cached url "+stringBuilder.toString().replace("\\", "/"));
    }


}
