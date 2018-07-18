package com.oceanos.FXMapModule.app.MyHttpProtocol;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MyImage {
    String path;

    public MyImage(String path) {
        this.path = path;
    }

    public boolean isFailed(){
        return Files.exists(Paths.get(path));
    }
}
