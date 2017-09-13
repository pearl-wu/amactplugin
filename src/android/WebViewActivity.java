package com.bais.amactplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tw.com.bais.amact.R;

public class WebViewActivity extends Activity {

    private WebView mWeb;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        mWeb = (WebView) findViewById(R.id.Webs);

        Button home = (Button)findViewById(R.id.button1);
        Button back = (Button)findViewById(R.id.button2);
        Button refresh = (Button)findViewById(R.id.button3);
        home.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                mWeb.loadUrl( url );
            }
        });
        back.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                mWeb.goBack();
            }
        });
        refresh.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                mWeb.reload();
            }
        });

        /*mWeb.setWebViewClient(new WebViewClient(){
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                String title = view.getTitle();
                                setTitle( title );
                                 //    view.loadUrl("javascript: alert(1)");
                            }
                        });*/
            /*mWeb.setWebChromeClient(new WebChromeClient(){
                            public boolean onConsoleMessage(ConsoleMessage cm) {
                                Log.d("MyApplication", cm.message() + " -- From line "
                                        + cm.lineNumber() + " of "
                                        + cm.sourceId() );
                                return true;
                            }
                        });*/



        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        mWeb.addJavascriptInterface(new JsInteration(mWeb), "control");
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(false);
        String ua = "Mozilla/5.0 (eBAIS 1.0) Chrome";
        settings.setUserAgentString(ua);
        mWeb.loadUrl( url );
        /*WebViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWeb.loadUrl( url );
                    }
                });*/
    }

    public void action(String key,String uid,String uname){
       /* Intent intent=new Intent();
                intent.putExtra("key", key);
                setResult(RESULT_OK,intent);*/
                //finish();
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
        public Object setUser(String key,String job, String uid, String uname) {
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
                action(key,uid,uname);
                return key.toString();
            }else{
                //KEY組起來跟回傳已組好的值不同時處理
            }
            return key.toString();
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

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }
}
