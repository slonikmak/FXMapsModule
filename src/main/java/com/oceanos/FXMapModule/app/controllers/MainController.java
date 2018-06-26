package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.view.LayerTreeCell;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.io.IOException;

public class MainController {
    private MapView mapView;

    @FXML
    private AnchorPane layerOptionsPane;

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
            marker.setIcon("file:/C:/Users/Oceanos/Downloads/icons8.png");
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

        layerTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.getValue());
            if (newValue.getValue() instanceof Marker){
                mapView.flyTo(((Marker)newValue.getValue()).getLat(), ((Marker)newValue.getValue()).getLng());
                fillOptionsPane(newValue.getValue());
            }
        });
    }

    private void fillOptionsPane(Layer value) {
        VBox elem = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/markerOptions.fxml"));
        try {
            elem = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerOptionsController controller = loader.getController();
        controller.setLayer(value);
        layerOptionsPane.getChildren().clear();
        layerOptionsPane.getChildren().add(elem);
    }

}
