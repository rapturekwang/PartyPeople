package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.display.DetailOneFragment;
import com.partypeople.www.partypeople.display.DetailThreeFragment;
import com.partypeople.www.partypeople.display.DetailTwoFragment;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-26.
 */
public class DetailTabAdapter extends FragmentPagerAdapter {
    public DetailTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(DetailOneFragment.newInstance("Tab1"));
        fragments.add(DetailTwoFragment.newInstance("Tab2"));
        fragments.add(DetailThreeFragment.newInstance("Tab3"));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_DETAIL_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
