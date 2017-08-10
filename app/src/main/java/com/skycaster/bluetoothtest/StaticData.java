package com.skycaster.bluetoothtest;

import android.Manifest;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public interface StaticData {
    String[] BLUE_TOOTH_PERMISSIONS=new String[]{Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_PRIVILEGED};
    int REQUEST_CODE_BLUE_TOOTH_PERMISSIONS=1997;
    int REQUEST_CODE_ENABLE_BLUETOOTH = 1998;
}
