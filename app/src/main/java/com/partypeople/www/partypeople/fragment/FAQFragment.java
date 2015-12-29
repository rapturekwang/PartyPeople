package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.FAQAdapter;
import com.partypeople.www.partypeople.data.Board;
import com.partypeople.www.partypeople.manager.NetworkManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    ExpandableListView listView;
    FAQAdapter mAdapter;

    public static FAQFragment newInstance(String name) {
        FAQFragment fragment = new FAQFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public FAQFragment() {
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
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        listView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        mAdapter = new FAQAdapter();
        listView.setAdapter(mAdapter);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        listView.setIndicatorBounds(width-50, width);

        initData();

        return view;
    }

    public void initData() {
        NetworkManager.getInstance().getBoards(getContext(), new NetworkManager.OnResultListener<Board[]>() {
            @Override
            public void onSuccess(Board[] result) {
                for(int i=0;i<result.length;i++) {
                    if(result[i].category.equals("FAQ")) {
                        mAdapter.addFAQ(result[i]);
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(getContext(), "통신이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
