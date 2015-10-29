package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.UserPageFragment;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class UserPageTabAdapter extends FragmentPagerAdapter {

    public UserPageTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return UserPageFragment.newInstance("Item : " + position);
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_USER_PAGE_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
