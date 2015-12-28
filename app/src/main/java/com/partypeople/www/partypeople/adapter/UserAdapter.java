package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.view.UserPagePartyItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 12. 11..
 */
public class UserAdapter extends BaseAdapter{
    List<Party> partyList = new ArrayList<Party>();

    @Override
    public int getCount() {
        return partyList.size();
    }

    @Override
    public Object getItem(int position) {
        return partyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Party party) {
        partyList.add(party);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserPagePartyItemView view;
        if(convertView == null) {
            view = new UserPagePartyItemView(parent.getContext());
        } else {
            view = (UserPagePartyItemView)convertView;
        }
        view.setItemData(partyList.get(position));
        return view;
    }

    public void removeAll() {
        partyList.clear();
    }
}
