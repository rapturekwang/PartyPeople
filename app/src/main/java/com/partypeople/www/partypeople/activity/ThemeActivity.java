package com.partypeople.www.partypeople.activity;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.view.GridItemView;

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
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
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
