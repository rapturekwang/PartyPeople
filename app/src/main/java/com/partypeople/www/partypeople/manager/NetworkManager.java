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
import com.partypeople.www.partypeople.data.Board;
import com.partypeople.www.partypeople.data.GooglePlaceResult;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.LocalInfoResult;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.data.PartysResult;
import com.partypeople.www.partypeople.data.PasswordChange;
import com.partypeople.www.partypeople.data.Token;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.utils.MyApplication;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by dongja94 on 2015-10-28.
 */
public class NetworkManager {
    Gson gson;
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

    public static final String URL_SERVER = "http://partypeople.me:3000";
    public static final String URL_PARTYS = URL_SERVER + "/api/v1/groups";
    public static final String URL_USERS = URL_SERVER + "/api/v1/users";
    public static final String URL_FOLLOWS = URL_SERVER + "/api/v1/follows/";
    public static final String URL_COMMENT = URL_SERVER + "/api/v1/comments";
    public static final String URL_BOARD = URL_SERVER + "/api/v1/boards";
    public static final String URL_AUTH = URL_SERVER + "/api/auth/local";
    public static final String URL_AUTH_FACEBOOK = URL_SERVER + "/api/auth/facebook/token";
    public static final String URL_GET_ID = URL_SERVER + "/api/v1/users/me";
    private static final String LOCATION_INFO = "https://apis.skplanetx.com/tmap/poi/areas";
    private static final String URL_SEARCH_LOCATION = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

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

    public void participate(Context context, String param1, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.post(context, URL_PARTYS + "/" + param1 + "/members/enroll", headers, params, null, new TextHttpResponseHandler() {
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

    public void searchLocation(Context context, String keyword, final OnResultListener<ArrayList<String>> listener) {
        RequestParams params = new RequestParams();
        params.put("input", keyword);
        params.put("components", "country:kr");
        params.put("key", "AIzaSyCXsjiz9Q-Jn2sOv76SiHNOiWLIBefJKm0");

        client.get(context, URL_SEARCH_LOCATION, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                GooglePlaceResult result = gson.fromJson(responseString, GooglePlaceResult.class);
                ArrayList<String> resultList = new ArrayList<String>();
                for(int i=0;i<result.predictions.size();i++) {
                    resultList.add(result.predictions.get(i).description);
                }
                if(!result.status.equals("OK")) {
                    Log.d("NetworkManager", "search location Success but not status ok" + responseString);
                }
                listener.onSuccess(resultList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "search location Fail" + statusCode + responseString);
                listener.onFail(statusCode);
            }
        });
    }

    public void postJson(final Context context, Party party, final OnResultListener<PartyResult> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        try {
            client.post(context, URL_PARTYS, headers, new StringEntity(gson.toJson(party,Party.class), "UTF-8"), "application/json", new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "post Success" + responseString);
                    PartyResult result = gson.fromJson(responseString, PartyResult.class);
                    Log.d("NetworkManager", "test:" + result.data.id);
                    listener.onSuccess(result);
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

    public void getPartys(Context context, String keyword, String parameter, final OnResultListener<PartysResult> listener) {
        RequestParams params = new RequestParams();
        if(keyword!=null) {
            params.put(keyword, parameter);
        }
        Log.d("NetworkManager", "keyword: " + keyword + "parameter: " + parameter);

        client.get(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get partys Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get partys Success " + responseString);
                PartysResult result = gson.fromJson(responseString, PartysResult.class);
                listener.onSuccess(result);
            }
        });
    }

//    public void getPartys(Context context, final OnResultListener<PartysResult> listener) {
//        RequestParams params = new RequestParams();
//
//        client.get(context, URL_PARTYS, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
//                Log.d("NetworkManager", "get Fail: " + statusCode + responseString);
//                listener.onFail(statusCode);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
//                Log.d("NetworkManager", "get partys Success " + responseString);
//                PartysResult result = gson.fromJson(responseString, PartysResult.class);
//                listener.onSuccess(result);
//            }
//        });
//    }

    public void getParty(Context context, String param1, final OnResultListener<PartyResult> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.get(context, URL_PARTYS + "/" + param1, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get party Success " + responseString);
                PartyResult result = gson.fromJson(responseString, PartyResult.class);
                listener.onSuccess(result);
            }
        });
    }

