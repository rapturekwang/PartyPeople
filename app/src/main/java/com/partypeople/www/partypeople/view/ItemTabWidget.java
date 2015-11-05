package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.utils.Constants;

public class ItemTabWidget extends FrameLayout {

    public ItemTabWidget(Context context) {
        super(context);
        init();
    }

    OnTabChangeListener mListener;
    public void setOnTabChangeListener(OnTabChangeListener listener) {
        mListener = listener;
    }

    TabHost tabHost;
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_item_tabwidget, this);
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        String[] tabTitle = getResources().getStringArray(R.array.user_page_tab_name);
        for (int i = 0; i < Constants.NUM_OF_USER_PAGE_TAB; i++) {
            tabHost.addTab(tabHost.newTabSpec(Constants.TAB_IDS[i]).setIndicator(tabTitle[i]).setContent(R.id.tab1));
        }
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                String tag = Constants.TAB_IDS[currentIndex];
                if (!tag.equals(tabId) && mListener != null) {
                    mListener.onTabChanged(tabId);
                }
            }
        });
    }

    private int currentIndex;
    public void setCurrentTab(int index) {
        currentIndex = index;
        tabHost.setCurrentTab(index);
    }
}