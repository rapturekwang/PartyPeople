package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.view.CommentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-23.
 */
public class CommentAdapter extends BaseAdapter{
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
        list.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentView view;
        if(convertView == null) {
            view = new CommentView(parent.getContext());
        } else {
            view = (CommentView)convertView;
        }
        view.setItemData(list.get(position), 0);
        return view;
    }
}
