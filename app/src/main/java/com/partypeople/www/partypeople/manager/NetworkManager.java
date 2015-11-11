package com.partypeople.www.partypeople.manager;

import android.content.Context;
import android.media.session.MediaSession;
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
import com.partypeople.www.partypeople.data.User;
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
    MediaSession.Token token;
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
    public static final String URL_USERS = "http://61.100.5.61:3000/api/v1/users";
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
                Log.d("NetworkManager", "get local info Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get local info Success" + responseString);
                LocalInfoResult result = gson.fromJson(responseString, LocalInfoResult.class);
                listener.onSuccess(result.localAreaInfo);
            }
        });
    }

    public void postPartys(Context context, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();

        client.post(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "post Success");
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
            }

        });
    }

    public void postJson(final Context context, String jsonString, final OnResultListener<String> listener) {
        Header[] headers = null;
        try {
            client.post(context, URL_PARTYS, headers, new StringEntity(jsonString), "application/json", new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "post Success");
                    listener.onSuccess(responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
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
                Log.d("NetworkManager", "get Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get Success " + responseString);
                Party[] result = gson.fromJson(responseString, Party[].class);
                listener.onSuccess(result);
            }
        });
    }

    public void putPartys(Context context, String param1, String key, String param2, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();
        params.put(key, param2);
        Log.d("NetworkManager", "id : " + param1 + "key : " + key + "value : " + param2);

        client.put(context, URL_PARTYS + "/" + param1, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "put Success");
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
            }

        });
    }

    public void deletePartys(Context context, String param1, final OnResultListener<String> listener ) {
        Header[] headers = null;
        RequestParams params = new RequestParams();

        client.delete(context, URL_PARTYS + "/" + param1, headers, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "delete Success");
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "delete Fail: " + statusCode + responseString);
            }

        });
    }

    public void postUser(Context context, String param1, String param2, String param3, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();
        params.put("email", param1);
        params.put("password", param2);
        params.put("name", param3);

        client.post(context, URL_USERS, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "post user Success" + headers.toString() + responseString);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
            }

        });
    }

    public void getUser(Context context, final OnResultListener<User[]> listener) {
        RequestParams params = new RequestParams();

        client.get(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get Success " + responseString);
                User[] result = gson.fromJson(responseString, User[].class);
                listener.onSuccess(result);
            }
        });
    }
}