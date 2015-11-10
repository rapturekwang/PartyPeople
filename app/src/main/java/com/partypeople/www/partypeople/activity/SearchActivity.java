package com.partypeople.www.partypeople.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.SearchFragment;
import com.partypeople.www.partypeople.fragment.SearchResultFragment;

import org.w3c.dom.Text;

public class SearchActivity extends AppCompatActivity {

    Fragment[] list = {SearchFragment.newInstance("search"),
            SearchResultFragment.newInstance("result")};
    int currentFragment = 0;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayShowTitleEnabled(false);

        initFragment();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, list[0]).commit();
    }

    public void nextFragment() {
        currentFragment++;
        if(currentFragment < list.length) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[currentFragment])
                    .addToBackStack(null)
                    .commit();
        }
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
        currentFragment--;
        super.onBackPressed();
    }
}
