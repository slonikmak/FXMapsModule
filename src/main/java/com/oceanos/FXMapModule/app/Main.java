package com.oceanos.FXMapModule.app;

import com.oceanos.FXMapModule.app.MyHttpProtocol.MyURLStreamHandlerFactory;
import com.oceanos.FXMapModule.app.controllers.MainController;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.cache.TileCache;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL.setURLStreamHandlerFactory(new MyURLStreamHandlerFactory());

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/sample.fxml"));
        MainController controller = loader.getController();
        primaryStage.setTitle("Менеджер мисий");
        primaryStage.setScene(new Scene(root, 1300, 900));



        primaryStage.show();
        //new JMetro(JMetro.Style.LIGHT).applyTheme(root);


        primaryStage.setOnCloseRequest((e)->{
            try {
                ResourceManager.getInstance().close();
                //FIXME: hardcode
                TileCache.run.setValue(false);
                //controller.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
