package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * Created by kwang on 15. 12. 17..
 */
public class LeaveDialog extends Dialog {
    public LeaveDialog(final Context context, final FragmentManager fragmentManager) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_leave);

        ImageView btn = (ImageView)findViewById(R.id.btn_leave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(fragmentManager, "loading");
                NetworkManager.getInstance().leaveService(getContext(), PropertyManager.getInstance().getUser().id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getContext(), "회원탈퇴 되었습니다", Toast.LENGTH_SHORT).show();
                        PropertyManager.getInstance().logout();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getContext().startActivity(intent);
                        dialogFragment.dismiss();
                    }

                    @Override
                    public void onFail(int code) {
                        dialogFragment.dismiss();
                    }
                });
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
