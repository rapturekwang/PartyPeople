package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.view.RewordItemView;
import com.partypeople.www.partypeople.view.SettingItemView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-23.
 */
public class RewordViewAdapter extends BaseAdapter{
    public List<PayMethod> list = new ArrayList<PayMethod>();
    boolean editable = true;

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

    public void add(PayMethod item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RewordItemView view;
        if(convertView == null) {
            view = new RewordItemView(parent.getContext());
        } else {
            view = (RewordItemView)convertView;
        }
        if(list.size()==1) {
            view.setItemData(list.get(position), -1);
        } else {
            view.setItemData(list.get(position), position);
        }

        return view;
    }
}
