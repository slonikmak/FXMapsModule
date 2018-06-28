package com.oceanos.FXMapModule.app.utills;

import javafx.beans.property.DoubleProperty;
import javafx.util.StringConverter;

/**
 * @autor slonikmak on 28.06.2018.
 */
public class GeoUtils {
    public static class CoordsToStringConverter extends StringConverter<Number>{

        @Override
        public String toString(Number object) {
            return String.valueOf(object);
        }

        @Override
        public Number fromString(String string) {
            return Double.parseDouble(string);
        }
    }
}
