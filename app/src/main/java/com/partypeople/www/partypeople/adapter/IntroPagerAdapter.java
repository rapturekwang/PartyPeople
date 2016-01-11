package com.partypeople.www.partypeople.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class IntroPagerAdapter extends PagerAdapter {

    int[] intro = {
            R.drawable.intro_0,
            R.drawable.intro_1,
            R.drawable.intro_2,
            R.drawable.intro_3
    };
    List<View> scrappedView = new ArrayList<View>();
    @Override
    public int getCount() {
        return intro.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if(scrappedView.size() > 0) {
            view = scrappedView.remove(0);
        } else {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_intro, container, false);
        }
        view.setBackgroundResource(intro[position]);
        container.addView(view);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
        scrappedView.add(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
