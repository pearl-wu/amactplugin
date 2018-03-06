package com.bais.amactplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.com.bais.amact.R;

import static com.bais.amactplugin.encode.MD5;

public class WebViewActivity extends Activity {
    private WebView mWeb;
    private WebView mWeb_m;
    private String url;
    private Boolean onlyBrowser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        onlyBrowser = bundle.getBoolean("onlyBrowser");

        if(onlyBrowser){
            setContentView(R.layout.activity_browser);
        }else{
            setContentView(R.layout.activity_register);
        }
        mWeb = (WebView) findViewById(R.id.Webs);
        WebSettings settings = mWeb.getSettings();
        mWeb.addJavascriptInterface(new JsInteration(mWeb), "control");


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
                try{
                    URL urlper = new URL(url);
                    // System.out.println("URL：" + urlper.toString());
                    // System.out.println("协议为：" + urlper.getProtocol());
                    // System.out.println("验证信息：" + urlper.getAuthority());
                    // System.out.println("文件名及请求参数：" + urlper.getFile());
                    // System.out.println("主机名：" + urlper.getHost());
                    // System.out.println("端口：" + urlper.getPort());
                    //System.out.println("路径：" + urlper.getPath());
                    // System.out.println("默认端口：" + urlper.getDefaultPort());
                    // System.out.println("请求参数：" + urlper.getQuery());
                    // System.out.println("定位位置：" + urlper.getRef());
                    String str=new String("/testpaper_data_p10.php");
                    if(onlyBrowser && urlper.getPath().equals(str)){
                        /*final ImageView back = (ImageView)findViewById(R.id.imageView2);
                                                    back.setVisibility(View.VISIBLE);
                                                    back.setOnClickListener(new Button.OnClickListener(){
                                                        public void onClick(View v){back.setVisibility(View.GONE);mWeb.goBack();}
                                                    });*/
                        Intent i = new Intent(WebViewActivity.this, WebViewActivity_s.class);
                        i.putExtra("url", url);
                        startActivity(i);
                        return true;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                return false;
            }
        });

       /* String url = "http://172.16.39.50/store.ebais.com.tw/~app/login";
                 String cookie = CookieManager.getInstance().getCookie(url);
                  String[] split_line = cookie.split(";");
                      for (String s: split_line) {
                         CookieManager.getInstance().setCookie(url, s);
                     }
                Log.i("test........",CookieManager.getInstance().getCookie(url));*/

        if(onlyBrowser){
            ImageView close = (ImageView)findViewById(R.id.imageView1);
            close.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    finish();
                }
            });
            return ;
        }

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
    }

    public void action(String key,String job,String uid,String uname){
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
            Log.i("resr......","................");
            return true;
        }
    }

    public class JsInteration{
        private WebView aWeb = null;
        private String UserAgent = null;
        private String mainKey = "";
        public JsInteration(WebView mWeb){ aWeb = mWeb; }

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
                        String url = "https://iam.ebais.net/~app/login?remember=1";
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

    /* @Override
    protected void onStop() {
        super.onStop();
        // Log.i("onStop","onStop onStop");
        callJavaScript();
        // The Application has been closed!
    }

   public void callJavaScript() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 非同步執行呼叫JavaScript的方式
            mWeb.evaluateJavascript("javascript:callAlert()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e("callJavaScript", "onReceiveValue" + value);//列印返回的值
                }
            });
        } else {
            if (mWeb != null) {
                // 同步阻塞UI執行緒序呼叫
                mWeb.loadUrl("javascript:callAlert()");
            }
        }
    }*/
}
