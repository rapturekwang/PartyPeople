package com.partypeople.www.partypeople.display;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.data.PartyItemData;
import com.partypeople.www.partypeople.R;

/**
 * Created by Tacademy on 2015-10-15.
 */
public class PartyItemView extends RelativeLayout {
    public PartyItemView(Context context) {
        super(context);
        init();
    }

    public PartyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView titleView, dateView, locationView, priceView, progressView, dueDateView;
    ImageView partyImgView;
    CheckBox bookMarkView;
    ProgressBar progressBar;
    PartyItemData mData;

    private void init() {
        inflate(getContext(), R.layout.view_party_item, this);
        partyImgView = (ImageView)findViewById(R.id.image_party);
        titleView = (TextView)findViewById(R.id.text_item_title);
        dateView = (TextView)findViewById(R.id.text_date);
        locationView = (TextView)findViewById(R.id.text_location);
        priceView = (TextView)findViewById(R.id.text_price);
        progressView = (TextView)findViewById(R.id.text_progress);
        dueDateView = (TextView)findViewById(R.id.text_duedate);
        bookMarkView = (CheckBox)findViewById(R.id.chbox_bookmark);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setItemData(PartyItemData data) {
        mData = data;
        if(data.partyImg != null){
            partyImgView.setImageDrawable(data.partyImg);
        }
        titleView.setText(data.title);
        dateView.setText(data.date);
        locationView.setText(data.location);
        priceView.setText(data.price);
        progressView.setText(data.progressText);
        dueDateView.setText(data.dueDate);
        progressBar.setProgress(data.progress);
        //bookMarkView.setChecked(data.bookMark);
    }
}
