package com.skycaster.bluetoothtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.presenter.ServerActivityPresenter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerActivity extends AppCompatActivity {

    private ListView mListView;
    private ToggleButton btn_startTransmit;
    private ServerActivityPresenter mPresenter;
    public AtomicBoolean isInDiscoverableMode=new AtomicBoolean(false);
    public AtomicBoolean isRemoteDeviceConnected=new AtomicBoolean(false);

    public static void start(Context context) {
        Intent starter = new Intent(context, ServerActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        mListView= (ListView) findViewById(R.id.activity_server_list_view);
        btn_startTransmit= (ToggleButton) findViewById(R.id.activity_server_tgbtn_send_data);
        mPresenter = new ServerActivityPresenter(this);
        mPresenter.init();
    }

    public ListView getListView() {
        return mListView;
    }

    public ToggleButton getBtn_startTransmit() {
        return btn_startTransmit;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkIfBluetoothEnable();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_activity,menu);
        MenuItem itemDisconnect = menu.findItem(R.id.menu_server_disconnect);
        if(isRemoteDeviceConnected.get()){
            itemDisconnect.setVisible(true);
        }else {
            itemDisconnect.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_server_request_discoverable:
                if(!isInDiscoverableMode.get()){
                    mPresenter.requestDiscoverable();
                }else {
                    try {
                        mPresenter.cancelAccepting();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.menu_server_disconnect:
                try {
                    mPresenter.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showLog("onActivityResult result code = "+requestCode);
        showLog("onActivityResult request code = "+requestCode);
        mPresenter.onActivityResult(requestCode,resultCode);
    }

    private void showLog(String msg){
        Log.e(getClass().getSimpleName(),msg);
    }
}
