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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.data.Data;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.adapter.MainTabAdapter;
import com.partypeople.www.partypeople.R;
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
    PropertyManager propertyManager = PropertyManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.tab_name);
        for (int i = 0; i < Constants.NUM_OF_MAIN_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }

        navigationView = (NavigationView)findViewById(R.id.navi);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.view_navigation_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(propertyManager.isLogin()) {
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    mDrawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        Button btn = (Button)header.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        TextView name = (TextView)header.findViewById(R.id.text_user_name);
        TextView email = (TextView)header.findViewById(R.id.text_user_email);

        if(!propertyManager.isLogin()) {
            navigationView.getMenu().getItem(1).setEnabled(false);
            navigationView.getMenu().getItem(2).setEnabled(false);
            navigationView.getMenu().getItem(3).setEnabled(false);
            navigationView.getMenu().getItem(5).setEnabled(false);
            btn.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        } else {
            navigationView.getMenu().getItem(1).setEnabled(true);
            navigationView.getMenu().getItem(2).setEnabled(true);
            navigationView.getMenu().getItem(3).setEnabled(true);
            navigationView.getMenu().getItem(5).setEnabled(true);
            btn.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);

            name.setText(propertyManager.getUser().data.name);
            email.setText(propertyManager.getUser().data.email);
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
            case R.id.party :
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                mDrawer.closeDrawer(GravityCompat.START);
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
            }
        });

        mActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //doShareAction();

        return super.onCreateOptionsMenu(menu);
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
