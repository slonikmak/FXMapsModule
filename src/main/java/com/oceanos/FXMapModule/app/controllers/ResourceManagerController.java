package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.awt.*;
import java.nio.file.Paths;

/**
 * @autor slonikmak on 18.07.2018.
 */
public class ResourceManagerController {
    @FXML
    private ListView<?> mapList;

    @FXML
    private TextField mapNameField;

    @FXML
    private TextField mapUrlField;

    @FXML
    private ListView<?> wmsList;

    @FXML
    private TextField wmsNameField;

    @FXML
    private TextField wmsUrlField;

    @FXML
    private TextField wmsLayers;

    @FXML
    private ChoiceBox<Boolean> wmsTransparancy;

    @FXML
    private ChoiceBox<String> wmsFormat;

    @FXML
    private ListView<String> iconsList;

    @FXML
    void addIcon(ActionEvent event) {

    }

    @FXML
    void addMap(ActionEvent event) {

    }

    @FXML
    void addWms(ActionEvent event) {

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

    public void initialize(){
       initWms();
       initIcons();
       initMaps();
    }

    private void initMaps() {

    }

    private void initIcons() {
        ObservableList<String> icons = FXCollections.observableArrayList(ResourceManager.getInstance().getIconsList());
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

    public void initWms(){
        wmsTransparancy.getItems().addAll(true, false);
        wmsTransparancy.getSelectionModel().select(0);
        wmsFormat.getItems().addAll("image/png");
        wmsFormat.getSelectionModel().select(0);
    }
}
