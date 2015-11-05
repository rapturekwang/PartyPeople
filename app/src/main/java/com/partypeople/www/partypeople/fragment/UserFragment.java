package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.PartyItemData;

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
        for (int i = 0; i < 10 ; i++) {
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