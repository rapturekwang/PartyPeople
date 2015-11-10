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
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.LocalInfoResult;
import com.partypeople.www.partypeople.utils.MyApplication;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-28.
 */
public class NetworkManager {
    Gson gson;
    List<Party> partysList = new ArrayList<Party>();
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    Handler mHandler = new Handler(Looper.getMainLooper());
    public void loginFacebookToken(Context context, String accessToken, final OnResultListener<String> listener) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("OK");
            }
        }, 1000);
    }

    public void signupFacebook(Context context, String message, final OnResultListener<String> listener) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("OK");
            }
        }, 1000);
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

    public static final String URL_PARTYS = "http://61.100.5.61:3000/api/v1/partys";
    private static final String LOCATION_INFO = "https://apis.skplanetx.com/tmap/poi/areas";

    public void getLocalInfo(Context context, int param1, String param2, int param3, final OnResultListener<LocalAreaInfo> listener) {
        RequestParams params = new RequestParams();
        params.put("version", param1);
        if(param2 != null) {
            params.put("searchFlag", param2);
        }
        if(param2.equals("M")) {
            params.put("areaLLCode", param3);
        }

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
            }
        });
    }

    public void postPartys(Context context, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();
        params.put("name", "강남 프라이빗 파티");
        params.put("date", "2015-11-03T02:11:11");
        params.put("location", "강남구 역삼동");
        params.put("info", "파리투나잇 상세 정보");
        params.put("private", false);
        params.put("password", "1234");
        params.put("expect_pay", "20000");
        params.put("bank", "국민");
        params.put("account", 123456789);
        params.put("active", true);
        params.put("theme", "");
//        List<PayMethod> pay_method= new ArrayList<PayMethod>();
//        PayMethod p = new PayMethod();
//        p.add("맥주1병", 12000);
//        pay_method.add(p);
//        params.put("pay_method", pay_method);

        client.post(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "onSuccess");
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "onFail");
            }

        });
    }

    public void postJson(final Context context, String jsonString, final OnResultListener<String> listener) {
        Header[] headers = null;
        try {
            client.post(context, URL_PARTYS, headers, new StringEntity(jsonString), "application/json", new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "Success : " + responseString);
                    listener.onSuccess(responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("NetworkManager", "onFail");
                    listener.onFail(statusCode);
                }

            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getPartys(Context context, final OnResultListener<Party[]> listener) {
        RequestParams params = new RequestParams();

        client.get(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "Fail");
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "Success : " + responseString);
                Party[] result = gson.fromJson(responseString, Party[].class);
                listener.onSuccess(result);
            }
        });
    }

//    public void deletePartys(Context context, String param1, final OnResultListener<String> listener ) {
//        RequestParams params = new RequestParams();
//        params.put("_id", param1);
////        List<PayMethod> pay_method= new ArrayList<PayMethod>();
////        PayMethod p = new PayMethod();
////        p.add("맥주1병", 12000);
////        pay_method.add(p);
////        params.put("pay_method", pay_method);
//
//        client.delete(context, URL_PARTYS, params, new TextHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
//                listener.onSuccess(responseString);
//                Log.d("NetworkManager", "onSuccess");
//            }
//
//            @Override
//            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
//                listener.onFail(statusCode);
//                Log.d("NetworkManager", "onFail");
//            }
//
//        });
//    }
}