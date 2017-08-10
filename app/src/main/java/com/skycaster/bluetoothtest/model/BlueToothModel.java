package com.skycaster.bluetoothtest.model;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.skycaster.bluetoothtest.StaticData;
import com.skycaster.bluetoothtest.base.BaseApplication;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 廖华凯 on 2017/8/10.
 */


/**
 * 教程 https://developer.android.google.cn/guide/topics/connectivity/bluetooth.html
 */
public class BlueToothModel {


    public void initBluetooth(Activity activity){
        if(!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出程序。");
            return;
        }
        BluetoothManager manager = getBluetoothManager(activity);
        if(manager==null){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出程序。");
            return;
        }
        BluetoothAdapter adapter = getBluetoothAdapter(activity);
        if(adapter==null){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出程序。");
            return;
        }
        if(!adapter.isEnabled()){
            requestEnableBluetooth(activity);
        }
    }

    public BluetoothManager getBluetoothManager(Activity activity){
        return (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public BluetoothAdapter getBluetoothAdapter(Activity activity){
        BluetoothManager manager = getBluetoothManager(activity);
        if(manager!=null){
            return manager.getAdapter();
        }
        return null;
    }

    public void requestEnableBluetooth(Activity activity){
        Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, StaticData.REQUEST_CODE_ENABLE_BLUETOOTH);
    }

    public boolean onRequestEnableBluetooth(int requestCode, int resultCode){
        if(requestCode==StaticData.REQUEST_CODE_ENABLE_BLUETOOTH){
            return resultCode==RESULT_OK;
        }
        return false;
    }

}
