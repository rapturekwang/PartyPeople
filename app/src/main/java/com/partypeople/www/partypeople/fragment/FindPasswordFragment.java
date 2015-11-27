package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.LoginActivity;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.Validate;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindPasswordFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    private Validate validate = Validate.getInstance();

    public static FindPasswordFragment newInstance(String name) {
        FindPasswordFragment fragment = new FindPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public FindPasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_find_password, container, false);

        final LoginActivity activity = (LoginActivity)getActivity();
        final EditText email = (EditText)view.findViewById(R.id.edit_email);

        ImageView btnBack = (ImageView)view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(Constants.STACK_POP);
            }
        });

        ImageView btn = (ImageView)view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate.validEmail(email.getText().toString())) {
                    Toast.makeText(getContext(), "이메일 주소가 유효하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                activity.goToFragment(Constants.STACK_POP);
            }
        });

        return view;
    }
}