package com.partypeople.www.partypeople.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by kwang on 16. 1. 14..
 */
public class ConfirmDialog extends Dialog{
    LoadingDialogFragment dialogFragment;
    Context context;

    public ConfirmDialog(final Context context, final Party party, final FragmentManager fragmentManager) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        TextView textView = (TextView)findViewById(R.id.text_description);
        textView.setText("모임 생성 후에는 수정이 불가합니다.\n모임을 만드시겠습니까?");

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(fragmentManager, "loading");
                NetworkManager.getInstance().makeParty(getContext(), party, new NetworkManager.OnResultListener<PartyResult>() {
                    @Override
                    public void onSuccess(final PartyResult result) {
                        NetworkManager.getInstance().putGroupImages(getContext(), party.imageFiles, result.data.id, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result2) {
                                if (party.imageFile != null) {
                                    NetworkManager.getInstance().putGroupImage(getContext(), party.imageFile, result.data.id, new NetworkManager.OnResultListener<String>() {
                                        @Override
                                        public void onSuccess(String result3) {
                                            successMakeParty(result.data);
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            failMakeParty("사진 업로드가 실패하였습니다");
                                            return;
                                        }
                                    });
                                } else {
                                    successMakeParty(result.data);
                                }
                            }

                            @Override
                            public void onFail(int code) {
                                failMakeParty("사진 업로드가 실패하였습니다");
                                return;
                            }
                        });
                    }

                    @Override
                    public void onFail(int code) {
                        failMakeParty("모임 생성이 실패하였습니다.");
                        return;
                    }
                });
            }
        });

        btn = (Button)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    void successMakeParty(Party party) {
        Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), PartyDetailActivity.class);
        intent.putExtra("startFrom", Constants.START_FROM_MAKE_PARTY);
        intent.putExtra("party", party);
        getContext().startActivity(intent);
        ((Activity)context).finish();
        dialogFragment.dismiss();
    }

    void failMakeParty(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dismiss();
        dialogFragment.dismiss();
    }
}
