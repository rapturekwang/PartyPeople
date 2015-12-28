package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;

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
        GlideUrl glideUrl = null;
        if (user.has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + user.photo);
        } else if (!user.has_photo && user.provider.equals("facebook")) {
            glideUrl = new GlideUrl(user.photo);
        }
        if(glideUrl!=null) {
            Glide.with(getContext())
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .transform(new CircleTransform(getContext()))
                    .into(imgView);
        }

        nameView.setText(user.name);
        if(user.address==null || user.address.equals("")) {
            addressView.setVisibility(View.GONE);
        } else {
            addressView.setText(user.address);
        }
        int owner = 0, memeber = 0;
        if(user.groups!=null) {
            for (int i = 0; i < user.groups.size(); i++) {
                if (user.groups.get(i).role.equals("OWNER"))
                    owner++;
                else if (user.groups.get(i).role.equals("MEMBER"))
                    memeber++;
            }
        }
        partysView.setText("개최 " + owner + " | 참여 " + memeber);
    }
}