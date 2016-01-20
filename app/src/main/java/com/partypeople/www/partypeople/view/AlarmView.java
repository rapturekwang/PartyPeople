package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;


/**
 * Created by kwang on 16. 1. 19..
 */
public class AlarmView extends RelativeLayout {
    TextView timeView, contentsView;
    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_alarm, this);
        timeView = (TextView)findViewById(R.id.text_time);
        contentsView = (TextView)findViewById(R.id.text_contents);
    }

    public void setItemData(String data) {
        timeView.setText("1시간 전");
        contentsView.setText(data);
    }
}
