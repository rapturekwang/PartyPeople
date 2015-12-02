package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;

/**
 * Created by kwang on 15. 11. 15..
 */
public class FollowItemView extends RelativeLayout {
    DisplayImageOptions options;
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
        addressView = (TextView)findViewById(R.id.text_tel);
        partysView = (TextView)findViewById(R.id.text_partys);
        imgView = (ImageView)findViewById(R.id.img_profile);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_img)
                .showImageForEmptyUri(R.drawable.profile_img)
                .showImageOnFail(R.drawable.profile_img)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .considerExifParams(true)
                .build();
    }

    public void setItemData(User user) {
        if(user.has_photo) {
            ImageLoader.getInstance().displayImage(NetworkManager.getInstance().URL_USERS + "/" + user.id + "/photo", imgView, options);
        }
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
        partysView.setText("개최 " + owner + " |참여 " + memeber);
    }
}