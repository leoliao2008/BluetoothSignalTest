package com.skycaster.bluetoothtest.base;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
