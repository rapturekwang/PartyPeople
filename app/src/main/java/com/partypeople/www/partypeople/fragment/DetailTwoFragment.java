package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.adapter.RewordViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTwoFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String mName;
    ListView listView;
    RewordViewAdapter mAdapter;
    LinearLayout layout;
    TextView payTitle;

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

        layout = (LinearLayout)view.findViewById(R.id.root_layout);

        listView = (ListView)view.findViewById(R.id.linearlayout_reword);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);

        payTitle = (TextView)view.findViewById(R.id.text_pay_title);

        initData();

        return view;
    }

    private void initData() {
        PartyDetailActivity activity = (PartyDetailActivity)getActivity();
        for(int i=0; i<activity.party.amount_method.size(); i++) {
            mAdapter.add(activity.party.amount_method.get(i));
        }

        if(activity.party.amount_method.size()==1 && !activity.party.amount_custom) {
            payTitle.setText("고정금액");
        } else if(activity.party.amount_method.size()==1 && activity.party.amount_custom) {
            payTitle.setText("최소금액");
        } else if(activity.party.amount_method.size()>1) {
            payTitle.setText("포함사항별 선택");
        }
    }

    public void changeHeight() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                ((PartyDetailActivity)getActivity()).setPagerHeight(height);
            }
        });
    }
}
