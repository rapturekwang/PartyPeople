package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.fragment.FindPasswordFragment;
import com.partypeople.www.partypeople.fragment.LoginFragment;
import com.partypeople.www.partypeople.fragment.LoginMainFragment;
import com.partypeople.www.partypeople.fragment.SignupFragment;
import com.partypeople.www.partypeople.fragment.TOSFragment;
import com.partypeople.www.partypeople.fragment.UserInfoPolicyFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    int mStartfrom;
    Intent intent;
    Toolbar toolbar;
    TextView title;
    Fragment[] list = {LoginMainFragment.newInstance(""),
            SignupFragment.newInstance(""),
            LoginFragment.newInstance(""),
            FindPasswordFragment.newInstance(""),
            TOSFragment.newInstance(""),
            UserInfoPolicyFragment.newInstance("")};
    CallbackManager callbackManager = CallbackManager.Factory.create();

    LoginManager mLoginManager = LoginManager.getInstance();
    AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        title = (TextView)findViewById(R.id.text_title);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        mStartfrom = getIntent().getExtras().getInt("startfrom");
        initFragment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loginWithFacebook(List<String> permissions) {
        loginWithFacebook(Arrays.asList("email", "basic_info"), true);
    }

    private void loginWithFacebook(List<String> permissions, boolean isRead) {
        final LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "loading");
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                token = AccessToken.getCurrentAccessToken();
                NetworkManager.getInstance().authFacebook(LoginActivity.this, token.getToken(), new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(final String result1) {
                        NetworkManager.getInstance().getMyId(LoginActivity.this, result1, new NetworkManager.OnResultListener<UserResult>() {
                            @Override
                            public void onSuccess(UserResult result2) {
                                PropertyManager.getInstance().setFacebookId(token.getUserId());
                                PropertyManager.getInstance().setToken(result1);
                                PropertyManager.getInstance().setUser(result2.data);
                                PropertyManager.getInstance().setLoginMethod(Constants.LOGIN_WITH_FACEBOOK);

                                User user = new User();
                                user.android_token = PropertyManager.getInstance().getRegistrationToken();
                                NetworkManager.getInstance().putUser(LoginActivity.this, user,new NetworkManager.OnResultListener<UserResult>() {
                                    @Override
                                    public void onSuccess(UserResult result) {

                                    }

                                    @Override
                                    public void onFail(int code) {

                                    }
                                });

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                                dialogFragment.dismiss();
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(LoginActivity.this, "통신에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                dialogFragment.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                });
            }

            @Override
            public void onCancel() {
                dialogFragment.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                dialogFragment.dismiss();
            }
        });
        if (isRead) {
            mLoginManager.logInWithReadPermissions(LoginActivity.this, permissions);
            dialogFragment.dismiss();
        } else {
            mLoginManager.logInWithPublishPermissions(LoginActivity.this, permissions);
            dialogFragment.dismiss();
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

        if(num==4) {
            toolbar.setVisibility(View.VISIBLE);
            title.setText("이용약관");
        } else if(num==5) {
            toolbar.setVisibility(View.VISIBLE);
            title.setText("개인정보 보호정책");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toolbar.setVisibility(View.GONE);
        title.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        Log.d("LoginActivity", "request code: " + requestCode + " result code: " + resultCode + " data: " + data);
    }

    public int getStartfrom() {
        return mStartfrom;
    }
}
