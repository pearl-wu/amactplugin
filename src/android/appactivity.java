package com.bais.amactplugin.appactivity;

import android.os.Bundle;

public class appactivity extends AppCompatActivity
{
    private WebView mWeb;

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    String url = "http://store.ebais.com.tw/iedu/register.html?justregsit=1&backurl=http%3A%2F%2Fstore.ebais.com.tw%2F~app%2Flogin&schoolname=%E5%8B%95%E6%95%B8%E5%AD%B8&website_url=http%3A%2F%2Fstore.ebais.com.tw%2F&lang=tw&application=1&skipapp=1";
                    mWeb.loadUrl( url );
                    return true;
                case R.id.refresh:
                    mWeb.reload();
                    return true;
                case R.id.back:
                    mWeb.goBack();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mWeb = (WebView) findViewById(R.id.Webs);
        mWeb.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                setTitle( title );


//                view.loadUrl("javascript: alert(1)");
            }
        });
        mWeb.setWebChromeClient(new WebChromeClient(){
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                return true;
            }
        });
        mWeb.addJavascriptInterface(new JsInteration(mWeb), "control");
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(false);
        String ua = "Mozilla/5.0 (eBAIS 1.0) Chrome";
        settings.setUserAgentString(ua);
//        String ua = settings.getUserAgentString();
//        if ( ua != null ) {
////            mWeb.loadUrl("javascript: alert('ua: " + ua + "');");
//            settings.setUserAgentString(ua);
//        }
        String url = "http://store.ebais.com.tw/iedu/register.html?justregsit=1&backurl=http%3A%2F%2Fstore.ebais.com.tw%2F~app%2Flogin&schoolname=%E5%8B%95%E6%95%B8%E5%AD%B8&website_url=http%3A%2F%2Fstore.ebais.com.tw%2F&lang=tw&application=1&skipapp=1";
        mWeb.loadUrl( url );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public class JsInteration{
        private WebView aWeb = null;
        private String UserAgent = null;
        public JsInteration(WebView mWeb){
            aWeb=mWeb;
        }

        @JavascriptInterface
        public void setUserAgent(String val){
            UserAgent = val;
        }
        @JavascriptInterface
        public void setUser(String key){
            setUser(key, "", "");
        }
        @JavascriptInterface
        public void setUser(String key, String uid){
            setUser(key, uid, "");
        }
        @JavascriptInterface
        public void setUser(String key, String uid, String uname){
            Log.d("key", key);
            Log.d("uid", uid);
            Log.d("uname", uname);
            String chkkey = "ae2b1fca515949e5d54fb22b8ed95575";
//            aWeb.loadUrl("javascript: alert('key: " + key + "\n\nuid: " + uid + "\nname: "+ uname + "');");
        }
    }
}
