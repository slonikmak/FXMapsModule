package com.oceanos.FXMapModule.app.utills;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * @autor slonikmak on 26.06.2018.
 */
public class FilesUtills {
    public static void deleteDirectory(Path dir) throws IOException {
       /* Files.list(dir).forEach(f->{
            if (Files.isDirectory(f)){
                try {
                    deleteDirectory(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Files.delete(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Files.delete(dir);*/
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
