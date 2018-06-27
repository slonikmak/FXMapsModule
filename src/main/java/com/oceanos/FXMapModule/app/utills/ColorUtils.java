package com.oceanos.FXMapModule.app.utills;

import javafx.scene.paint.Color;

/**
 * @autor slonikmak on 27.06.2018.
 */
public class ColorUtils {

    public static String colorToHex(Color color) {
        /*return colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue())
                + colorChanelToHex(color.getOpacity());*/
        return "#" + Integer.toHexString(color.hashCode()).substring(0,6);
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }

}