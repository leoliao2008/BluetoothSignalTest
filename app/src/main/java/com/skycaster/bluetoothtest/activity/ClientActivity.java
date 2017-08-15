package com.skycaster.bluetoothtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.presenter.ClientActivityPresenter;

public class ClientActivity extends AppCompatActivity {
    private ListView mListView;
    private ClientActivityPresenter mClientActivityPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ClientActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mClientActivityPresenter =new ClientActivityPresenter(this);
        mClientActivityPresenter.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mClientActivityPresenter.checkIfBluetoothOpen();
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.list_view);
    }

    public ListView getListView() {
        return mListView;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       mClientActivityPresenter.onRequestEnableBluetooth(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClientActivityPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_client_activity_discover_device:
                mClientActivityPresenter.startDiscoveringDevice();
                break;
        }
        return true;
    }
}
