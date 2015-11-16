package com.partypeople.www.partypeople.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MessageTabAdapter;
import com.partypeople.www.partypeople.fragment.MakePartyOneFragment;
import com.partypeople.www.partypeople.fragment.MakePartyThreeFragment;
import com.partypeople.www.partypeople.fragment.MakePartyTwoFragment;
import com.partypeople.www.partypeople.fragment.MessageFragment;
import com.partypeople.www.partypeople.fragment.ReplyFragment;
import com.partypeople.www.partypeople.utils.Constants;

public class MessageActivity extends AppCompatActivity {

    Fragment[] list = {null,
            MessageFragment.newInstance("two"),
            ReplyFragment.newInstance("three")};
    int currentFragment;
    TabLayout tabs;
    ViewPager pager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayShowTitleEnabled(false);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        MessageTabAdapter adpater = new MessageTabAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.message_page_tab_name);
        for (int i = 0; i < Constants.NUM_OF_MESSAGE_PAGE_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }

        init();
    }

    private void init() {
        currentFragment = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextFragment() {
        currentFragment++;
        if(currentFragment>0) {
            toolbar.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
        }
        if(currentFragment < list.length) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[currentFragment])
                    .addToBackStack(null)
                    .commit();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentFragment>0) {
            currentFragment--;
        }
        if(currentFragment==0) {
            toolbar.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
        }
        super.onBackPressed();
    }
}

