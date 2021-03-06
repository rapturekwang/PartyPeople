package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Area;
import com.partypeople.www.partypeople.data.LocalAreaInfo;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.ThemeItemView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<Area> areaList = new ArrayList<Area>();
    ArrayAdapter<String> mCityAdapter, mGuAdapter;
    GridView gridView;
    gridAdapter mAdapter;
    PropertyManager propertyManager = PropertyManager.getInstance();
    String location="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        Button btn = (Button)findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray array = gridView.getCheckedItemPositions();
                for (int i = 0; i < array.size(); i++) {
                    if (!array.get(array.keyAt(i))) {
                        array.delete(array.keyAt(i));
                        i = -1;
                    }
                }
                int[] theme = new int[array.size()];
                for (int index = 0; index < array.size(); index++) {
                    int position = array.keyAt(index);
                    if (array.get(position)) {
                        theme[index] = position;
                    }
                }

                User user = new User();
                user.themes = theme;
                user.favorite_address = location;
                NetworkManager.getInstance().putUser(SearchActivity.this, user, new NetworkManager.OnResultListener<UserResult>() {
                    @Override
                    public void onSuccess(UserResult result) {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        setResult(1, intent);
                        finish();
                        Toast.makeText(SearchActivity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(SearchActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                });
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
            public void onFail(String response) {

            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0 && gridView.isItemChecked(0)) {
                    for(int i=1;i<6;i++) {
                        gridView.setItemChecked(i, true);
                    }
                } else if(position==0 && !gridView.isItemChecked(0)) {
                    for(int i=1;i<6;i++) {
                        gridView.setItemChecked(i, false);
                    }
                } else if(!gridView.isItemChecked(position) && gridView.isItemChecked(0)){
                    gridView.setItemChecked(0, false);
                } else if(gridView.isItemChecked(1) && gridView.isItemChecked(2) && gridView.isItemChecked(3) &&
                        gridView.isItemChecked(4) && gridView.isItemChecked(5)) {
                    gridView.setItemChecked(0, true);
                }
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

    private void setDateSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.spinner_city);
        mCityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        mCityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(mCityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    location = getCityname(mCityAdapter.getItem(position));
                }
                Area area;
                mGuAdapter.clear();
                mGuAdapter.add("군/구");
                for(int i=0; i<areaList.size(); i++) {
                    area = areaList.get(i);
                    if(area.upperDistName.equals(mCityAdapter.getItem(position))){
                        NetworkManager.getInstance().getLocalInfo(SearchActivity.this, 1, "M", area.upperDistCode, new NetworkManager.OnResultListener<LocalAreaInfo>() {
                            @Override
                            public void onSuccess(LocalAreaInfo result) {
                                for (Area s : result.areas.area) {
                                    if(!s.middleDistName.equals(""))
                                        mGuAdapter.add(s.middleDistName);
                                }
                            }

                            @Override
                            public void onFail(String response) {

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
        mGuAdapter = new ArrayAdapter<String>(SearchActivity.this, R.layout.spinner_item);
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

    public class gridAdapter extends BaseAdapter {
        private int[] ids = {R.drawable.theme_0,
                R.drawable.theme_1,
                R.drawable.theme_2,
                R.drawable.theme_3,
                R.drawable.theme_4,
                R.drawable.theme_5};

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return ids[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ThemeItemView view;
            if (convertView == null) {
                view =  new ThemeItemView(parent.getContext());
            } else {
                view = (ThemeItemView) convertView;
            }
            view.setGridItem(ids[position]);

            return view;
        }
    }

    String getCityname(String city) {
        String changedName = null;
        switch (city) {
            case "충북":
                changedName = "충청북도";
                break;
            case "충남":
                changedName = "충청남도";
                break;
            case "전북":
                changedName = "전라북도";
                break;
            case "전남":
                changedName = "전라남도";
                break;
            case "경북":
                changedName = "경상북도";
                break;
            case "경남":
                changedName = "경상남도";
                break;
            default:
                changedName = city;
                break;
        }

        return changedName;
    }
}
