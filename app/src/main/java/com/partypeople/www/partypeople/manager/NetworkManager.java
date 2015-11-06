package com.partypeople.www.partypeople.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.partypeople.www.partypeople.location.LocalAreaInfo;
import com.partypeople.www.partypeople.location.LocalInfoResult;
import com.partypeople.www.partypeople.utils.MyApplication;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;

/**
 * Created by dongja94 on 2015-10-28.
 */
//public class NetworkManager {
//    private static NetworkManager instance;
//    public static NetworkManager getInstance() {
//        if (instance == null) {
//            instance = new NetworkManager();
//        }
//        return instance;
//    }
//    private NetworkManager() {
//
//    }
//
//    public interface OnResultListener<T> {
//        public void onSuccess(T result);
//        public void onFail(int code);
//    }
//
//    Handler mHandler = new Handler(Looper.getMainLooper());
//    public void loginFacebookToken(Context context, String accessToken, final OnResultListener<String> listener) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                listener.onSuccess("NOTREGISTER");
//            }
//        }, 1000);
//    }
//
//    public void signupFacebook(Context context, String message, final OnResultListener<String> listener) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                listener.onSuccess("OK");
//            }
//        }, 1000);
//    }
//
//}

public class NetworkManager {
    Gson gson;
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    AsyncHttpClient client;
    public static final int NETWORK_TIMEOUT = 30000;

    private NetworkManager() {
        client=new AsyncHttpClient();
        client.setTimeout(NETWORK_TIMEOUT);
        client.setCookieStore(new PersistentCookieStore(MyApplication.getContext()));
        gson = new Gson();
    }

    public interface OnResultListener<T> {
        public void onSuccess(T result);
        public void onFail(int code);
    }

    public HttpClient getHttpClient() {
        return client.getHttpClient();
    }

    public static final String URL_GET_METHOD = "http://61.100.5.61:3000/api/v1/partys";
    public static final String URL_GET_TEST = "http://54.65.35.77:3000/test";
    public static final String URL_POST_TEST = "http://54.65.35.77:3000/test";

    private static final String LOCATION_INFO = "https://apis.skplanetx.com/tmap/poi/areas";

    public void useGetMethod(Context context, int param1, final OnResultListener<LocalAreaInfo> listener) {
        RequestParams params = new RequestParams();
        params.put("version", param1);

        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Accept", "application/json");
        headers[1] = new BasicHeader("appKey", "8d709b60-6811-3e70-9e0b-e2cb992de402");

        client.get(context, LOCATION_INFO, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "Fail");
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "Success");
                Log.d("NetworkManager", responseString);
                LocalInfoResult result = gson.fromJson(responseString, LocalInfoResult.class);
                listener.onSuccess(result.localAreaInfo);
                //LocalInfoResult result = gson.fromJson(responseString, LocalInfoResult.class);
                //listener.onSuccess(result.localAreaInfo);
            }
        });
    }

    public void usePostMethod(Context context, String param1, String param2, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();
        params.put("param1",param1);
        params.put("param2", param2);
        client.post(context, URL_POST_TEST, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
            }

        });
    }
}