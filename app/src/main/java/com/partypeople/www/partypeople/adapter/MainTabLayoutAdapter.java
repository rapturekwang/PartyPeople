package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.utils.Constant;
import com.partypeople.www.partypeople.view.MainTabLayoutFragment;

/**
 * Created by dongja94 on 2015-10-14.
 */
public class MainTabLayoutAdapter extends FragmentPagerAdapter {

    public MainTabLayoutAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainTabLayoutFragment.newInstance("Item : " + position);
    }

    @Override
    public int getCount() {
        return Constant.NUM_OF_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
