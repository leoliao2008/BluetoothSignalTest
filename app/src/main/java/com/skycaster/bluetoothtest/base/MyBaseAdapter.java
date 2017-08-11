package com.skycaster.bluetoothtest.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by 廖华凯 on 2017/8/11.
 */

public abstract class MyBaseAdapter<Bean,VH extends BaseViewHolder> extends BaseAdapter {
    private ArrayList<Bean> mList;
    private Context mContext;
    private int mItemLayoutId;

    public MyBaseAdapter(ArrayList<Bean> list, Context context,int itemLayoutId) {
        mList = list;
        mContext = context;
        mItemLayoutId=itemLayoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if(convertView==null){
            convertView=View.inflate(mContext,mItemLayoutId,null);
            vh= instantiateViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh= (VH) convertView.getTag();
        }
        populateViewHolder(vh,mList.get(position),position,convertView,parent);
        return convertView;
    }

    protected abstract void populateViewHolder(VH vh, Bean bean, int position, View convertView, ViewGroup parent);


    protected abstract VH instantiateViewHolder(View convertView);
}
