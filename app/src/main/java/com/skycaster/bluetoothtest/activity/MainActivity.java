package com.skycaster.bluetoothtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.skycaster.bluetoothtest.R;
import com.skycaster.bluetoothtest.presenter.Presenter;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPresenter=new Presenter(this);
        mPresenter.initData();
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.list_view);
    }

    public ListView getListView() {
        return mListView;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode,resultCode,data);
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
}
