package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Comment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;

/**
 * Created by Tacademy on 2015-11-23.
 */
public class CommentView extends RelativeLayout{
    TextView timeView, nameView, commentView;
    ImageView profileView;

    public CommentView(Context context) {
        super(context);
        init();
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_comment, this);
        timeView = (TextView)findViewById(R.id.text_time);
        nameView = (TextView)findViewById(R.id.text_name);
        commentView = (TextView)findViewById(R.id.text_comment);
        profileView = (ImageView)findViewById(R.id.img_profile);
    }

    public void setItemData(Comment data) {
        commentView.setText(data.comment);
        nameView.setText(data.from.name);
        int diffTime = DateUtil.getInstance().getDiffDay(data.created_at);
        if(diffTime == 0) {
            timeView.setText(DateUtil.getInstance().getDiffHour(data.created_at) + "시간 전");
        } else {
            timeView.setText(diffTime + "일 전");
        }
        if(data.participant) {
            nameView.append("   참여자");
        }
        GlideUrl glideUrl = null;
        if (data.from.has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + data.from.photo);
        } else if (!data.from.has_photo && data.from.provider.equals("facebook")) {
            glideUrl = new GlideUrl(data.from.photo);
        } else {
            profileView.setImageResource(R.drawable.default_profile);
        }
        if(glideUrl!=null) {
            Glide.with(getContext())
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .transform(new CircleTransform(getContext()))
                    .into(profileView);
        }
    }
}
