package com.bais.amactplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import static com.bais.amactplugin.encode.MD5;

public class amactplugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private JSONObject params;
    private static final String LOG_TAG = "WebViewPlugin";
    private static CallbackContext subscribeCallbackContext = null;
    public static final int REQUEST_CODE = 0;
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if(action.equals("version")){
            try {
                PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(cordova.getActivity().getPackageName(), 0);
                this.callbackContext.success(packageInfo.versionName);
            } catch (NameNotFoundException e) {
                //Handle exception
                this.callbackContext.error(e.hashCode());
            }
          return true;
       }else if(action.equals("openweb")){
            this.params = args.getJSONObject(0);
            String webUrl = params.getString("url");
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            Uri uri = Uri.parse(webUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            cordova.getActivity().startActivity(intent);
            return true;
        }else if(action.equals("openapp")){
            this.params = args.getJSONObject(0);
            String webUrl = params.getString("url");
            String webApp = params.getString("app");
            try {
                Intent intent;
                if (webUrl.startsWith("intent://")) {
                    try {
                        intent = Intent.parseUri(webUrl, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                            intent.setSelector(null);
                        }
                        cordova.getActivity().startActivityIfNeeded(intent, -1);
                        List<ResolveInfo> resolves = cordova.getActivity().getPackageManager().queryIntentActivities(intent, 0);

                        if(resolves.size()>0){
                            cordova.getActivity().startActivityIfNeeded(intent, -1);
                            //Toast.makeText(cordova.getActivity(),"開啟中...",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                if (!webUrl.startsWith("http")) {
                    try {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        cordova.getActivity().startActivity(intent);
                        this.callbackContext.success("開啟中，請稍待...");
                        //Toast.makeText(cordova.getActivity(),"開啟IBC",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.callbackContext.error("開啟失敗");
                        //Toast.makeText(cordova.getActivity(),"尚未安裝IBC, 前往Google play 安裝IBC。",Toast.LENGTH_LONG).show();
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webApp));
                        cordova.getActivity().startActivity(intent);

                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
          }else if(action.equals("openwebshow")){
              this.params = args.getJSONObject(0);
              String webUrl = params.getString("url");
              //this.callbackContext.success("OK");
              //Toast.makeText(this.cordova.getActivity(),webUrl,Toast.LENGTH_SHORT).show();
             cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
              Boolean shouldShowLoading = false;
              try{
                  shouldShowLoading = args.getBoolean(1);
              }
              catch(Exception e){

              }
             if(!"".equals(webUrl)) {
                showWebView(webUrl, shouldShowLoading);
                JSONObject r = new JSONObject();
                r.put("responseCode", "ok");
                //callbackContext.success(r);
              }
              return true;
          }else if(action.equals("hide")) {
              LOG.d(LOG_TAG, "Hide Web View");
              hideWebView();
              JSONObject r = new JSONObject();
              r.put("responseCode", "ok");
              this.callbackContext.success(r);
          }else if(action.equals("cookie-get")){
              this.params = args.getJSONObject(0);
              String webUrl = params.getString("url");
              CookieManager.setAcceptFileSchemeCookies(true); //available in android level 12
              CookieManager.getInstance().setAcceptCookie(true); //available in android level 12
                 String cookie = CookieManager.getInstance().getCookie(webUrl);
                // String[] AfterSplit = cookie.split(",");
                this.callbackContext.success(cookie);
               // Log.d("cookie ", cookie);
               //Log.d("PHPGUID: ", CookieManager.getInstance().getCookie("PHPGUID"));
               //Log.d("MjMtZA%3D%3D: ", CookieManager.getInstance().getCookie("MjMtZA%3D%3D"));
               //Log.d("MjMtcw%3D%3D: ", CookieManager.getInstance().getCookie("MjMtcw%3D%3D"));
          }else if(action.equals("cookie-clear")){
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                      CookieManager.getInstance().removeAllCookies(null);
                      CookieManager.getInstance().flush();
                  } else {
                      CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(cordova.getActivity());
                      cookieSyncManager.startSync();
                      CookieManager cookieManager = CookieManager.getInstance();
                      cookieManager.removeAllCookie();
                      //cookieManager.removeSessionCookie();
                      cookieSyncManager.stopSync();
                      cookieSyncManager.sync();
                  }
          }else if(action.equals("cookie-set")){
            CookieSyncManager.createInstance(cordova.getActivity());
            CookieSyncManager.getInstance().startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.getInstance().setAcceptCookie(true);
            params = args.getJSONObject(0);
            String d1 = params.getString("url");
            String d2 = params.getString("d1");
            String d3 = params.getString("d2");
            String d4 = params.getString("d3");
            //Toast.makeText(cordova.getActivity(),d2+"......."+d1,Toast.LENGTH_SHORT).show();
            //String[] d3 = params.getString("d3").split(":");
            cookieManager.setCookie(d1, d2);
            cookieManager.setCookie(d1, d3);
            cookieManager.setCookie(d1, d4);
            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.getInstance().sync();
            } else {
                CookieManager.getInstance().flush();
            }
            String cookie = CookieManager.getInstance().getCookie("https://store.ebais.com.tw/~app/login");
            this.callbackContext.success(cookie);
          }else if(action.equals("encode")){
            this.params = args.getJSONObject(0);
            JSONArray json = new JSONArray();
            String cookie1 = params.getString("d1");
            String cookie2 = params.getString("d2");
            String guid = params.getString("d3");
            String key = "5c3b3ac2abf098b325d89005deccd7e6";
            //驗證
            String[] strarrc = {key,guid};
            String code_c = MD5(implode("/", strarrc));

            //加密
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(cookie1);
            jsonArray.put(cookie2);
            jsonArray.put(guid);
            String Encoded = Base64.encodeToString((jsonArray.toString()).getBytes(), Base64.DEFAULT);
            String code_p = URLEncoder.encode( Encoded );
            json.put(code_c);
            json.put(code_p);
            this.callbackContext.success(json);
        }
        return false;
     }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(cordova.getActivity(),requestCode+"......",Toast.LENGTH_SHORT).show();
        JSONArray json = new JSONArray();
        if (resultCode == Activity.RESULT_OK) {
            json.put(data.getStringExtra("key"));
            json.put(data.getStringExtra("job"));
            json.put(data.getStringExtra("uid"));
            json.put(data.getStringExtra("uname"));
            this.callbackContext.success(json);
        }
    }

    private void showWebView(final String url, Boolean shouldShowLoading) {
        LOG.d(LOG_TAG, "Url: " + url);
        Intent i = new Intent(cordova.getActivity(), WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("shouldShowLoading", shouldShowLoading);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cordova.startActivityForResult((CordovaPlugin)this, i, REQUEST_CODE);
        //cordova.setActivityResultCallback(this);
        //this.cordova.getActivity().getApplicationContext().startActivity(i);
      }

      private void hideWebView() {
        LOG.d(LOG_TAG, "hideWebView");
        cordova.getActivity().finish();
        if(subscribeCallbackContext != null){
          LOG.d(LOG_TAG, "Calling subscribeCallbackContext success");
          subscribeCallbackContext.success();
          subscribeCallbackContext = null;
        }
      }
    private static String implode(String glue, String[] array){
        boolean first = true;
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            if (!first) str.append(glue);
            str.append(s);
            first = false;
        }
        return str.toString();
    }


    public static class DealedUrl {
        public String url;
        public String params;
    }
}
