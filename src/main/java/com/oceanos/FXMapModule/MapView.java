package com.oceanos.FXMapModule;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mohamnag.fxwebview_debugger.DevToolsDebuggerServer;
import com.oceanos.FXMapModule.JSControllers.EventController;
import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventListener;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MapMouseEvent;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.PolyLine;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.mapControllers.EditadleController;
import com.oceanos.FXMapModule.repository.Repository;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.*;
import java.util.stream.Collectors;

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


    public MapView(){
        super();
        repository = new Repository();
        eventController = new EventController(repository);
        gson = new Gson();
        eventListeners = new HashMap<>();
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
                                EditadleController.jsObject = (JSObject) webEngine.executeScript(EditadleController.jSController);
                                EditadleController.mapView = this;
                                PolyLine.jsObject = (JSObject) webEngine.executeScript(PolyLine.jSController);
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
        if (layer.getId() == 0) layer.addToMap();
        repository.addLayer(layer);
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
        System.out.println("Fire event from js");
        eventListeners.entrySet().stream()
                .filter(e->e.getKey().equals(event.getType()))
                .findFirst().map(Map.Entry::getValue)
                .ifPresent((l)->l.forEach((listener)->listener.handle(event)));
        System.out.println("end Fire event from js");
    }

    public void removeEventListener(MapEventType eventType, MapEventListener listener){
        eventListeners.get(eventType).remove(listener);
    }

    public void fireEventFromJS(String event){
        System.out.println("get event from js");
        MapEvent mapEvent = EventController.parseEventFromJs(event);
        fireEvent(mapEvent);

    }

    public ObservableList<Layer> getLayers(){
        return repository.getLayers();
    }
}
