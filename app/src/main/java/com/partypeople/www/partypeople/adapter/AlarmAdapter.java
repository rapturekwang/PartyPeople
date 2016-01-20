package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.view.AlarmView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 16. 1. 19..
 */
public class AlarmAdapter extends BaseAdapter {
    public List<String> list = new ArrayList<String>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(String comment) {
        list.add(0, comment);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmView view;
        if(convertView == null) {
            view = new AlarmView(parent.getContext());
        } else {
            view = (AlarmView)convertView;
        }
        view.setItemData(list.get(position));
        return view;
    }

    public void removeAll() {
        list.clear();
    }
}

