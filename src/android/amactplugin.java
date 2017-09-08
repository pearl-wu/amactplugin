package com.bais.amactplugin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class amactplugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private JSONObject params;

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
              callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
              Toast.makeText(this.cordova.getActivity(),webUrl,Toast.LENGTH_SHORT).show();    
              return true;
          }else if(action.equals("hide")) {
              LOG.d(LOG_TAG, "Hide Web View");
              hideWebView();
              JSONObject r = new JSONObject();
              r.put("responseCode", "ok");
              this.callbackContext.success(r);
            }
        return false;
     }
}
