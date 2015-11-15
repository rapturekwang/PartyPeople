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

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.view.FollowItemView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowTabFragment extends Fragment {
    private static final String ARG_INDEX = "index";
    private int index;
    private GridView gridView;
    gridAdapter mAdapter;
    String id;

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
        View view = inflater.inflate(R.layout.fragment_follow_tab, container, false);

        gridView = (GridView)view.findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        return view;
    }

    public class gridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FollowItemView view;
            if (convertView == null) {
                view =  new FollowItemView(parent.getContext());
            } else {
                view = (FollowItemView) convertView;
            }
            view.setItemData("정광희", "서울시 관악구", "개최수/참여수", "");

            return view;
        }
    }
}
