package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.fragment.MainTabFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.adapter.MainTabAdapter;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;
import com.partypeople.www.partypeople.utils.DateUtil;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    User user;
    TabLayout tabs;
    ViewPager pager;
    DrawerLayout mDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle mDrawerToggle;
    FloatingActionButton fab;
    FrameLayout layout;
    MainTabFragment fragment = MainTabFragment.newInstance(3);
    PropertyManager propertyManager = PropertyManager.getInstance();
    RelativeLayout relativeLayout;
    TextView name, email, address;
    Button headerBtn;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (FrameLayout)findViewById(R.id.container);
        user = propertyManager.getUser();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(propertyManager.isLogin()) {
                Intent intent = new Intent(MainActivity.this, MakePartyActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show();
            }
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.drawer);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.drawer,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                fab.setTranslationX(slideOffset * 200);
            }
        };

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);
        mDrawer.setDrawerListener(mDrawerToggle);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        MainTabAdapter adpater = new MainTabAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);
        pager.setOffscreenPageLimit(2);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.tab_name);
        for (int i = 0; i < Constants.NUM_OF_MAIN_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }

        navigationView = (NavigationView)findViewById(R.id.navi);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.inflateHeaderView(R.layout.view_navigation_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (propertyManager.isLogin()) {
                    NetworkManager.getInstance().getUser(MainActivity.this, propertyManager.getUser().id, new NetworkManager.OnResultListener<User>() {
                        @Override
                        public void onSuccess(final User result) {
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            intent.putExtra("user", result);
                            startActivity(intent);
                            mDrawer.closeDrawer(GravityCompat.START);
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(MainActivity.this, "네트워크 상태를 체크해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        headerBtn = (Button)header.findViewById(R.id.btn_login);
        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("startfrom", Constants.START_FROM_MAIN);
                startActivity(intent);
            }
        });

        name = (TextView)header.findViewById(R.id.text_user_name);
        email = (TextView)header.findViewById(R.id.text_user_email);
        address = (TextView)header.findViewById(R.id.text_user_address);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        setNavigation();
    }

    public void setNavigation() {
        ImageView imgView = (ImageView)header.findViewById(R.id.img_profile);
        if(propertyManager.getUser().has_photo) {
            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            GlideUrl glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + propertyManager.getUser().photo);
            Glide.with(this)
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.profile_img)
                    .error(R.drawable.profile_img)
                    .transform(new CircleTransform(this))
                    .into(imgView);
        }

        if(!propertyManager.isLogin()) {
            navigationView.getMenu().getItem(1).setEnabled(false);
            navigationView.getMenu().getItem(2).setEnabled(false);
            navigationView.getMenu().getItem(3).setEnabled(false);
            navigationView.getMenu().getItem(5).setEnabled(false);
            headerBtn.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        } else {
            navigationView.getMenu().getItem(1).setEnabled(true);
            navigationView.getMenu().getItem(2).setEnabled(true);
            navigationView.getMenu().getItem(3).setEnabled(true);
            navigationView.getMenu().getItem(5).setEnabled(true);
            headerBtn.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

            name.setText(propertyManager.getUser().name);
            email.setText(propertyManager.getUser().email);
            address.setText(propertyManager.getUser().address);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.home :
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.theme :
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.user :
                NetworkManager.getInstance().getUser(MainActivity.this, propertyManager.getUser().id, new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(final User result) {
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra("user", result);
                        startActivity(intent);
                        mDrawer.closeDrawer(GravityCompat.START);
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(MainActivity.this, "네트워크 상태를 체크해 주세요", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.make_party :
                startActivity(new Intent(MainActivity.this, MakePartyActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.guide :
                intent = new Intent(MainActivity.this, IntroActivity.class);
                intent.putExtra("startFrom", Constants.START_FROM_NAVIGATION);
                startActivity(intent);
                mDrawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.setting :
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
        return false;
    }

    EditText keywordView;
    ShareActionProvider mActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.search_text);

        View view = MenuItemCompat.getActionView(item);
        keywordView = (EditText)view.findViewById(R.id.edit_keyword);
        ImageView btn = (ImageView)view.findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordView.getText().toString();
                Toast.makeText(MainActivity.this, "Keyword : " + keyword, Toast.LENGTH_SHORT).show();
                pager.setVisibility(View.GONE);
                tabs.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        });

        mActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //doShareAction();

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                pager.setVisibility(View.VISIBLE);
                tabs.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
