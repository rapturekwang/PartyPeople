package com.partypeople.www.partypeople.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.fragment.MakePartyFourFragment;
import com.partypeople.www.partypeople.fragment.MakePartyOneFragment;
import com.partypeople.www.partypeople.fragment.MakePartyThreeFragment;
import com.partypeople.www.partypeople.fragment.MakePartyTwoFragment;
import com.partypeople.www.partypeople.utils.Constants;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MakePartyActivity extends AppCompatActivity {

    public Party party = new Party();
    Fragment[] list = {MakePartyOneFragment.newInstance("one"),
            MakePartyTwoFragment.newInstance("two"),
            MakePartyThreeFragment.newInstance("three"),
            MakePartyFourFragment.newInstance("four")};
    String[] stringList = {"모임 설명", "모임 모금 방식", "본인 인증 및 계좌 정보", "서비스 이용약관 확인"};
    int currentFragment;

    TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_party);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestExternalPermission();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
        currentFragment = 0;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, list[currentFragment]).commit();
        titleView = (TextView)findViewById(R.id.text_title);
        titleView.setText(stringList[currentFragment]);
    }

    public void nextFragment() {
        currentFragment++;
        if(currentFragment < list.length) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, list[currentFragment])
                    .addToBackStack(null)
                    .commit();
            titleView.setText(stringList[currentFragment]);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentFragment>0) {
            currentFragment--;
        }
        titleView.setText(stringList[currentFragment]);
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void requestExternalPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MakePartyActivity.this, "권한사용을 동의하셔야 사진등록을 할수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
