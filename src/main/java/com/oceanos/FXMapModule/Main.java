package com.oceanos.FXMapModule;

import com.mohamnag.fxwebview_debugger.DevToolsDebuggerServer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();

        /*primaryStage.setOnCloseRequest((e)->{
            try {
                DevToolsDebuggerServer.stopDebugServer();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });*/
    }


    public static void main(String[] args) {
       launch(args);
    }
}
