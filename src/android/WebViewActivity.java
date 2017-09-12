package com.bais.amactplugin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.cordova.CordovaActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tw.com.bais.amact.R;

public class WebViewActivity extends CordovaActivity {

    private WebView mWeb;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");


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
        //String url = "http://store.ebais.com.tw/iedu/register.html?justregsit=1&backurl=http%3A%2F%2Fstore.ebais.com.tw%2F~app%2Flogin&schoolname=%E5%8B%95%E6%95%B8%E5%AD%B8&website_url=http%3A%2F%2Fstore.ebais.com.tw%2F&lang=tw&application=1&skipapp=1";
        WebViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWeb.loadUrl( url );
            }
        });
    }


    public class JsInteration{
        private WebView aWeb = null;
        private String UserAgent = null;
        private String mainKey = "5c3b3ac2abf098b325d89005deccd7e6";
        public JsInteration(WebView mWeb){
            aWeb = mWeb;
        }
        @JavascriptInterface
        public void setUserAgent(String val){
            UserAgent = val;
        }
        @JavascriptInterface
        public void setUser(final String key,String job, final String uid, final String uname) {
            /**
                             key=已組好KEY值
                             job=哪個社群(google, facebook, twitter, ...?)
                            uid=UDC USER ID
                            uname=UDC USER NAME
                         * */
            String chkkey = md5( mainKey + "-" + md5( uid + "-" + uname ) );
            Log.d("key", key);
            Log.d("uid", uid);
            Log.d("uname", uname);

            if(key == chkkey){
                WebViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            /*
                                                            *  WebView mainWeb = (WebView) findViewById(R.id.Webs);
                                                                String url = "http://store.ebais.com.tw/~app/login?remember=1";
                                                                mainWeb.loadUrl( url );
                                                            * */
                            //aWeb.loadUrl("javascript: alert('key: " + key + "\n\nuid: " + uid + "\nname: "+ uname + "');");
                            Intent intent=new Intent();
                            intent.putExtra("key", key);
                            setResult(RESULT_OK,intent);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
            }else{
                //KEY組起來跟回傳已組好的值不同時處理

            }
            finish();
        }
        @JavascriptInterface
        public void setUser(String key, String job, String uid){
            setUser(key, job, uid, "");
        }
        @JavascriptInterface
        public void setUser(String key, String job){
            setUser(key, job, "");
        }
        @JavascriptInterface
        public void setUser(String key){
            setUser(key, "");
        }
    }

    public String md5(String s)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for(int i=0; i<messageDigest.length; i++)
            {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
