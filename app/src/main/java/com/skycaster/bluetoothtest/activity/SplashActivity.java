package com.skycaster.bluetoothtest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.skycaster.bluetoothtest.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void toServerActivity(View view) {
        ServerActivity.start(this);
        finish();
    }

    public void toClientActivity(View view) {
        ClientActivity.start(this);
        finish();
    }
}
