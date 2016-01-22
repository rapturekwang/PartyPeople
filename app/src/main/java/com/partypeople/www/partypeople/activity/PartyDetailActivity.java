package com.partypeople.www.partypeople.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.cocosw.bottomsheet.BottomSheet;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.DetailImagePagerAdapter;
import com.partypeople.www.partypeople.adapter.DetailTabAdapter;
import com.partypeople.www.partypeople.data.Like;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.fragment.DetailOneFragment;
import com.partypeople.www.partypeople.fragment.DetailThreeFragment;
import com.partypeople.www.partypeople.fragment.DetailTwoFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;


public class PartyDetailActivity extends AppCompatActivity {

    TabLayout tabs, fakeTabs;
    ViewPager pager, imagePager;
    TextView titleView, dateView, locationView, priceView, totalPriceView, progressView, duedateView;
    CheckBox chboxView, chboxView2;
    ImageView imageView;
    ProgressBar progressBar;
    DateUtil dateUtil = DateUtil.getInstance();
    public Party party;
    DetailTabAdapter mAdpater;
    BottomSheet sheet;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    int startFrom;

    int[] ids = {0,
            R.drawable.main_theme_1,
            R.drawable.main_theme_2,
            R.drawable.main_theme_3,
            R.drawable.main_theme_4,
            R.drawable.main_theme_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);

        Intent intent = getIntent();
        party = (Party)intent.getSerializableExtra("party");
        startFrom = intent.getIntExtra("startFrom", -1);

        imagePager = (ViewPager)findViewById(R.id.image_pager);
        DetailImagePagerAdapter adapter = new DetailImagePagerAdapter(this, party.photos, party.has_photos);
        imagePager.setAdapter(adapter);

