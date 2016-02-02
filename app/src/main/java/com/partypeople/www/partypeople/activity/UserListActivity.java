package com.partypeople.www.partypeople.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.view.FollowItemView;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private GridView gridView;
    gridAdapter mAdapter;
    List<String> participants = new ArrayList<>();
    LoadingDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

        gridView = (GridView)findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "loading");
                NetworkManager.getInstance().getUser(UserListActivity.this, mAdapter.list.get(position).id, new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(final User result) {
                        Intent intent = new Intent(UserListActivity.this, UserActivity.class);
                        intent.putExtra("user", result);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(UserListActivity.this, response, Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                });
            }
        });

        initData();
    }

    void initData() {
        Intent intent = getIntent();
        participants = intent.getStringArrayListExtra("userlist");

        for (int i = 0; i < participants.size(); i++) {
            NetworkManager.getInstance().getUser(UserListActivity.this, participants.get(i), new NetworkManager.OnResultListener<User>() {
                @Override
                public void onSuccess(User result) {
                    mAdapter.add(result);
                }

                @Override
                public void onFail(String response) {
                    Toast.makeText(UserListActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public class gridAdapter extends BaseAdapter {
        List<User> list = new ArrayList<User>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void add(User user) {
            list.add(user);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FollowItemView view;
            if (convertView == null) {
                view =  new FollowItemView(parent.getContext());
            } else {
                view = (FollowItemView) convertView;
            }
            view.setItemData(list.get(position));

            return view;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }
}
