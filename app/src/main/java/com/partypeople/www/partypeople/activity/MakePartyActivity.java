package com.partypeople.www.partypeople.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.MakePartyOneFragment;
import com.partypeople.www.partypeople.fragment.MakePartyThreeFragment;
import com.partypeople.www.partypeople.fragment.MakePartyTwoFragment;

public class MakePartyActivity extends AppCompatActivity {

    Fragment[] list = {MakePartyOneFragment.newInstance("one"),
            MakePartyTwoFragment.newInstance("two"),
            MakePartyThreeFragment.newInstance("three")};
    String[] stringList = {"모임 설명", "모임 모금 방식", "모임 호스트 정보"};
    int currentFragment;

    TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_party);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayShowTitleEnabled(false);

        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
        currentFragment = 0;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, list[currentFragment]).commit();
        titleView = (TextView)findViewById(R.id.text_title);
        titleView.setText(stringList[currentFragment]);
    }

    public void nextFragment() {
        currentFragment++;
        if(currentFragment < list.length) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[currentFragment])
                    .addToBackStack(null)
                    .commit();
            titleView.setText(stringList[currentFragment]);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        currentFragment--;
        titleView.setText(stringList[currentFragment]);
        super.onBackPressed();
    }
}
