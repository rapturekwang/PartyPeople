package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.FAQAdapter;
import com.partypeople.www.partypeople.adapter.ReportAdapter;
import com.partypeople.www.partypeople.data.Report;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    ExpandableListView listView;
    ReportAdapter mAdapter;

    public static ReportFragment newInstance(String name) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        listView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        mAdapter = new ReportAdapter();
        listView.setAdapter(mAdapter);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        listView.setIndicatorBounds(width - 50, width);

        Button btnReport = (Button)view.findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SendReportFragment.newInstance("신고 & 문의하기"))
                        .addToBackStack(null).commit();
            }
        });

        initData();

        return view;
    }

    public void initData() {
        NetworkManager.getInstance().getReport(getContext(), PropertyManager.getInstance().getUser().id, new NetworkManager.OnResultListener<Report[]>() {
            @Override
            public void onSuccess(Report[] result) {
                for (int i = 0; i < result.length; i++) {
                    mAdapter.addReport(result[i]);
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(getContext(), "통신이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
