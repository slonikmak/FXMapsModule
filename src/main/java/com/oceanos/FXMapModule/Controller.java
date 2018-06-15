package com.oceanos.FXMapModule;

import com.oceanos.FXMapModule.events.MapEvent;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.MapMouseEvent;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.TileLayer;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    AnchorPane mainPane;

    public void  initialize(){
        MapView mapView = new MapView();
        TileLayer tileLayer = new TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png","{}");
        mapView.onLoad(()->{
            mapView.addLayer(tileLayer);
            mapView.addEventListener(MapEventType.click, (e)->{
                MapMouseEvent event = (MapMouseEvent) e;
                Marker marker = new Marker(event.getLat(), event.getLng());
                marker.addOption("draggable", "true");
                mapView.addLayer(marker);
                marker.addEventListener(MapEventType.click, (event1 -> {
                    System.out.println("I have got event!!");
                }));
            });
        });
        mapView.initWebView();
        mainPane.getChildren().add(mapView);
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 0d);
    }
}
