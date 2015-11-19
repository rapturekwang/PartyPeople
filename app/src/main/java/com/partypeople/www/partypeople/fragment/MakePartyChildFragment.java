package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.view.RewordItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakePartyChildFragment extends Fragment {
    LinearLayout linearLayout;

    //List<RewordItemView> listReword = new ArrayList<RewordItemView>();
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
        //listReword.add(new RewordItemView(getContext()));
    }

    public MakePartyChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_party_child, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        linearLayout.addView(new RewordItemView(getContext()));
//        for(int i=0; i<listReword.size(); i++) {
//            linearLayout.addView(listReword.get(i));
//        }

        Button btn = (Button)view.findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                RewordItemView rewordItemView = new RewordItemView(getContext());
                linearLayout.addView(rewordItemView);
//                listReword.add(rewordItemView);
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
