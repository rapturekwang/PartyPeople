package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;
import com.partypeople.www.partypeople.data.PayMethod;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTwoFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    RewordViewAdapter mAdapter;

    public static DetailTwoFragment newInstance(String name) {
        DetailTwoFragment fragment = new DetailTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailTwoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_detail_two, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);

        initData();

        return view;
    }

    private void initData() {
        for(int i=0; i<3; i++) {
            PayMethod payMethod = new PayMethod();
            payMethod.price = 10000;
            payMethod.title = "FREE 음료 1개, 스낵";
            mAdapter.add(payMethod);
        }
    }
}
