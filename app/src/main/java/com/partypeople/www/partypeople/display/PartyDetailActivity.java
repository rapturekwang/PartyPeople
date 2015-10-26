package com.partypeople.www.partypeople.display;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.DetailTabAdapter;
import com.partypeople.www.partypeople.adapter.MainTabAdapter;
import com.partypeople.www.partypeople.data.PartyItemData;
import com.partypeople.www.partypeople.utils.Constants;

public class PartyDetailActivity extends AppCompatActivity {

    ImageView imageView;
    PartyItemView itemView;
    TextView desView;

    TabLayout tabs;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager_tab);
        DetailTabAdapter adpater = new DetailTabAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.detail_tab_name);
        for (int i = 0; i < Constants.NUM_OF_DETAIL_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }

//        imageView = (ImageView)findViewById(R.id.image_party_detail);
//        itemView = (PartyItemView)findViewById(R.id.custom_detail);
//        desView = (TextView)findViewById(R.id.text_des);

        initData();
    }

    private void initData() {
//        imageView.setImageResource(R.drawable.demo_img);
//        PartyItemData data = new PartyItemData();
//        data.title = "Come to House Party!";
//        data.date = "5월 7일 / 19:00-21:30";
//        data.partyImg = null;
//        data.location = "서울시 서초구";
//        data.price = "$25";
//        data.progress = 50;
//        data.progressText = data.progress+"% 모금됨";
//        data.dueDate = "7일 남음";
//        itemView.setVisible(View.GONE);
//        itemView.setItemData(data);
//        desView.setText("테스트 문자입니다. 가나다라마바사아자차카파타하 abcdefghijklmnopqrstuvwxyz 1234567890");
    }
}
