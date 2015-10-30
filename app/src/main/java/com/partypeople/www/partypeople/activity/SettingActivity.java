package com.partypeople.www.partypeople.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.ChangePasswordFragment;
import com.partypeople.www.partypeople.fragment.FAQFragment;
import com.partypeople.www.partypeople.fragment.PushAlarmFragment;
import com.partypeople.www.partypeople.fragment.SettingFragment;
import com.partypeople.www.partypeople.fragment.TOSFragment;
import com.partypeople.www.partypeople.fragment.UserInfoPolicyFragment;


public class SettingActivity extends AppCompatActivity {
    Fragment[] fragmentList = {PushAlarmFragment.newInstance("one"),
            ChangePasswordFragment.newInstance("two"),
            TOSFragment.newInstance("three"),
            UserInfoPolicyFragment.newInstance("four"),
            FAQFragment.newInstance("five")};

    DrawerLayout mDrawer;
    TextView titleView;
    ActionBar actionBar;

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
                .addToBackStack(null)
                .commit();
        titleView.setText(SettingFragment.SETTING_MENUS[num]);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }
}
