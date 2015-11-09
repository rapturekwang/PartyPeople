package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;

public class UserFragment extends Fragment {

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
                    mAdapter.add(result[i]);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
}