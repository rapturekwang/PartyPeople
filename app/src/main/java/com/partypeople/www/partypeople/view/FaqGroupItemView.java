package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class FaqGroupItemView extends FrameLayout {
    public FaqGroupItemView(Context context) {
        super(context);
        init();
    }
    TextView textView;
    private void init() {
        inflate(getContext(), R.layout.view_faq_group_item, this);
        textView = (TextView)findViewById(R.id.text);
    }

    public void setGroupItem(String text) {
        textView.setText(text);
    }

}
