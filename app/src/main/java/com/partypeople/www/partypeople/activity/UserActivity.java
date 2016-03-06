package com.partypeople.www.partypeople.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.UserTabAdapter;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by kwang on 15. 12. 11..
 */
public class UserActivity extends AppCompatActivity{
    User user;
    public TabLayout tabs, fakeTabs;
    ViewPager pager;
    public FrameLayout header;
    TextView followView, nameView, addressView, takeFollow, takeUnfollow;
    RelativeLayout relativeLayout, relativeLayout2;
    ImageView modify, profileView;
    UserTabAdapter mAdapter;
    int[] partys = {0, 0, 0};

    public static final int REQUEST_CODE_EDIT = 0;
    public static final int RESULT_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        tabs = (TabLayout)findViewById(R.id.tabs);
        fakeTabs = (TabLayout)findViewById(R.id.fake_tabs);
        pager = (ViewPager)findViewById(R.id.container);
        mAdapter = new UserTabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        pager.setOffscreenPageLimit(Constants.NUM_OF_USER_PAGE_TAB - 1);

        setPagerHeight(1000);

        header = (FrameLayout)findViewById(R.id.header);
        header.addView(LayoutInflater.from(UserActivity.this).inflate(R.layout.view_user_header, null));

        tabs.setupWithViewPager(pager);
        fakeTabs.setupWithViewPager(pager);

        if(user.groups != null && user.groups.size()>0) {
            for (int i = 0; i < user.groups.size(); i++) {
                for(int j=0; j<user.groups.get(i).members.size(); j++) {
                    if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("OWNER"))
                        partys[0]++;
                    else if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("MEMBER"))
                        partys[1]++;
                }
            }
        }
        if(user.likes==null) {
            partys[2] = 0;
        } else {
            partys[2] = user.likes.size();
        }

        tabs.removeAllTabs();
        fakeTabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.user_page_tab_name);
        for (int i = 0; i < Constants.NUM_OF_USER_PAGE_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i] + " " + partys[i]));
            fakeTabs.addTab(fakeTabs.newTab().setText(tabTitle[i] + " " + partys[i]));
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
                startActivityForResult(new Intent(UserActivity.this, EditProfileActivity.class), REQUEST_CODE_EDIT);
            }
        });
        profileView = (ImageView)findViewById(R.id.image_profile);
        TextView textBtn = (TextView)findViewById(R.id.text_btn_message);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(UserActivity.this, MessageActivity.class));
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

        takeFollow = (TextView)findViewById(R.id.image_btn_follow);
        takeFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().takeFollow(UserActivity.this, user.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(UserActivity.this, "팔로우 하였습니다", Toast.LENGTH_SHORT).show();
                        takeFollow.setVisibility(View.INVISIBLE);
                        takeUnfollow.setVisibility(View.VISIBLE);
                        updateHeader();
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(UserActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        takeUnfollow = (TextView)findViewById(R.id.image_btn_unfollow);
        takeUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().takeUnfollow(UserActivity.this, user.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(UserActivity.this, "팔로우를 취소 하였습니다", Toast.LENGTH_SHORT).show();
                        takeFollow.setVisibility(View.VISIBLE);
                        takeUnfollow.setVisibility(View.INVISIBLE);
                        updateHeader();
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(UserActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        TextView takeMessage = (TextView)findViewById(R.id.image_btn_message);
        takeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+user.email));
                startActivity(intent);
            }
        });

        final ScrollView scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() > tabs.getY()) {
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
                changeHeight(partys[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_EDIT && resultCode==RESULT_CODE_EDIT) {
            user = (User)data.getSerializableExtra("updateduser");
            initData();
        }
    }

    void updateHeader() {
        NetworkManager.getInstance().getUser(getApplicationContext(), user.id, new NetworkManager.OnResultListener<User>() {
            @Override
            public void onSuccess(User result) {
                user = result;
                initData();
            }

            @Override
            public void onFail(String response) {
                Toast.makeText(UserActivity.this, "유저정보 업데이트 실패\n실패코드 : " + response, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        GlideUrl glideUrl = null;
        if (user.has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + user.photo);
        } else if (!user.has_photo && user.provider.equals("facebook")) {
            glideUrl = new GlideUrl(user.photo);
        }
        if(glideUrl!=null) {
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

        if(user.id != null && !user.id.equals(PropertyManager.getInstance().getUser().id)) {
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

    public void changeHeight(int partyCount) {
        int height = (int)Math.ceil((partyCount * 129) * getResources().getDisplayMetrics().density);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenHeight = size.y - (int)Math.ceil(285 * getResources().getDisplayMetrics().density);
        //285 means statusbar + header + tablayout height
        if(screenHeight > height)
            height = screenHeight;
        setPagerHeight(height);
    }

    public User getUser() {
        return user;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
