package com.bais.amactplugin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.cordova.CordovaActivity;

public class WebViewActivity extends CordovaActivity {
    static Dialog dialog;
    static Activity activity2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
    }

    public static boolean showLoading() {
       
        return true;
    }

    public static boolean hideLoading() {
       
        return true;
    }
}

