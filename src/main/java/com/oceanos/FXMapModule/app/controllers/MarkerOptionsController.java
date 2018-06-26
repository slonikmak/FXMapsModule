package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.properties.ResourceManager;
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
    private ComboBox<Path> iconBox;

    @Override
    public void setLayer(Layer layer) {
        this.layer = (Marker) layer;
        try {
            setIconSelection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void initialize(){
        initChoiceBox();
    }

    private void initChoiceBox(){

        iconBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Path> call(ListView<Path> param) {
                final ListCell<Path> cell = new ListCell<Path>() {
                    {
                        //super.setPrefWidth(100);
                    }

                    @Override
                    public void updateItem(Path item,
                                           boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getFileName().toString());
                            try {
                                String localUrl = item.toFile().toURI().toURL().toString();
                                ImageView imageView = new ImageView();
                                setIconView(localUrl, imageView);
                                setGraphic(imageView);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        iconBox.setConverter(new StringConverter<Path>() {
            @Override
            public String toString(Path object) {
                if (object!=null){
                    return object.getFileName().toString();
                } else return "";

            }

            @Override
            public Path fromString(String string) {
                return null;
            }
        });
        try {
            iconBox.getItems().addAll(ResourceManager.getInstance().getIconsList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIconSelection() throws MalformedURLException {
        if (this.layer.getIcon()!=null){
            iconBox.getSelectionModel().select(new File(this.layer.getIcon().getIconSrc()).toPath());
                setIconView(new File(this.layer.getIcon().getIconSrc()).toURI().toURL().toString(), iconPreview);

        } else {
            iconBox.getSelectionModel().select(Paths.get(ResourceManager.getInstance().getDefaultIcon()));
            setIconView(new File(ResourceManager.getInstance().getDefaultIcon()).toURI().toURL().toString(), iconPreview);
        }

        iconBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                layer.setIcon(newValue.toUri().toURL().toString());
                setIconView(newValue.toUri().toURL().toString(), iconPreview);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
}
