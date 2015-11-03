package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class SettingItemView extends LinearLayout{
    public SettingItemView(Context context) {
    super(context);
    init();
}

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView nameView, versionView;
    ImageView imageView;

    private void init() {
        inflate(getContext(), R.layout.view_setting_item, this);
        nameView = (TextView)findViewById(R.id.text_name);
        versionView = (TextView)findViewById(R.id.text_version);
        imageView = (ImageView)findViewById(R.id.image);
    }

    public void setItemData(String name, String version, boolean image) {
        nameView.setText(name);
        versionView.setText(version);
        if(image == true) {
            imageView.setImageResource(R.drawable.my_icon);
        }
        imageView.setVisibility(GONE);
    }
}