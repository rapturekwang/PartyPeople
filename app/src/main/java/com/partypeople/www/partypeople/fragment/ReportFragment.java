package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.FAQAdapter;
import com.partypeople.www.partypeople.adapter.ReportAdapter;
import com.partypeople.www.partypeople.data.Report;

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
        for(int i=0;i<10;i++) {
            Report report = new Report();
            report.category = "결제 취소 요청";
            report.question = "제가 결제한 모임에 참석 할 수 없을 것 같아서 결제 취소 하고 싶어요.";
            report.answer = "결제취소는 모금마감일 3일전, 목표금액이 달성되지 않은 경우에 한하여 취소가 가능합니다.";
            mAdapter.addReport(report);
        }
//        NetworkManager.getInstance().getBoards(getContext(), new NetworkManager.OnResultListener<Board[]>() {
//            @Override
//            public void onSuccess(Board[] result) {
//                for(int i=0;i<result.length;i++) {
//                    if(result[i].category.equals("FAQ")) {
//                        mAdapter.addFAQ(result[i]);
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(int code) {
//                Toast.makeText(getContext(), "통신이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