//    public void putPartys(Context context, String param1, String key, String param2, final OnResultListener<String> listener ) {
//        RequestParams params = new RequestParams();
//        params.put(key, param2);
//        Log.d("NetworkManager", "id : " + param1 + "key : " + key + "value : " + param2);
//
//        client.put(context, URL_PARTYS + "/" + param1, params, new TextHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
//                listener.onSuccess(responseString);
//                Log.d("NetworkManager", "put Success");
//            }
//
//            @Override
//            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
//                listener.onFail(statusCode);
//                Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
//            }
//
//        });
//    }

//    public void deletePartys(Context context, String param1, final OnResultListener<String> listener ) {
//        Header[] headers = null;
//        RequestParams params = new RequestParams();
//
//        client.delete(context, URL_PARTYS + "/" + param1, headers, new TextHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
//                listener.onSuccess(responseString);
//                Log.d("NetworkManager", "delete Success");
//            }
//
//            @Override
//            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
//                listener.onFail(statusCode);
//                Log.d("NetworkManager", "delete Fail: " + statusCode + responseString);
//            }
//
//        });
//    }

    public void getBoards(Context context, final OnResultListener<Board[]> listener) {
        client.get(context, URL_BOARD, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get boards Success " + responseString);
                Board[] result = gson.fromJson(responseString, Board[].class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get boards Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }
        });
    }

    public void removeComment(Context context, String commentId, final OnResultListener<String> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.delete(context, URL_COMMENT + "/" + commentId, headers, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "remove Comment Success " + responseString);
                listener.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "remove Comment Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }
        });
    }

    public void addComment(Context context, String groupId, String comment, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("group", groupId);
        params.put("comment", comment);
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.post(context, URL_COMMENT, headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "add Comment Success " + responseString);
                listener.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "add Comment Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }
        });
    }

    public void takeFollow(Context context, String param1, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("to", param1);
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.post(context, URL_FOLLOWS + param1, headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "follow Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "follow Success " + responseString);
                listener.onSuccess(responseString);
            }
        });
    }

    public void takeUnfollow(Context context, String param1, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("to", param1);
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.delete(context, URL_FOLLOWS + param1, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "unfollow Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "unfollow Success " + responseString);
                listener.onSuccess(responseString);
            }
        });
    }

    public void takeUnlike(Context context, String param1, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.post(context, URL_PARTYS + "/" + param1 + "/likes/unvote", headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "unlike Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "unlike Success " + responseString);
                listener.onSuccess(responseString);
            }
        });
    }

    public void takeLike(Context context, String groupId, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.post(context, URL_PARTYS + "/" + groupId + "/likes/vote", headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "taking like Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "taking like Success " + responseString);
                listener.onSuccess(responseString);
            }
        });
    }

    public void postUser(Context context, String param1, String param2, String param3, final OnResultListener<UserResult> listener ) {
        RequestParams params = new RequestParams();
        params.put("email", param1);
        params.put("password", param2);
        params.put("name", param3);

        client.post(context, URL_USERS, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "post user Success" + responseString);
                UserResult result = gson.fromJson(responseString, UserResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
            }

        });
    }

    public void getUser(Context context, String param1, final OnResultListener<User> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.get(context, URL_USERS + "/" + param1, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get Fail: " + statusCode + responseString);
                listener.onFail(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get user Success " + responseString);
                UserResult result = gson.fromJson(responseString, UserResult.class);
                listener.onSuccess(result.data);
            }
        });
    }

    public void putUser(Context context, User user, final OnResultListener<UserResult> listener ) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        UserResult userResult = new UserResult();
        userResult.data = user;

        try {
            client.put(context, URL_USERS + "/" + PropertyManager.getInstance().getUser().id, headers,
                    new StringEntity(gson.toJson(user, User.class), "UTF-8"), "application/json", new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            UserResult result = gson.fromJson(responseString, UserResult.class);
                            listener.onSuccess(result);
                            Log.d("NetworkManager", "put Success" + responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode);
                            Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void putUserImage(Context context, File param1, String param2, final OnResultListener<String> listener ) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        try {
            params.put("photo", param1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = URL_USERS + "/" + param2 + "/photo";
        Log.d("NetworkManager", url);

        try {
            client.post(context, url, headers, params,
                    null, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            listener.onSuccess(responseString);
                            Log.d("NetworkManager", "put Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode);
                            Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putGroupImages(Context context, ArrayList<File> photos, String id, final OnResultListener<String> listener ) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        try {
            if(photos != null) {
                for(int i=0;i<photos.size();i++) {
                    params.put("photo", photos.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = URL_PARTYS + "/" + id + "/photos";

        try {
            client.post(context, url, headers, params,
                    null, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            listener.onSuccess(responseString);
                            Log.d("NetworkManager", "put Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode);
                            Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putGroupImage(Context context, File param1, String param2, final OnResultListener<String> listener ) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        try {
            params.put("photo", param1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = URL_PARTYS + "/" + param2 + "/photo";
        Log.d("NetworkManager", url);

        try {
            client.post(context, url, headers, params,
                    null, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            listener.onSuccess(responseString);
                            Log.d("NetworkManager", "put Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode);
                            Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leaveService(Context context, String id, final OnResultListener<String> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.delete(context, URL_USERS + "/" + id, headers, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                listener.onSuccess(responseString);
                Log.d("NetworkManager", "leave service Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "leave service Fail: " + statusCode + responseString);
            }
        });
    }

    public void changePassword(Context context, String oldPassword, String newPassword, final OnResultListener<String> listener) {
        PasswordChange passwordChange = new PasswordChange();
        passwordChange.oldPassword = oldPassword;
        passwordChange.newPassword = newPassword;
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        Log.d("Networkmanager", "token: " + PropertyManager.getInstance().getToken());

        try {
            client.put(context, URL_USERS + "/" + PropertyManager.getInstance().getUser().id + "/password", headers,
                    new StringEntity(gson.toJson(passwordChange, PasswordChange.class), "UTF-8"),
                    "application/json", new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    listener.onSuccess(responseString);
                    Log.d("NetworkManager", "change password Success");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    listener.onFail(statusCode);
                    Log.d("NetworkManager", "change password Fail: " + statusCode + responseString);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void authFacebook(Context context, String token, final OnResultListener<String> listener ) {
        RequestParams params = new RequestParams();
        params.put("access_token", token);
        Header[] headers = null;

        client.get(context, URL_AUTH_FACEBOOK, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "facebook auth Success" + responseString);
                Token token = gson.fromJson(responseString, Token.class);
                listener.onSuccess(token.token);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "facebook auth Fail: " + statusCode + responseString);
            }
        });
    }

    public void authUser(Context context, String jsonString, final OnResultListener<UserResult> listener ) {
        Header[] headers = null;

        try {
            client.post(context, URL_AUTH, headers, new StringEntity(jsonString), "application/json", new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "auth Success" + responseString);
                    UserResult result = gson.fromJson(responseString, UserResult.class);
                    listener.onSuccess(result);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    listener.onFail(statusCode);
                    Log.d("NetworkManager", "auth Fail: " + statusCode + responseString);
                }

            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getMyId(Context context, String token, final OnResultListener<UserResult> listener ) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + token);

        client.get(context, URL_GET_ID, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get id Success" + responseString);
                UserResult result = gson.fromJson(responseString, UserResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode);
                Log.d("NetworkManager", "get id Fail: " + statusCode + responseString);
            }

        });
    }
}