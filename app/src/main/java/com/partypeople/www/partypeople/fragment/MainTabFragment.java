package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.DateUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    MainFragmentAdapter mAdapter;


    public static MainTabFragment newInstance(String name) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
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
            mName = getArguments().getString(ARG_NAME);
        }
//        Log.d("MainTabFragment", DateUtil.getInstance().getCurrentDate());
//        long temp = DateUtil.getInstance().changeStringToLong("2015-11-03T02:11:11");
//        DateUtil d = DateUtil.getInstance();
//        Log.d("MainTabFragment", ""+d.getDiffDay("2015-11-03T02:11:11", d.getCurrentDate()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        //((TextView)view.findViewById(R.id.text_name)).setText(mName);
        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new MainFragmentAdapter();
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
        NetworkManager.getInstance().getPartys(getContext(), new NetworkManager.OnResultListener<Party[]>() {
            @Override
            public void onSuccess(Party[] result) {
                for (int i=0 ;i<result.length; i++) {
                    mAdapter.add(result[i]);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
}
