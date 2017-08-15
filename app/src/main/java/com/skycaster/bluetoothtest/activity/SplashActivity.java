package com.skycaster.bluetoothtest.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.model.PermissionModel;

public class SplashActivity extends AppCompatActivity {

    private PermissionModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mModel=new PermissionModel();
    }

    public void toServerActivity(View view) {
        ServerActivity.start(this);
        finish();
    }

    public void toClientActivity(View view) {
        ClientActivity.start(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mModel.checkBlueToothPermissions(this)){
            mModel.requestBlueToothPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mModel.onPermissionResult(this,requestCode,permissions,grantResults);
    }
}
