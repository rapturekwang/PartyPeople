package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Report;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class ReportGroupItemView extends FrameLayout {
    public ReportGroupItemView(Context context) {
        super(context);
        init();
    }
    TextView textView;
    TextView categoryView;
    private void init() {
        inflate(getContext(), R.layout.view_report_group_item, this);
        textView = (TextView)findViewById(R.id.text);
        categoryView = (TextView)findViewById(R.id.category);
    }

    public void setGroupItem(Report data) {
        textView.setText(data.question);
        categoryView.setText(data.category);
    }

}
