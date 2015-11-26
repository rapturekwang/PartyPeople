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
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.ThemeItemView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

//    Fragment[] list = {SearchFragment.newInstance("search"),
//            SearchResultFragment.newInstance("result")};
    int currentFragment = 0;
    List<Area> areaList = new ArrayList<Area>();
    ArrayAdapter<String> mCityAdapter, mGuAdapter;
    GridView gridView;
    gridAdapter mAdapter;
    PropertyManager propertyManager = PropertyManager.getInstance();
    String location;

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
                int[] theme = new int[array.size()];
                for (int index = 0; index < array.size(); index++) {
                    int position = array.keyAt(index);
                    if(array.get(position)) {
                        theme[index] = position;
                    }
                }

                User user = propertyManager.getUser();
                user.theme = theme;
                user.favorite_address = location;
                propertyManager.setUser(user);
                user = new User();
                user.theme = theme;
                user.favorite_address = location;
                NetworkManager.getInstance().putUser(SearchActivity.this, user, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFail(int code) {

                    }
                });

                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        gridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    @Override
    public void onBackPressed() {
        currentFragment--;
        super.onBackPressed();
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
        mGuAdapter = new ArrayAdapter<String>(SearchActivity.this, R.layout.spinner_item);
        mGuAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(mGuAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
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
//            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 110));

            return view;
        }
    }
}
