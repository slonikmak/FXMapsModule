package com.oceanos.FXMapModule.app.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.layers.TileLayer;
import com.oceanos.FXMapModule.layers.WMSTileLayer;
import com.oceanos.FXMapModule.options.WmsLayerOptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @autor slonikmak on 18.07.2018.
 */
public class ResourceManagerController {

    //TODO: переделать всё на объекты Layers

    MapView mapView;

    ObservableList<String> icons;
    ObservableList<WMSTileLayer> wmsLayers;
    ObservableList<TileLayer> tileLayers;

    JsonObject currentWmsJsonObj;
    JsonObject currentTileJsonObj;


    @FXML
    private ListView<TileLayer> mapList;

    @FXML
    private TextField mapNameField;

    @FXML
    private TextField mapUrlField;

    @FXML
    private ListView<WMSTileLayer> wmsList;

    @FXML
    private TextField wmsNameField;

    @FXML
    private TextField wmsUrlField;

    @FXML
    private TextField wmsLayersField;

    @FXML
    private ChoiceBox<Boolean> wmsTransparancy;

    @FXML
    private ChoiceBox<String> wmsFormat;

    @FXML
    private ListView<String> iconsList;

    @FXML
    void addWmsOnMap() {

    }

    @FXML
    void addMapOnMap() {
        mapView.addLayer(mapList.getSelectionModel().getSelectedItem());
    }

    @FXML
    void addIcon(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите иконку");
        List<File> file = fileChooser.showOpenMultipleDialog(iconsList.getScene().getWindow());
        if (file != null) {
            try {
                List<Path> result = FilesUtills.copyFiles(file.stream().map(f -> f.toString()).collect(Collectors.toList()), ResourceManager.getInstance().getIconsFolder());
                icons.addAll(result.stream().map(Path::toString).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void addMap(ActionEvent event) {
        TileLayer tileLayer = new TileLayer("empty");
        tileLayer.setName("новый слой");
        tileLayers.add(tileLayer);
    }

    @FXML
    void addWms(ActionEvent event) {
        WMSTileLayer layer = new WMSTileLayer("empty", new WmsLayerOptions());
        layer.setName("новый слой");
        wmsLayers.add(layer);
    }

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void deleteIcon(ActionEvent event) {

    }

    @FXML
    void deleteMap(ActionEvent event) {

    }

    @FXML
    void deleteWms(ActionEvent event) {

    }

    @FXML
    void saveWms() {
        JsonArray array = new JsonArray();
        wmsLayers.stream().map(WMSTileLayer::convertToRawJsonObject).forEach(array::add);
        FilesUtills.saveFile(ResourceManager.getInstance().getLayersWmsFile(), array.toString());
    }

    @FXML
    void saveTile() {
        JsonArray array = new JsonArray();
        tileLayers.stream().map(TileLayer::convertToRawJsonObject).forEach(array::add);
        FilesUtills.saveFile(ResourceManager.getInstance().getLayersTileFile(), array.toString());
    }

    public void initialize() {
        initWms();
        initIcons();
        initMaps();
    }


    private void initIcons() {
        icons = FXCollections.observableArrayList(ResourceManager.getInstance().getIconsList());
        iconsList.setItems(icons);
        iconsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            System.out.println(item);
                            //fixme: сделать нормальный путь
                            Image image = new Image(FilesUtills.normalizePath(item));
                            ImageView imageView = new ImageView(image);
                            setText(Paths.get(item).getFileName().toString());
                            setGraphic(imageView);

                        }
                    }
                };
            }
        });

    }


    private void initMaps() {
        tileLayers = FXCollections.observableArrayList(ResourceManager.getInstance().getTileLayers());

        mapList.setItems(tileLayers);
        mapList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TileLayer> call(ListView<TileLayer> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(TileLayer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            setText(item.getName());
                            item.nameProperty().addListener((observable, oldValue, newValue) -> {
                                setText(newValue);
                            });
                        } else setText("");
                    }
                };
            }
        });
        mapList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mapNameField.textProperty().bindBidirectional(newValue.nameProperty());
            mapUrlField.textProperty().bindBidirectional(newValue.urlProperty());
        });
    }

    public void initWms() {
        wmsTransparancy.getItems().addAll(true, false);
        wmsTransparancy.getSelectionModel().select(0);
        wmsFormat.getItems().addAll("image/png");
        wmsFormat.getSelectionModel().select(0);

        wmsLayers = FXCollections.observableArrayList(ResourceManager.getInstance().getWmsLayers());

        wmsList.setItems(wmsLayers);
        wmsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<WMSTileLayer> call(ListView<WMSTileLayer> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(WMSTileLayer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            System.out.println(item);
                            setText(item.getName());
                            item.nameProperty().addListener((observable, oldValue, newValue) -> {
                                setText(newValue);
                            });
                        }
                    }
                };
            }
        });

        wmsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            WMSTileLayer layer = newValue;
            WmsLayerOptions options = (WmsLayerOptions) layer.getOptions();
            wmsFormat.valueProperty().bindBidirectional(options.formatProperty());
            wmsLayersField.textProperty().bindBidirectional(options.layersProperty());
            wmsUrlField.textProperty().bindBidirectional(layer.urlProperty());
            wmsNameField.textProperty().bindBidirectional(layer.nameProperty());
            wmsTransparancy.valueProperty().bindBidirectional(options.transparentProperty());
        });
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
}
