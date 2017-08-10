package com.skycaster.bluetoothtest.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.skycaster.bluetoothtest.StaticData;
import com.skycaster.bluetoothtest.activity.MainActivity;
import com.skycaster.bluetoothtest.model.BlueToothModel;
import com.skycaster.bluetoothtest.model.PermissionModel;

import java.util.ArrayList;

import static android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_STATE;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_ON;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public class Presenter {
    private MainActivity mActivity;
    private ArrayList<String> mList=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private PermissionModel mPermissionModel;
    private BluetoothStateChangeReceiver mStateChangeReceiver;
    private BlueToothModel mBlueToothModel;

    public Presenter(MainActivity mActivity) {
        this.mActivity = mActivity;
        mPermissionModel=new PermissionModel();
        mBlueToothModel=new BlueToothModel();
    }

    public void initData(){
        mAdapter=new ArrayAdapter<String>(
                mActivity,
                android.R.layout.simple_list_item_1,
                mList
        );
        mActivity.getListView().setAdapter(mAdapter);

        if(mPermissionModel.checkBlueToothPermissions(mActivity)){
            mPermissionModel.requestBlueToothPermissions(mActivity);
        }
        registerReceiver();
        mBlueToothModel.initBluetooth(mActivity);
    }

    private void registerReceiver(){
        mStateChangeReceiver=new BluetoothStateChangeReceiver();
        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mStateChangeReceiver,filter);
    }
    
    private void unRegisterReceiver(){
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mStateChangeReceiver);
    }

    public void onPermissionRequestResult(int requestCode,String[] permissions,int[] results){
        mPermissionModel.onPermissionResult(mActivity,requestCode,permissions,results);
    }
    
    public void onDestroy(){
        unRegisterReceiver();
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode== StaticData.REQUEST_CODE_ENABLE_BLUETOOTH){
            if(mBlueToothModel.onRequestEnableBluetooth(requestCode, resultCode)){
                //// TODO: 2017/8/10
                showLog("REQUEST_CODE_ENABLE_BLUETOOTH == true");
            }else {
                //// TODO: 2017/8/10
                showLog("REQUEST_CODE_ENABLE_BLUETOOTH == false");
            }
        }

    }

    private class BluetoothStateChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String newState = intent.getStringExtra(EXTRA_STATE);
            String preState = intent.getStringExtra(EXTRA_PREVIOUS_STATE);
            showLog("preState = "+preState);
            showLog("newState = "+newState);
            if(preState.equals(STATE_ON)&&newState.equals(STATE_OFF)){
                //// TODO: 2017/8/10
                showLog("state on to state off");
            }else if(preState.equals(STATE_OFF)&&newState.equals(STATE_ON)){
                //// TODO: 2017/8/10
                showLog("state off to state on");
            }
        }
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }
}
