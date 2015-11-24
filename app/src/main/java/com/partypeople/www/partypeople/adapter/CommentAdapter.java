package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.data.PayMethod;
import com.partypeople.www.partypeople.view.CommentView;
import com.partypeople.www.partypeople.view.RewordItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-23.
 */
public class CommentAdapter extends BaseAdapter{
    //public List<PayMethod> list = new ArrayList<PayMethod>();

    @Override
    public int getCount() {
        return 5;
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
        CommentView view;
        if(convertView == null) {
            view = new CommentView(parent.getContext());
        } else {
            view = (CommentView)convertView;
        }
        view.setItemData("test", 0);
        return view;
    }
}
