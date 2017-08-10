package com.skycaster.bluetoothtest.model;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.skycaster.bluetoothtest.utils.AlertDialogUtil;
import com.skycaster.bluetoothtest.StaticData;

/**
 * Created by 廖华凯 on 2017/8/10.
 */

public class PermissionModel {

    public boolean checkBlueToothPermissions(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String p: StaticData.BLUE_TOOTH_PERMISSIONS){
                int result = activity.checkSelfPermission(p);
                if(result!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    public void requestBlueToothPermissions(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(StaticData.BLUE_TOOTH_PERMISSIONS,StaticData.REQUEST_CODE_BLUE_TOOTH_PERMISSIONS);
        }
    }

    public void onPermissionResult(final Activity activity, int requestCode,String[] permissions, int[] grantedResults){
        if(requestCode!=StaticData.REQUEST_CODE_BLUE_TOOTH_PERMISSIONS){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(int i=0,len=permissions.length;i<len;i++){
                if(grantedResults[i]!=PackageManager.PERMISSION_GRANTED){
                    if(activity.shouldShowRequestPermissionRationale(permissions[i])){
                        AlertDialogUtil.showDialog(
                                activity,
                                "为了保证程序正常运行，需要获取蓝牙权限，点击确定重新申请，点击取消退出此页面",
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        requestBlueToothPermissions(activity);
                                    }
                                },
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.onBackPressed();
                                    }
                                },
                                false

                        );
                    }else {
                        AlertDialogUtil.showDialog(
                                activity,
                                "您已经禁用了蓝牙权限，请到系统设置中为本程序分配蓝牙权限。",
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.onBackPressed();
                                    }
                                },
                                null,
                                true
                        );
                    }
                }
            }
        }
    }
}
