package com.partypeople.www.partypeople.adapter;

import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.view.SettingItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class SettingListAdapter extends BaseAdapter {
    public static final String[] SETTING_MENUS = new String[] { "푸쉬알림", "비밀번호 변경",
            "이용약관", "개인정보 취급방침", "자주 하는 질문", "버전정보",
            "로그아웃", "회원탈퇴", "rapturekwang@gmali.com" };
    public static final String[] VERSION = new String[] { null, null,
            null, null, null, Constants.VERSION,
            null, null, null };
    public static final boolean[] IMAGE = new boolean[] {
            true, true, true, true, true, false, false, false, false };

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
        SettingItemView view;
        if(convertView == null) {
            view = new SettingItemView(parent.getContext());
        } else {
            view = (SettingItemView)convertView;
        }
        view.setItemData(SETTING_MENUS[position], VERSION[position], IMAGE[position]);
        return view;
    }
}
