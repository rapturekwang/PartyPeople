package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyOneActivity;
import com.partypeople.www.partypeople.data.PartyItemData;

/**
 * Created by Tacademy on 2015-10-28.
 */
public class MakePartyOneView extends ScrollView {
    Button btn;
    public MakePartyOneView(Context context) {
        super(context);
        init();
    }

    public MakePartyOneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_make_party_one, this);

//        btn = (Button)view.findViewById(R.id.btn_next);
//        btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    public void setItemData(PartyItemData data) {
    }
}
