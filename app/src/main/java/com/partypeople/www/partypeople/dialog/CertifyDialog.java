package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by kwang on 16. 1. 7..
 */
public class CertifyDialog extends Dialog{
    public CertifyDialog(final Context context, boolean success, int type) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_certify);

        String result = "에러";
        if(type== Constants.CERTIFY_ACCOUNT) {
            if (success) {
                result = "계좌 인증이 완료되었습니다.";
            } else {
                result = "본인의 계좌를 정확히 확인하시고\n다시 입력해주세요.";
            }
        } else if(type==Constants.CERTIFY_IDENTIFY) {
            if (success) {
                result = "본인 인증이 완료되었습니다.";
            } else {
                result = "본인 인증이 실패하였습니다.\n다시 시도해주세요.";
            }
        }

        TextView textView = (TextView)findViewById(R.id.text_description);
        textView.setText(result);

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
