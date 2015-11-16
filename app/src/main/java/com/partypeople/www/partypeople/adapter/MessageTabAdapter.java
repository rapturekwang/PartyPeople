package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.FollowTabFragment;
import com.partypeople.www.partypeople.fragment.MessageTabFragment;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by Tacademy on 2015-11-16.
 */
public class MessageTabAdapter extends FragmentPagerAdapter {

    public MessageTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MessageTabFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return Constants.NUM_OF_MESSAGE_PAGE_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }
}
