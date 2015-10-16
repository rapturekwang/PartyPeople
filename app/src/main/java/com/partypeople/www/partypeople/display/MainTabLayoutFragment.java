package com.partypeople.www.partypeople.display;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.partypeople.www.partypeople.data.PartyItemData;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainTabFragmentAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabLayoutFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    MainTabFragmentAdapter mAdapter;


    public static MainTabLayoutFragment newInstance(String name) {
        MainTabLayoutFragment fragment = new MainTabLayoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MainTabLayoutFragment() {
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
        mAdapter = new MainTabFragmentAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "pushed party number : " + position, Toast.LENGTH_SHORT).show();
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
            d.progressText = d.progress+"% 모금됨";
            d.dueDate = "7일 남음";
            mAdapter.add(d);
        }
    }
}
