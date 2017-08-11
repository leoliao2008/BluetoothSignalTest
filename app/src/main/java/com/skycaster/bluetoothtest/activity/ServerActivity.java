package com.skycaster.bluetoothtest.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skycaster.bluetoothtest.R;

public class ServerActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ServerActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
    }
}
