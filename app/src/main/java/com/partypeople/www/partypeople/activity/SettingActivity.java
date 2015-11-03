package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.SettingListAdapter;
import com.partypeople.www.partypeople.fragment.ChangePasswordFragment;
import com.partypeople.www.partypeople.fragment.FAQFragment;
import com.partypeople.www.partypeople.fragment.PushAlarmFragment;
import com.partypeople.www.partypeople.fragment.SettingFragment;
import com.partypeople.www.partypeople.fragment.TOSFragment;
import com.partypeople.www.partypeople.fragment.UserInfoPolicyFragment;
import com.partypeople.www.partypeople.utils.Constants;


public class SettingActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    Fragment[] fragmentList = {PushAlarmFragment.newInstance("one"),
            ChangePasswordFragment.newInstance("two"),
            TOSFragment.newInstance("three"),
            UserInfoPolicyFragment.newInstance("four"),
            FAQFragment.newInstance("five")};

    DrawerLayout mDrawer;
    TextView titleView;
    ActionBar actionBar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);

        navigationView = (NavigationView)findViewById(R.id.navi);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.view_navigation_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, UserActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });

        init();
    }

    public void init() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, SettingFragment.newInstance("setting"), "setting").commit();
        titleView = (TextView)findViewById(R.id.text_title);
        titleView.setText("설정");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(getSupportFragmentManager().findFragmentByTag("setting").isVisible()) {
                mDrawer.openDrawer(GravityCompat.START);
            } else {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            titleView.setText("설정");
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        }
    }

    public void changeFragment(int num) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentList[num])
                .addToBackStack(null).commit();
        titleView.setText(SettingListAdapter.SETTING_MENUS[num]);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.home :
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.theme :
                startActivity(new Intent(SettingActivity.this, ThemeActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.party :
                startActivity(new Intent(SettingActivity.this, UserActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.make_party :
                startActivity(new Intent(SettingActivity.this, MakePartyActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.guide :
                intent = new Intent(SettingActivity.this, IntroActivity.class);
                intent.putExtra("startFrom", Constants.START_FROM_NAVIGATION);
                startActivity(intent);
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.setting :
                mDrawer.closeDrawer(GravityCompat.START);
                break;
        }
        return false;
    }
}
