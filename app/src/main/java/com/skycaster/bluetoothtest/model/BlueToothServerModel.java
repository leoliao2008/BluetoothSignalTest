package com.skycaster.bluetoothtest.model;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.skycaster.bluetoothtest.StaticData;
import com.skycaster.bluetoothtest.base.BaseBluetoothModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
import static android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;
import static android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE;
import static android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_STATE;
import static android.bluetooth.BluetoothAdapter.EXTRA_SCAN_MODE;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.SCAN_MODE_NONE;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public class BlueToothServerModel extends BaseBluetoothModel {

    private Callback mCallback;
    private Handler mHandler;
    private BluetoothStateChangeReceiver mBluetoothStateChangeReceiver;
    private DiscoverableModeStateReceiver mDiscoverableModeStateReceiver;
    private BluetoothSocket mBluetoothSocket;

    public BlueToothServerModel(Callback callback) {
        mCallback = callback;
        mHandler=new Handler(Looper.getMainLooper());
    }

    public void registerBluetoothStateChangeReceiver(Activity activity){
        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mBluetoothStateChangeReceiver=new BluetoothStateChangeReceiver();
        activity.registerReceiver(mBluetoothStateChangeReceiver,filter);
    }

    public void unRegisterBluetoothStateChangeReceiver(Activity activity){
        if(mBluetoothStateChangeReceiver!=null){
            activity.unregisterReceiver(mBluetoothStateChangeReceiver);
            mBluetoothStateChangeReceiver=null;
        }
    }

    private class BluetoothStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int newState = intent.getIntExtra(EXTRA_STATE,-99);
            int preState = intent.getIntExtra(EXTRA_PREVIOUS_STATE,-99);
            mCallback.onBluetoothStateChange(preState,newState);
        }
    }

    public void requestDiscoverable(Activity activity){
        Intent intent=new Intent(ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(EXTRA_DISCOVERABLE_DURATION,StaticData.RESULT_CODE_REQUEST_DICOVERABLE);
        activity.startActivity(intent);
        showLog("requestDiscoverable");
    }

    public void onRequestDiscoverable(int resultCode,Runnable onGranted){
        showLog("result code = "+resultCode);
        if(resultCode==StaticData.RESULT_CODE_REQUEST_DICOVERABLE){
            onGranted.run();
        }
    }

    public void registerDiscoverableModeStateReceiver(Activity activity){
        mDiscoverableModeStateReceiver=new DiscoverableModeStateReceiver();
        IntentFilter intentFilter=new IntentFilter(ACTION_SCAN_MODE_CHANGED);
        activity.registerReceiver(mDiscoverableModeStateReceiver,intentFilter);
    }

    public void unRegisterDiscoverableModeStateReceiver(Activity activity){
        if(mDiscoverableModeStateReceiver!=null){
            activity.unregisterReceiver(mDiscoverableModeStateReceiver);
        }
    }

    private class DiscoverableModeStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int newState = intent.getIntExtra(EXTRA_SCAN_MODE, SCAN_MODE_NONE);
            int preMode = intent.getIntExtra(EXTRA_PREVIOUS_SCAN_MODE, SCAN_MODE_NONE);
            mCallback.onDiscoverableModeChange(preMode,newState);
        }
    }

    public BluetoothServerSocket getServerSocket(Activity activity) throws IOException {
        return getBluetoothAdapter(activity).listenUsingRfcommWithServiceRecord(
                StaticData.SEVER_NAME,
                UUID.fromString(StaticData.UUID));
    }

    public void accept(final BluetoothServerSocket serverSocket){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while (true){
                    try {
                        mBluetoothSocket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                    if(mBluetoothSocket !=null){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onGettingBluetoothSocket(mBluetoothSocket);
                            }
                        });
                        break;
                    }else {
                        showLog("blue tooth socket is null, retry...");
                    }
                }
            }
        }).start();
    }

    public void cancelAccepting(BluetoothServerSocket serverSocket) throws IOException {
        serverSocket.close();
    }

    public void disconnect(BluetoothSocket socket) throws IOException {
        socket.close();
    }

    public void sendTestLine(String testLine,BluetoothSocket socket) throws IOException,NullPointerException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(testLine.getBytes());
    }


    public interface Callback{
        void onBluetoothStateChange(int preState,int newState);
        void onDiscoverableModeChange(int preState,int newState);
        void onGettingBluetoothSocket(BluetoothSocket bluetoothSocket);
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }

}
