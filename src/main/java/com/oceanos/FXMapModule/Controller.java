package com.oceanos.FXMapModule;

import com.oceanos.FXMapModule.events.*;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;

public class Controller {
    private MapView mapView;

    @FXML
    AnchorPane mainPane;

    MapEventListener clickOnAddMarkerListener = (e)->{
        clickOnAddMarkerHandler((MapMouseEvent) e);
    };

    private void clickOnAddMarkerHandler(MapMouseEvent e){
        MapEventListener that = clickOnAddMarkerListener;
        MapMouseEvent event = (MapMouseEvent) e;
        Marker marker = new Marker(event.getLat(), event.getLng());
        //marker.setIcon("file:/C:/Users/Oceanos/Downloads/icons8.png");
        //marker.getOptions().setOption("draggable", true);
        mapView.addLayer(marker);
        mapView.removeEventListener(MapEventType.click, clickOnAddMarkerListener);
    }
    @FXML
    void startDrawLine(){
        EditableController.startPolyLine();
    }

    @FXML
    void addCustomLine(){
        //lat=51.50158353472559 lng=-0.11003494262695312 lat=51.5116270804117 lng=-0.07518768310546876
        PolyLine line = new PolyLine(Arrays.asList(new LatLng(51.50158353472559,-0.11003494262695312), new LatLng(51.5116270804117,-0.07518768310546876)));
        //line.getOptions().setOption("color", "red");
        mapView.addLayer(line);
        line.addEventListener(MapEventType.click, (event -> System.out.println("click on line")));
    }

    @FXML
    void createMarker(MouseEvent event){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addOnMapByClick = new MenuItem("Добавить маркер на карте");
        MenuItem addOnMapByCoords = new MenuItem("Ввести координаты");
        contextMenu.getItems().addAll(addOnMapByClick, addOnMapByCoords);
        contextMenu.show((Node)event.getSource(), event.getScreenX(),event.getScreenY());

        addOnMapByClick.setOnAction((e)->{
            //mapView.addEventListener(MapEventType.click, clickOnAddMarkerListener);
            Marker marker = EditableController.startMarker();
            marker.setIcon("file:/C:/Users/Oceanos/Downloads/icons8.png");
            marker.addEventListener(MapEventType.editable_drawing_commit, event1 -> {
                System.out.println("add circle!!!");
                Circle circle = new Circle(new LatLng(((EditableEvent)event1).getLat(), ((EditableEvent)event1).getLng()), 50);
                marker.latProperty().addListener((a,b,c)->{
                    circle.setLatLng(marker.getLat(), marker.getLng());
                });
                mapView.addLayer(circle);
            });

        });
    }

    public void  initialize(){
        mapView = new MapView();
        TileLayer tileLayer = new TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png","{}");
        mapView.onLoad(()->{
            EditableController.createTooltip();
            mapView.getLayers().addListener((ListChangeListener<Layer>) c -> {
                c.next();
                if (c.wasAdded()){
                    System.out.println("add layer "+c.getAddedSubList().get(c.getAddedSubList().size()-1));
                }
            });
            mapView.addLayer(tileLayer);
            mapView.addEventListener(MapEventType.click, (e)->{
               /* MapMouseEvent event = (MapMouseEvent) e;
                Marker marker = new Marker(event.getLat(), event.getLng());
                marker.setIcon("file:/C:/Users/Oceanos/Downloads/icons8.png");
                marker.addOption("draggable", "true");
                mapView.addLayer(marker);
                marker.addEventListener(MapEventType.click, (event1 -> {
                    System.out.println("I have got event!!");
                }));*/
            });
        });

        //FIXME: слишком много инициализаторов
        mapView.initWebView();
        mainPane.getChildren().add(mapView);
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 0d);
    }
}
