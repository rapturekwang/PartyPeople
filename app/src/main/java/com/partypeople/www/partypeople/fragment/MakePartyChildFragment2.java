package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.PayMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwang on 16. 1. 20..
 */
public class MakePartyChildFragment2 extends Fragment{
    List<PayMethod> list = new ArrayList<PayMethod>();
    EditText priceView, contentsView;

    private static final String ARG_NAME = "index";
    private int mIndex;

    public static MakePartyChildFragment2 newInstance(int index) {
        MakePartyChildFragment2 fragment = new MakePartyChildFragment2();
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

    public MakePartyChildFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_party_child2, container, false);

        priceView = (EditText)view.findViewById(R.id.edit_price);
        contentsView = (EditText)view.findViewById(R.id.edit_contents);

        return view;
    }

    public List<PayMethod> getItem() {
        PayMethod payMethod = new PayMethod();
        try {
            payMethod.price = Integer.parseInt(priceView.getText().toString());
        } catch (Exception e) {
            payMethod.price = -1;
        }
        payMethod.title = contentsView.getText().toString();
        list.add(payMethod);

        return list;
    }
}
