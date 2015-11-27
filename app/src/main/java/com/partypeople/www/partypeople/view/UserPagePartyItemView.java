package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class UserPagePartyItemView extends RelativeLayout {
    DateUtil dateUtil = DateUtil.getInstance();
    Context mContext;
    DisplayImageOptions options;

    public UserPagePartyItemView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public UserPagePartyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView titleView, dateView, locationView, priceView, progressView, dueDateView;
    ImageView partyImgView;
    ProgressBar progressBar;
    Party mData;

    private void init() {
        inflate(getContext(), R.layout.view_user_page_party_item, this);
        partyImgView = (ImageView)findViewById(R.id.image_party);
        titleView = (TextView)findViewById(R.id.text_title);
        dateView = (TextView)findViewById(R.id.text_date);
        locationView = (TextView)findViewById(R.id.text_location);
        priceView = (TextView)findViewById(R.id.text_price);
        progressView = (TextView)findViewById(R.id.text_progress);
        dueDateView = (TextView)findViewById(R.id.text_duedate);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }

    public void setItemData(Party data) {
        mData = data;

        titleView.setText(data.name);
        dateView.setText(dateUtil.changeToViewFormat(data.start_at, data.end_at));
        dueDateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), data.pay_end_at) + "일 남음");
        String[] array = data.location.split(" ");
        if(array.length==1)
            locationView.setText(array[0]);
        else
            locationView.setText(array[0] + " " + array[1]);
        priceView.setText((int)data.expect_pay + "원");
        int progress = (int)((data.members.size()*data.pay_method.get(0).price)/data.expect_pay*100);
        progressView.setText(progress+"% 모금됨");
        progressBar.setProgress(progress);
        ImageLoader.getInstance().displayImage(NetworkManager.getInstance().URL_SERVER + data.photo, partyImgView, options);
    }
}
