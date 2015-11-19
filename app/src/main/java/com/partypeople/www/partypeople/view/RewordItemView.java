package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class RewordItemView extends RelativeLayout implements Checkable {
    boolean isCheck = false;
    EditText priceView, rewordView;
    TextView numberView;

    public RewordItemView(Context context) {
        super(context);
        init();
    }

    public RewordItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isCheck != checked) {
            isCheck = checked;
            //drawCheck();
        }
    }

    @Override
    public void toggle() {
        isCheck = !isCheck;
    }

    private void init() {
        inflate(getContext(), R.layout.view_reword_item, this);
        priceView = (EditText)findViewById(R.id.price);
        rewordView = (EditText)findViewById(R.id.reword);
        numberView = (TextView)findViewById(R.id.number);
    }

    public void setItemData(PayMethod payMethod, int num) {
        priceView.setText(payMethod.price);
        rewordView.setText(payMethod.title);
        numberView.setText(num);
    }

//    private void drawCheck() {
//        if (isCheck) {
//            textView.setBackgroundColor(getResources().getColor(R.color.checked));
//        } else {
//            textView.setBackgroundColor(getResources().getColor(R.color.unchecked));
//        }
//    }
}