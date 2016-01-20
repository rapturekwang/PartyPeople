package com.partypeople.www.partypeople.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.AlarmAdapter;

public class AlarmActivity extends AppCompatActivity {
    ListView listView;
    AlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        listView = (ListView)findViewById(R.id.listView);
        mAdapter = new AlarmAdapter();
        listView.setAdapter(mAdapter);

        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void initData() {
        mAdapter.add("모금이 성공하였습니다1");
        mAdapter.add("모금이 성공하였습니다2");
        mAdapter.add("모금이 성공하였습니다3");
        mAdapter.add("모금이 성공하였습니다4");
    }
}
