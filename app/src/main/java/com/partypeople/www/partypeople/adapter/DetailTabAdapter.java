package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.DetailOneFragment;
import com.partypeople.www.partypeople.fragment.DetailThreeFragment;
import com.partypeople.www.partypeople.fragment.DetailTwoFragment;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-26.
 */
public class DetailTabAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<Fragment>();

    public DetailTabAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(DetailOneFragment.newInstance("Tab1"));
        fragments.add(DetailTwoFragment.newInstance("Tab2"));
        fragments.add(DetailThreeFragment.newInstance("Tab3"));
    }

    @Override
    public Fragment getItem(int position) {
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
