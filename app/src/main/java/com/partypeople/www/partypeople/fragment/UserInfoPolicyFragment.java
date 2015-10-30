package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoPolicyFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;

    public static UserInfoPolicyFragment newInstance(String name) {
        UserInfoPolicyFragment fragment = new UserInfoPolicyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoPolicyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_info_policy, container, false);

        return view;
    }
}