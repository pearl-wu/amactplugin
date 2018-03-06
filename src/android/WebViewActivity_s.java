package com.bais.amactplugin;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import tw.com.bais.amact.R;

public class WebViewActivity_s extends Activity {
    private WebView mWeb;
    private WebView mWeb_m;
    private String url;
    private Boolean onlyBrowser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        setContentView(R.layout.activity_browser);
        mWeb = (WebView) findViewById(R.id.Webs);
        ImageView close = (ImageView)findViewById(R.id.imageView1);
        close.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        WebSettings settings = mWeb.getSettings();
        mWeb.setWebChromeClient(new WebChromeClient());
        mWeb.setWebViewClient(new WebViewClient());
          settings.setDomStorageEnabled(true);
          settings.setJavaScriptEnabled(true);
          settings.setJavaScriptCanOpenWindowsAutomatically(true);
          settings.setLoadWithOverviewMode(true);
          settings.setBuiltInZoomControls(false);
          settings.setDatabaseEnabled(true);
          settings.setAllowFileAccess(true);
          settings.setAppCacheEnabled(true);
          settings.setAllowContentAccess(true);
          settings.setAllowUniversalAccessFromFileURLs(true);
          settings.setAllowFileAccessFromFileURLs(true);
          settings.setAppCacheEnabled(true);
          settings.setSupportZoom(false);
          //settings.setSupportMultipleWindows(true);
          settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
          String ua = "Mozilla/5.0 (eBAIS 1.0) Chrome";
          settings.setUserAgentString(ua);

        mWeb.setWebViewClient(new InsideWebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB_MR1) { CookieManager.setAcceptFileSchemeCookies(true); }
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) { cookieManager.setAcceptThirdPartyCookies(mWeb, true); }
        mWeb.loadUrl( url );
        mWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
}
