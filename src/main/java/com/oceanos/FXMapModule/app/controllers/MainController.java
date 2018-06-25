package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.view.LayerTreeCell;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class MainController {
    private MapView mapView;

    @FXML
    private TreeView<Layer> layerTreeView;

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

    public void addMarkerByClick() {
        EditableController.startMarker();
    }

    public void addMarkerByCoords() {

    }

    public void initialize() {
        mapView = new MapView();
        mapPane.getChildren().add(mapView);
        initTreeView();
        TileLayer tileLayer = new TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", "{}");
        tileLayer.setName("osm map");
        mapView.onLoad(() -> {
            mapView.addLayer(tileLayer);
            Marker marker = new Marker(60.055142,30.3400618);
            Marker marker1 = new Marker(60.0555,30.34007);
            marker.setName("marker 1");
            marker.setName("marker 2");

            mapView.addLayer(marker);
            mapView.addLayer(marker1);
        });

        mapView.initWebView();
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 40.0);


    }

    private void initTreeView() {
        TreeItem<Layer> root = new TreeItem<>();
        root.setExpanded(true);
        //root.getChildren().addAll(new TreeItem<>("one"), new TreeItem<>("two"));
        layerTreeView.setRoot(root);
        layerTreeView.setShowRoot(false);
        //layersPane.getChildren().add(layerTreeView);
        layerTreeView.setCellFactory(param -> new LayerTreeCell());
        mapView.getLayers().addListener((ListChangeListener<Layer>) c -> {
            c.next();
            if (c.wasAdded()) {
                Layer layer = c.getAddedSubList().get(0);
                layerTreeView.getRoot().getChildren().add(new TreeItem<>(layer));
            }
        });
    }

}
