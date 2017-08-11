package com.skycaster.bluetoothtest.base;

import android.view.View;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public abstract class BaseViewHolder {
    private View contentView;

    public BaseViewHolder(View contentView) {
        this.contentView = contentView;
        initChildViews(contentView);
    }

    protected abstract void initChildViews(View contentView);

    public View getContentView(){
        return contentView;
    }

    public View findViewById(int id){
        return contentView.findViewById(id);
    }

}
