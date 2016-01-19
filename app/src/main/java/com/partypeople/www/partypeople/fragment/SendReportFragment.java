package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.ReportAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendReportFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    ReportAdapter mAdapter;
    Spinner reportView;
    ArrayAdapter<String> mReportAdapter;
    TextView textCancel;

    public static SendReportFragment newInstance(String name) {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SendReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_send_report, container, false);

        textCancel = (TextView)view.findViewById(R.id.text_cancel);

        reportView = (Spinner)view.findViewById(R.id.spinner);
        mReportAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mReportAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] reports = getResources().getStringArray(R.array.report_category);
        for (int i = 0; i < reports.length; i++) {
            mReportAdapter.add(reports[i]);
        }
        reportView.setAdapter(mReportAdapter);

        reportView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1) {
                    textCancel.setVisibility(View.VISIBLE);
                } else {
                    textCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
