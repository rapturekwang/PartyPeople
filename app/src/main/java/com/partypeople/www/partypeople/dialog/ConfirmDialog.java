package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.manager.NetworkManager;

/**
 * Created by kwang on 16. 1. 14..
 */
public class ConfirmDialog extends Dialog{
    public ConfirmDialog(final Context context, final Party party) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        TextView textView = (TextView)findViewById(R.id.text_description);
        textView.setText("모임 생성 후에는 수정이 불가합니다.\n모임을 만드시겠습니까?");

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().makeParty(getContext(), party, new NetworkManager.OnResultListener<PartyResult>() {
                    @Override
                    public void onSuccess(final PartyResult result) {
                        NetworkManager.getInstance().putGroupImages(getContext(), party.imageFiles, result.data.id, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result2) {
                                if (party.imageFile != null) {
                                    NetworkManager.getInstance().putGroupImage(getContext(), party.imageFile, result.data.id, new NetworkManager.OnResultListener<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            getContext().startActivity(intent);
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "사진 업로드가 실패하였습니다", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                            return;
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getContext().startActivity(intent);
                                }
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getContext(), "사진 업로드가 실패하였습니다", Toast.LENGTH_SHORT).show();
                                dismiss();
                                return;
                            }
                        });
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getContext(), "모임 생성이 실패하였습니다", Toast.LENGTH_SHORT).show();
                        dismiss();
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
}
