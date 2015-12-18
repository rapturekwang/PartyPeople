package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PayMethod;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.view.RewordItemView;

public class ParticipateActivity extends AppCompatActivity {
    Party party;
    LinearLayout layoutRewords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);

        Intent intent = getIntent();
        party = (Party)intent.getSerializableExtra("party");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        Button btn = (Button)findViewById(R.id.btn_pay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().participate(ParticipateActivity.this, party.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });

        layoutRewords = (LinearLayout)findViewById(R.id.linearlayout_reword);

        TextView textBtn = (TextView)findViewById(R.id.text_btn_tos);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipateActivity.this, NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_TOS);
                startActivity(intent);
            }
        });
        textBtn = (TextView)findViewById(R.id.text_btn_policy);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipateActivity.this, NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_POLICY);
                startActivity(intent);
            }
        });

        initData();
    }

    private void initData() {
        for(int i=0; i<party.pay_method.size(); i++) {
            RewordItemView rewordItemView = new RewordItemView(this);
            rewordItemView.setItemData(party.pay_method.get(i), i, false);
            layoutRewords.addView(rewordItemView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
