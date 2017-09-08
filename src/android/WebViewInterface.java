package com.bais.amactplugin;

import android.webkit.JavascriptInterface;

import org.apache.cordova.CordovaActivity;

import tw.com.bais.amact.MainActivity;

public class WebViewInterface {
    public CordovaActivity _self = null;
    public CordovaActivity _other = null;
    public static CordovaActivity _parent = null;
    public static MainActivity parent = null;
    public static CordovaActivity _child = null;
    public static WebViewActivity child = null;

    public WebViewInterface(String role, CordovaActivity self, CordovaActivity other) {
        this._self = self;
        this._other = other;
        WebViewInterface._parent = role.equals("parent") ? self : other;
        WebViewInterface._child = role.equals("child") ? self : other;
        WebViewInterface.parent = MainActivity.self;
        WebViewInterface.child = WebViewActivity.self;
    }

    @JavascriptInterface
    public boolean childExists() {
        return WebViewInterface._child != null;
    }

    @JavascriptInterface
    public boolean isParent() {
        return _self == WebViewInterface._parent;
    }

    @JavascriptInterface
    public boolean isChild() {
        return _self == WebViewInterface._child;
    }

    @JavascriptInterface
    public void executeSelf(String expression) {
        loadSelf("javascript:" + expression);
    }

    @JavascriptInterface
    public void executeOther(String expression) {
        loadOther("javascript:" + expression);
    }

    @JavascriptInterface
    public void loadSelf(String url) {
        if (_self == WebViewInterface.parent) WebViewInterface.parent.load(url);
        if (_other == WebViewInterface.parent) WebViewInterface.child.load(url);
    }

    @JavascriptInterface
    public void loadOther(String url) {
        if (_self == WebViewInterface.parent) WebViewInterface.child.load(url);
        if (_other == WebViewInterface.parent) WebViewInterface.parent.load(url);
    }
}
