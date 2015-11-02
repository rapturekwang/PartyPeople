package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.IntroPagerAdapter;

public class IntroActivity extends AppCompatActivity {

    ViewPager pager;
    IntroPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new IntroPagerAdapter();
        pager.setAdapter(mAdapter);

        Button btn = (Button)findViewById(R.id.btn_skip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
