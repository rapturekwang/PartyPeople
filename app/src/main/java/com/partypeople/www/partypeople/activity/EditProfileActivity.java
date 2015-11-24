package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Area;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    ArrayAdapter<String> mCityAdapter, mGuAdapter;
    List<Area> areaList = new ArrayList<Area>();
    PropertyManager propertyManager = PropertyManager.getInstance();
    User user = new User();
    ImageView imageView;
    File mSavedFile;
    public static final int REQUEST_CODE_CROP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView name = (TextView)findViewById(R.id.text_name);
        TextView email = (TextView)findViewById(R.id.text_address);

        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = propertyManager.getUser();
                propertyManager.setUser(user);
                finish();
            }
        });

        imageView = (ImageView)findViewById(R.id.img_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("crop", "true");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                photoPickerIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                photoPickerIntent.putExtra("aspectX", imageView.getWidth());
                photoPickerIntent.putExtra("aspectY", imageView.getHeight());
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
            }
        });

        setDateSpinner();

        mCityAdapter.add("시/도");
        mGuAdapter.add("군/구");
        NetworkManager.getInstance().getLocalInfo(this, 1, "L", 0, new NetworkManager.OnResultListener<LocalAreaInfo>() {
            @Override
            public void onSuccess(LocalAreaInfo result) {
                for (Area s : result.areas.area) {
                    mCityAdapter.add(s.upperDistName);
                    areaList.add(s);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            imageView.setImageBitmap(bm);
        }
    }

    private Uri getTempUri() {
        mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);

        return Uri.fromFile(mSavedFile);
    }

    private void setDateSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.spinner_city);
        mCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mCityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    user.location = mCityAdapter.getItem(position);
                }
                Area area;
                mGuAdapter.clear();
                mGuAdapter.add("군/구");
                for (int i = 0; i < areaList.size(); i++) {
                    area = areaList.get(i);
                    if (area.upperDistName.equals(mCityAdapter.getItem(position))) {
                        NetworkManager.getInstance().getLocalInfo(EditProfileActivity.this, 1, "M", area.upperDistCode, new NetworkManager.OnResultListener<LocalAreaInfo>() {
                            @Override
                            public void onSuccess(LocalAreaInfo result) {
                                for (Area s : result.areas.area) {
                                    if (!s.middleDistName.equals(""))
                                        mGuAdapter.add(s.middleDistName);
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner = (Spinner)findViewById(R.id.spinner_gu);
        mGuAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_item);
        mGuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mGuAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    user.location = user.location + " " + mGuAdapter.getItem(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
