package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 11. 15..
 */
public class MainTabHeaderView extends LinearLayout {
    public MainTabHeaderView(Context context) {
        super(context);
        init();
    }

    public MainTabHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView locationView;
    ImageView theme1, theme2, theme3, theme4;

    private void init() {
        inflate(getContext(), R.layout.view_maintab_header, this);
        locationView = (TextView)findViewById(R.id.text_location);
        theme1 = (ImageView)findViewById(R.id.image_theme1);
        theme2 = (ImageView)findViewById(R.id.image_theme2);
        theme3 = (ImageView)findViewById(R.id.image_theme3);
        theme4 = (ImageView)findViewById(R.id.image_theme4);
    }

    public void setItemData(String location, int theme1, int theme2, int theme3, int theme4) {
        locationView.setText(location);
        this.theme1.setImageDrawable(null);
        this.theme2.setImageDrawable(null);
        this.theme3.setImageDrawable(null);
        this.theme4.setImageDrawable(null);
    }
}
