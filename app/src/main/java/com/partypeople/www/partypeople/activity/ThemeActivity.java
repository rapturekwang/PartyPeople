package com.partypeople.www.partypeople.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.view.GridItemView;

import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends AppCompatActivity {

    DrawerLayout mDrawer;
    GridView gridView;
    gridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);

        gridView = (GridView)findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    for(int i = 1; i<mAdapter.getCount(); i++) {
                        gridView.setItemChecked(i, gridView.isItemChecked(0));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_theme, menu);

        MenuItem item = menu.findItem(R.id.select);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SparseBooleanArray array = gridView.getCheckedItemPositions();
                List<String> pathList = new ArrayList<String>();
                for (int index = 0; index < array.size(); index++) {
                    int position = array.keyAt(index);
                    if(array.get(position)) {
                        String s = "" + (position+1);
                        pathList.add(s);
                    }
                }

                Toast.makeText(ThemeActivity.this, "테마 : " + pathList, Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public class gridAdapter extends BaseAdapter{
        private String[] GRID_DATA = new String[] {
                "올~ㅋ",
                "테마1",
                "테마2",
                "테마3",
                "테마4",
                "테마5"
        };

        @Override
        public int getCount() {
            return GRID_DATA.length;
        }

        @Override
        public Object getItem(int position) {
            return GRID_DATA[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridItemView view;
            if (convertView == null) {
                view =  new GridItemView(parent.getContext());
            } else {
                view = (GridItemView) convertView;
            }
            view.setGridItem(GRID_DATA[position]);
            return view;
        }
    }
}
