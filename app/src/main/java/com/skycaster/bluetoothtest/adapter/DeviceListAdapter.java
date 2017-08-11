package com.skycaster.bluetoothtest.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.base.MyBaseAdapter;
import com.skycaster.bluetoothtest.view_holder.BtDeviceViewHolder;

import java.util.ArrayList;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public class DeviceListAdapter extends MyBaseAdapter<BluetoothDevice,BtDeviceViewHolder> {
    private Callback mCallback;
    public DeviceListAdapter(ArrayList<BluetoothDevice> list, Context context, Callback callback) {
        super(list, context, R.layout.item_blue_tooth_device);
        mCallback=callback;
    }

    @Override
    protected void populateViewHolder(BtDeviceViewHolder holder, final BluetoothDevice device, final int position, View convertView, ViewGroup parent) {
        holder.getTv_name().setText(device.getName());
        holder.getTv_mac().setText(device.getAddress());
        holder.getBtn_connect().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressConnect(device,position);
            }
        });
    }

    @Override
    protected BtDeviceViewHolder instantiateViewHolder(View convertView) {
        return new BtDeviceViewHolder(convertView);
    }

    public interface Callback{
        void onPressConnect(BluetoothDevice device,int position);
    }
}
