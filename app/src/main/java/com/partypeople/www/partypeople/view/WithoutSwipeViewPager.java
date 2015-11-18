package com.partypeople.www.partypeople.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class WithoutSwipeViewPager extends ViewPager {
    public WithoutSwipeViewPager(Context context) {
        super(context);
    }

    public WithoutSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
