package com.partypeople.www.partypeople.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.Service.RegistrationIntentService;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginManager mLoginManager = LoginManager.getInstance();
    AccessTokenTracker mTokenTracker;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doRealStart();
            }
        };
        setUpIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();
        }
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }

    private void setUpIfNeeded() {
        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegistrationToken();
//            Log.d("regId", regId);
            if (!regId.equals("")) {
                doRealStart();
            } else {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private void doRealStart() {
        new MarketVersion().execute();
        if(PropertyManager.getInstance().isLogin()) {
            NetworkManager.getInstance().getMyId(this, PropertyManager.getInstance().getToken(), new NetworkManager.OnResultListener<UserResult>() {
                @Override
                public void onSuccess(UserResult result) {
                    PropertyManager.getInstance().setUser(result.data);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goMainActivity();
                        }
                    }, Constants.SPLASH_TIME_OUT);
                }

                @Override
                public void onFail(String response) {
                    Toast.makeText(SplashActivity.this, response, Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
            });
        } else {
            final String id = PropertyManager.getInstance().getFaceBookId();
            if (!TextUtils.isEmpty(id)) {
                // facebook login
                mTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        if (token != null) {
                            if (token.getUserId().equals(id)) {
                                NetworkManager.getInstance().loginFacebookToken(SplashActivity.this, token.getToken(), new NetworkManager.OnResultListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        if (result.equals("OK")) {
                                            NetworkManager.getInstance().getMyId(SplashActivity.this, PropertyManager.getInstance().getToken(), new NetworkManager.OnResultListener<UserResult>() {
                                                @Override
                                                public void onSuccess(UserResult result) {
                                                    PropertyManager.getInstance().setUser(result.data);
                                                    goMainActivity();
                                                }

                                                @Override
                                                public void onFail(String response) {
                                                    Toast.makeText(SplashActivity.this, response, Toast.LENGTH_SHORT).show();
                                                    goLoginActivity();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFail(String response) {
                                        Toast.makeText(SplashActivity.this, response, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(SplashActivity.this, "facebook id change", Toast.LENGTH_SHORT).show();
                                mLoginManager.logOut();
                                goLoginActivity();
                            }
                        }
                    }
                };
                mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SplashActivity.this, "facebook login fail...", Toast.LENGTH_SHORT).show();
                        goLoginActivity();
                    }
                });

                mLoginManager.logInWithReadPermissions(this, null);
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goLoginActivity();
                    }
                }, Constants.SPLASH_TIME_OUT);
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTokenTracker != null) {
            mTokenTracker.stopTracking();
        }
    }

    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goLoginActivity() {
        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        finish();
    }

    private class MarketVersion extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            // Confirmation of market information in the Google Play Store
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName()).get();
                Elements Version = doc.select(".content");
                for (Element mElement : Version) {
                    if (mElement.attr("itemprop").equals("softwareVersion")) {
                        PropertyManager.getInstance().setMarketVersion(mElement.text().trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}