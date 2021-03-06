package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.FollowActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.view.FollowItemView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowTabFragment extends Fragment {
    private static final String ARG_INDEX = "index";
    private int index;
    private GridView gridView;
    gridAdapter mAdapter;
    String id;
    LoadingDialogFragment dialogFragment;

    public static FollowTabFragment newInstance(int index) {
        FollowTabFragment fragment = new FollowTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        gridView = (GridView)view.findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getFragmentManager(), "loading");
                NetworkManager.getInstance().getUser(getContext(), mAdapter.list.get(position).id, new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(final User result) {
                        Intent intent = new Intent(getContext(), UserActivity.class);
                        intent.putExtra("user", result);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        initData();

        return view;
    }

    void initData() {
        FollowActivity activity = (FollowActivity)getActivity();
        if(activity.getUser().follower.size()!=0 && index==1) {
            for (int i = 0; i < activity.getUser().follower.size(); i++) {
                NetworkManager.getInstance().getUser(getContext(), activity.getUser().follower.get(i).from, new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(User result) {
                        mAdapter.add(result);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        if(activity.getUser().following.size()!=0 && index==0) {
            for (int i = 0; i < activity.getUser().following.size(); i++) {
                NetworkManager.getInstance().getUser(getContext(), activity.getUser().following.get(i).to, new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(User result) {
                        mAdapter.add(result);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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
    public void onPause() {
        super.onPause();
        if(dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }
}
