package com.skycaster.bluetoothtest.view_holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.base.BaseViewHolder;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public class BtDeviceViewHolder extends BaseViewHolder {
    private TextView tv_mac;
    private TextView tv_name;
    private Button btn_connect;

    public BtDeviceViewHolder(View contentView) {
        super(contentView);
    }

    @Override
    protected void initChildViews(View contentView) {
        tv_mac= (TextView) findViewById(R.id.item_bt_device_tv_mac);
        btn_connect= (Button) findViewById(R.id.item_bt_device_btn_connect);
        tv_name= (TextView) findViewById(R.id.item_bt_device_tv_name);
    }

    public TextView getTv_mac() {
        return tv_mac;
    }

    public Button getBtn_connect() {
        return btn_connect;
    }

    public TextView getTv_name() {
        return tv_name;
    }
}
