package com.bais.amactplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.com.bais.amact.R;

import static com.bais.amactplugin.encode.MD5;



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

        mWeb = (WebView) findViewById(R.id.Webs);
        //Button home = (Button)findViewById(R.id.button1);
        Button back = (Button)findViewById(R.id.button2);
        Button refresh = (Button)findViewById(R.id.button3);
        /*home.setOnClickListener(new Button.OnClickListener(){
                        public void onClick(View v){
                            mWeb.loadUrl( url );
                        }
                    });*/
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
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String ua = "Mozilla/5.0 (eBAIS 1.0) Chrome";
        settings.setUserAgentString(ua);
        mWeb.setWebViewClient(new InsideWebViewClient());
        mWeb.loadUrl( url );
    }

    public void action(String key,String job,String uid,String uname){
        Toast.makeText(WebViewActivity.this, "action", Toast.LENGTH_SHORT).show();
        try{
            Intent intent = new Intent();
            intent.putExtra("key", key);
            intent.putExtra("job", job);
            intent.putExtra("uid", uid);
            intent.putExtra("uname", uname);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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
        public void setUser(final String key, final String job, final String uid, final String uname){
            String chkkey = MD5( sJoin("-", mainKey, MD5( sJoin("-", uid, uname) )) );
            Log.d("param", "show get data"
                    +"\nkey: "+ key
                    +"\njob: "+ job
                    +"\nuid: "+ uid
                    +"\nuname: "+ uname
            );
            if( chkkey.equals(key) ) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //取得主要 webview , 載入記憶自動登入並取得使用者相關資料
                        String url = "https://store.ebais.com.tw/~app/login?remember=1";
                        action(key,job,uid,uname);
            //                        WebView mainWeb = (WebView) findViewById(R.id.Webs);
            //                        mainWeb.loadUrl( url );
                    }
                });
            }else{
                //KEY組起來跟回傳已組好的值不同時處理
                Log.e("weberror","Key not match!"
                        + "\nmainKey: "+ mainKey
                        + "\nchkkey: "+ chkkey
                );
            }
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

    public String sJoin(String key, String... args){
        List<String> ls = new ArrayList<String>();
        for( String argv : args ){
            ls.add( argv );
        }
        String val = TextUtils.join(key, ls );
        return  val;
    }
}
