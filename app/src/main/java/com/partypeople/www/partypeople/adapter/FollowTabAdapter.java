package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.FollowTabFragment;
import com.partypeople.www.partypeople.fragment.MainTabFragment;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by kwang on 15. 11. 15..
 */
public class FollowTabAdapter extends FragmentPagerAdapter {

    public FollowTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FollowTabFragment.newInstance(position);
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
