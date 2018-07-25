package com.oceanos.FXMapModule.app.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

/**
 * @autor slonikmak on 25.07.2018.
 */
public class addCsvController {
    private Path sourceFile;

    private StringProperty featureType = new SimpleStringProperty();
    private StringProperty separatorType = new SimpleStringProperty();

    @FXML
    private TextField header;

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
        }
    }

    public void initialize(){
        separators.getChildren().forEach(n->{
            ((RadioButton)n).selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {



                    separators.getChildren().forEach(n1->{
                        if (!n1.equals(n)){
                            ((RadioButton)n).setSelected(false);
                        }
                    });
                }
            });
        });
    }
}
