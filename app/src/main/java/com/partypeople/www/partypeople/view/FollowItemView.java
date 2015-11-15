package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

import org.w3c.dom.Text;

/**
 * Created by kwang on 15. 11. 15..
 */
public class FollowItemView extends RelativeLayout {
    public FollowItemView(Context context) {
        super(context);
        init();
    }

    public FollowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView nameView, addressView, partysView;
    ImageView imgView;

    private void init() {
        inflate(getContext(), R.layout.view_follow_item, this);
        nameView = (TextView)findViewById(R.id.text_name);
        addressView = (TextView)findViewById(R.id.text_address);
        partysView = (TextView)findViewById(R.id.text_partys);
        imgView = (ImageView)findViewById(R.id.img_profile);
    }

    public void setItemData(String name, String address, String partys, String imgUrl) {
        nameView.setText(name);
        addressView.setText(address);
        partysView.setText(partys);
    }
}