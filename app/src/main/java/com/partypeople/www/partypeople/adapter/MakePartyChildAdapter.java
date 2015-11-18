package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.view.PushAlarmItemView;
import com.partypeople.www.partypeople.view.RewordItemView;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class MakePartyChildAdapter extends BaseAdapter{
    int count = 1;
    Context mContext;

    public MakePartyChildAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RewordItemView view;
        if(convertView == null) {
            view = new RewordItemView(parent.getContext());
        } else {
            view = (RewordItemView)convertView;
        }
//        view.setItemData();
        return view;
    }
}
