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
import com.partypeople.www.partypeople.data.BoardResult;
import com.partypeople.www.partypeople.data.GooglePlaceResult;
import com.partypeople.www.partypeople.data.IamportResult;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.LocalInfoResult;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.data.PartysResult;
import com.partypeople.www.partypeople.data.PasswordChange;
import com.partypeople.www.partypeople.data.Report;
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
        public void onFail(String response);
    }

    public HttpClient getHttpClient() {
        return client.getHttpClient();
    }

    public static final String URL_SERVER = "http://partypeople.me";
    public static final String URL_PARTYS = URL_SERVER + "/api/v1/groups";
    public static final String URL_USERS = URL_SERVER + "/api/v1/users";
    public static final String URL_FOLLOWS = URL_SERVER + "/api/v1/follows/";
    public static final String URL_COMMENT = URL_SERVER + "/api/v1/comments";
    public static final String URL_REPORT = URL_SERVER + "/api/v1/contacts";
    public static final String URL_BOARD = URL_SERVER + "/api/v1/boards";
    public static final String URL_AUTH = URL_SERVER + "/api/auth/local";
    public static final String URL_AUTH_FACEBOOK = URL_SERVER + "/api/auth/facebook/token";
    public static final String URL_GET_ID = URL_SERVER + "/api/v1/users/me";
    private static final String LOCATION_INFO = "https://apis.skplanetx.com/tmap/poi/areas";
    private static final String URL_GET_IAMPORT_TOKEN = "https://api.iamport.kr/users/getToken";
    private static final String URL_GET_PAYMENT_RESULT = "https://api.iamport.kr/payments";

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
                listener.onFail(statusCode + ":" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get local info Success" + responseString);
                LocalInfoResult result = gson.fromJson(responseString, LocalInfoResult.class);
                listener.onSuccess(result.localAreaInfo);
            }
        });
    }

    public void getIamportToken(Context context, final OnResultListener<IamportResult> listener) {
        RequestParams params = new RequestParams();
        params.put("imp_key", "0728738849435849");
        params.put("imp_secret", "pgC7d4SfP83yqSoSMQcnICkU7abJnWJMclUqqNFfVIOmBceo3qLL36FagRUDNDq0Ykrp76E3OspqFDOJ");

        client.post(context, URL_GET_IAMPORT_TOKEN, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get iamport token Success : " + responseString);
                IamportResult result = gson.fromJson(responseString, IamportResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "get iamport token Fail: " + statusCode + responseString);
            }
        });
    }

    public void getPaymentResult(Context context, String impUid, String token, final OnResultListener<IamportResult> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("Authorization", token);

        String url = URL_GET_PAYMENT_RESULT + "/" + impUid;

        client.get(context, url, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get payment result Success : " + responseString);
                IamportResult result = gson.fromJson(responseString, IamportResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "get payment result Fail: " + statusCode + responseString);
            }
        });
    }

    public void participate(Context context, String param1, String title, int price, final OnResultListener<String> listener) {
        RequestParams params = new RequestParams();
        params.put("title", title);
        params.put("price", price);
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
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
            }

        });
    }

    public void makeParty(final Context context, Party party, final OnResultListener<PartyResult> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        try {
            client.post(context, URL_PARTYS, headers, new StringEntity(gson.toJson(party,Party.class), "UTF-8"), "application/json", new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "post Success" + responseString);
                    PartyResult result = gson.fromJson(responseString, PartyResult.class);
                    listener.onSuccess(result);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("NetworkManager", "post Fail: " + statusCode + responseString);
                    listener.onFail(statusCode + ":" + responseString);
                }

            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getPartys(Context context, String keyword, String parameter, ArrayList<Integer> parameters, int skipNum, final OnResultListener<PartysResult> listener) {
        RequestParams params = new RequestParams();
        params.put("skip", skipNum);
        if(keyword!=null) {
            params.put(keyword, parameter);
        }
        if(parameters.size()==1) {
            params.put("themes", parameters.get(0));
        } else if(parameters.size()>1){
            String themes = parameters.get(0)+"";
            for (int i = 1; i < parameters.size(); i++) {
                themes += "&themes=" + parameters.get(i);
            }
            params.put("themes", themes);
        }

        client.get(context, URL_PARTYS, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get partys Fail: " + statusCode + responseString);
                listener.onFail(statusCode + ":" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.d("NetworkManager", "get partys Success " + responseString);
                PartysResult result = gson.fromJson(responseString, PartysResult.class);
                listener.onSuccess(result);
            }
        });
    }

    public void getParty(Context context, String param1, final OnResultListener<PartyResult> listener) {
        RequestParams params = new RequestParams();
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());

        client.get(context, URL_PARTYS + "/" + param1, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get party Fail: " + statusCode + responseString);
                listener.onFail(statusCode + ":" + responseString);
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

    public void sendReport(Context context, Report report, final OnResultListener<Report> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        params.put("question", report.question);
        params.put("category", report.category);

        client.post(context, URL_REPORT, headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "send report Success " + responseString);
                Report result = gson.fromJson(responseString, Report.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "send report Fail: " + statusCode + responseString);
                listener.onFail(statusCode + ":" + responseString);
            }
        });
    }

    public void getReport(Context context, String id, final OnResultListener<Report[]> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();

        client.get(context, URL_REPORT + "/" + id, headers, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get report Success " + responseString);
                Report[] result = gson.fromJson(responseString, Report[].class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get report Fail: " + statusCode + responseString);
                listener.onFail(statusCode + ":" + responseString);
            }
        });
    }

    public void getBoards(Context context, final OnResultListener<BoardResult> listener) {
        RequestParams params = new RequestParams();
        params.put("sort", "CREATED");

        client.get(context, URL_BOARD, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "get boards Success " + responseString);
                BoardResult result = gson.fromJson(responseString, BoardResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("NetworkManager", "get boards Fail: " + statusCode + responseString);
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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

        try {
            client.put(context, URL_USERS + "/" + PropertyManager.getInstance().getUser().id, headers,
                    new StringEntity(gson.toJson(user, User.class), "UTF-8"), "application/json", new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            UserResult result = gson.fromJson(responseString, UserResult.class);
                            listener.onSuccess(result);
                            PropertyManager.getInstance().setUser(result.data);
                            Log.d("NetworkManager", "put Success" + responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode + ":" + responseString);
                            Log.d("NetworkManager", "put Fail: " + statusCode + responseString);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void putReportImage(Context context, File image, String id, final OnResultListener<String> listener) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        try {
            params.put("photo", image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = URL_REPORT + "/" + id + "/photo";

        try {
            client.post(context, url, headers, params, null, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    listener.onSuccess(responseString);
                    Log.d("NetworkManager", "put report image Success");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    listener.onFail(statusCode + ":" + responseString);
                    Log.d("NetworkManager", "put report image Fail: " + statusCode + responseString);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putUserImage(Context context, File image, String id, final OnResultListener<String> listener ) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("authorization", "Bearer " + PropertyManager.getInstance().getToken());
        RequestParams params = new RequestParams();
        try {
            params.put("photo", image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String url = URL_USERS + "/" + id + "/photo";

        try {
            client.post(context, url, headers, params, null, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("NetworkManager", "put user image Success : " + responseString);
                    listener.onSuccess(responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("NetworkManager", "put user image Fail: " + statusCode + responseString);
                    listener.onFail(statusCode + ":" + responseString);
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
                            Log.d("NetworkManager", "put group images Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode + ":" + responseString);
                            Log.d("NetworkManager", "put groups images Fail: " + statusCode + responseString);
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

        try {
            client.post(context, URL_PARTYS + "/" + param2 + "/photo", headers, params,
                    null, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            listener.onSuccess(responseString);
                            Log.d("NetworkManager", "put group image Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            listener.onFail(statusCode + ":" + responseString);
                            Log.d("NetworkManager", "put group image Fail: " + statusCode + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
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
                    listener.onFail(statusCode + ":" + responseString);
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
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "facebook auth Fail: " + statusCode + responseString);
            }
        });
    }

    public void authUser(Context context, String email, String password, final OnResultListener<UserResult> listener ) {
        Header[] headers = null;
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        client.post(context, URL_AUTH, headers, params, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("NetworkManager", "auth Success" + responseString);
                UserResult result = gson.fromJson(responseString, UserResult.class);
                listener.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "auth Fail: " + statusCode + responseString);
            }
        });
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
                listener.onFail(statusCode + ":" + responseString);
                Log.d("NetworkManager", "get id Fail: " + statusCode + responseString);
            }

        });
    }
}