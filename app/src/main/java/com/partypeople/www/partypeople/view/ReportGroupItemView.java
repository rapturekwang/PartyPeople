package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Report;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class ReportGroupItemView extends FrameLayout {
    public ReportGroupItemView(Context context) {
        super(context);
        init();
    }
    TextView textView, categoryView;
    ImageView imgReport;

    private void init() {
        inflate(getContext(), R.layout.view_report_group_item, this);
        textView = (TextView)findViewById(R.id.text);
        categoryView = (TextView)findViewById(R.id.category);
        imgReport = (ImageView)findViewById(R.id.img_report);
    }

    public void setGroupItem(Report data) {
        textView.setText(data.question);
        categoryView.setText(data.category);
        if(data.has_photo) {
            imgReport.setVisibility(VISIBLE);
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            GlideUrl glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + data.photo);
            Glide.with(getContext())
                    .load(glideUrl)
                    .placeholder(Color.TRANSPARENT)
                    .error(Color.TRANSPARENT)
                    .into(imgReport);
        } else {
            imgReport.setVisibility(GONE);
        }
    }

}
