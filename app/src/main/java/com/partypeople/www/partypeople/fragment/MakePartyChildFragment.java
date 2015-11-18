package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MakePartyChildAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakePartyChildFragment extends Fragment {
    ListView listView;
    MakePartyChildAdapter mAdapter;

    private static final String ARG_NAME = "index";
    private int mIndex;

    public static MakePartyChildFragment newInstance(int index) {
        MakePartyChildFragment fragment = new MakePartyChildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_NAME);
        }
    }

    public MakePartyChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_party_child, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new MakePartyChildAdapter(getContext());
        listView.setAdapter(mAdapter);

        Button btn = (Button)view.findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        if(getArguments().getInt(ARG_NAME)!=2) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
