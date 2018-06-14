package com.oceanos.FXMapModule;

import com.mohamnag.fxwebview_debugger.DevToolsDebuggerServer;
import com.oceanos.FXMapModule.JSControllers.EventController;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * @autor slonikmak on 13.06.2018.
 */
public class MapView extends AnchorPane {
    private WebView webView;
    private WebEngine webEngine;
    private EventController eventController;
    private JSObject window;


    public MapView(){
        super();
        eventController = new EventController();
        initWebView();
        this.getChildren().add(webView);
    }

    public void initWebView(){
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.setMaxHeight(Integer.MAX_VALUE);
        webView.setMaxWidth(Integer.MAX_VALUE);
        AnchorPane.setBottomAnchor(webView, 0d);
        AnchorPane.setTopAnchor(webView, 0d);
        AnchorPane.setRightAnchor(webView, 0d);
        AnchorPane.setLeftAnchor(webView, 0d);
/*        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);*/
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/html/index.html").toExternalForm());
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            System.out.println(message + "[at " + lineNumber + "]");
        });
        webEngine.getLoadWorker()
                .stateProperty()
                .addListener((ov, oldState, newState) -> {
                            if (newState == Worker.State.SUCCEEDED) {
                                //webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");


                                /*try {
                                    DevToolsDebuggerServer.startDebugServer(webEngine.impl_getDebugger(), 51742);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/
                                window = (JSObject) webEngine.executeScript("window");
                                window.setMember("javaEventController", eventController);
                                //JSObject jsToJava = (JSObject) webEngine.executeScript("fromJavaToJs");
                                //id = javaToJsBridge.getMapId();
                                //jsBridge.initJavaController();
                                //repository.setJsBridge(jsBridge);
                                //jsBridge.resizeMap(webView.getWidth(), webView.getHeight());
                                //setZoom((int) window.call("getZoom"));
                       /* // all next classes are from org.w3c.dom domain
                        org.w3c.dom.events.EventListener listener = (ev) -> {
                            System.out.println("#" + (org.w3c.dom.Element) ev.getTarget());
                        };
                        org.w3c.dom.Document doc = webEngine.getDocument();
                        org.w3c.dom.Element el = doc.getElementById("mapid");
                        ((org.w3c.dom.events.EventTarget) el).addEventListener("click", listener, false);*/
                                //jsBridge.setPosition();
                            }

                            //setMapUrl();
                        }
                );


        /*try {
            DevToolsDebuggerServer.startDebugServer(webEngine.impl_getDebugger(), 51742);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
