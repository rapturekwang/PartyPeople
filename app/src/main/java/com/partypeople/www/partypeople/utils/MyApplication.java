package com.partypeople.www.partypeople.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.manager.FontManager;
import com.partypeople.www.partypeople.manager.NetworkManager;

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
        initImageLoader(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_img)
                .showImageForEmptyUri(R.drawable.profile_img)
                .showImageOnFail(R.drawable.profile_img)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .considerExifParams(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options)
                .imageDownloader(new HttpClientImageDownloaderWithHeader(context, NetworkManager.getInstance().getHttpClient()))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
