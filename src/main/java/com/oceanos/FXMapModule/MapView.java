package com.oceanos.FXMapModule;

import com.mohamnag.fxwebview_debugger.DevToolsDebuggerServer;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @autor slonikmak on 13.06.2018.
 */
public class MapView extends AnchorPane {
    private WebView webView;
    private WebEngine webEngine;

    public MapView(){
        super();
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
        /*try {
            DevToolsDebuggerServer.startDebugServer(webEngine.impl_getDebugger(), 51742);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
