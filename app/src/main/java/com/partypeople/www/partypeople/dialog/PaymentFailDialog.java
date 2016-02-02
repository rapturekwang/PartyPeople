package com.partypeople.www.partypeople.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.IamportResult;
import com.partypeople.www.partypeople.manager.NetworkManager;

import java.net.URLDecoder;

/**
 * Created by kwang on 16. 1. 26..
 */
public class PaymentFailDialog extends Dialog {
    TextView nameView, reasonView, numberView;
    String uid = null;
    LoadingDialogFragment dialogFragment;

    public PaymentFailDialog(final Context context, final String response, FragmentManager fragmentManager) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_fail);

        dialogFragment = new LoadingDialogFragment();
        dialogFragment.show(fragmentManager, "loading");

        nameView = (TextView)findViewById(R.id.text_name);
        reasonView = (TextView)findViewById(R.id.text_reason);
        numberView = (TextView)findViewById(R.id.text_number);

        String[] arrays = response.split("&");
        for(int i=0;i<arrays.length;i++) {
            String[] values = arrays[i].split("=");
            if(values[0].equals("imp_uid"))
                uid = values[1];
        }


        NetworkManager.getInstance().getIamportToken(context, new NetworkManager.OnResultListener<IamportResult>() {
            @Override
            public void onSuccess(IamportResult result) {
                NetworkManager.getInstance().getPaymentResult(context, uid, result.response.access_token, new NetworkManager.OnResultListener<IamportResult>() {
                    @Override
                    public void onSuccess(IamportResult result) {
                        nameView.setText(result.response.name);
                        numberView.setText(uid + "\n" + result.response.merchant_uid);
                        reasonView.setText(result.response.fail_reason);

                        dialogFragment.dismiss();
                    }

                    @Override
                    public void onFail(String response) {
                        dialogFragment.dismiss();
                        Toast.makeText(getContext(), "결제정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(String response) {
                dialogFragment.dismiss();
                Toast.makeText(getContext(), "결제정보를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
