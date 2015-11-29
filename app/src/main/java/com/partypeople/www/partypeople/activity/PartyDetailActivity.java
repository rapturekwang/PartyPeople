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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.DetailTabAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
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
    public Party party;
    SharePopupWindow popup;

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
                        setPagerHeight(2000);
                        break;
                    case 1:
                        setPagerHeight(108 + 197*party.pay_method.size());
                        break;
                    case 2:
                        setPagerHeight(228 + 240*party.comments.size());
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
                    Intent intent = new Intent(PartyDetailActivity.this, ParticipateActivity.class);
                    intent.putExtra("party", party);
                    startActivity(intent);
                } else {
                    Toast.makeText(PartyDetailActivity.this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView img_btn = (ImageView)findViewById(R.id.img_btn_share);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new SharePopupWindow(PartyDetailActivity.this);
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
//        ImageLoader.getInstance().displayImage(NetworkManager.getInstance().URL_SERVER + data.photo, imageParty, options);

        Intent intent = getIntent();
        party = (Party)intent.getSerializableExtra("party");

        titilView.setText(party.name);
        dateView.setText(dateUtil.changeToViewFormat(party.start_at, party.end_at));
        String[] array = party.location.split(" ");
        if(array.length==1)
            locationView.setText(array[0]);
        else
            locationView.setText(array[0] + " " + array[1]);
        priceView.setText(party.pay_method.get(0).price+"원");
        int progress = (int)((party.members.size()*party.pay_method.get(0).price)/party.expect_pay*100);
        progressView.setText(progress+"% 모금됨");
        progressBar.setProgress(progress);
        duedateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.pay_end_at) + "일 남음");
        totalPriceView.setText((int)party.expect_pay+"원");
    }

    private void initView() {
        titilView = (TextView)findViewById(R.id.text_title);
        dateView = (TextView)findViewById(R.id.text_date);
        locationView = (TextView)findViewById(R.id.text_location);
        priceView = (TextView)findViewById(R.id.text_price);
        totalPriceView = (TextView)findViewById(R.id.text_total_price);
        progressView = (TextView)findViewById(R.id.text_progress);
        duedateView = (TextView)findViewById(R.id.text_duedate);
        imageView = (ImageView)findViewById(R.id.image_party);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setPagerHeight(int height) {
        LayoutParams params = pager.getLayoutParams();
        params.height=height;
        pager.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        if(popup.isShowing())
            return;
        else
            super.onBackPressed();
    }
}
