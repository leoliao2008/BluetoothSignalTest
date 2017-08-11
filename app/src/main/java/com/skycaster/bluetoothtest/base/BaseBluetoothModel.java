package com.skycaster.bluetoothtest.base;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.skycaster.bluetoothtest.StaticData;

import java.io.IOException;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public class BaseBluetoothModel {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private BluetoothSocket mSocket;

    public BaseBluetoothModel() {
        mHandler=new Handler(Looper.getMainLooper());
    }

    public BluetoothManager getBluetoothManager(Activity activity){
        if(mBluetoothManager==null){
            mBluetoothManager=(BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        return mBluetoothManager;
    }

    public BluetoothAdapter getBluetoothAdapter(Activity activity){
        if(mBluetoothAdapter==null){
            mBluetoothAdapter = getBluetoothManager(activity).getAdapter();
        }
        return mBluetoothAdapter;
    }

    public boolean checkIfBluetoothAvailable(Activity activity){
        if(!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出本页面。");
            return false;
        }
        if(getBluetoothManager(activity) ==null){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出本页面。");
            return false;
        }
        if(getBluetoothAdapter(activity)==null){
            activity.onBackPressed();
            BaseApplication.showToast("设备无蓝牙功能，退出本页面。");
            return false;
        }
        return getBluetoothAdapter(activity).isEnabled();
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

    public Set<BluetoothDevice> getBondedDevices(Activity activity){
        return getBluetoothAdapter(activity).getBondedDevices();
    }

    public void disconnectDevice(BluetoothSocket socket) throws IOException {
        socket.close();
    }

    public void disconnectDevice() throws IOException {
        if(mSocket!=null){
            mSocket.close();
        }
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }


}
