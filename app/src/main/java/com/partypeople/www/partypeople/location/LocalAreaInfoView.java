package com.partypeople.www.partypeople.location;

import android.content.Context;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class LocalAreaInfoView extends FrameLayout {
    public LocalAreaInfoView(Context context) {
        super(context);
        init();
    }

//    ImageView iconView;
//    TextView titleView, directorView;
//    LocalAreaInfo mItem;
//
//    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_local_info, this);
//        iconView = (ImageView)findViewById(R.id.image_icon);
//        titleView = (TextView)findViewById(R.id.text_title);
//        directorView = (TextView)findViewById(R.id.text_director);
//
    }

    public void setLocalAreaInfo(LocalAreaInfo item) {
//        titleView.setText(Html.fromHtml(item.title));
//        directorView.setText(item.director);
//
//        ImageLoader.getInstance().displayImage(item.image, iconView, options);
    }
}