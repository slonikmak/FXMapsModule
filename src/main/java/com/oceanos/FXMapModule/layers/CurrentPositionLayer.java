package com.oceanos.FXMapModule.layers;

import com.fazecast.jSerialComm.SerialPort;
import com.oceanos.FXMapModule.app.properties.ResourceManager;
import com.oceanos.FXMapModule.utils.GpsReader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @autor slonikmak on 17.07.2018.
 */
public class CurrentPositionLayer extends Marker {
    private GpsReader reader;

    //869Gmmor
    public CurrentPositionLayer(){
        reader = new GpsReader();
        ResourceManager.getInstance().getIconPath("icons8-submarine-48.png").ifPresent(i->{
            System.out.println("icon: "+i);
            setIcon("file:/"+i);
        });
    }

    public void setPort(SerialPort portName){
        reader.setPort(portName);
    }

    public void setBaudrate(int baudrate){
        reader.setBaudrate(baudrate);
    }

    public SerialPort[] getPortNames(){
        return SerialPort.getCommPorts();
    }

    public void startPort(){
        reader.startPort();
    }

    @Override
    public void addToMap(){
        super.addToMap();
        //reader.startPort();
        reader.latProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                setLat((Double) newValue);
            });
        });
        reader.lngProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                setLng((Double) newValue);
            });
        });
//        latProperty().bind(reader.latProperty());
//        lngProperty().bind(reader.lngProperty());
    }

    @Override
    public void remove(){
        super.remove();
        closeReader();
    }

    public void closeReader(){
        reader.close();
    }
}
