package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PartyItemData;

/**
 * Created by Tacademy on 2015-10-28.
 */
public class MakePartyThreeView extends ScrollView {
    public MakePartyThreeView(Context context) {
        super(context);
        init();
    }

    public MakePartyThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_make_party_three, this);
    }

    public void setItemData(PartyItemData data) {
    }
}
