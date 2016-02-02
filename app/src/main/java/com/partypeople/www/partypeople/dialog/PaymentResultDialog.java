package com.partypeople.www.partypeople.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.IamportResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;

/**
 * Created by kwang on 16. 1. 26..
 */
public class PaymentResultDialog extends Dialog {
    TextView nameView, priceView, buyerView, emailView, telView, timeView, confirmView, receiptView;
    String uid;
    LoadingDialogFragment dialogFragment;

    public PaymentResultDialog(final Context context, String response, FragmentManager fragmentManager) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_result);

        dialogFragment = new LoadingDialogFragment();
        dialogFragment.show(fragmentManager, "loading");

        nameView = (TextView)findViewById(R.id.text_name);
        priceView = (TextView)findViewById(R.id.text_price);
        buyerView = (TextView)findViewById(R.id.text_buyer);
        emailView = (TextView)findViewById(R.id.text_email);
        telView = (TextView)findViewById(R.id.text_tel);
        timeView = (TextView)findViewById(R.id.text_time);
        confirmView = (TextView)findViewById(R.id.text_confirm_num);
        receiptView = (TextView)findViewById(R.id.text_receipt);

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
                        priceView.setText(result.response.amount + " 원");
                        buyerView.setText(result.response.buyer_name);
                        emailView.setText(result.response.buyer_email);
                        telView.setText(result.response.buyer_tel);
                        timeView.setText(DateUtil.getInstance().getPaymentTime(result.response.pg_tid));
                        confirmView.setText(result.response.apply_num);
                        receiptView.setText(result.response.receipt_url);

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
                ((Activity)context).finish();
            }
        });
    }
}
