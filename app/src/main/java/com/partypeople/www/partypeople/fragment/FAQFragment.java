package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.FAQAdapter;

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

        return view;
    }
}
