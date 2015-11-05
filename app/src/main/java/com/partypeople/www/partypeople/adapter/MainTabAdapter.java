package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.fragment.MainTabFragment;

/**
 * Created by dongja94 on 2015-10-14.
 */
public class MainTabAdapter extends FragmentPagerAdapter {

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainTabFragment.newInstance("Item : " + position);
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_MAIN_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
