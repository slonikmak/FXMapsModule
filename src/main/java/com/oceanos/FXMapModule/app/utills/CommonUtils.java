package com.oceanos.FXMapModule.app.utills;

public class CommonUtils {
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public static String firstLetterLowerCase(final String line){
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }
}
