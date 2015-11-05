package com.partypeople.www.partypeople.activity;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabWidget;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.UserFragment;
import com.partypeople.www.partypeople.utils.Constants;

public class UserActivity extends AppCompatActivity {

    FragmentTabHost tabHost;
    TabWidget tabWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        tabWidget = (TabWidget)findViewById(android.R.id.tabs);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        String[] tabTitle = getResources().getStringArray(R.array.user_page_tab_name);
        Bundle bundle;
        for (int i = 0; i < Constants.NUM_OF_USER_PAGE_TAB; i++) {
            bundle = new Bundle();
            bundle.putInt("index", i);
            tabHost.addTab(tabHost.newTabSpec(Constants.TAB_IDS[i]).setIndicator(tabTitle[i]), UserFragment.class, bundle);
        }
    }

    public void setTabCurrent(String tag) {
        tabHost.setCurrentTabByTag(tag);
    }

    public void setTabWidgetVisible(boolean isVisible) {
        if (isVisible) {
            tabWidget.setVisibility(View.VISIBLE);
        } else {
            tabWidget.setVisibility(View.GONE);
        }
    }

}