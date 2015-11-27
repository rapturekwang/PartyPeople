package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;

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

    public void setItemData(String test, int num) {
//        priceView.setText(payMethod.price);
//        rewordView.setText(payMethod.title);
//        commentView.setText("다음에 또 파티 열리면 좋겠어요\n주최자가 엄청 친절하고 맛있는 음식도 많아서 좋았어요");
        commentView.setText(test);
    }
}
