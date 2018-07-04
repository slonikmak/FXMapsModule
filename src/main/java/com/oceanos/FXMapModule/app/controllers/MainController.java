package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.app.view.LayerTreeCell;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.layers.mission.Mission;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import com.oceanos.FXMapModule.options.WmsLayerOptions;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

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
    private Label lat;

    @FXML
    private Label lng;

    @FXML
    void addCircle(ActionEvent event) {
        EditableController.startCircle();
    }

    @FXML
    void addMarker(ActionEvent event) {
        EditableController.startMarker();
    }

    @FXML
    void addMission(ActionEvent event) {
        EditableController.startMission();
    }

    @FXML
    void addRectangle(ActionEvent event) {
        EditableController.startPolygon();
    }

    @FXML
    void startPath(ActionEvent event) {
        EditableController.startPolyLine();
    }

    @FXML
    void saveSelected(ActionEvent event){
        Optional.ofNullable(mapView.activeLayerProperty().getValue()).ifPresent(l->{
            if (l instanceof Mission){
                saveMission((Mission) l);
            }
        });
    }

    private void saveMission(Mission l) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения миссии");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл миссии", "*.mis"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = l.convertToJson();
        FilesUtills.saveFile(file.toPath(), content);
        System.out.println(file);
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
        //"http://oceanos.nextgis.com/resource/1/display/tiny?base=osm-mapnik&amp;lon=29.9525&amp;lat=60.7220&amp;angle=0&amp;zoom=16&amp;styles=15%2C28%2C32%2C30%2C26%2C7%2C17%2C20%2C22%2C24%2C13%2C38&amp;linkMainMap=true"
        //"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        //"http://{s}.tiles.mapbox.com/v3/gvenech.m13knc8e/{z}/{x}/{y}.png"
        TileLayer tileLayer = new TileLayer("http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png");
        WMSTileLayer wmsTileLayer = new WMSTileLayer("http://oceanos.nextgis.com/api/resource/65/wms", new WmsLayerOptions());
        wmsTileLayer.setName("карта глубин");
        tileLayer.setName("osm map");
        mapView.onLoad(() -> {
            mapView.addLayer(tileLayer);
            mapView.addLayer(wmsTileLayer);
            Marker marker = new Marker(60.055142,30.3400618);
            marker.setIcon(FilesUtills.normalizePath(ResourceManager.getInstance().getIconsList().get(0)));
            Marker marker1 = new Marker(60.0555,30.34007);
            marker.setName("marker 1");

            /*mapView.addLayer(marker);
            mapView.addLayer(marker1);*/
        });

        mapView.initWebView();
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 40.0);

        initHandlers();

        lat.textProperty().bindBidirectional(mapView.currentLatProperty(), new NumberStringConverter());
        lng.textProperty().bindBidirectional(mapView.currentLngProperty(), new NumberStringConverter());

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
                if (layer instanceof Waypoint) return;
                TreeItem<Layer> treeItem = new TreeItem<>(layer);
                layerTreeView.getRoot().getChildren().add(treeItem);
                if (layer instanceof Mission) {
                    ((Mission)layer).getWaypoints().addListener((ListChangeListener<Waypoint>) c1 -> {
                        c1.next();
                        if (c1.wasAdded()){
                            treeItem.getChildren().add(new TreeItem<>(c1.getAddedSubList().get(0)));
                        }
                    });
                }
            } else if (c.wasRemoved()){
                System.out.println("remove");
                Layer layer = c.getRemoved().get(0);
                if (layer instanceof Waypoint) return;
                layerTreeView.getRoot().getChildren()
                        .stream()
                        .filter((i)->i.getValue().equals(layer))
                        .findFirst()
                        .ifPresent((layerTreeItem -> layerTreeView.getRoot().getChildren().remove(layerTreeItem)));
            }
        });

        layerTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mapView.setActivLayer(newValue.getValue());
        });


    }

    private void initHandlers(){
        mapView.activeLayerProperty().addListener((observable, oldValue, newValue) -> {
            //System.out.println(newValue);
            if (newValue instanceof Marker){
                mapView.flyTo(((Marker)newValue).getLat(), ((Marker)newValue).getLng());
            } else if (newValue instanceof Circle){
                mapView.flyTo(((Circle)newValue).getLat(), ((Circle)newValue).getLng());
            }
            fillOptionsPane(newValue);

            int index = mapView.getLayers().indexOf(newValue);

            layerTreeView.getSelectionModel().select(index);
            /*layerTreeView.getSelectionModel().select*/
        });
    }

    private void fillOptionsPane(Layer value) {
        if (value instanceof Marker){
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
        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.PolyLine") || value.getClass().getName().equals("com.oceanos.FXMapModule.layers.Polygon")){
            VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/polyLineOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PolylineOptionsController controller = loader.getController();
            controller.setLayer(value);
            layerOptionsPane.getChildren().clear();
            layerOptionsPane.getChildren().add(elem);
        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.Circle")){
            VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/circleOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            CircleOptionsController controller = loader.getController();
            controller.setLayer(value);
            layerOptionsPane.getChildren().clear();
            layerOptionsPane.getChildren().add(elem);
        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.mission.Mission")) {
            Accordion elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/missionOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MissionOptionsController controller = loader.getController();
            controller.setLayer(value);
            layerOptionsPane.getChildren().clear();
            layerOptionsPane.getChildren().add(elem);
        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.mission.Waypoint")) {
            VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waypointOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            WaypointController controller = loader.getController();
            controller.setLayer(value);
            layerOptionsPane.getChildren().clear();
            layerOptionsPane.getChildren().add(elem);
        }
    }

}
