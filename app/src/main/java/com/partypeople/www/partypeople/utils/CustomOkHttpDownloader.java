//package com.partypeople.www.partypeople.utils;
//
//import android.content.Context;
//
//import com.partypeople.www.partypeople.manager.PropertyManager;
//import com.squareup.okhttp.Interceptor;
//import com.squareup.okhttp.Request;
//import com.squareup.picasso.OkHttpDownloader;
//
//import java.io.IOException;
//
///**
// * Created by kwang on 15. 12. 9..
// */
//public class CustomOkHttpDownloader extends OkHttpDownloader {
//    public CustomOkHttpDownloader(final Context context) {
//        super(context);
//        getClient().interceptors().add(new Interceptor() {
//            @Override
//            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
//                Request newRequest = chain.request().newBuilder()
//                        .addHeader("Authorization", "Bearer " +PropertyManager.getInstance().getToken())
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        });
//    }
//}
