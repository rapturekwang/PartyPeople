package com.partypeople.www.partypeople.activity;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);

        gridView = (GridView)findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
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

    public class gridAdapter extends BaseAdapter{
        private String[] GRID_DATA = new String[] {
                "테마1",
                "테마2",
                "테마3",
                "테마4",
                "테마5",
                "테마6",
                "테마7",
                "테마8"
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
