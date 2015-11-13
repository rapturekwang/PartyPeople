package com.partypeople.www.partypeople.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.partypeople.www.partypeople.manager.FontManager;

/**
 * Created by dongja94 on 2015-10-27.
 */
public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FacebookSdk.sdkInitialize(this);
        FontManager.setDefaultFont(this, "DEFAULT", "Roboto-Regular.ttf");
    }

    public static Context getContext() {
        return mContext;
    }
}
