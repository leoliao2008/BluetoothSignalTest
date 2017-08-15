package com.skycaster.bluetoothtest;

import android.Manifest;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public interface StaticData {
    String[] BLUE_TOOTH_PERMISSIONS=new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    int REQUEST_CODE_BLUE_TOOTH_PERMISSIONS=1997;
    int REQUEST_CODE_ENABLE_BLUETOOTH = 1998;
    String UUID="00001101-0000-1000-8000-00805F9B34FB";
    int REQUEST_CODE_DISCOVERABLE =1999;
    String SEVER_NAME="SkyCasterBluetoothServer";
    String TEST_LINE_1="$GPGGA,061923.00,2234.22210054,N,11356.24785338,E,5,05,2.8,48.923,M,-3.475,M,0.0,0693*67";
    String TEST_LINE_2="$GPGGA,061924.00,2234.22226763,N,11356.24775916,E,4,05,2.8,49.498,M,-3.475,M,0.0,0693*63";
    String TEST_LINE_3="$GPGGA,061925.00,2234.22237929,N,11356.24770956,E,5,05,2.8,49.872,M,-3.475,M,0.0,0693*6B";
    String [] TEST_LINES=new String[]{TEST_LINE_1,TEST_LINE_2,TEST_LINE_3};
    int RESULT_CODE_REQUEST_DICOVERABLE=300;
}
