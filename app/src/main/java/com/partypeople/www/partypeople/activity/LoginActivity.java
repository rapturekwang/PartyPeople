package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.FindPasswordFragment;
import com.partypeople.www.partypeople.fragment.LoginFragment;
import com.partypeople.www.partypeople.fragment.LoginMainFragment;
import com.partypeople.www.partypeople.fragment.SignupFragment;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Fragment[] list = {LoginMainFragment.newInstance(""),
            SignupFragment.newInstance(""),
            LoginFragment.newInstance(""),
            FindPasswordFragment.newInstance("")};
    CallbackManager callbackManager = CallbackManager.Factory.create();

    LoginManager mLoginManager = LoginManager.getInstance();
    AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFragment();
    }

    public void login(List<String> permissions) {
        login(permissions, true);
    }

    private void login(List<String> permissions, boolean isRead) {
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                token = AccessToken.getCurrentAccessToken();
                PropertyManager.getInstance().setFacebookId(token.getUserId());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                Log.d("LoginActivity", "onCancel");
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

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, list[0]).commit();
    }

    public void goToFragment(int stack_status) {
        goToFragment(-1, stack_status);
    }

    public void goToFragment(int num, int stack_status) {
        if(stack_status == Constants.STACK_ADD) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[num])
                    .addToBackStack(null).commit();
        } else if(stack_status == Constants.STACK_REPLACE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[num]).commit();
        } else if(stack_status == Constants.STACK_POP) {
            getSupportFragmentManager().popBackStack();
            if(num != -1) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, list[num])
                        .addToBackStack(null).commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
