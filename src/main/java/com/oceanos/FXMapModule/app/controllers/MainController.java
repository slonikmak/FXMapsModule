package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainController {
    private MapView mapView;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private AnchorPane layersPane;

    @FXML
    private AnchorPane preferencePane;

    @FXML
    private AnchorPane stylesPane;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void addCircle(ActionEvent event) {

    }

    @FXML
    void addMarker(ActionEvent event) {
        EditableController.startMarker();
    }

    @FXML
    void addMission(ActionEvent event) {

    }

    @FXML
    void addRectangle(ActionEvent event) {

    }

    @FXML
    void startPath(ActionEvent event) {

    }

    public void addMarkerByClick(){
        EditableController.startMarker();
    }

    public void addMarkerByCoords(){

    }

    public void initialize(){
        mapView = new MapView();
        mapPane.getChildren().add(mapView);
        TileLayer tileLayer = new TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png","{}");

        mapView.onLoad(()->{
            mapView.addLayer(tileLayer);
        });

        mapView.initWebView();
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 40.0);
    }

    private void initTreeView(){

    }
}
