package com.partypeople.www.partypeople.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.partypeople.www.partypeople.data.Report;
import com.partypeople.www.partypeople.view.ReportChildItemView;
import com.partypeople.www.partypeople.view.ReportGroupItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class ReportAdapter extends BaseExpandableListAdapter {
    List<Report> reports = new ArrayList<Report>();

    @Override
    public int getGroupCount() {
        return reports.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(reports.get(groupPosition).answer!=null)
            return 1;
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return reports.get(groupPosition).question;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return reports.get(groupPosition).answer;
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
        ReportGroupItemView v;
        if (convertView != null) {
            v = (ReportGroupItemView)convertView;
        } else {
            v = new ReportGroupItemView(parent.getContext());
        }
        v.setGroupItem(reports.get(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ReportChildItemView v;
        if (convertView != null) {
            v = (ReportChildItemView)convertView;
        } else {
            v = new ReportChildItemView(parent.getContext());
        }
        v.setChildItem(reports.get(groupPosition).answer);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addReport(Report report) {
        reports.add(report);
        notifyDataSetChanged();
    }
}
