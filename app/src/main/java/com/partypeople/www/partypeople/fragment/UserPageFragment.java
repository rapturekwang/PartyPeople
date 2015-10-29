package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.adapter.UserPageFragmentAdapter;
import com.partypeople.www.partypeople.data.PartyItemData;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class UserPageFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    UserPageFragmentAdapter mAdapter;


    public static UserPageFragment newInstance(String name) {
        UserPageFragment fragment = new UserPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public UserPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        //((TextView)view.findViewById(R.id.text_name)).setText(mName);
        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new UserPageFragmentAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                startActivity(i);
            }
        });

        initData();

        return view;
    }

    private void initData() {
        for (int i = 0; i < 5 ; i++) {
            PartyItemData d = new PartyItemData();
            d.title = "Come to House Party!";
            d.date = "5월 7일 / 19:00-21:30";
            d.partyImg = getResources().getDrawable(R.drawable.demo_img);
            d.location = "서울시 서초구";
            d.price = "$25";
            d.progress = 50;
            d.progressText = d.progress+"%";
            d.dueDate = "7일 남음";
            d.currentState = "모금중";
            mAdapter.add(d);
        }
    }
}