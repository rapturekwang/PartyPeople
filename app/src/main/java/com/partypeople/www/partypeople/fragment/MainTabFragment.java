package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.view.MainTabHeaderView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment extends Fragment {

    List<Party> partyList = new ArrayList<Party>();
    private static final String ARG_INDEX = "index";
    private int index;
    ListView listView;
    MainFragmentAdapter mAdapter;
    String id;

    public static MainTabFragment newInstance(int index) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public MainTabFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //((TextView)view.findViewById(R.id.text_name)).setText(mName);
        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new MainFragmentAdapter(getContext());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                Party party = partyList.get(position);
                i.putExtra("party", party);
                startActivity(i);
            }
        });

        switch (getArguments().getInt(ARG_INDEX)) {
            case 0:
                initData();
                break;
            case 1:
                MainTabHeaderView header = new MainTabHeaderView(getContext());
                listView.addHeaderView(header);
                initData();
                break;
            case 2:
                initData();
                break;
        }

        return view;
    }

    private void initData() {
        NetworkManager.getInstance().getPartys(getContext(), new NetworkManager.OnResultListener<Party[]>() {
            @Override
            public void onSuccess(Party[] result) {
                for (int i = 0; i < result.length; i++) {
                    partyList.add(result[i]);
                    mAdapter.add(result[i]);
                }
            }

            @Override
            public void onFail(int code) {
                for (int i = 0; i < 5 ; i++) {
                    Party d = new Party();
                    d.name = "Come to House Party!";
                    d.date = "2015-12-04T02:11:11";
                    d.location = "서울시 서초구";
                    d.expect_pay = 25000;
                    partyList.add(d);
                    mAdapter.add(d);
                }
            }
        });
    }
}
