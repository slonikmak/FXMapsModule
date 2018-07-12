package com.oceanos.FXMapModule;

import com.google.gson.Gson;
import com.oceanos.FXMapModule.JSControllers.EventController;
import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventListener;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MapMouseEvent;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.layers.mission.Mission;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import com.oceanos.FXMapModule.mapControllers.GeoJsonController;
import com.oceanos.FXMapModule.repository.Repository;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.awt.event.MouseEvent;
import java.util.*;

/**
 * @autor slonikmak on 13.06.2018.
 */
public class MapView extends AnchorPane {
    private long id;

    private WebView webView;
    private WebEngine webEngine;
    private EventController eventController;
    private JSObject window;
    private Repository repository;
    private Runnable onLoadHandler;
    private Map<MapEventType, List<MapEventListener>> eventListeners;
    private Gson gson;

    private DoubleProperty currentLat = new SimpleDoubleProperty();
    private DoubleProperty currentLng = new SimpleDoubleProperty();


    public MapView(){
        super();
        repository = new Repository();
        eventController = new EventController(repository);
        gson = new Gson();
        eventListeners = new HashMap<>();

        addEventListener(MapEventType.mousemove, event -> {
            currentLat.setValue(((MapMouseEvent)event).getLat());
            currentLng.setValue(((MapMouseEvent)event).getLng());
        });
    }

    public void initWebView(){
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.setMaxHeight(Integer.MAX_VALUE);
        webView.setMaxWidth(Integer.MAX_VALUE);
        AnchorPane.setBottomAnchor(webView, 0d);
        AnchorPane.setTopAnchor(webView, 0d);
        AnchorPane.setRightAnchor(webView, 0d);
        AnchorPane.setLeftAnchor(webView, 0d);
/*        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);*/
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/index.html").toExternalForm());
        /*WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            System.out.println(message + "[at " + lineNumber + "]");
        });*/
        webEngine.getLoadWorker()
                .stateProperty()
                .addListener((ov, oldState, newState) -> {
                            if (newState == Worker.State.SUCCEEDED) {
                                /*try {
                                    DevToolsDebuggerServer.startDebugServer(webEngine.impl_getDebugger(), 51742);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/
                                window = (JSObject) webEngine.executeScript("window");

                                int res = (int) window.call("getMapId");
                                id = res;

                                window.setMember("javaEventController", eventController);
                                window.setMember("mapEventController", this);
                                //TODO: сделать универсальный способ инициализировать JS controller
                                Marker.jsObject = (JSObject) webEngine.executeScript(Marker.jSController);
                                TileLayer.jsObject = (JSObject) webEngine.executeScript(TileLayer.jSController);
                                WMSTileLayer.jsObject = (JSObject)webEngine.executeScript(WMSTileLayer.jSController);
                                EditableController.jsObject = (JSObject) webEngine.executeScript(EditableController.jSController);
                                EditableController.mapView = this;
                                PolyLine.jsObject = (JSObject) webEngine.executeScript(PolyLine.jSController);
                                Circle.jsObject = (JSObject) webEngine.executeScript(Circle.jSController);
                                Polygon.jsObject = (JSObject)webEngine.executeScript(Polygon.jSController);
                                Mission.jsObject = (JSObject)webEngine.executeScript(Mission.jSController);
                                GeoJsonController.jsObject = (JSObject)webEngine.executeScript(GeoJsonController.jSController);
                                //Waypoint.jsObject = (JSObject)webEngine.executeScript(Waypoint.jSController);
                                if (onLoadHandler != null){
                                    onLoadHandler.run();
                                }
                                //JSObject jsToJava = (JSObject) webEngine.executeScript("fromJavaToJs");
                                //id = javaToJsBridge.getMapId();
                                //jsBridge.initJavaController();
                                //repository.setJsBridge(jsBridge);
                                //jsBridge.resizeMap(webView.getWidth(), webView.getHeight());
                                //setZoom((int) window.call("getZoom"));
                       /* // all next classes are from org.w3c.dom domain
                        org.w3c.dom.events.EventListener listener = (ev) -> {
                            System.out.println("#" + (org.w3c.dom.Element) ev.getTarget());
                        };
                        org.w3c.dom.Document doc = webEngine.getDocument();
                        org.w3c.dom.Element el = doc.getElementById("mapid");
                        ((org.w3c.dom.events.EventTarget) el).addEventListener("click", listener, false);*/
                                //jsBridge.setPosition();
                            }

                            //setMapUrl();
                        }
                );

        /*try {
            DevToolsDebuggerServer.startDebugServer(webEngine.impl_getDebugger(), 51742);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        this.getChildren().add(webView);

    }

    public void addLayer(Layer layer){
        if (layer.getId() == 0) {
            layer.addToMap();
        }
        repository.addLayer(layer);
        registerHandlers(layer);
        layer.addEventListener(MapEventType.remove, (e)->{
            repository.removeLayer(layer);
            System.out.println("remove");
        });
    }

    public void removeLayer(Layer layer){
        repository.removeLayer(layer);
        System.out.println("remove");
    }

    private void registerHandlers(Layer layer) {
        layer.addEventListener(MapEventType.click, (e)->{
            repository.setActiveLayer(layer);
        });
    }

    public void onLoad(Runnable handler){
        this.onLoadHandler = handler;
    }

    public void addEventListener(MapEventType eventType, MapEventListener listener){
        List<MapEventListener> listeners = eventListeners.entrySet().stream().filter(e->e.getKey().equals(eventType)).findFirst().map(Map.Entry::getValue).orElse(new ArrayList<>());
        if (listeners.isEmpty()) eventListeners.put(eventType, listeners);
        listeners.add(listener);
    }

    public void fireEvent(MapEvent event){
        //stem.out.println("Fire event from js");
        eventListeners.entrySet().stream()
                .filter(e->e.getKey().equals(event.getType()))
                .findFirst().map(Map.Entry::getValue)
                .ifPresent((l)->l.forEach((listener)->listener.handle(event)));
        //System.out.println("end Fire event from js");
    }

    public void removeEventListener(MapEventType eventType, MapEventListener listener){
        eventListeners.get(eventType).remove(listener);
    }

    public void fireEventFromJS(String event){
        //System.out.println("get event from js");
        MapEvent mapEvent = EventController.parseEventFromJs(event);
        fireEvent(mapEvent);
    }

    public void flyTo(double lat, double lng){
        window.call("flyTo", lat, lng);
    }

    public ObservableList<Layer> getLayers(){
        return repository.getLayers();
    }

    public ObjectProperty<Layer> activeLayerProperty(){
        return repository.activeLayerProperty();
    }

    public void setActivLayer(Layer layer){
        repository.setActiveLayer(layer);
    }

    public double getCurrentLat() {
        return currentLat.get();
    }

    public DoubleProperty currentLatProperty() {
        return currentLat;
    }

    public double getCurrentLng() {
        return currentLng.get();
    }

    public DoubleProperty currentLngProperty() {
        return currentLng;
    }
}
