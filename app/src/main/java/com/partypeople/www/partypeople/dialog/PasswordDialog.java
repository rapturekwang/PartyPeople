package com.partypeople.www.partypeople.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;

/**
 * Created by kwang on 15. 12. 22..
 */
public class PasswordDialog extends Dialog {
    Party party;
    EditText editText;

    public PasswordDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_password);

        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(party.password)) {
                    Intent i = new Intent(getContext(), PartyDetailActivity.class);
                    i.putExtra("party", party);
                    getContext().startActivity(i);
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
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
