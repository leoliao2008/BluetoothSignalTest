package com.skycaster.bluetoothtest.presenter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.skycaster.bluetoothtest.StaticData;
import com.skycaster.bluetoothtest.activity.ClientActivity;
import com.skycaster.bluetoothtest.adapter.DeviceListAdapter;
import com.skycaster.bluetoothtest.base.BaseApplication;
import com.skycaster.bluetoothtest.model.BlueToothClientModel;
import com.skycaster.bluetoothtest.model.PermissionModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public class ClientActivityPresenter {
    private ClientActivity mActivity;
    private ArrayList<BluetoothDevice> mDevices=new ArrayList<>();
    private DeviceListAdapter mAdapter;
    private PermissionModel mPermissionModel;
    private BlueToothClientModel mBlueToothClientModel;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;

    public ClientActivityPresenter(final ClientActivity mActivity) {
        this.mActivity = mActivity;
        mHandler=new Handler();
        mPermissionModel=new PermissionModel();
        mBlueToothClientModel =new BlueToothClientModel(new BlueToothClientModel.Callback() {
            @Override
            public void onBluetoothStateChange(int preState, int newState) {
                if(preState== BluetoothAdapter.STATE_ON &&newState==BluetoothAdapter.STATE_TURNING_OFF){
                    mBlueToothClientModel.cancelDiscoveringDevice(mActivity);
                    try {
                        mBlueToothClientModel.disconnectDevice();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BaseApplication.showToast("蓝牙模块已关闭，终止本次蓝牙通信。");
                }

            }

            @Override
            public void onStartDiscoveringDevices() {
                mProgressDialog = ProgressDialog.show(
                        mActivity,
                        "搜索设备",
                        "正在搜索蓝牙设备,请稍候...",
                        true,
                        true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mBlueToothClientModel.cancelDiscoveringDevice(mActivity);
                                BaseApplication.showToast("您终止了本次扫描。");
                            }
                        }
                );

            }

            @Override
            public void onCancelDiscoveringDevices() {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                mBlueToothClientModel.unRegisterDiscoverReceiver(mActivity);

            }

            @Override
            public void onDeviceDiscovered(BluetoothClass bluetoothClass, BluetoothDevice bluetoothDevice) {
                BaseApplication.showToast("找到了设备 "+bluetoothDevice.getName());
                updateDeviceList(bluetoothDevice);
            }

            @Override
            public void onStartConnectingDevice(BluetoothDevice device) {
                mProgressDialog=ProgressDialog.show(
                        mActivity,
                        "设备连接",
                        "正在连接蓝牙设备 " + device.getName() + " ,请稍候......",
                        true,
                        true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                try {
                                    mBlueToothClientModel.disconnectDevice();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                BaseApplication.showToast("您终止了本次连接。");

                            }
                        }
                );

            }

            @Override
            public void onFailToConnectDevice(BluetoothDevice device) {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }

                BaseApplication.showToast("连接失败。");
            }

            @Override
            public void onDeviceConnected(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                BaseApplication.showToast("连接成功。");
            }

            @Override
            public void onDisconnectDevice(BluetoothDevice device) {
                BaseApplication.showToast("连接取消。");
            }



            @Override
            public void onDataObtained(byte[] data, int dataLen) {

            }
        });
    }

    private void updateDeviceList(BluetoothDevice bluetoothDevice) {
        if(checkIfDuplicate(bluetoothDevice)){
            return;
        }
        mDevices.add(bluetoothDevice);
        mAdapter.notifyDataSetChanged();
        mActivity.getListView().smoothScrollToPosition(Integer.MAX_VALUE);
    }

    private boolean checkIfDuplicate(BluetoothDevice device){
        for (int i=0;i<mDevices.size();i++){
            BluetoothDevice temp = mDevices.get(i);
            if(temp.getAddress().equals(device.getAddress())){
                return true;
            }
        }
        return false;
    }

    public void initData(){
        mAdapter=new DeviceListAdapter(mDevices, mActivity, new DeviceListAdapter.Callback() {
            @Override
            public void onPressConnect(BluetoothDevice device, int position) {
                showLog("onPressConnect");
                mBlueToothClientModel.connectToServer(device, UUID.fromString(StaticData.UUID));
            }
        });
        mActivity.getListView().setAdapter(mAdapter);

        registerReceivers();
    }

    public void checkIfBluetoothOpen(){
        if(!mBlueToothClientModel.checkIfBluetoothAvailable(mActivity)){
            mBlueToothClientModel.requestEnableBluetooth(mActivity);
        }
    }

    private void registerReceivers(){
        mBlueToothClientModel.registerBluetoothStateChangeReceiver(mActivity);
    }
    
    private void unRegisterReceivers(){
        mBlueToothClientModel.unRegisterBluetoothStateChangeReceiver(mActivity);
        mBlueToothClientModel.unRegisterDiscoverReceiver(mActivity);
    }
    
    public void onDestroy(){
        unRegisterReceivers();
    }

    public void onRequestEnableBluetooth(int requestCode,int resultCode,Intent data){
        mBlueToothClientModel.onRequestEnableBluetooth(
                requestCode,
                resultCode,
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.showToast("蓝牙启动成功。");
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.showToast("蓝牙启动被拒，程序将退出。");
                        mActivity.onBackPressed();
                    }
                });
    }

    public void startDiscoveringDevice(){
        mBlueToothClientModel.cancelDiscoveringDevice(mActivity);
        try {
            mBlueToothClientModel.disconnectDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDevices.clear();
        mDevices.addAll(mBlueToothClientModel.getBondedDevices(mActivity));
        mBlueToothClientModel.registerDiscoverReceiver(mActivity);
        mBlueToothClientModel.startDiscoveringDevice(mActivity);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBlueToothClientModel.cancelDiscoveringDevice(mActivity);
            }
        },24000);
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }
}
