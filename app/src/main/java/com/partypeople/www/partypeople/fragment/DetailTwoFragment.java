package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
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
    LinearLayout layout;

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
//        ViewTreeObserver vto = layout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int height = layout.getMeasuredHeight();
//                Log.d("DetailTwo", height + "");
//                ((PartyDetailActivity)getActivity()).setPagerHeight(height);
//            }
//        });

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new RewordViewAdapter();
        listView.setAdapter(mAdapter);

        initData();

        return view;
    }

    private void initData() {
        PartyDetailActivity activity = (PartyDetailActivity)getActivity();
        for(int i=0; i<activity.party.pay_method.size(); i++) {
//            PayMethod payMethod = new PayMethod();
//            payMethod.price = 10000;
//            payMethod.title = "FREE 음료 1개, 스낵";
            mAdapter.add(activity.party.pay_method.get(i), false);
        }
    }

    public void test() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                Log.d("DetailTwo", height+"");
                ((PartyDetailActivity)getActivity()).setPagerHeight(height + 50);
            }
        });
    }
}
