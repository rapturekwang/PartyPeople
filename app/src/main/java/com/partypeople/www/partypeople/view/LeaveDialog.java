package com.partypeople.www.partypeople.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;

/**
 * Created by kwang on 15. 12. 17..
 */
public class LeaveDialog extends Dialog {
    public LeaveDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_leave);

        ImageView btn = (ImageView)findViewById(R.id.btn_leave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "회원탈퇴", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btn = (ImageView)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
