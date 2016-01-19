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
import com.viewpagerindicator.CirclePageIndicator;

public class IntroActivity extends AppCompatActivity {

    ViewPager pager;
    IntroPagerAdapter mAdapter;
    Button btnStart;
    CirclePageIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pager = (ViewPager)findViewById(R.id.container);
        mAdapter = new IntroPagerAdapter();
        pager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);

        btnStart = (Button)findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mAdapter.getCount() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void goToNext() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("startfrom", Constants.START_FROM_INTRO);
        startActivity(intent);
        finish();
    }
}
