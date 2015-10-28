package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PartyItemData;

/**
 * Created by Tacademy on 2015-10-28.
 */
public class UserPageView extends RelativeLayout {
    public UserPageView(Context context) {
        super(context);
        init();
    }

    public UserPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_user_header, this);
    }

    public void setItemData(PartyItemData data) {
    }
}
