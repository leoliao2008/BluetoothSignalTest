package com.skycaster.bluetoothtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.base.BaseApplication;
import com.skycaster.bluetoothtest.presenter.Presenter;

public class ClientActivity extends AppCompatActivity {
    private ListView mListView;
    private Presenter mPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ClientActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPresenter=new Presenter(this);
        mPresenter.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkIfBluetoothOpen();
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.list_view);
    }

    public ListView getListView() {
        return mListView;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean enableBluetooth = mPresenter.onRequestEnableBluetooth(requestCode, resultCode, data);
        if(enableBluetooth){
            BaseApplication.showToast("你开启了蓝牙。");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onPermissionRequestResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_discover_device:
                mPresenter.startDiscoveringDevice();
                break;
        }
        return true;
    }
}
