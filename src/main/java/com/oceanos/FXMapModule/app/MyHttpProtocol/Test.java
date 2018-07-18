package com.oceanos.FXMapModule.app.MyHttpProtocol;

import java.io.IOException;
import java.nio.file.Paths;

public class Test {
    //https://stackoverflow.com/questions/27877609/how-to-add-a-local-img-with-file-in-javafx-webkit
    //https://stackoverflow.com/questions/17522343/custom-javafx-webview-protocol-handler
    public static void main(String[] args) {
        MyURLStreamHandlerFactory factory = new MyURLStreamHandlerFactory();
        MyURLHandler handler = (MyURLHandler) factory.createURLStreamHandler("myapp");
        try {
            System.out.println(Paths.get("C:\\Users\\User\\Desktop\\cash\\OSM\\13\\4804\\2396.png").toUri().toURL());
            handler.openConnection(Paths.get("C:\\Users\\User\\Desktop\\cash\\OSM\\13\\4804\\2396.png").toUri().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
