package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.manager.NetworkManager;

/**
 * Created by kwang on 15. 12. 22..
 */
public class PasswordDialog extends Dialog {
    Party party;
    EditText editText;

    public PasswordDialog(Context context, final FragmentManager fragmentManager) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_password);

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(party.password)) {
                    final LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
                    dialogFragment.show(fragmentManager, "loading");
                    NetworkManager.getInstance().getParty(getContext(), party.id, new NetworkManager.OnResultListener<PartyResult>() {
                        @Override
                        public void onSuccess(PartyResult result) {
                            Intent i = new Intent(getContext(), PartyDetailActivity.class);
                            i.putExtra("party", result.data);
                            getContext().startActivity(i);
                            dialogFragment.dismiss();
                        }

                        @Override
                        public void onFail(String response) {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            dialogFragment.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        btn = (Button)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        editText = (EditText)findViewById(R.id.edit_password);
        Drawable wrappedDrawable = DrawableCompat.wrap(editText.getBackground());
        DrawableCompat.setTint(wrappedDrawable, getContext().getResources().getColor(android.R.color.white));
        editText.setBackgroundDrawable(wrappedDrawable);
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
