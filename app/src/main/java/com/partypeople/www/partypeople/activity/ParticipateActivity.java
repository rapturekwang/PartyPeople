package com.partypeople.www.partypeople.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;
import com.partypeople.www.partypeople.data.PayMethod;

public class ParticipateActivity extends AppCompatActivity {
    ListView listView;
    RewordViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayShowTitleEnabled(false);

        Button btn = (Button)findViewById(R.id.btn_pay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        for(int i=0; i<2; i++) {
            PayMethod payMethod = new PayMethod();
            payMethod.price = 10000;
            payMethod.title = "FREE 음료 1개, 스낵";
            mAdapter.add(payMethod);
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
