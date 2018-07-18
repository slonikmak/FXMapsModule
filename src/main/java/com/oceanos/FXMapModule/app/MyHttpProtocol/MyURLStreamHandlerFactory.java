package com.oceanos.FXMapModule.app.MyHttpProtocol;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class MyURLStreamHandlerFactory implements URLStreamHandlerFactory
{

    public URLStreamHandler createURLStreamHandler(String protocol)
    {
        if (protocol.equals("myapp"))
        {
            System.out.println("EEEEEEEEE");
            return new MyURLHandler();
        }
        return null;
    }

}