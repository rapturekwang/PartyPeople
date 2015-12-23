package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;
import com.partypeople.www.partypeople.view.RewordItemEditView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakePartyChildFragment extends Fragment {
    LinearLayout linearLayout;
    List<PayMethod> list = new ArrayList<PayMethod>();

    //List<RewordItemView> listReword = new ArrayList<RewordItemView>();
    private static final String ARG_NAME = "index";
    private int mIndex;
    ImageView btnAdd, btnRemove;

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
        //linearLayout.removeAllViews();
        if(linearLayout.getChildCount()==0) {
            RewordItemEditView rewordItemEditView = new RewordItemEditView(getContext());
            linearLayout.addView(rewordItemEditView);
        }

        btnAdd = (ImageView)view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewordItemEditView rewordItemEditView = new RewordItemEditView(getContext());
                rewordItemEditView.setItemData(null, linearLayout.getChildCount());
                linearLayout.addView(rewordItemEditView);
                if(linearLayout.getChildCount()==5) {
                    btnAdd.setVisibility(View.GONE);
                }
            }
        });

        btnRemove = (ImageView)view.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getChildCount()>1) {
                    linearLayout.removeViewAt(linearLayout.getChildCount()-1);
                }
            }
        });

        if(getArguments().getInt(ARG_NAME)!=2) {
            btnAdd.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        } else if(linearLayout.getChildCount() < 5){
            btnAdd.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public List<PayMethod> getItem() {
        for(int i=0;i<linearLayout.getChildCount();i++) {
            list.add(((RewordItemEditView)linearLayout.getChildAt(i)).getItemData());
        }
        return list;
    }

}
