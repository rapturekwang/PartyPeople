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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.fragment.DetailOneFragment;
import com.partypeople.www.partypeople.fragment.DetailThreeFragment;
import com.partypeople.www.partypeople.fragment.DetailTwoFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.utils.NumberUtil;
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
    RelativeLayout faketabBack;

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
        faketabBack = (RelativeLayout)findViewById(R.id.faketab_background);
        pager = (ViewPager)findViewById(R.id.container);
        mAdpater = new DetailTabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdpater);

        pager.setOffscreenPageLimit(Constants.NUM_OF_DETAIL_TAB-1);

        setPagerHeight(50000);

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
                if(scrollView.getScrollY() + (int)Math.ceil(10 * getResources().getDisplayMetrics().density) > tabs.getY()) {
                    fakeTabs.setVisibility(View.VISIBLE);
                    faketabBack.setVisibility(View.VISIBLE);
                } else {
                    fakeTabs.setVisibility(View.INVISIBLE);
                    faketabBack.setVisibility(View.INVISIBLE);
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
                        setPagerHeight(50000);
                        ((DetailOneFragment) mAdpater.getItem(position)).changeHeight();
                        break;
                    case 1:
                        setPagerHeight(500 + 500 * party.amount_method.size());
                        ((DetailTwoFragment) mAdpater.getItem(position)).changeHeight();
                        break;
                    case 2:
                        setPagerHeight(1000 + 1000 * party.comments.size());
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
                DateUtil dateUtil = DateUtil.getInstance();
                if(dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.amount_end_at) < 0) {
                    Toast.makeText(PartyDetailActivity.this, "모금 기한이 지난 모임입니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(PropertyManager.getInstance().isLogin()) {
                    for(int i=0;i<party.members.size();i++) {
                        if (party.members.get(i).role.equals("MEMBER") && PropertyManager.getInstance().getUser().id.equals(party.members.get(i).id)) {
                            Toast.makeText(PartyDetailActivity.this, "이미 참여한 모임 입니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    Intent intent = new Intent(PartyDetailActivity.this, ParticipateActivity.class);
                    intent.putExtra("party", party);
                    startActivityForResult(intent, Constants.REQUEST_CODE_PARTICIPATE);
                } else {
                    Toast.makeText(PartyDetailActivity.this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PartyDetailActivity.this, LoginActivity.class);
                    intent.putExtra("startfrom", Constants.START_FROM_MAIN);
                    startActivity(intent);
                }
            }
        });
        ImageView imgBtn = (ImageView)findViewById(R.id.img_btn_share);
        imgBtn.setOnClickListener(new View.OnClickListener() {
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
                if(!PropertyManager.getInstance().isLogin()) {
                    Toast.makeText(getApplicationContext(), "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    chboxView2.setChecked(isChecked);
                }
            }
        });
        chboxView2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!PropertyManager.getInstance().isLogin()) {
                    Toast.makeText(getApplicationContext(), "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
                } else {
                    chboxView.setChecked(isChecked);
                    changeLike(isChecked);
                }
            }
        });

        imgBtn = (ImageView)findViewById(R.id.arrow_left);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePager.setCurrentItem(imagePager.getCurrentItem()-1);
            }
        });

        imgBtn = (ImageView)findViewById(R.id.arrow_right);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePager.setCurrentItem(imagePager.getCurrentItem()+1);
            }
        });

        initView();
        initData();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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
                public void onFail(String response) {

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
                public void onFail(String response) {

                }
            });
        }
    }

    private void initData() {
        titleView.setText(party.name);
        titleView.setCompoundDrawablesWithIntrinsicBounds(ids[party.themes[0]], 0, 0, 0);
        dateView.setText(dateUtil.changeToViewFormat(party.start_at));
        if(party.location!=null) {
            String[] array = party.location.split(" ");
            if (array.length == 1)
                locationView.setText(array[0]);
            else
                locationView.setText(array[0] + " " + array[1]);
        } else {
            locationView.setText("");
        }
        priceView.setText(NumberUtil.getInstance().changeToPriceForm((int) party.amount_total) + "원");
        int progress = (int)(party.amount_total/party.amount_expect * 100);
        progressView.setText(progress + "% 모임");
        progressBar.setProgress(progress);
        int dueDate = dateUtil.getDiffDay(dateUtil.getCurrentDate(), party.amount_end_at);
        duedateView.setText(dueDate>=0 ? dueDate + "일 남음" : "모금 종료");
        totalPriceView.setText(NumberUtil.getInstance().changeToPriceForm((int)party.amount_expect) + "원");
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
                    .setContentUrl(Uri.parse("https://www.facebook.com/partypeopleteam"))
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
//                    .addWebButton("Party People 구경하기", "https://play.google.com/store/apps/details?id=com.partypeople.www.partypeople");
                    .addAppButton("파티피플 구경하기");
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
                    .text("[" + party.name + "]\n" + description + "https://www.facebook.com/partypeopleteam")
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
        if(requestCode==Constants.REQUEST_CODE_PARTICIPATE && resultCode==Constants.RESULT_CODE_PARTICIPATE) {
            NetworkManager.getInstance().getParty(this, party.id, new NetworkManager.OnResultListener<PartyResult>() {
                @Override
                public void onSuccess(PartyResult result) {
                    party = result.data;
                    ((DetailOneFragment) mAdpater.getItem(0)).initData();
                }

                @Override
                public void onFail(String response) {
//                    Toast.makeText(PartyDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
