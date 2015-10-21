package com.partypeople.www.partypeople.display;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.adapter.MainTabLayoutAdapter;
import com.partypeople.www.partypeople.R;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager pager;
    DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        MainTabLayoutAdapter adpater = new MainTabLayoutAdapter(getSupportFragmentManager());
        pager.setAdapter(adpater);

        tabs.setupWithViewPager(pager);

        tabs.removeAllTabs();
        String[] tabTitle = getResources().getStringArray(R.array.tab_name);
        for (int i = 0; i < Constants.NUM_OF_TAB; i++) {
            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
        }
    }

    EditText keywordView;
    ShareActionProvider mActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.search);

        View view = MenuItemCompat.getActionView(item);
        keywordView = (EditText)view.findViewById(R.id.edit_keyword);
        Button btn = (Button)view.findViewById(R.id.btn_search);
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
