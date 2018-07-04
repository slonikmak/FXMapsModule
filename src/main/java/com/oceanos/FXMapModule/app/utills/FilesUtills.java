package com.oceanos.FXMapModule.app.utills;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    public static void saveFile(Path toPath, String content) {
        try {
            Files.write(toPath, content.getBytes(Charset.forName("UTF-8")), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
