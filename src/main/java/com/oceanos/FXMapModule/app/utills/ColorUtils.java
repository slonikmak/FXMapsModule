package com.oceanos.FXMapModule.app.utills;

import javafx.scene.paint.Color;
import javafx.util.StringConverter;

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

    public static class ColorStringConverter extends StringConverter<Color>{
        @Override
        public String toString(Color object) {
            return ColorUtils.colorToHex(object);
        }

        @Override
        public Color fromString(String string) {
            return Color.web(string);
        }
    }
}