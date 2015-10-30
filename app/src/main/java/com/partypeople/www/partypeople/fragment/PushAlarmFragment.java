package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PushAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PushAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PushAlarmFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;

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

        return view;
    }
}
