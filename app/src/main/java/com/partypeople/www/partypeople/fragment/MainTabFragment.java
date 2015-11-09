package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partypeople.www.partypeople.data.PartyItemData;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.location.Area;
import com.partypeople.www.partypeople.location.LocalAreaInfo;
import com.partypeople.www.partypeople.manager.NetworkManager;


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
        NetworkManager.getInstance().getPartys(getContext(), new NetworkManager.OnResultListener<LocalAreaInfo>() {
            @Override
            public void onSuccess(LocalAreaInfo result) {
//                for (Area s : result.areas.area) {
//                    mCityAdapter.add(s.upperDistName);
//                    areaList.add(s);
//                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        for (int i = 0; i < 5 ; i++) {
            PartyItemData d = new PartyItemData();
            d.title = "Come to House Party!";
            d.date = "5월 7일 / 19:00-21:30";
            d.partyImg = getResources().getDrawable(R.drawable.demo_img);
            d.location = "서울시 서초구";
            d.price = "$25";
            d.progress = 50;
            d.progressText = d.progress+"% 모금됨";
            d.dueDate = "7일 남음";
            mAdapter.add(d);
        }
    }
}
