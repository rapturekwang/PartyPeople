package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class RewordItemView extends RelativeLayout implements Checkable {
    boolean isCheck = false;
    EditText priceView, rewordView;
    ImageView numView;
    int[] ids = {
            R.drawable.contain_0,
            R.drawable.contain_1,
            R.drawable.contain_2,
            R.drawable.contain_3,
            R.drawable.contain_4,
    };

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
        numView = (ImageView)findViewById(R.id.image_num);
    }

    public void setItemData(PayMethod payMethod, int num) {
        priceView.setText(payMethod.price+"원");
        rewordView.setText(payMethod.title);
        numView.setImageResource(ids[num]);
    }

    public PayMethod getItemData() {
        PayMethod payMethod = new PayMethod();
        payMethod.title = rewordView.getText().toString();
        try {
            payMethod.price = Integer.parseInt(priceView.getText().toString());
        } catch (Exception e) {
            payMethod.price = -1;
        }
        return payMethod;
    }

//    private void drawCheck() {
//        if (isCheck) {
//            textView.setBackgroundColor(getResources().getColor(R.color.checked));
//        } else {
//            textView.setBackgroundColor(getResources().getColor(R.color.unchecked));
//        }
//    }
}