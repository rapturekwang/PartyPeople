package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 12. 18..
 */
public class NoticeBodyView extends RelativeLayout {
    public NoticeBodyView(Context context) {
        super(context);
        init();
    }

    public NoticeBodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView headerView, bodyView;

    private void init() {
        inflate(getContext(), R.layout.view_notice_body, this);
        headerView = (TextView)findViewById(R.id.text_header);
        bodyView = (TextView)findViewById(R.id.text_body);
    }

    public void setItemData(String header, String body) {
        headerView.setText(header);
        bodyView.setText(body);
    }
}
