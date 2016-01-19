package com.partypeople.www.partypeople.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.CertifyFragment;
import com.partypeople.www.partypeople.fragment.ChangePasswordFragment;
import com.partypeople.www.partypeople.fragment.FAQFragment;
import com.partypeople.www.partypeople.fragment.PushAlarmFragment;
import com.partypeople.www.partypeople.fragment.ReportFragment;
import com.partypeople.www.partypeople.fragment.TOSFragment;
import com.partypeople.www.partypeople.fragment.UserInfoPolicyFragment;
import com.partypeople.www.partypeople.dialog.LeaveDialog;
import com.partypeople.www.partypeople.dialog.LogoutDialog;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.SettingItemView;
import com.tsengvn.typekit.TypekitContextWrapper;


public class SettingActivity extends AppCompatActivity {

    TextView titleView;
    ActionBar actionBar;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    ScrollView scrollView;
    MenuItem item;
    boolean onTopFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout)findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout)findViewById(R.id.linearLayout4);

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        SettingItemView settingItemView = new SettingItemView(this);
        settingItemView.setItemData("푸쉬알림", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("푸쉬알림", PushAlarmFragment.newInstance("push"));
            }
        });
        linearLayout1.addView(settingItemView);

        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("버전정보", "1.0");
        linearLayout2.addView(settingItemView);

        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("이용약관", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("이용약관", TOSFragment.newInstance("tos"));
            }
        });
        linearLayout3.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("개인정보 취급방침", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("개인정보 취급방침", UserInfoPolicyFragment.newInstance("policy"));
            }
        });
        linearLayout3.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("자주 하는 질문", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("자주하는 질문", FAQFragment.newInstance("faq"));
            }
        });
        linearLayout3.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("신고 / 문의", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("신고 & 문의", ReportFragment.newInstance("report"));
            }
        });
        linearLayout3.addView(settingItemView);

        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("본인인증 및 계좌정보", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment("본인인증 및 계좌정보", CertifyFragment.newInstance("인증"));
            }
        });
        linearLayout4.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("비밀번호 변경", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PropertyManager.getInstance().getUser().provider.equals("facebook")) {
                    Toast.makeText(SettingActivity.this, "페이스북 아이디는 비밀번호를 변경하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    changeFragment("비밀번호 변경", ChangePasswordFragment.newInstance("changePassword"));
                }
            }
        });
        linearLayout4.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("로그아웃", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutDialog dialog = new LogoutDialog(SettingActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        linearLayout4.addView(settingItemView);
        settingItemView = new SettingItemView(this);
        settingItemView.setItemData("회원탈퇴", "");
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveDialog dialog = new LeaveDialog(SettingActivity.this, getSupportFragmentManager());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        linearLayout4.addView(settingItemView);

        titleView = (TextView)findViewById(R.id.text_title);
        titleView.setText("설정");
        onTopFlag = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        item = menu.findItem(R.id.close_setting);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount()==0) {
            this.item.setVisible(true);
            titleView.setText("설정");
            onTopFlag = true;
            actionBar.setDisplayHomeAsUpEnabled(false);
            scrollView.setVisibility(View.VISIBLE);
        }
//        if(onTopFlag) {
//            super.onBackPressed();
//        } else {
//            this.item.setVisible(true);
//            titleView.setText("설정");
//            onTopFlag = true;
//            actionBar.setDisplayHomeAsUpEnabled(false);
//            scrollView.setVisibility(View.VISIBLE);
//        }
    }

    private void changeFragment(String title, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).addToBackStack(null).commit();
        scrollView.setVisibility(View.GONE);
        item.setVisible(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        titleView.setText(title);
        onTopFlag = false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
