package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
    TextView priceView, rewordView;
    ImageView numView;
    int num;
    int[] ids = {
            R.drawable.contain_0,
            R.drawable.contain_1,
            R.drawable.contain_2,
            R.drawable.contain_3,
            R.drawable.contain_4,
    };
    int[] ids_checked = {
            R.drawable.contain_selected_0,
            R.drawable.contain_selected_1,
            R.drawable.contain_selected_2,
            R.drawable.contain_selected_3,
            R.drawable.contain_selected_4,
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
            drawCheck();
        }
    }

    @Override
    public void toggle() {
        isCheck = !isCheck;
    }

    private void init() {
        inflate(getContext(), R.layout.view_reword_item, this);
        numView = (ImageView)findViewById(R.id.image_num);
        priceView = (TextView)findViewById(R.id.price);
        rewordView = (TextView)findViewById(R.id.reword);
    }

    public void setItemData(PayMethod payMethod, int num) {
        if(payMethod!=null) {
            priceView.setText(payMethod.price + "원");
            rewordView.setText(payMethod.title);
        }
        this.num = num;
        if(num==-1) {
            numView.setVisibility(View.GONE);
        } else {
            numView.setImageResource(ids[num]);
        }
    }

    private void drawCheck() {
        if(num==-1) {
            return;
        } else {
            if (isCheck) {
                numView.setImageResource(ids_checked[num]);
            } else {
                numView.setImageResource(ids[num]);
            }
        }
    }
}