package com.oceanos.FXMapModule.app.utills.cache;

import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.app.utills.FilesUtills;
import com.oceanos.FXMapModule.app.utills.HttpDownloadUtility;
import com.oceanos.FXMapModule.events.MapEventType;
import com.oceanos.FXMapModule.events.TileEvent;
import com.oceanos.FXMapModule.layers.TileLayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @autor slonikmak on 25.07.2018.
 */
public class TileCache {
    //FIXME как-то убрать хардкод
    public static BooleanProperty run = new SimpleBooleanProperty(false);

    private final TileLayer layer;
    private LinkedBlockingQueue<TileEvent> queue = new LinkedBlockingQueue<>();
    private Path cacheFolder;

    private ExecutorService service = Executors.newCachedThreadPool();

    public TileCache(TileLayer layer){
        this.layer = layer;

        cacheFolder = ResourceManager.getInstance().getLayersCacheFolder().resolve(layer.getName());
        if (!Files.exists(cacheFolder)){
            try {
                Files.createDirectory(cacheFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        layer.addEventListener(MapEventType.tileload, (e)->{
            if (layer.isCashed()){
                try {
                    System.out.println("put event to queue");
                    queue.put((TileEvent) e);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        run.setValue(true);

        runConsumer();

        run.addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                service.shutdownNow();
            }
        });
    }

    private void runConsumer(){
        service.submit(()->{
            System.out.println("Run cache consumer");
            while (run.get()){
                try {
                    TileEvent event = queue.take();
                    service.submit(()-> download(event));
                } catch (InterruptedException e) {
                    System.out.println("close executor service");
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean exists(Path path){
        return Files.exists(path);
    }

    private void download(TileEvent event){
        try {
            System.out.println("Event "+event.getUrl());

            Path z = cacheFolder.resolve(String.valueOf(event.getZ()));
            if (!exists(z)) {
                Files.createDirectory(z);
            }
            Path x = z.resolve(String.valueOf(event.getX()));
            if (!exists(x)){
                Files.createDirectory(x);
            }
            Path y = x.resolve(event.getY()+".png");
            System.out.println("Path to dounload "+y);
            if (!exists(y)){
                HttpDownloadUtility.downloadFile(event.getUrl(), x.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
