package com.oceanos.FXMapModule.utils;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * @autor slonikmak on 16.07.2018.
 */
public class GpsReader {

    DoubleProperty lat = new SimpleDoubleProperty();
    DoubleProperty lng = new SimpleDoubleProperty();

    SerialPort comPort;
    int baudrate;



    private static StringProperty rawData = new SimpleStringProperty();

    public GpsReader(){

        //startPort();
    }

    public void setPort(SerialPort port) {
        this.comPort = port;
    }

    public void close(){
        comPort.closePort();
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    private void parseRawData(String str){
        String[] arr = str.split(",");
        if (arr[0].equals("$GNGGA") || arr[0].equals("$GPGGA")){
            System.out.println("Parse "+str);

            String rawLat = arr[2];
            String let1 = arr[3];
            String rawLng = arr[4];
            String let2 = arr[5];
            String rawQuality = arr[6];
            int quality = Integer.parseInt(rawQuality);
            //if (quality == 2 || quality ==3){
                lat.setValue((double) latitude2Decimal(rawLat, let1));
                lng.setValue((double)longitude2Decimal(rawLng, let2));
            //}
        }
    }

    public void startPort(){
        //SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            StringBuffer buffer = new StringBuffer();

            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String string = new String(newData);
                buffer.append(string);
                if (string.endsWith("\n")){
                    parseRawData(buffer.toString());
                    buffer = new StringBuffer();
                }

            }
        });
    }

    public double getLat() {
        return lat.get();
    }

    public DoubleProperty latProperty() {
        return lat;
    }

    public double getLng() {
        return lng.get();
    }

    public DoubleProperty lngProperty() {
        return lng;
    }

    public static float longitude2Decimal(String lon, String WE) {
        if (lon.equals("")) {
            return 0.0f;
        }
        float med = Float.parseFloat(lon.substring(3)) / 60.0f;
        med += Float.parseFloat(lon.substring(0, 3));
        if (WE.startsWith("W")) {
            med = -med;
        }
        return med;
    }

    public static float latitude2Decimal(String lat, String NS) {
        if (lat.equals("")) {
            return 0.0f;
        }

        float med = Float.parseFloat(lat.substring(2)) / 60.0f;
        med += Float.parseFloat(lat.substring(0, 2));

        if (NS.startsWith("S")) {
            med = -med;
        }
        return med;
    }
}
