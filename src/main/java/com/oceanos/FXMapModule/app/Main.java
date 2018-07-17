package com.oceanos.FXMapModule.app;

import com.oceanos.FXMapModule.app.controllers.MainController;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/sample.fxml"));
        MainController controller = loader.getController();
        primaryStage.setTitle("Менеджер мисий");
        primaryStage.setScene(new Scene(root, 1300, 900));
        primaryStage.show();
        ResourceManager.getInstance();

        primaryStage.setOnCloseRequest((e)->{
            try {
                ResourceManager.getInstance().close();
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
