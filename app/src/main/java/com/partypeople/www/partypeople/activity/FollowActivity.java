package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.FollowTabAdapter;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.utils.Constants;

public class FollowActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager pager;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.container);
        FollowTabAdapter adpater = new FollowTabAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.follow_page_tab_name);
        for (int i = 0; i < Constants.NUM_OF_FOLLOW_PAGE_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public User getUser() {
        return user;
    }
}
