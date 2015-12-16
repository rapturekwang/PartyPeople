package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.UserTabAdapter;
import com.partypeople.www.partypeople.data.Follow;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.fragment.UserFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by kwang on 15. 12. 11..
 */
public class UserActivity extends AppCompatActivity{
    User user;
    public TabLayout tabs, fakeTabs;
    ViewPager pager;
    public FrameLayout header;
    TextView followView, nameView, addressView;
    RelativeLayout relativeLayout, relativeLayout2;
    ImageView modify, profileView, takeFollow, takeUnfollow;
    UserTabAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        tabs = (TabLayout)findViewById(R.id.tabs);
        fakeTabs = (TabLayout)findViewById(R.id.fake_tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new UserTabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        pager.setOffscreenPageLimit(Constants.NUM_OF_USER_PAGE_TAB - 1);

        setPagerHeight(1000);

        header = (FrameLayout)findViewById(R.id.header);
        header.addView(LayoutInflater.from(UserActivity.this).inflate(R.layout.view_user_header, null));

        tabs.setupWithViewPager(pager);
        fakeTabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        fakeTabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.user_page_tab_name);
        for (int i = 0; i < Constants.NUM_OF_USER_PAGE_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
            fakeTabs.addTab(fakeTabs.newTab().setText(tabTitle[i]));
        }

        ImageView btn = (ImageView)findViewById(R.id.btn_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modify = (ImageView)findViewById(R.id.btn_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, EditProfileActivity.class));
            }
        });
        profileView = (ImageView)findViewById(R.id.image_profile);
        TextView textBtn = (TextView)findViewById(R.id.text_btn_message);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, MessageActivity.class));
            }
        });
        followView = (TextView)findViewById(R.id.text_follow);
        followView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, FollowActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        nameView = (TextView)findViewById(R.id.text_name);
        addressView = (TextView)findViewById(R.id.text_address);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout_user);
        relativeLayout2 = (RelativeLayout)findViewById(R.id.relativelayout);

        takeFollow = (ImageView)findViewById(R.id.image_btn_follow);
        takeFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().takeFollow(UserActivity.this, user.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(UserActivity.this, "팔로우 하였습니다", Toast.LENGTH_SHORT).show();
                        takeFollow.setVisibility(View.INVISIBLE);
                        takeUnfollow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });
        takeUnfollow = (ImageView)findViewById(R.id.image_btn_unfollow);
        takeUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().takeUnfollow(UserActivity.this, user.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(UserActivity.this, "팔로우를 취소 하였습니다", Toast.LENGTH_SHORT).show();
                        takeFollow.setVisibility(View.VISIBLE);
                        takeUnfollow.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });

        ImageView takeMessage = (ImageView)findViewById(R.id.image_btn_message);
        takeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this, "아직 준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        final ScrollView scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY()>tabs.getY()) {
                    fakeTabs.setVisibility(View.VISIBLE);
                } else {
                    fakeTabs.setVisibility(View.INVISIBLE);
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPagerHeight(1000);
                        ((UserFragment) mAdapter.getItem(position)).changeHeight();
                        break;
                    case 1:
                        setPagerHeight(1000);
                        ((UserFragment) mAdapter.getItem(position)).changeHeight();
                        break;
                    case 2:
                        setPagerHeight(1000);
                        ((UserFragment) mAdapter.getItem(position)).changeHeight();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initData();
    }

    private void initData() {
        if(user.has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            GlideUrl glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + user.photo);
            Glide.with(this)
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .transform(new CircleTransform(this))
                    .into(profileView);
        }
        nameView.setText(user.name);
        if(user.address==null) {
            addressView.setVisibility(View.INVISIBLE);
        } else {
            addressView.setVisibility(View.VISIBLE);
            addressView.setText(user.address);
        }

        followView.setText("팔로잉 " + user.following.size() + " | 팔로워 " + user.follower.size());

        if(!user.id.equals(PropertyManager.getInstance().getUser().id)) {
            modify.setVisibility(View.INVISIBLE);
            relativeLayout2.setVisibility(View.INVISIBLE);
            if(user.follower.size()>0) {
                for (int i = 0; i < user.follower.size(); i++) {
                    if (user.follower.get(i).from.equals(PropertyManager.getInstance().getUser().id)) {
                        takeFollow.setVisibility(View.INVISIBLE);
                        takeUnfollow.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setPagerHeight(int height) {
        LayoutParams params = pager.getLayoutParams();
        params.height=height;
        pager.setLayoutParams(params);
    }

    public User getUser() {
        return user;
    }
}
