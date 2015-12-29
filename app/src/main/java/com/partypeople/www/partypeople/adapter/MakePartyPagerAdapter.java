package com.partypeople.www.partypeople.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.MakePartyOneFragment;
import com.partypeople.www.partypeople.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-14.
 */
public class MakePartyPagerAdapter extends PagerAdapter {
    Context mContext;
    Fragment mFragment;
    private List<View> views = new ArrayList<View>();

    public MakePartyPagerAdapter(Context context, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        return index;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public int addView (View v) {
        return addView(v, views.size());
    }

    public int addView (View v, int position) {
        views.add(position, v);
        notifyDataSetChanged();
        return position;
    }

    public int removeView(ViewPager pager, View v) {
        return removeView(pager, views.indexOf(v));
    }

    public int removeView(ViewPager pager, int position) {
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);

        return position;
    }

    public View getView (int position) {
        return views.get(position);
    }
}