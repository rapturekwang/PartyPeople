package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.data.PartyItemData;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {
    private static final String ARG_NAME = "name";
    ListView listView;
    TextView header;
    MainFragmentAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String name;


    public static SearchResultFragment newInstance(String name) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchResultFragment() {
        // Required empty public constructor
    }

    TextView nameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        listView = (ListView)view.findViewById(R.id.listView);

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
