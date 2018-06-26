package com.oceanos.FXMapModule.app.controllers;

import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.layers.Layer;
import com.oceanos.FXMapModule.layers.Marker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class MarkerOptionsController implements LayerOptionsController {
    private Marker layer;

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
    }

    public void initialize(){
        initChoiceBox();
    }

    private void initChoiceBox(){
        try {
            iconBox.getItems().addAll(ResourceManager.getInstance().getIconsList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        iconBox.setCellFactory(new Callback<ListView<Path>, ListCell<Path>>() {
            @Override
            public ListCell<Path> call(ListView<Path> param) {
                final ListCell<Path> cell = new ListCell<Path>() {
                    {
                        //super.setPrefWidth(100);
                    }
                    @Override public void updateItem(Path item,
                                                     boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getFileName().toString());
                            try {
                                String localUrl = item.toFile().toURI().toURL().toString();
                                ImageView imageView = new ImageView(new Image(localUrl));
                                setGraphic(imageView);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

    }
}
