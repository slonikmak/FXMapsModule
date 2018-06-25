package com.oceanos.FXMapModule.app.view;

import com.oceanos.FXMapModule.layers.Layer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;

/**
 * @autor slonikmak on 25.06.2018.
 */
public class LayerTreeCell extends TreeCell<Layer> {
    @Override
    public void updateItem(Layer item, boolean empty) {
        super.updateItem(item, empty);
        HBox hBox = new HBox();
        Button deleteBtn = new Button("");
        Button toogleVisibleBtn = new Button("");
        toogleVisibleBtn.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EYE));
        toogleVisibleBtn.getStyleClass().add("tree-view-btn");
        FontAwesomeIconView icon1 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        deleteBtn.setGraphic(icon1);
        deleteBtn.getStyleClass().add("tree-view-btn");
        hBox.getChildren().addAll(deleteBtn, toogleVisibleBtn);
        if (empty) {
            setText("");
            setGraphic(null);
        } else {
            String name = item.getName();
            setText(name + " marker");
            hBox.getChildren().add(new MaterialIconView(MaterialIcon.LOCATION_ON));
            //com.oceanos.FXMapModule.layers.Marker
            if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.Marker")) {
                //(name + " marker");
                        /*deleteBtn.setOnAction(event -> {
                            jsBridge.deletePoint(item.getId(), repository.getLines()
                                    .filtered(trackLine -> trackLine.getPoints().stream()
                                            .anyMatch(waypoint -> waypoint.getId() == item.getId()))
                                    .get(0).getId());
                        });*/
                //com.oceanos.FXMapModule.layers.PolyLine
            } else if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.PolyLine")) {
                //setText(name + "polyline");
                        /*((TrackLine) item).nameProperty().addListener((observable, oldValue, newValue) -> {
                            treeViewProperty().getValue().refresh();
                        });*/
                ToggleButton delBtn = new ToggleButton("");
                FontAwesomeIconView icon2 = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                delBtn.setGraphic(icon2);
                hBox.getChildren().add(delBtn);
                        /*deleteBtn.setOnAction(event -> {
                            jsBridge.deleteLine(item.getId());
                        });*/
            } /*else if (item.getClass().getName().equals("models.Marker")) {
                        setText(name);
                        deleteBtn.setOnAction(event -> {
                            jsBridge.deletMarker(item.getId());
                        });
                    } else if (item.getClass().getName().equals("models.DoneTrack")) {
                        //System.out.println("name "+name);
                        setText(name);
                        if (((DoneTrack) item).getColor() != null) {
                            hBox.setStyle("-fx-background-color: " + ((DoneTrack) item).getColor());
                        }
                        deleteBtn.setOnAction(event -> {
                            if (item.getName().equals("Записанные треки")) return;
                            jsBridge.deleteLine(item.getId());
                        });
                    }*/
            setGraphic(hBox);
        }
    }
}
