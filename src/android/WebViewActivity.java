package com.bais.amactplugin;

import android.os.Bundle;
import android.webkit.WebView;

import org.apache.cordova.CordovaActivity;

import tw.com.bais.amact.MainActivity;

public class WebViewActivity extends CordovaActivity {
    public static WebViewActivity self = null;
    public static WebViewInterface Interface = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        //noinspection ConstantConditions
        loadUrl((url.matches("^(.*://|javascript:)[\\s\\S]*$") ? "" : "file:///android_asset/www/") + url);
        WebViewActivity.self = this;
        MainActivity.Interface._other = WebViewActivity.self;
        WebViewInterface._child = WebViewActivity.self;
        WebViewActivity.Interface = new WebViewInterface("child", WebViewActivity.self, MainActivity.self);
        ((WebView) appView.getEngine().getView()).addJavascriptInterface(WebViewActivity.Interface, "Interface");
    }

    public void load(String _url) {
        final String url = _url;
        appView.getEngine().getView().post(new Runnable() {
            @Override
            public void run() {
                appView.loadUrl((url.matches("^(.*://|javascript:)[\\s\\S]*$") ? "" : "file:///android_asset/www/") + url);
            }
        });
    }
}
