package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.MainTabFragment;
import com.partypeople.www.partypeople.fragment.MakePartyChildFragment;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class MakePartyTwoTabAdapter extends FragmentPagerAdapter {

    public MakePartyTwoTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MakePartyChildFragment.newInstance("Item : " + position);
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_PARTY_TWO_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
