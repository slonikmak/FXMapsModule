package com.oceanos.FXMapModule.app.MyHttpProtocol;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Register a protocol handler for URLs like this: <code>myapp:///pics/sland.gif</code><br>
 */
public class MyURLConnection extends URLConnection
{

    private byte[] data;

    private String PROTOCOL = "myapp";

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected MyURLConnection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException
    {
        if (connected)
        {
            return;
        }
        loadImage();
        connected = true;
    }

    public String getHeaderField(String name)
    {
        if ("Content-Type".equalsIgnoreCase(name))
        {
            return getContentType();
        }
        else if ("Content-Length".equalsIgnoreCase(name))
        {
            return "" + getContentLength();
        }
        return null;
    }

    public String getContentType()
    {
        String fileName = getURL().getFile();
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        return "image/" + ext; // TODO: switch based on file-type
    }

    public int getContentLength()
    {
        return data.length;
    }

    public long getContentLengthLong()
    {
        return data.length;
    }

    public boolean getDoInput()
    {
        return true;
    }

    public InputStream getInputStream() throws IOException
    {
        connect();

        ByteArrayInputStream arr;
        try {
            arr = new ByteArrayInputStream(data);
        } catch (NullPointerException e){
            return new ByteArrayInputStream(new byte[]{});
        }
        return arr;
    }
    private void loadImage() throws IOException {
        String imgPath = getURL().toExternalForm();
        imgPath = URLDecoder.decode(imgPath, "utf-8");
        //System.out.println(URLDecoder.decode(imgPath, "utf-8"));
        //System.out.println(new String(imgPath.getBytes(Charset.forName("UTF-8")), Charset.forName("CP1251")));
        String ext = imgPath.substring(imgPath.lastIndexOf('.')+1);
        imgPath = imgPath.startsWith(PROTOCOL+"://") ?
                imgPath.substring((PROTOCOL+"://").length()) :
                imgPath.substring((PROTOCOL+":").length());
        //System.out.println(imgPath);

        File file = new File(imgPath);
        if (file.toPath().endsWith(".png")||file.toPath().endsWith(".jpg")){
            try{
                BufferedImage read = ImageIO.read(file);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(read, ext, os);
                data=os.toByteArray();
            } catch(IOException ioe){
                System.out.println("IO exception: "+ioe);
            }
        } else {
            try{
                Path fileLocation = file.toPath();
                //System.out.println(fileLocation);
                data = Files.readAllBytes(fileLocation);
                OutputStream os = getOutputStream();
                os.write(data);
                //System.out.println(Arrays.toString(data));
                /*ImageIO.write(read, ext, os);
                data=os.toByteArray();*/
            } catch(IOException ioe){
                System.out.println("IO exception: "+ioe);
            }
        }

    }

    /*private void loadImage() throws IOException
    {
        if (data != null)
        {
            return;
        }
        *//*try
        {
            int timeout = this.getConnectTimeout();
            long start = System.currentTimeMillis();
            URL url = getURL();

            String imgPath = url.toExternalForm();
            imgPath = imgPath.startsWith("myapp://") ? imgPath.substring("myapp://".length()) : imgPath.substring("myapp:".length()); // attention: triple '/' is reduced to a single '/'

            // this is my own asynchronous image implementation
            // instead of this part (including the following loop) you could do your own (synchronous) loading logic
            MyImage img = MyApp.getImage(imgPath);
            do
            {
                if (img.isFailed())
                {
                    throw new IOException("Could not load image: " + getURL());
                }
                else if (!img.hasData())
                {
                    long now = System.currentTimeMillis();
                    if (now - start > timeout)
                    {
                        throw new SocketTimeoutException();
                    }
                    Thread.sleep(100);
                }
            } while (!img.hasData());
            data = img.getData();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }*//*
    }*/

    public OutputStream getOutputStream() throws IOException
    {
        // this might be unnecessary - the whole method can probably be omitted for our purposes
        //return getURL().openConnection().getOutputStream();
        return new ByteArrayOutputStream();
    }

    public java.security.Permission getPermission() throws IOException
    {
        return null; // we need no permissions to access this URL
    }

}