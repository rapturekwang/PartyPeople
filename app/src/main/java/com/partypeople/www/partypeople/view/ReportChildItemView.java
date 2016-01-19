package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Report;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class ReportChildItemView extends FrameLayout {
    public ReportChildItemView(Context context) {
        super(context);
        init();
    }
    TextView contentView;

    private void init() {
        inflate(getContext(), R.layout.view_report_child_item, this);
        contentView = (TextView)findViewById(R.id.text);
    }

    public void setChildItem(String answer) {
        contentView.setText(answer);
    }
}
