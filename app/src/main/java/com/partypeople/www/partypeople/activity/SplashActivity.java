package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Data;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginManager mLoginManager = LoginManager.getInstance();
    AccessTokenTracker mTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(PropertyManager.getInstance().isLogin()) {
            NetworkManager.getInstance().getMyId(this, PropertyManager.getInstance().getToken(), new NetworkManager.OnResultListener<User>() {
                @Override
                public void onSuccess(User result) {
                    User user = result;
                    user.token = PropertyManager.getInstance().getToken();
                    PropertyManager.getInstance().setUser(user);
                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(SplashActivity.this, "통신에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            });
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMainActivity();
                }
            }, Constants.SPLASH_TIME_OUT);
        } else {
            final String id = PropertyManager.getInstance().getFaceBookId();
            if (!TextUtils.isEmpty(id)) {
                // facebook login
                mTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        Log.d("SplashActivity", "Token changed");
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        if (token != null) {
                            Log.d("SplashActivity", "Token is not null");
                            if (token.getUserId().equals(id)) {
                                Log.d("SplashActivity", "Token user id is same as before");
                                NetworkManager.getInstance().loginFacebookToken(SplashActivity.this, token.getToken(), new NetworkManager.OnResultListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        if (result.equals("OK")) {
                                            goMainActivity();
                                        }
                                    }

                                    @Override
                                    public void onFail(int code) {

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
//                    Toast.makeText(SplashActivity.this, "login success", Toast.LENGTH_SHORT).show();
//                    Log.d("SplashActivity", "onSuccess()");
//                    goMainActivity();
                    }

                    @Override
                    public void onCancel() {
//                    Log.d("SplashActivity", "onCancel()");
//                    goMainActivity();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SplashActivity.this, "facebook login fail...", Toast.LENGTH_SHORT).show();
                        goLoginActivity();
                    }
                });

                Log.d("SplashActivity", "loginWithReadPermissions");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);
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
        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
        intent.putExtra("startFrom", Constants.START_FROM_SPLASH);
        startActivity(intent);
        finish();
    }

}