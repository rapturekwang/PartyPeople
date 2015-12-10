package com.partypeople.www.partypeople.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * Created by kwang on 15. 12. 9..
 */
public class CustomGlideUrl {
    public GlideUrl getGlideUrl(String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + PropertyManager.getInstance().getToken())
                .build());
        return glideUrl;
    }
}
