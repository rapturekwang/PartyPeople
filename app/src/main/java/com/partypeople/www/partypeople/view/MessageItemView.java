package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by Tacademy on 2015-11-16.
 */
public class MessageItemView extends RelativeLayout

    {
        public MessageItemView(Context context) {
        super(context);
        init();
    }

        public MessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

        TextView nameView, timeView, messageView;
        ImageView imgView;

    private void init() {
        inflate(getContext(), R.layout.view_message_item, this);
        nameView = (TextView)findViewById(R.id.text_name);
        timeView = (TextView)findViewById(R.id.text_time);
        messageView = (TextView)findViewById(R.id.text_message);
        imgView = (ImageView)findViewById(R.id.img_profile);
    }

    public void setItemData(String name, String time, String message, String imgUrl) {
        nameView.setText(name);
        timeView.setText(time);
        messageView.setText(message);
    }
}
