package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;

/**
 * Created by kwang on 15. 12. 21..
 */
public class RewordItemEditView extends RelativeLayout {
    EditText priceView, rewordView;
    ImageView numView;
    RelativeLayout relativeLayout;
    int num;
    int[] ids = {
            R.drawable.contain_0,
            R.drawable.contain_1,
            R.drawable.contain_2,
            R.drawable.contain_3,
            R.drawable.contain_4,
    };

    public RewordItemEditView(Context context) {
        super(context);
        init();
    }

    public RewordItemEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_reword_edit, this);
        priceView = (EditText)findViewById(R.id.price);
        rewordView = (EditText)findViewById(R.id.reword);
        numView = (ImageView)findViewById(R.id.image_num);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
    }

    public void setItemData(PayMethod payMethod, int num) {
        this.num = num;
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

}