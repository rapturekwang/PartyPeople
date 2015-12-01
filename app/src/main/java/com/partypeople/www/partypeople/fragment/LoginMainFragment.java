package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.LoginActivity;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginMainFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;

    public static LoginMainFragment newInstance(String name) {
        LoginMainFragment fragment = new LoginMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginMainFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        final LoginActivity activity = (LoginActivity)getActivity();

        TextView btnText = (TextView)view.findViewById(R.id.btn_skip);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        if(activity.getStartfrom()==Constants.START_FROM_MAIN) {
            btnText.setVisibility(View.INVISIBLE);
        }

        ImageView btn = (ImageView)view.findViewById(R.id.btn_signup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(1, Constants.STACK_ADD);
            }
        });

        btn = (ImageView)view.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(2, Constants.STACK_ADD);
            }
        });

        btn = (ImageView)view.findViewById(R.id.btn_facebook);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
//                activity.login(null);
            }
        });

        btn = (ImageView)view.findViewById(R.id.btn_kakao);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textBtn = (TextView)view.findViewById(R.id.text_link1);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "서비스 이용약관", Toast.LENGTH_SHORT).show();
            }
        });

        textBtn = (TextView)view.findViewById(R.id.text_link2);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "개인정보 보호정책", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}