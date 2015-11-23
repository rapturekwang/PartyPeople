package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    List<Party> partyList = new ArrayList<Party>();
    ListView listView;
    int index;

    UserAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        listView = (ListView)v.findViewById(R.id.listView);
        mAdapter = new UserAdapter(getActivity(), index , new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                ((UserActivity)getActivity()).setTabCurrent(tabId);
            }
        });
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    ((UserActivity) getActivity()).setTabWidgetVisible(false);
                } else {
                    ((UserActivity) getActivity()).setTabWidgetVisible(true);
                }
            }
        });
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                Party party = partyList.get(position-2);
                i.putExtra("party", party);
                Log.d("UserFragment", "pushed : " + (position-2));
                startActivity(i);
            }
        });

        initData();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
    }

    private void initData() {
        NetworkManager.getInstance().getPartys(getContext(), new NetworkManager.OnResultListener<Party[]>() {
            @Override
            public void onSuccess(Party[] result) {
                for (int i=0 ;i<result.length; i++) {
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