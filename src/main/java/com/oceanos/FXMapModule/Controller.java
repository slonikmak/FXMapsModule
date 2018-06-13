package com.oceanos.FXMapModule;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    AnchorPane mainPane;

    public void  initialize(){
        MapView mapView = new MapView();
        mainPane.getChildren().add(mapView);
        AnchorPane.setBottomAnchor(mapView, 0d);
        AnchorPane.setTopAnchor(mapView, 0d);
        AnchorPane.setRightAnchor(mapView, 0d);
        AnchorPane.setLeftAnchor(mapView, 0d);
    }
}
