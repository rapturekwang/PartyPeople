package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.IntroPagerAdapter;
import com.partypeople.www.partypeople.utils.Constants;

public class IntroActivity extends AppCompatActivity {

    ViewPager pager;
    IntroPagerAdapter mAdapter;
    int mStartFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new IntroPagerAdapter();
        pager.setAdapter(mAdapter);

        Intent intent = getIntent();
        mStartFrom = intent.getExtras().getInt("startFrom");

        Button btn = (Button)findViewById(R.id.btn_skip);
        if(mStartFrom == Constants.START_FROM_SPLASH) {
            btn.setText("건너뛰기");
        } else if (mStartFrom == Constants.START_FROM_NAVIGATION) {
            btn.setText("메인으로");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStartFrom == Constants.START_FROM_SPLASH) {
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    intent.putExtra("startfrom", Constants.START_FROM_INTRO);
                    startActivity(intent);
                    finish();
                } else if (mStartFrom == Constants.START_FROM_NAVIGATION) {
                    finish();
                }
            }
        });
    }
}
