package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Area;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    ArrayAdapter<String> mCityAdapter, mGuAdapter;
    List<Area> areaList = new ArrayList<Area>();
    PropertyManager propertyManager = PropertyManager.getInstance();
    ImageView imageView;
    TextView name, tel;
    File mSavedFile;
    public static final int REQUEST_CODE_CROP = 0;
    String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        name = (TextView)findViewById(R.id.text_name);
        tel = (TextView)findViewById(R.id.text_address);

        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            User user = new User();
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") && tel.getText().toString().equals("") && location.equals("") && mSavedFile==null) {
                    Toast.makeText(EditProfileActivity.this, "아무런 값도 입력하지 않으셨습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!name.getText().toString().equals("")) {
                    user.name = name.getText().toString();
                }
                Log.d("EditProfileActivity", "test: " + tel.getText().toString());
                if(!tel.getText().toString().equals("")) {
                    user.tel = Double.parseDouble(tel.getText().toString());
                }
                if(!location.equals("")) {
                    user.address = location;
                }
                if(!name.getText().toString().equals("") || !tel.getText().toString().equals("") || !location.equals("")) {
                    Log.d("EditProfileActivity", "call putuser");
                    user.has_photo = propertyManager.getUser().has_photo;
                    NetworkManager.getInstance().putUser(EditProfileActivity.this, user, new NetworkManager.OnResultListener<UserResult>() {
                        @Override
                        public void onSuccess(UserResult result) {
                            propertyManager.setUser(result.data);
                            if (mSavedFile == null) {
                                Intent intent = new Intent();
                                intent.putExtra("updateduser", result.data);
                                setResult(UserActivity.RESULT_CODE_EDIT, intent);
                                finish();
                                Toast.makeText(EditProfileActivity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(EditProfileActivity.this, "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if(mSavedFile!=null) {
                    Log.d("EditProfileActivity", "call putuserimage");
                    NetworkManager.getInstance().putUserImage(EditProfileActivity.this, mSavedFile, propertyManager.getUser().id, new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            NetworkManager.getInstance().getUser(EditProfileActivity.this, propertyManager.getUser().id, new NetworkManager.OnResultListener<User>() {
                                @Override
                                public void onSuccess(User result) {
                                    propertyManager.setUser(result);
                                    Intent intent = new Intent();
                                    intent.putExtra("updateduser", result);
                                    setResult(1, intent);
                                    finish();
                                    Toast.makeText(EditProfileActivity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(EditProfileActivity.this, "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(EditProfileActivity.this, "통신상태가 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        imageView = (ImageView)findViewById(R.id.img_profile);

        ImageView imgBtn = (ImageView)findViewById(R.id.img_btn_profile);
        imgBtn.setOnClickListener(new View.OnClickListener() {
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
                photoPickerIntent.putExtra("noFaceDetection", true);
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
            }
        });

        imgBtn = (ImageView)findViewById(R.id.img_btn_bg);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
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
            Glide.with(this)
                    .load(mSavedFile)
                    .placeholder(R.drawable.default_profile)
                    .transform(new CircleTransform(this))
                    .into(imageView);
        }
    }

    private Uri getTempUri() {
        mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);

        return Uri.fromFile(mSavedFile);
    }

    private void setDateSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.spinner_city);
        mCityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        mCityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(mCityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    location = mCityAdapter.getItem(position);
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
        mGuAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_item);
        mGuAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(mGuAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    location = location + " " + mGuAdapter.getItem(position);
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
