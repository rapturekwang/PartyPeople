package com.partypeople.www.partypeople.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partypeople.www.partypeople.fragment.UserFragment;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 15. 12. 10..
 */
public class UserTabAdapter extends FragmentPagerAdapter {
        public UserTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            List<Fragment> fragments = new ArrayList<Fragment>();
            fragments.add(UserFragment.newInstance(0));
            fragments.add(UserFragment.newInstance(1));
            fragments.add(UserFragment.newInstance(2));
            return fragments.get(position);
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
