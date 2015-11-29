package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;

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

    public void setItemData(User user) {
        nameView.setText(user.name);
        addressView.setText(user.address);
        int owner = 0, memeber = 0;
        if(user.groups!=null) {
            for (int i = 0; i < user.groups.size(); i++) {
                if (user.groups.get(i).role.equals("OWNER"))
                    owner++;
                else if (user.groups.get(i).role.equals("MEMBER"))
                    memeber++;
            }
        }
        partysView.setText("개최 " + owner + "|참여 " + memeber);
    }
}