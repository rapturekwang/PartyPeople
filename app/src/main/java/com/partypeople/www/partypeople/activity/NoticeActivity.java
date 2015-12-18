package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.fragment.TOSFragment;
import com.partypeople.www.partypeople.fragment.UserInfoPolicyFragment;
import com.partypeople.www.partypeople.utils.Constants;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView title = (TextView)findViewById(R.id.text_title);

        Intent intent = getIntent();
        int call = intent.getIntExtra("call", Constants.CALL_TOS);

        if(call==Constants.CALL_TOS) {
            title.setText("이용 약관");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TOSFragment.newInstance("이용약관")).commit();
        } else if(call==Constants.CALL_POLICY) {
            title.setText("개인정보 취급방침");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserInfoPolicyFragment.newInstance("개인정보")).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
