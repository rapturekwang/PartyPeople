package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.data.PartyItemData;
import com.partypeople.www.partypeople.display.PartyItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-15.
 */
public class MainFragmentAdapter extends BaseAdapter {
    List<PartyItemData> items = new ArrayList<PartyItemData>();

    public void add(PartyItemData item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        PartyItemView view;
        if(convertView == null) {
            view = new PartyItemView(parent.getContext());
        } else {
            view = (PartyItemView)convertView;
        }
        view.setItemData(items.get(position));
        return view;
    }
}
