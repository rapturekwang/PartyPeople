package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.DetailTabAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.popup.SharePopupWindow;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;

public class PartyDetailActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager pager;
    TextView titilView, dateView, locationView, priceView, totalPriceView, progressView, duedateView;
    CheckBox chboxView;
    ImageView imageView;
    ProgressBar progressBar;
    DateUtil dateUtil = DateUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        DetailTabAdapter adpater = new DetailTabAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);

        setPagerHeight(2100);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.detail_tab_name);
        for (int i = 0; i < Constants.NUM_OF_DETAIL_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPagerHeight(2100);
                        break;
                    case 1:
                        setPagerHeight(700);
                        break;
                    case 2:
                        setPagerHeight(1450);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        
        Button btn = (Button)findViewById(R.id.btn_parti);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PropertyManager.getInstance().isLogin()) {
                    startActivity(new Intent(PartyDetailActivity.this, ParticipateActivity.class));
                } else {
                    Toast.makeText(PartyDetailActivity.this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView img_btn = (ImageView)findViewById(R.id.img_btn_share);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePopupWindow popup = new SharePopupWindow(PartyDetailActivity.this);
                popup.setOutsideTouchable(true);
                popup.showAsDropDown(v, 0, -400);
            }
        });

        chboxView = (CheckBox)findViewById(R.id.chbox_img_bookmark);
        chboxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Party party = (Party)intent.getSerializableExtra("party");

        titilView.setText(party.name);
        dateView.setText(dateUtil.changeToViewFormat(party.date));
        locationView.setText(party.location);
        priceView.setText(party.expect_pay+"원");
        //totalPriceView.setText();
        progressView.setText("50%");
        progressBar.setProgress(50);
        duedateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.date) + "일 남음");
//        descriptionView.setText(party.description);
//        imageView
    }

    private void initView() {
        titilView = (TextView)findViewById(R.id.text_title);
        dateView = (TextView)findViewById(R.id.text_date);
        locationView = (TextView)findViewById(R.id.text_location);
        priceView = (TextView)findViewById(R.id.text_price);
        totalPriceView = (TextView)findViewById(R.id.text_total_price);
        progressView = (TextView)findViewById(R.id.text_progress);
        duedateView = (TextView)findViewById(R.id.text_duedate);
        //descriptionView = (TextView)findViewById(R.id.text_des);
        imageView = (ImageView)findViewById(R.id.image_party);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setPagerHeight(int height) {
        LayoutParams params = pager.getLayoutParams();
        params.height=height;
        pager.setLayoutParams(params);
    }
}
