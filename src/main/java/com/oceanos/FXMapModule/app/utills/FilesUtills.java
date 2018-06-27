package com.oceanos.FXMapModule.app.utills;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class FilesUtills {
    public static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static String normalizePath(String path) {
        if (path.startsWith("file:/")){
            path = path.replace("file:/","");
        }
        String result = new File(path).toURI().toString();
        return result;
    }
}
