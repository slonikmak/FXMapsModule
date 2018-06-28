package com.oceanos.FXMapModule.app.view;

import com.oceanos.FXMapModule.layers.Circle;
import com.oceanos.FXMapModule.layers.Layer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
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
            //com.oceanos.FXMapModule.layers.Marker
            if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.Marker")) {
                MaterialIconView iconView = new MaterialIconView(MaterialIcon.LOCATION_ON);
                iconView.getStyleClass().add("tree-icon-view");
                iconView.setFill(Color.BLUE);
                hBox.getChildren().add(iconView);
                //com.oceanos.FXMapModule.layers.PolyLine
            } else if (item.getClass().getName().equals("com.oceanos.FXMapModule.layers.PolyLine")) {
                MaterialIconView iconView = new MaterialIconView(MaterialIcon.GESTURE);
                iconView.getStyleClass().add("tree-icon-view");
                hBox.getChildren().addAll(iconView);
            } else if (item instanceof Circle){
                MaterialIconView iconView = new MaterialIconView(MaterialIcon.ADJUST);
                iconView.getStyleClass().add("tree-icon-view");
                hBox.getChildren().addAll(iconView);
            }
            setGraphic(hBox);
        }
    }
}
