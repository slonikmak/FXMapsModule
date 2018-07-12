package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class MarkerOptionsController implements LayerOptionsController {
    private Marker layer;

    @FXML
    private ImageView iconPreview;

    @FXML
    private AnchorPane preferencePane;

    @FXML
    private TextField latField;

    @FXML
    private TextField lngField;

    @FXML
    private TextField altField;

    @FXML
    private TextField nameField;

    @FXML
    private AnchorPane stylesPane;

    @FXML
    private TextField labelField;

    @FXML
    private ComboBox<String> iconBox;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (Marker) layer;
        try {
            setIconSelection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        fillOptions();
    }

    public void fillOptions() {
        lngField.textProperty().bindBidirectional(layer.lngProperty(), new MyStringConverter());
        latField.textProperty().bindBidirectional(layer.latProperty(), new MyStringConverter());
        nameField.textProperty().bindBidirectional(layer.nameProperty());

    }

    public void initialize(){
        initChoiceBox();
    }

    private void initChoiceBox(){

        iconBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(String item,
                                           boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            String localUrl = FilesUtills.normalizePath(item);
                            ImageView imageView = new ImageView();
                            setIconView(localUrl, imageView);
                            setGraphic(imageView);
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        iconBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object!=null){
                    return new File(object).getName();
                } else return "";

            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });
        iconBox.getItems().addAll(ResourceManager.getInstance().getIconsList());
    }

    private void setIconSelection() throws MalformedURLException {
        if (this.layer.getIcon()!=null){
            iconBox.getSelectionModel().select(FilesUtills.normalizePath(this.layer.getIcon().getIconSrc()));
                setIconView(FilesUtills.normalizePath(this.layer.getIcon().getIconSrc()), iconPreview);

        } else {
            iconBox.getSelectionModel().select(ResourceManager.getInstance().getDefaultIcon());
            setIconView(FilesUtills.normalizePath(ResourceManager.getInstance().getDefaultIcon()), iconPreview);
        }

        iconBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            layer.setIcon(FilesUtills.normalizePath(newValue));
            setIconView(FilesUtills.normalizePath(newValue), iconPreview);
        });
    }

    private void setIconView(String url, ImageView imageView){
        Image image = new Image(url);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        double size = 32.0 / image.getHeight();
        imageView.setFitWidth(image.getWidth() * size);
        imageView.setFitHeight(image.getHeight() * size);
    }

    private class MyStringConverter extends StringConverter<Number>{

        @Override
        public String toString(Number object) {
            return String.valueOf(object);
        }

        @Override
        public Double fromString(String string) {
            return Double.valueOf(string);
        }
    }
}
