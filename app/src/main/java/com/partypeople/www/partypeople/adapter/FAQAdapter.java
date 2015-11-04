package com.partypeople.www.partypeople.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.partypeople.www.partypeople.view.FaqChildItemView;
import com.partypeople.www.partypeople.view.FaqGroupItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class FAQAdapter extends BaseExpandableListAdapter {
    public static final String[] FAQS = new String[] {
            "# 비밀번호를 분실했습니다.", "# 호스트의 계좌번호가 변경되었습니다.",
            "# 일이 생겨 모임참석을 취소하고 싶습니다.", "# 환불규정은 어떻게 되나요?"};

    public static final String[] ANSWERS = new String[] {
            "저런 저런....", "그렇군요....",
            "참여할땐 맘대로지만 취소할땐 아니란다.", "그런거 없음!"};

    @Override
    public int getGroupCount() {
        return FAQS.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return FAQS[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ANSWERS[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long)groupPosition << 32 | 0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)groupPosition << 32 | childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FaqGroupItemView v;
        if (convertView != null) {
            v = (FaqGroupItemView)convertView;
        } else {
            v = new FaqGroupItemView(parent.getContext());
        }
        v.setGroupItem(FAQS[groupPosition]);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FaqChildItemView v;
        if (convertView != null) {
            v = (FaqChildItemView)convertView;
        } else {
            v = new FaqChildItemView(parent.getContext());
        }
        v.setChildItem(ANSWERS[groupPosition]);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