        tabs = (TabLayout)findViewById(R.id.tabs);
        fakeTabs = (TabLayout)findViewById(R.id.fake_tabs);
        pager = (ViewPager)findViewById(R.id.container);
        mAdpater = new DetailTabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdpater);

        pager.setOffscreenPageLimit(Constants.NUM_OF_DETAIL_TAB-1);

        setPagerHeight(5000);

        tabs.setupWithViewPager(pager);
        fakeTabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        fakeTabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.detail_tab_name);
        for (int i = 0; i < Constants.NUM_OF_DETAIL_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
            fakeTabs.addTab(fakeTabs.newTab().setText(tabTitle[i]));
        }

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
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(5000);
                        ((DetailOneFragment) mAdpater.getItem(position)).changeHeight();
                        break;
                    case 1:
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(200 + 230 * party.amount_method.size());
                        ((DetailTwoFragment) mAdpater.getItem(position)).changeHeight();
                        break;
                    case 2:
                        Log.d("PartyDetail", position + "selected");
                        setPagerHeight(350 + 300 * party.comments.size());
                        ((DetailThreeFragment) mAdpater.getItem(position)).changeHeight();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        
        Button btn = (Button)findViewById(R.id.btn_parti);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PropertyManager.getInstance().isLogin()) {
                    for(int i=0;i<party.members.size();i++) {
                        if (PropertyManager.getInstance().getUser().id.equals(party.members.get(i).id)) {
                            Toast.makeText(PartyDetailActivity.this, "이미 참여한 모임 입니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    Intent intent = new Intent(PartyDetailActivity.this, ParticipateActivity.class);
                    intent.putExtra("party", party);
                    startActivity(intent);
                } else {
                    Toast.makeText(PartyDetailActivity.this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PartyDetailActivity.this, LoginActivity.class);
                    intent.putExtra("startfrom", Constants.START_FROM_MAIN);
                    startActivity(intent);
                }
            }
        });
        ImageView img_btn = (ImageView)findViewById(R.id.img_btn_share);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        chboxView = (CheckBox)findViewById(R.id.chbox_img_bookmark);
        chboxView2 = (CheckBox)findViewById(R.id.chbox_bookmark);

        chboxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chboxView2.setChecked(isChecked);
            }
        });
        chboxView2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chboxView.setChecked(isChecked);
                changeLike(isChecked);
            }
        });

        initView();
        initData();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("PartyDetailActivity", "success");
            }

            @Override
            public void onCancel() {
                Log.d("PartyDetailActivity", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("PartyDetailActivity", "onerror");
            }
        });

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_app_key), getResources().getString(R.string.twitter_secret));
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        if(startFrom==Constants.START_FROM_MAKE_PARTY)
            showDialog(0);
    }

    private void changeLike(boolean isChecked) {
        if(isChecked) {
            if(party.likes.size()>0) {
                for (int i = 0; i < party.likes.size(); i++) {
                    if (party.likes.get(i).user.equals(PropertyManager.getInstance().getUser().id)) {
                        return;
                    }
                }
            }
            NetworkManager.getInstance().takeLike(PartyDetailActivity.this, party.id, new NetworkManager.OnResultListener<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(PartyDetailActivity.this, "즐겨찾기가 추가되었습니다", Toast.LENGTH_SHORT).show();
                    Like like = new Like();
                    like.user = PropertyManager.getInstance().getUser().id;
                    party.likes.add(like);
                    initData();
                }

                @Override
                public void onFail(int code) {

                }
            });
        } else {
            NetworkManager.getInstance().takeUnlike(PartyDetailActivity.this, party.id, new NetworkManager.OnResultListener<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(PartyDetailActivity.this, "즐겨찾기가 취소되었습니다", Toast.LENGTH_SHORT).show();
                    if(party.likes.size()>0) {
                        for (int i = 0; i < party.likes.size(); i++) {
                            if (party.likes.get(i).user.equals(PropertyManager.getInstance().getUser().id)) {
                                party.likes.remove(i);
                            }
                        }
                    }
                    initData();
                }

                @Override
                public void onFail(int code) {

                }
            });
        }
    }

    private void initData() {
        titleView.setText(party.name);
        titleView.setCompoundDrawablesWithIntrinsicBounds(ids[party.themes[0]], 0, 0, 0);
        dateView.setText(dateUtil.changeToViewFormat(party.start_at));
        String[] array = party.location.split(" ");
        if(array.length==1)
            locationView.setText(array[0]);
        else
            locationView.setText(array[0] + " " + array[1]);
        priceView.setText(party.amount_method.get(0).price + "원");
        int progress = (int)((party.members.size()*party.amount_method.get(0).price)/party.amount_expect*100);
        progressView.setText(progress + "% 모임");
        progressBar.setProgress(progress);
        duedateView.setText(dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.pay_end_at) + "일 남음");
        totalPriceView.setText((int) party.amount_expect + "원");
        if(party.likes!=null) {
            chboxView.setText("" + party.likes.size());
            if (PropertyManager.getInstance().isLogin() && party.likes.size() > 0) {
                for (int i = 0; i < party.likes.size(); i++) {
                    if (party.likes.get(i).user.equals(PropertyManager.getInstance().getUser().id)) {
                        chboxView.setChecked(true);
                        break;
                    }
                }
            }
        }

        Glide.with(this)
                .load(NetworkManager.getInstance().URL_SERVER + party.photos.get(0))
                .placeholder(Color.TRANSPARENT)
                .error(Color.TRANSPARENT)
                .into(imageView);
    }

    private void initView() {
        titleView = (TextView)findViewById(R.id.text_title);
        dateView = (TextView)findViewById(R.id.text_date);
        locationView = (TextView)findViewById(R.id.text_location);
        priceView = (TextView)findViewById(R.id.text_payment);
        totalPriceView = (TextView)findViewById(R.id.text_total_price);
        progressView = (TextView)findViewById(R.id.text_progress);
        duedateView = (TextView)findViewById(R.id.text_duedate);
        imageView = (ImageView)findViewById(R.id.image_party);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void setPagerHeight(int height) {
        LayoutParams params = pager.getLayoutParams();
        params.height=height;
        pager.setLayoutParams(params);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(final int position, Bundle args) {
        sheet = new BottomSheet.Builder(this, R.style.BottomSheet_CustomizedDialog).grid().title("\n친구들과 모임을 공유해 보세요\n").sheet(R.menu.list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PartyDetailActivity.this.onClick(getDescription(), which);
            }
        }).grid().build();
        return sheet;
    }

    void shareWithFacebook(String description) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(party.name)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse("http://partypeople.me:3000"))
                    .setImageUrl(Uri.parse(NetworkManager.getInstance().URL_SERVER + party.photos.get(0)))
                    .build();
            shareDialog.show(content);
        }
    }

    void shareWithKakao(String description) {
        try {
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText("[" + party.name + "]\n" + description)
                    .addImage(NetworkManager.getInstance().URL_SERVER + party.photos.get(0), 300, 200)
                    .addWebButton("Party People 구경하기", "http://partypeople.me:3000");
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
        } catch (KakaoParameterException e) {
            e.getMessage();
        }
    }

    void shareWithTwitter(String description) {
        Bitmap bmp = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStorageDirectory(), "temp_" + System.currentTimeMillis() + ".jpg");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new TweetComposer.Builder(this)
                    .text("[" + party.name + "]\n" + description)
                    .image(bmpUri)
                    .show();
        }
    }

    void onClick(String description, int which) {
        switch (which) {
            case R.id.share_fb:
                shareWithFacebook(description);
                break;
            case R.id.share_kko:
                shareWithKakao(description);
                break;
            case R.id.share_twitter:
                shareWithTwitter(description);
                break;
        }
    }

    String getDescription() {
        String result;

        result = DateUtil.getInstance().getSharingFormat(party.start_at);
        String[] array = party.location.split(" ");
        if(array.length==1)
            result += array[0];
        else
            result = result + " " + array[0] + " " + array[1];

        return result;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party data) {
        party = data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
