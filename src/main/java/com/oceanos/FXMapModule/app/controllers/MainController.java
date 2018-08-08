package com.oceanos.FXMapModule.app.controllers;

import com.fazecast.jSerialComm.SerialPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.app.utills.cache.TileCache;
import com.oceanos.FXMapModule.app.view.LayerTreeCell;
import com.oceanos.FXMapModule.layers.*;
import com.oceanos.FXMapModule.layers.mission.Mission;
import com.oceanos.FXMapModule.layers.mission.Waypoint;
import com.oceanos.FXMapModule.mapControllers.EditableController;
import com.oceanos.FXMapModule.options.CircleOptions;
import com.oceanos.FXMapModule.options.PathOptions;
import com.oceanos.FXMapModule.options.WmsLayerOptions;
import com.oceanos.FXMapModule.utils.GpsReader;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {
    private MapView mapView;
    private ContextMenu layerContextMenu;
    private ContextMenu missionContextMenu;
    private ContextMenu geoGsonLayerContextMenu;

    @FXML
    private AnchorPane layerOptionsPane;

    @FXML
    private TreeView<Layer> layerTreeView;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private AnchorPane mapContainer;

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
            Scene scene = new Scene(parent,800, 500);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadCsv(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCSV.fxml"));
        try {
            Parent parent = loader.load();
            AddCsvController controller = loader.getController();
            controller.setMapView(mapView);
            Scene scene = new Scene(parent,780, 270);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTrackingLayer(){
        AnchorPane root = new AnchorPane();
        CurrentPositionLayer positionLayer = new CurrentPositionLayer();
        positionLayer.setName("GPS позиция");
        //positionLayer.setIcon(FilesUtills.normalizePath("/icons/icons8-submarine-48.png"));
        Stage stage = new Stage();
        HBox hBox = new HBox();
        hBox.getStyleClass().add("gps-box");
        hBox.getStylesheets().addAll(getClass().getResource("/fxml-css/main.css").toExternalForm(), getClass().getResource("/fxml-css/jMetro.css").toExternalForm());
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        AnchorPane.setTopAnchor(hBox, 0.);
        AnchorPane.setRightAnchor(hBox, 0.);
        AnchorPane.setBottomAnchor(hBox, 0.);
        AnchorPane.setLeftAnchor(hBox, 0.);
        ChoiceBox<String> choicePort = new ChoiceBox<>();
        SerialPort[] ports = positionLayer.getPortNames();
        choicePort.getItems().addAll(Stream.of(ports).map(SerialPort::getDescriptivePortName).collect(Collectors.toList()));
        choicePort.getSelectionModel().select(0);
        ChoiceBox<Integer> choiceBoudrate = new ChoiceBox<>();
        choiceBoudrate.getItems().addAll(9600, 115200);
        choiceBoudrate.getSelectionModel().select(0);
        Button okBtn = new Button("Добавить");
        Button cancelBtn = new Button("Отмена");
        okBtn.setPrefWidth(100);
        cancelBtn.setPrefWidth(100);

        root.getChildren().add(hBox);

        hBox.getChildren().addAll(choicePort, choiceBoudrate, okBtn, cancelBtn);
        Scene scene = new Scene(root, 500, 50);
        stage.setScene(scene);

        cancelBtn.setOnAction(e->{
            stage.close();
        });
        okBtn.setOnAction(e->{
            if (choicePort.getSelectionModel().getSelectedIndex() >= 0){
                positionLayer.setBaudrate(choiceBoudrate.getValue());
                SerialPort port = ports[choicePort.getSelectionModel().getSelectedIndex()];
                positionLayer.setPort(port);
                positionLayer.startPort();
                mapView.addLayer(positionLayer);
                mapPane.getScene().getWindow().setOnCloseRequest(e1->{
                    positionLayer.closeReader();
                });
            }
            stage.close();
        });
        stage.showAndWait();



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
        Marker marker = Marker.getFromJson(content);
        mapView.addLayer(marker);
    }

    private void loadMapLayer(String content) {

    }

    private void loadMission(String content) {
        PathOptions options = new PathOptions();
        options.fillOptions(ResourceManager.getInstance().getDefaultMissionOptions());
        CircleOptions waypointOptions = new CircleOptions();
        waypointOptions.fillOptions(ResourceManager.getInstance().getDefaultWaypointOptions());
        Mission mission = Mission.getFromJson(content, mapView, options, waypointOptions);
        mapView.addLayer(mission);
    }

    private void saveMission(Mission l) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения миссии");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл миссии", "*.mis"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = l.convertToMissionJson();
        FilesUtills.saveFile(file.toPath(), content, false);
    }

    private void saveMissionAsGeoJson(Mission mission){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения миссии в формате GeoJson");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файл JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = mission.convertToJson();
        FilesUtills.saveFile(file.toPath(), content, false);
    }

    public void addMarkerByClick() {
        EditableController.startMarker();
    }

    public void addMarkerByCoords() {

    }


    public void initialize() {
        mapView = new MapView();
        //FIXME: HARDCODE!!
        ResourceManager.mapView = mapView;
        ResourceManager.getInstance();
        //
        System.out.println("end init resources");

        mapContainer.getChildren().add(mapView);
        initTreeView();
        initContextMenu();

        //"http://oceanos.nextgis.com/resource/1/display/tiny?base=osm-mapnik&amp;lon=29.9525&amp;lat=60.7220&amp;angle=0&amp;zoom=16&amp;styles=15%2C28%2C32%2C30%2C26%2C7%2C17%2C20%2C22%2C24%2C13%2C38&amp;linkMainMap=true"
        //"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        //"http://{s}.tiles.mapbox.com/v3/gvenech.m13knc8e/{z}/{x}/{y}.png"
        TileLayer tileLayer = new TileLayer("http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png");
        // http://oceanos.nextgis.com/api/resource/68/wms
        //http://oceanos.nextgis.com/api/resource/65/wms
        WMSTileLayer wmsTileLayer = new WMSTileLayer("http://oceanos.nextgis.com/api/resource/65/wms", new WmsLayerOptions());
        wmsTileLayer.setName("карта глубин");
        tileLayer.setName("osm map");

        //TileCache cache = new TileCache(tileLayer);

        mapView.onLoad(() -> {
            System.out.println("add layers");
            //Добавляем слои из последнего проекта
            for (Layer layer:
                    ResourceManager.getInstance().getFilesInProject().keySet()) {
                if (layer.getClass().getName().equals("com.oceanos.FXMapModule.layers.TileLayer")){
                    TileCache cache = new TileCache((TileLayer) layer);
                }
                mapView.addLayer(layer);
            }
           /* ResourceManager.getInstance().getFilesInProject().forEach((k,v)->{
                if (k.getClass().getName().equals("com.oceanos.FXMapModule.layers.TileLayer")){
                    TileCache cache = new TileCache((TileLayer) k);
                    mapView.addLayer(k);
                } else {
                    mapView.addLayer(k);
                }
            });*/
           //Подписываемся на добавление/удаление слоя для сохранения проекта
            mapView.getLayers().addListener((ListChangeListener<Layer>) c -> {
                       c.next();
                       if (c.wasAdded() && !(c.getAddedSubList().get(0) instanceof Waypoint)){
                           ResourceManager.getInstance().addLayerToProject(c.getAddedSubList().get(0));
                       } else if (c.wasRemoved() && !(c.getRemoved().get(0) instanceof Waypoint)){
                           ResourceManager.getInstance().removeLayerFromProject(c.getRemoved().get(0));
                       }
            });

            /*mapView.addLayer(tileLayer);
            mapView.addLayer(wmsTileLayer);*/
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
        AnchorPane.setLeftAnchor(mapView, 0.0);

        initHandlers();

        lat.textProperty().bindBidirectional(mapView.currentLatProperty(), new NumberStringConverter());
        lng.textProperty().bindBidirectional(mapView.currentLngProperty(), new NumberStringConverter());

    }

    private void initContextMenu() {
        layerContextMenu = new ContextMenu();
        missionContextMenu = new ContextMenu();
        geoGsonLayerContextMenu = new ContextMenu();

        MenuItem goToItem = new MenuItem("Переместиться к слою");
        MenuItem deleteItem = new MenuItem("Удалить");
        MenuItem hideItem = new MenuItem("Скрыть/показать");
        MenuItem saveItem = new MenuItem("Сохранить как GeoJson");
        MenuItem saveAsMission = new MenuItem("Сохранить Миссию");
        MenuItem convertToLayer = new MenuItem("Преобразовать в слой");

        convertToLayer.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            if (layer instanceof GeoJsonLayer){
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(((GeoJsonLayer)layer).getString()).getAsJsonObject();
                if (object.get("type").getAsString().toLowerCase().equals("feature")){
                    System.out.println(object.get("geometry").getAsJsonObject().get("type").getAsString());
                    if (object.get("geometry").getAsJsonObject().get("type").getAsString().equals("LineString")){
                        PolyLine polyLine = PolyLine.getFromJson(((GeoJsonLayer)layer).getString());
                        mapView.addLayer(polyLine);
                    } else if (object.get("geometry").getAsJsonObject().get("type").getAsString().equals("Polygon")){
                        Polygon polygon = Polygon.getFromJson(((GeoJsonLayer)layer).getString());
                        mapView.addLayer(polygon);
                    }
                }

                //System.out.println("ok!");

            }
        });

        saveItem.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            saveAsGeoJson(layer);
        });

        goToItem.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            if (layer instanceof Marker){
                Marker marker = (Marker) layer;
                mapView.flyTo(marker.getLat(), marker.getLng());
            } else if (layer instanceof PolyLine){
                mapView.flyTo(((PolyLine)layer).getLatLngs().get(0).getLat(), ((PolyLine)layer).getLatLngs().get(0).getLng());
            }
        });

        saveAsMission.setOnAction(event -> {
            Layer layer = layerTreeView.getSelectionModel().getSelectedItem().getValue();
            if (layer instanceof Mission){
                Mission mission = (Mission) layer;
                saveMission(mission);
            }
        });

        layerContextMenu.getItems().addAll(goToItem, deleteItem, saveItem);
        missionContextMenu.getItems().addAll(goToItem, deleteItem, saveItem, saveAsMission);
        geoGsonLayerContextMenu.getItems().addAll(deleteItem, convertToLayer);
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
            layerContextMenu.hide();
            geoGsonLayerContextMenu.hide();
            missionContextMenu.hide();
            Layer layer = null;
            Object o = ((TreeCell<Layer>)((Node)event.getTarget()).getParent()).getTreeItem().getValue();
            try {
              layer = (Layer) o;
            } catch (ClassCastException ex){

            }
            if (layer != null){
                if (layer instanceof Mission){
                    missionContextMenu.show(layerTreeView, event.getScreenX(), event.getScreenY());
                } else if (layer instanceof GeoJsonLayer){
                    geoGsonLayerContextMenu.show(layerTreeView, event.getScreenX(), event.getScreenY());
                } else {
                    layerContextMenu.show(layerTreeView, event.getScreenX(), event.getScreenY());
                }
            }

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
        Node elem = null;

        if (value instanceof Marker){
            //VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/markerOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MarkerOptionsController controller = loader.getController();
            controller.setLayer(value);

        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.PolyLine") || value.getClass().getName().equals("com.oceanos.FXMapModule.layers.Polygon")){
            //VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/polyLineOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PolylineOptionsController controller = loader.getController();
            controller.setLayer(value);

        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.Circle")){
            //VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/circleOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            CircleOptionsController controller = loader.getController();
            controller.setLayer(value);

        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.mission.Mission")) {
            //Accordion elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/missionOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MissionOptionsController controller = loader.getController();
            controller.setLayer(value);

        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.mission.Waypoint")) {
            //Accordion elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waypointOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            WaypointController controller = loader.getController();
            controller.setLayer(value);

        } else if (value.getClass().getName().equals("com.oceanos.FXMapModule.layers.TileLayer")) {
            //VBox elem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tileLayerOptions.fxml"));
            try {
                elem = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            TileLayerOptionsController controller = loader.getController();
            controller.setLayer(value);

        } else {
            layerOptionsPane.getChildren().clear();
        }

        layerOptionsPane.getChildren().clear();
        if (elem!=null){
            AnchorPane.setLeftAnchor(elem,0.);
            AnchorPane.setBottomAnchor(elem,0.);
            AnchorPane.setRightAnchor(elem,0.);
            AnchorPane.setTopAnchor(elem,0.);
            layerOptionsPane.getChildren().add(elem);
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
        FilesUtills.saveFile(file.toPath(), content, false);
        System.out.println(file);
    }

    private void savePolyline(PolyLine layer) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения слоя");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Geo JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        String content = layer.convertToJson();
        System.out.println(content);
        FilesUtills.saveFile(file.toPath(), content, false);
        System.out.println(file);
    }

    private void saveMarker(Marker marker) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл для сохранения слоя");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Geo JSON", "*.json"));
        File file = chooser.showSaveDialog(layerTreeView.getScene().getWindow());
        if (file!=null){
            String content = marker.convertToJson();
            FilesUtills.saveFile(file.toPath(), content, false);
        }
    }



}
