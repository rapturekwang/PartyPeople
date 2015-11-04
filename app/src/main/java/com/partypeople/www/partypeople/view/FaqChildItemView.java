package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class FaqChildItemView extends FrameLayout {
    public FaqChildItemView(Context context) {
        super(context);
        init();
    }
    TextView contentView;

    private void init() {
        inflate(getContext(), R.layout.view_faq_child_item, this);
        contentView = (TextView)findViewById(R.id.text);
    }

    public void setChildItem(String text) {
        contentView.setText(text);
    }
}
