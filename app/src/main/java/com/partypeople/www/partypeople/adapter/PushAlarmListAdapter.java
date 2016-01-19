package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.view.PushAlarmItemView;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class PushAlarmListAdapter extends BaseAdapter {
    public static final String[] SETTING_MENUS = new String[] { "모금종료 알림",
            "모임 일시 알림 24시간전", "모임 참여자 알림", "관심 테마모임 알림", "신고/문의 알림" };

    @Override
    public int getCount() {
        return SETTING_MENUS.length;
    }

    @Override
    public Object getItem(int position) {
        return SETTING_MENUS[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PushAlarmItemView view;
        if(convertView == null) {
            view = new PushAlarmItemView(parent.getContext());
        } else {
            view = (PushAlarmItemView)convertView;
        }
        view.setItemData(SETTING_MENUS[position]);
        return view;
    }
}
