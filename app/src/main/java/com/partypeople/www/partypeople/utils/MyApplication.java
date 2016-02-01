package com.partypeople.www.partypeople.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.partypeople.www.partypeople.manager.FontManager;
import com.tsengvn.typekit.Typekit;

/**
 * Created by dongja94 on 2015-10-27.
 */
public class MyApplication extends MultiDexApplication{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FacebookSdk.sdkInitialize(this);
        FontManager.setDefaultFont(this, "DEFAULT", "Spoqa_Han_Sans_Regular.ttf");
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "Spoqa_Han_Sans_Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "Spoqa_Han_Sans_Bold_win_subset.ttf"));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
