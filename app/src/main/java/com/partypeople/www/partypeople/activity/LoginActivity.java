package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.manager.PropertyManager;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    Button btn;

    LoginManager mLoginManager = LoginManager.getInstance();
    AccessToken token;// = AccessToken.getCurrentAccessToken();
    //AccessTokenTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = (Button)findViewById(R.id.btn_facebook);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login(null);
//            }
//        });

        Button btn = (Button)findViewById(R.id.btn_skip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public static final int MODE_NONE = -1;
    public static final int MODE_PROFILE = 1;
    public static final int MODE_POST = 2;
    int mode = MODE_NONE;

    private void login(List<String> permissions) {
        login(permissions, true);
    }

    private void login(List<String> permissions, boolean isRead) {
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                if (mode == MODE_PROFILE) {
//                    getProfile();
//                    mode = MODE_NONE;
//                } else if (mode == MODE_POST) {
//                    postMessage();
//                    mode = MODE_NONE;
//                }
                token = AccessToken.getCurrentAccessToken();
                PropertyManager.getInstance().setFacebookId(token.getUserId());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (isRead) {
            mLoginManager.logInWithReadPermissions(LoginActivity.this, permissions);
        } else {
            mLoginManager.logInWithPublishPermissions(LoginActivity.this, permissions);
        }
    }

//    private void getProfile() {
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        GraphRequest request = new GraphRequest(token, "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse response) {
//                JSONObject object = response.getJSONObject();
//                if (object == null) {
//                    String message = response.getError().getErrorMessage();
//                    Toast.makeText(LoginActivity.this, "error : " + message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "profile : " + object.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        request.executeAsync();
//    }

//    private void postMessage() {
//        String message = "facebook test message";
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        String graphPath = "/me/feed";
//        Bundle parameters = new Bundle();
//        parameters.putString("message",message);
//        parameters.putString("link", "http://developers.facebook.com/docs/android");
//        parameters.putString("picture", "https://raw.github.com/fbsamples/.../iossdk_logo.png");
//        parameters.putString("name", "Hello Facebook");
//        parameters.putString("description", "The 'Hello Facebook' sample  showcases simple …");
//        GraphRequest request = new GraphRequest(accessToken, graphPath, parameters, HttpMethod.POST,
//                new GraphRequest.Callback() {
//                    @Override
//                    public void onCompleted(GraphResponse response) {
//                        JSONObject data = response.getJSONObject();
//                        String id = (data == null)?null:data.optString("id");
//                        if (id == null) {
//                            Toast.makeText(LoginActivity.this, "error : " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "post object id : " + id, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//        request.executeAsync();
//    }

//    private void setLabel() {
//        if (!isLogin()) {
//            btnLogin.setText("login");
//        } else {
//            btnLogin.setText("logout");
//        }
//    }

//    private boolean isLogin() {
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        return token==null?false:true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
