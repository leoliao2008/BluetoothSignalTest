package com.skycaster.bluetoothtest.presenter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.skycaster.bluetoothtest.StaticData;
import com.skycaster.bluetoothtest.activity.ServerActivity;
import com.skycaster.bluetoothtest.base.BaseApplication;
import com.skycaster.bluetoothtest.model.BlueToothServerModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.bluetooth.BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_ON;
import static android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_TURNING_ON;

/**
 * Created by 廖华凯 on 2017/8/14.
 */

public class ServerActivityPresenter {

    private ServerActivity mActivity;
    private BlueToothServerModel mServerModel;
    private ArrayList<String> contents=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothServerSocket mBluetoothServerSocket;
    private ProgressDialog mProgressDialog;
    private AtomicBoolean isSendingData=new AtomicBoolean(false);

    public ServerActivityPresenter(ServerActivity activity) {
        mActivity = activity;
        mServerModel=new BlueToothServerModel(new BlueToothServerModel.Callback() {
            @Override
            public void onBluetoothStateChange(int preState, int newState) {
                if(preState== STATE_OFF&&newState==STATE_TURNING_ON){
                    BaseApplication.showToast("蓝牙被打开。");
                }else if(preState==STATE_ON&&newState==STATE_TURNING_OFF){
                    BaseApplication.showToast("蓝牙被关闭。");
                    try {
                        cancelAccepting();
                        disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onDiscoverableModeChange(int preState, int newState) {
                if(preState!=SCAN_MODE_CONNECTABLE_DISCOVERABLE&&newState==SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    mActivity.isInDiscoverableMode.compareAndSet(false,true);
                    BaseApplication.showToast("启动被搜索功能......");
                }else if(preState== SCAN_MODE_CONNECTABLE_DISCOVERABLE&&newState!= SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    mActivity.isInDiscoverableMode.compareAndSet(true,false);
                    BaseApplication.showToast("停止被搜索功能......");
                }
            }

            @Override
            public void onGettingBluetoothSocket(BluetoothSocket bluetoothSocket) {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                mBluetoothSocket=bluetoothSocket;
                BaseApplication.showToast("远程设备连接成功！");
            }
        });
    }

    public void init(){
        mAdapter=new ArrayAdapter<String>(
                mActivity,
                android.R.layout.simple_list_item_1,
                contents
        );
        mActivity.getListView().setAdapter(mAdapter);

        mActivity.getBtn_startTransmit().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startSendingData();
                }else {
                    stopSendingData();
                }
            }
        });

        registerReceivers();

        try {
            mBluetoothServerSocket=mServerModel.getServerSocket(mActivity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mBluetoothServerSocket!=null){
            startAccepting();
        }
    }

    private void registerReceivers(){
        mServerModel.registerBluetoothStateChangeReceiver(mActivity);
        mServerModel.registerDiscoverableModeStateReceiver(mActivity);
    }

    private void unRegisterReceivers(){
        mServerModel.unRegisterBluetoothStateChangeReceiver(mActivity);
        mServerModel.unRegisterDiscoverableModeStateReceiver(mActivity);
    }

    public void onDestroy(){
        unRegisterReceivers();
        try {
            cancelAccepting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkIfBluetoothEnable(){
        if(!mServerModel.checkIfBluetoothAvailable(mActivity)){
            mServerModel.requestEnableBluetooth(mActivity);
        }
    }

    private void onRequestEnableBluetooth(int requestCode,int resultCode){
        mServerModel.onRequestEnableBluetooth(
                requestCode,
                resultCode,
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.showToast("您启动了蓝牙。");

                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.showToast("蓝牙启动失败，程序将退出。");
                        mActivity.onBackPressed();
                    }
                }

        );
    }


    public void requestDiscoverable(){
//        mProgressDialog = ProgressDialog.show(
//                mActivity,
//                "等待连接",
//                "正在等待远程设备连接请求，请稍候......",
//                true,
//                true,
//                new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        try {
//                            mServerModel.cancelAccepting(mBluetoothServerSocket);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        BaseApplication.showToast("你取消了远程设备连接监听。");
//
//                    }
//                }
//        );
        mServerModel.requestDiscoverable(mActivity);
    }

    public void startSendingData(){
        isSendingData.compareAndSet(false,true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while (isSendingData.get()){
                    i++;
                    final String testLine = StaticData.TEST_LINES[i % StaticData.TEST_LINES.length];
                    try {
                        mServerModel.sendTestLine(testLine,mBluetoothSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        isSendingData.compareAndSet(true,false);
                        break;
                    } catch (NullPointerException e){
                        e.printStackTrace();
                        isSendingData.compareAndSet(true,false);
                        break;
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateListView(testLine);
                        }
                    });
                    SystemClock.sleep(1000);
                }
            }
        }).start();

    }

    private void updateListView(String msg){
        contents.add(msg);
        mAdapter.notifyDataSetChanged();
        mActivity.getListView().smoothScrollToPosition(Integer.MAX_VALUE);
    }

    public void stopSendingData(){
        isSendingData.compareAndSet(true,false);
    }

    private void onRequestDiscoverable(int resultCode){
        mServerModel.onRequestDiscoverable(
                resultCode,
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.showToast("当前设备可以被其他蓝牙设备扫描。");
                        startAccepting();
                    }
                }
        );
    }

    public void onActivityResult(int requestCode, int resultCode){
        showLog("resultCode = "+requestCode);
        onRequestDiscoverable(resultCode);
        onRequestEnableBluetooth(requestCode,resultCode);
    }

    public void disconnect() throws IOException {
        if(mBluetoothSocket!=null){
            mServerModel.disconnect(mBluetoothSocket);
            mBluetoothSocket=null;
            mActivity.isRemoteDeviceConnected.compareAndSet(true,false);
            mActivity.supportInvalidateOptionsMenu();
        }
    }

    public void startAccepting(){
        if(mBluetoothServerSocket!=null){
            mServerModel.accept(mBluetoothServerSocket);
        }
    }


    public void cancelAccepting() throws IOException {
        if(mBluetoothServerSocket!=null){
            mServerModel.cancelAccepting(mBluetoothServerSocket);
        }
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }
}
