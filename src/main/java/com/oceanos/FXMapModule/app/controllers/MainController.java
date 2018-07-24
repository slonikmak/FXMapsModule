package com.oceanos.FXMapModule.app.controllers;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.app.view.LayerTreeCell;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.layers.mission.Mission;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import com.oceanos.FXMapModule.options.WmsLayerOptions;
import com.oceanos.FXMapModule.utils.GpsReader;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;


import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {
    private MapView mapView;
    private ContextMenu layerContextMenu;

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

    @FXML
    void openResourceManager(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resourceManager.fxml"));
        try {
            Parent parent = loader.load();
            ResourceManagerController controller = loader.getController();
            controller.setMapView(mapView);
            Scene scene = new Scene(parent,600, 400);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTrackingLayer(){
        CurrentPositionLayer positionLayer = new CurrentPositionLayer();
        positionLayer.setName("GPS позиция");
        //positionLayer.setIcon(FilesUtills.normalizePath("/icons/icons8-submarine-48.png"));
        Stage stage = new Stage();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        ChoiceBox<String> choicePort = new ChoiceBox<>();
        SerialPort[] ports = positionLayer.getPortNames();
        choicePort.getItems().addAll(Stream.of(ports).map(SerialPort::getDescriptivePortName).collect(Collectors.toList()));
        choicePort.getSelectionModel().select(0);
        ChoiceBox<Integer> choiceBoudrate = new ChoiceBox<>();
        choiceBoudrate.getItems().addAll(9600, 115200);
        choiceBoudrate.getSelectionModel().select(0);
        Button okBtn = new Button("Добавить");
        Button cancelBtn = new Button("Отмена");
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(okBtn, cancelBtn);
        hBox.getChildren().addAll(choicePort, choiceBoudrate, buttonBar);
        Scene scene = new Scene(hBox, 400, 50);
        stage.setScene(scene);
        cancelBtn.setOnAction(e->{
            stage.close();
        });
        okBtn.setOnAction(e->{
            positionLayer.setBaudrate(choiceBoudrate.getValue());
            SerialPort port = ports[choicePort.getSelectionModel().getSelectedIndex()];
            positionLayer.setPort(port);
            positionLayer.startPort();
            mapView.addLayer(positionLayer);
            stage.close();
        });
        stage.showAndWait();

        mapPane.getScene().getWindow().setOnCloseRequest(e->{
            positionLayer.closeReader();
        });

       /* GpsReader reader = new GpsReader();
        reader.setBaudrate(9600);
        reader.setPort("COM3");
        reader.startPort();*/

    }

    @FXML
    void loadLayer(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл для загрузки");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы миссии", "*.mis"), new FileChooser.ExtensionFilter("Json","*.json"));
        File file = fileChooser.showOpenDialog(layerTreeView.getScene().getWindow());
        String content = FilesUtills.openFile(file.toPath());
        if (file.getName().endsWith("mis")) {
            loadMission(content);
        } else if (file.getName().endsWith(".json")) {
            loadMapLayer(content);
        }
    }

    @FXML
    void loadMissionLayer(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл миссии");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы миссии", "*.mis"));
        File file = fileChooser.showOpenDialog(layerTreeView.getScene().getWindow());
        String content = FilesUtills.openFile(file.toPath());
        loadMission(content);
    }

    @FXML
    void loadMarkerLayer(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл маркера");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы маркера", "*.json"));
        File file = fileChooser.showOpenDialog(layerTreeView.getScene().getWindow());
        try {
            String content = FilesUtills.openFile(file.toPath());
            loadMarker(content);
        } catch (NullPointerException e){
            //FIXME: Нормально отлавливать исключения
            System.out.println("Нет файла");
        }
    }

    @FXML
    void loadLineLayer(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл линии");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы линии", "*.json"), new FileChooser.ExtensionFilter("GeoJson","*.geojson"));
        File file = fileChooser.showOpenDialog(layerTreeView.getScene().getWindow());
        String content = FilesUtills.openFile(file.toPath());
        loadLine(content);
    }

    private void loadLine(String content) {
        GeoJsonLayer layer = new GeoJsonLayer(content);
        mapView.addLayer(layer);
        //GeoJsonController.addLayerFromJson(content);
    }

    private void loadMarker(String content) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(content).getAsJsonObject();
        JsonObject geometry = jsonObject.getAsJsonObject("geometry");
        JsonArray coords = geometry.getAsJsonArray("coordinates");
        Double lng = coords.get(0).getAsDouble();
        Double lat = coords.get(1).getAsDouble();
        JsonObject properties = jsonObject.getAsJsonObject("properties");
        String name = properties.get("name").getAsString();
        Marker marker = new Marker(lat, lng);
        marker.setName(name);
        mapView.addLayer(marker);
    }

    private void loadMapLayer(String content) {

    }

    private void loadMission(String content) {
        Mission mission = Mission.getFromJson(content, mapView);
        mapView.addLayer(mission);
    }

    private void saveMission(Mission l) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения миссии");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл миссии", "*.mis"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = l.convertToMissionJson();
        FilesUtills.saveFile(file.toPath(), content);
    }

    private void saveMissionAsGeoJson(Mission mission){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения миссии в формате GeoJson");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = mission.convertToJson();
        FilesUtills.saveFile(file.toPath(), content);
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
        initContextMenu();
        //"http://oceanos.nextgis.com/resource/1/display/tiny?base=osm-mapnik&amp;lon=29.9525&amp;lat=60.7220&amp;angle=0&amp;zoom=16&amp;styles=15%2C28%2C32%2C30%2C26%2C7%2C17%2C20%2C22%2C24%2C13%2C38&amp;linkMainMap=true"
        //"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        //"http://{s}.tiles.mapbox.com/v3/gvenech.m13knc8e/{z}/{x}/{y}.png"
        TileLayer tileLayer = new TileLayer("http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png");
        WMSTileLayer wmsTileLayer = new WMSTileLayer("http://oceanos.nextgis.com/api/resource/65/wms", new WmsLayerOptions());
        wmsTileLayer.setName("карта глубин");
        tileLayer.setName("osm map");
        mapView.onLoad(() -> {
            System.out.println("add layers");
            mapView.addLayer(tileLayer);
            mapView.addLayer(wmsTileLayer);
            Marker marker = new Marker(60.055142,30.3400618);
            marker.setIcon(FilesUtills.normalizePath(ResourceManager.getInstance().getIconsList().get(0)));
            Marker marker1 = new Marker(60.0555,30.34007);
            marker.setName("marker 1");

           // mapView.addLayer(marker);
            //mapView.addLayer(marker1);
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

    private void initContextMenu() {
        layerContextMenu = new ContextMenu();
        MenuItem goToItem = new MenuItem("Переместиться к слою");
        MenuItem deleteItem = new MenuItem("Удалить");
        MenuItem hideItem = new MenuItem("Скрыть/показать");
        MenuItem saveItem = new MenuItem("Сохранить как GeoJson");
        MenuItem saveAsLayer = new MenuItem("Сохранить слой");

        saveItem.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            saveAsGeoJson(layer);
        });

        goToItem.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            if (layer instanceof Marker){
                Marker marker = (Marker) layer;
                mapView.flyTo(marker.getLat(), marker.getLng());
            }
        });

        saveAsLayer.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            if (layer instanceof Mission){
                Mission mission = (Mission) layer;
                saveMission(mission);
            }
        });

        layerContextMenu.getItems().addAll(goToItem, deleteItem, hideItem, saveItem, saveAsLayer);
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
                    ((Mission) layer).getWaypoints().forEach(w->{
                        treeItem.getChildren().add(new TreeItem<>(w));
                    });
                    ((Mission)layer).getWaypoints().addListener((ListChangeListener<Waypoint>) c1 -> {
                        c1.next();
                        if (c1.wasAdded()){
                            System.out.println("add waypoint item");
                            treeItem.getChildren().add(new TreeItem<>(c1.getAddedSubList().get(0)));
                            treeItem.getChildren().sort(Comparator.comparing(i->((Waypoint)i.getValue()).getIndex()));
                        }
                    });
                }
            } else if (c.wasRemoved()){
                System.out.println("remove");
                Layer layer = c.getRemoved().get(0);
                if (layer instanceof Waypoint) {
                    layerTreeView.getRoot().getChildren()
                            .stream()
                            .filter((i)->i.getValue().equals(((Waypoint)layer).getMission()))
                            .findFirst()
                            .ifPresent((missionTreeItem -> missionTreeItem.getChildren()
                                    .stream()
                                    .filter(layerTreeItem1 -> layerTreeItem1.getValue().equals(layer)).findFirst()
                                    .ifPresent(waypointItem->missionTreeItem.getChildren().remove(waypointItem))));
                } else {
                    layerTreeView.getRoot().getChildren()
                            .stream()
                            .filter((i)->i.getValue().equals(layer))
                            .findFirst()
                            .ifPresent((layerTreeItem -> layerTreeView.getRoot().getChildren().remove(layerTreeItem)));
                }

            }
        });

        layerTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.equals(oldValue)) mapView.setActivLayer(newValue.getValue());
        });

        layerTreeView.setOnContextMenuRequested(event -> {
            //event.getTarget()
            layerContextMenu.hide();
            layerContextMenu.show(layerTreeView, event.getScreenX(), event.getScreenY());

        });


    }

    private void initHandlers(){
        mapView.activeLayerProperty().addListener((observable, oldValue, newValue) -> {
/*
            if (newValue instanceof Marker){
                mapView.flyTo(((Marker)newValue).getLat(), ((Marker)newValue).getLng());
            } else if (newValue instanceof Circle){
                mapView.flyTo(((Circle)newValue).getLat(), ((Circle)newValue).getLng());
            }*/
            fillOptionsPane(newValue);

            int index = mapView.getLayers().indexOf(newValue);

           // layerTreeView.getSelectionModel().select(index);
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
        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.TileLayer")) {
            VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tileLayerOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            TileLayerOptionsController controller = loader.getController();
            controller.setLayer(value);
            layerOptionsPane.getChildren().clear();
            layerOptionsPane.getChildren().add(elem);
        } else {
            layerOptionsPane.getChildren().clear();
        }
    }

    private void saveAsGeoJson(Layer layer){
        if (layer instanceof Marker){
            Marker marker = (Marker) layer;
            saveMarker(marker);
        } else if (layer instanceof Mission){
            saveMissionAsGeoJson((Mission) layer);
        } else if (layer instanceof PolyLine){
            savePolyline((PolyLine)layer);
        } else if ( layer instanceof Polygon){
            savePolygon((Polygon)layer);
        }
    }

    private void savePolygon(Polygon layer) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения слоя");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Geo JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = layer.convertToJson();
        System.out.println(content);
        FilesUtills.saveFile(file.toPath(), content);
        System.out.println(file);
    }

    private void savePolyline(PolyLine layer) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения слоя");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Geo JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = layer.convertToJson();
        System.out.println(content);
        FilesUtills.saveFile(file.toPath(), content);
        System.out.println(file);
    }

    private void saveMarker(Marker marker) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения слоя");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Geo JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        if (file!=null){
            String content = marker.convertToJson();
            FilesUtills.saveFile(file.toPath(), content);
        }
    }



}
