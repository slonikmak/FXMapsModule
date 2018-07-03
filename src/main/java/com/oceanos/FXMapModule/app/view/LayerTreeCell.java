package com.oceanos.FXMapModule.app.view;

import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.layers.Layer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

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
            setText(name);
            MaterialIconView iconView = null;
            //com.oceanos.FXMapModule.layers.Marker
            if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.Marker")) {
                iconView = new MaterialIconView(MaterialIcon.LOCATION_ON);
                iconView.setFill(Color.BLUE);
                //com.oceanos.FXMapModule.layers.PolyLine
            } else if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.PolyLine")) {
               iconView = new MaterialIconView(MaterialIcon.GESTURE);

            } else if (item instanceof Circle){
               iconView = new MaterialIconView(MaterialIcon.ADJUST);

            } else if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.Polygon")){
                iconView = new MaterialIconView(MaterialIcon.CROP_5_4);
            } else if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.mission.Mission")){
                iconView = new MaterialIconView(MaterialIcon.TIMELINE);
            }
            if (iconView != null){
                iconView.getStyleClass().add("tree-icon-view");
                hBox.getChildren().add(iconView);
            }
            setGraphic(hBox);
        }
        deleteBtn.setOnAction(event -> {
            item.remove();
        });
    }
}
