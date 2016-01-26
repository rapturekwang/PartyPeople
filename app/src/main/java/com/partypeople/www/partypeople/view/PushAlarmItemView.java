package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class PushAlarmItemView extends LinearLayout {
    public PushAlarmItemView(Context context) {
        super(context);
        init();
    }

    public PushAlarmItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView nameView;
    public SwitchCompat switchCompat;

    private void init() {
        inflate(getContext(), R.layout.view_push_alarm_item, this);
        nameView = (TextView)findViewById(R.id.text_name);
        switchCompat = (SwitchCompat)findViewById(R.id.switch_compat);
    }

    public void setItemData(String name) {
        nameView.setText(name);
    }
}