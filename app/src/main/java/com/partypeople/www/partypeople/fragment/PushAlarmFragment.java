package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.PushAlarmListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PushAlarmFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    PushAlarmListAdapter mAdapter;
    ListView listView;

    public static PushAlarmFragment newInstance(String name) {
        PushAlarmFragment fragment = new PushAlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public PushAlarmFragment() {
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
        View view = inflater.inflate(R.layout.fragment_push_alarm, container, false);

        listView = (ListView)view.findViewById(R.id.linearlayout_reword);
        mAdapter = new PushAlarmListAdapter();
        listView.setAdapter(mAdapter);

//        Button btn = (Button)view.findViewById(R.id.btn_save);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "푸쉬알림이 저장되었습니다.", Toast.LENGTH_SHORT).show();
//                SettingActivity activity = (SettingActivity)getActivity();
//                activity.onBackPressed();
//            }
//        });

        return view;
    }
}
