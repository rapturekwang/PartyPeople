package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TabHost;

import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.view.ItemTabWidget;
import com.partypeople.www.partypeople.view.UserPagePartyItemView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {

    ArrayList<Party> items = new ArrayList<Party>();
    Context mContext;
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int TAB_WIDGET_VIEW = 0;
    private static final int TEXT_VIEW = 1;

    int currentIndex;
    TabHost.OnTabChangeListener mTabListener;

    public UserAdapter(Context context, int index,
                       TabHost.OnTabChangeListener listener) {
        mContext = context;
        currentIndex = index;
        mTabListener = listener;
    }

    public void add(Party data) {
        items.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TAB_WIDGET_VIEW;
            default:
                return TEXT_VIEW;
        }
    }

    @Override
    public Object getItem(int position) {
        switch (position) {
//            case 0:
            case 0:
                return null;
            default:
                return items.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (position) {
            case 0:
                ItemTabWidget itw = (ItemTabWidget)convertView;
                if (itw == null) {
                    itw = new ItemTabWidget(mContext);
                    itw.setOnTabChangeListener(mTabListener);
                }
                itw.setCurrentTab(currentIndex);
                return itw;
            default:
                UserPagePartyItemView view = (UserPagePartyItemView)convertView;
                if (view == null) {
                    view = new UserPagePartyItemView(parent.getContext());
                } else {
                    view = (UserPagePartyItemView)convertView;
                }
                view.setItemData(items.get(position-1));

                return view;
        }
    }

}
