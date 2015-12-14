package com.partypeople.www.partypeople.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.DetailTabAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.fragment.DetailOneFragment;
import com.partypeople.www.partypeople.fragment.DetailThreeFragment;
import com.partypeople.www.partypeople.fragment.DetailTwoFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.popup.SharePopupWindow;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;

public class PartyDetailActivity extends AppCompatActivity {

    TabLayout tabs, fakeTabs;
    ViewPager pager;
    TextView titleView, dateView, locationView, priceView, totalPriceView, progressView, duedateView;
    CheckBox chboxView;
    ImageView imageView, imgPartyView;
    ProgressBar progressBar;
    DateUtil dateUtil = DateUtil.getInstance();
    public Party party;
    SharePopupWindow popup;
    DetailTabAdapter mAdpater;

    int[] ids = {0,
            R.drawable.main_theme_1,
            R.drawable.main_theme_2,
            R.drawable.main_theme_3,
            R.drawable.main_theme_4,
            R.drawable.main_theme_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);

        tabs = (TabLayout)findViewById(R.id.tabs);
        fakeTabs = (TabLayout)findViewById(R.id.fake_tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        mAdpater = new DetailTabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdpater);

        pager.setOffscreenPageLimit(Constants.NUM_OF_DETAIL_TAB-1);

        setPagerHeight(2800);

        tabs.setupWithViewPager(pager);
        fakeTabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        fakeTabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.detail_tab_name);
        for (int i = 0; i < Constants.NUM_OF_DETAIL_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
            fakeTabs.addTab(fakeTabs.newTab().setText(tabTitle[i]));
        }

        final ScrollView scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY()>tabs.getY()) {
                    fakeTabs.setVisibility(View.VISIBLE);
                } else {
                    fakeTabs.setVisibility(View.INVISIBLE);
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(2800);
                        ((DetailOneFragment)mAdpater.getItem(position)).changeHeight();
                        break;
                    case 1:
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(200 + 230 * party.pay_method.size());
                        ((DetailTwoFragment)mAdpater.getItem(position)).changeHeight();
                        break;
                    case 2:
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(350 + 300 * party.comments.size());
                        ((DetailThreeFragment)mAdpater.getItem(position)).changeHeight();
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
                    if(PropertyManager.getInstance().getUser().id.equals(party.owner.id)) {
                        Toast.makeText(PartyDetailActivity.this, "본인의 모임에는 참여신청을 할수 없습니다", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(PartyDetailActivity.this, ParticipateActivity.class);
                        intent.putExtra("party", party);
                        startActivity(intent);
                    }
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
        Intent intent = getIntent();
        party = (Party)intent.getSerializableExtra("party");

        Glide.with(this)
                .load(NetworkManager.getInstance().URL_SERVER + party.photo)
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)
                .into(imgPartyView);
        titleView.setText(party.name);
        titleView.setCompoundDrawablesWithIntrinsicBounds(ids[party.themes[0]], 0, 0, 0);
        dateView.setText(dateUtil.changeToViewFormat(party.start_at, party.end_at));
        String[] array = party.location.split(" ");
        if(array.length==1)
            locationView.setText(array[0]);
        else
            locationView.setText(array[0] + " " + array[1]);
        priceView.setText(party.pay_method.get(0).price + "원");
        int progress = (int)((party.members.size()*party.pay_method.get(0).price)/party.expect_pay*100);
        progressView.setText(progress+"% 모금됨");
        progressBar.setProgress(progress);
        duedateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.pay_end_at) + "일 남음");
        totalPriceView.setText((int) party.expect_pay + "원");
    }

    private void initView() {
        imgPartyView = (ImageView)findViewById(R.id.img_party);
        titleView = (TextView)findViewById(R.id.text_title);
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

//    @Override
//    public void onBackPressed() {
//        if(popup.isShowing())
//            return;
//        else
//            super.onBackPressed();
//    }
}
