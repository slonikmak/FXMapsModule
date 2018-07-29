package com.oceanos.FXMapModule.app.controllers;

import com.google.gson.JsonObject;
import com.oceanos.FXMapModule.MapView;
import com.oceanos.FXMapModule.layers.GeoJsonLayer;
import com.oceanos.FXMapModule.utils.GeoJsonUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @autor slonikmak on 25.07.2018.
 */
public class AddCsvController {
    private MapView mapView;

    private Path sourceFile;
    private List<String> lines;
    private List<String[]> data;

    int latIndex, lngIndex;

    private StringProperty featureType = new SimpleStringProperty();
    private StringProperty separatorType = new SimpleStringProperty();

    @FXML
    private TextField header;

    @FXML
    private TextField name;


    @FXML
    private Label records;

    @FXML
    private ColorPicker color;

    @FXML
    private RadioButton line;

    @FXML
    private RadioButton dots;

    @FXML
    private HBox separators;

    @FXML
    private RadioButton dotSeparator;

    @FXML
    private RadioButton colomnSeparator;

    @FXML
    private RadioButton spaceSeparator;

    @FXML
    private RadioButton tabSeparator;

    @FXML
    void add(ActionEvent event) {
        if (lines != null && lines.size()>1){
            prepareCoords();
            if (line.isSelected()){
                loadAsLine();
            } else if (dots.isSelected()){
                loadDots();
            }
        }
    }

    private void prepareCoords() {

        separators.getChildren().forEach(c->{
            if (((RadioButton)c).isSelected()){
                switch (((RadioButton) c).getText()) {
                    case ",":
                        separatorType.setValue(",");
                        break;
                    case ";":
                        separatorType.setValue(";");
                        break;
                    case "пробел":
                        separatorType.setValue(" ");
                        break;
                    case "табуляция":
                        separatorType.setValue("\\t");
                }
            }
        });

        data = lines.stream().skip(1).map(l->l.split(separatorType.get())).collect(Collectors.toList());
        String[] headers = header.getText().split(",");
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].toLowerCase().equals("latitude")) {
                latIndex = i;
            }
            if (headers[i].toLowerCase().equals("longitude")){
                lngIndex = i;
            }
        }
    }

    private void loadDots() {
        GeoJsonUtils.GeoJsonBuilder builder = new GeoJsonUtils.GeoJsonBuilder();

        data.forEach(d->{
            Double lat = null;
            Double lng = null;
            try {
                lat = Double.parseDouble(d[latIndex]);
                lng = Double.parseDouble(d[lngIndex]);
                builder.addPoint(lat, lng);
            } catch (NumberFormatException e){
                //e.printStackTrace();
            }


        });
        System.out.println(builder.build());
        GeoJsonLayer layer = new GeoJsonLayer(builder.build());
        if (!name.getText().equals("")) layer.setName(name.getText());
        mapView.addLayer(layer);
    }

    private void loadAsLine() {

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите CSV файл");
        File file = fileChooser.showOpenDialog(color.getParent().getScene().getWindow());
        if (file != null){
            sourceFile = file.toPath();
            try {
                lines = Files.lines(file.toPath()).collect(Collectors.toList());
                records.setText(String.valueOf(lines.size()-1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(){
        separators.getChildren().forEach(n->{
            ((RadioButton)n).selectedProperty().addListener((observable, oldValue, newValue) -> {
               /* if (newValue) {
                    separators.getChildren().forEach(n1->{
                        if (!n1.equals(n)){
                            ((RadioButton)n).setSelected(false);
                        }
                    });
                }*/

            });

        });
    }

    public void setMapView(MapView mapView){
        this.mapView = mapView;
    }
}
