package com.oceanos.FXMapModule.layers;

import javafx.scene.image.Image;

/**
 * @autor slonikmak on 20.06.2018.
 */
public class Icon {
    private String iconUrl;
    private int[] iconSize;

    public Icon(String src){
        iconUrl = src;
        Image image = new Image(src);
        iconSize = new int[]{(int) image.getWidth(), (int) image.getHeight()};
    }

    public String getIconSrc() {
        return iconUrl;
    }

    public int[] getIconSize() {
        return iconSize;
    }
}
