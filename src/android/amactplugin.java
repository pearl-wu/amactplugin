package com.bais.amactplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class amactplugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private JSONObject params;
    private static final String LOG_TAG = "WebViewPlugin";
    private static CallbackContext subscribeCallbackContext = null;
    public static final int REQUEST_CODE = 0;

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
            this.cordova.getActivity().startActivity(intent);
            return true;
        }else if(action.equals("openapp")){
            this.params = args.getJSONObject(0);
            String webUrl = params.getString("url");
            Intent intent =  this.cordova.getActivity().getPackageManager().getLaunchIntentForPackage(webUrl);
            // intent空，沒安装
            if (intent != null) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                Toast.makeText(this.cordova.getActivity(),"開啟IBC...",Toast.LENGTH_SHORT).show();
                //intent.putExtra("name", "air.tw.com.bais.ibc");
                 this.cordova.getActivity().startActivity(intent);
            } else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                Toast.makeText(this.cordova.getActivity(),"尚未安裝IBC",Toast.LENGTH_LONG).show();
            }
            return true;
          }else if(action.equals("openwebshow")){
              this.params = args.getJSONObject(0);
              String webUrl = params.getString("url");
              //this.callbackContext.success("OK");
              //Toast.makeText(this.cordova.getActivity(),webUrl,Toast.LENGTH_SHORT).show();
             this.cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
                callbackContext.success(r);
              }
              return true;
          }else if(action.equals("hide")) {
              LOG.d(LOG_TAG, "Hide Web View");
              hideWebView();
              JSONObject r = new JSONObject();
              r.put("responseCode", "ok");
              this.callbackContext.success(r);
          }else if(action.equals("cookie-get")){
              CookieManager.setAcceptFileSchemeCookies(true); //available in android level 12
              CookieManager.getInstance().setAcceptCookie(true); //available in android level 12
                Log.d("PHPGUID: ", CookieManager.getInstance().getCookie("PHPGUID"));
                Log.d("MjMtZA%3D%3D: ", CookieManager.getInstance().getCookie("MjMtZA%3D%3D"));
                Log.d("MjMtcw%3D%3D: ", CookieManager.getInstance().getCookie("MjMtcw%3D%3D"));
            }

        return false;
     }
    
    private void showWebView(final String url, Boolean shouldShowLoading) {
        LOG.d(LOG_TAG, "Url: " + url);
        Intent i = new Intent(this.cordova.getActivity(), WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("shouldShowLoading", shouldShowLoading);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.cordova.getActivity().startActivityForResult(i, REQUEST_CODE);
        //this.cordova.getActivity().getApplicationContext().startActivity(i);
      }

      private void hideWebView() {
        LOG.d(LOG_TAG, "hideWebView");
        this.cordova.getActivity().finish();
        if(subscribeCallbackContext != null){
          LOG.d(LOG_TAG, "Calling subscribeCallbackContext success");
          subscribeCallbackContext.success();
          subscribeCallbackContext = null;
        }
      }
    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LOG.d("..............................................", "Calling subscribeCallbackContext success");
        if(resultCode== Activity.RESULT_OK){
            String key=intent.getStringExtra("key");
            Toast.makeText(this.cordova.getActivity(),key,Toast.LENGTH_SHORT).show();
            this.callbackContext.success(key);
        }
     }
}
