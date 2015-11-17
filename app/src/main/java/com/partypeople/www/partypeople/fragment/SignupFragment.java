package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.Validate;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    Validate validate = Validate.getInstance();
    EditText email, password, idName;
    Button signup, login;
    PropertyManager propertyManager = PropertyManager.getInstance();

    public static SignupFragment newInstance(String name) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupFragment() {
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        final LoginActivity activity = (LoginActivity)getActivity();

        email = (EditText)view.findViewById(R.id.edit_email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup.setEnabled(validSignup());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password = (EditText)view.findViewById(R.id.edit_password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup.setEnabled(validSignup());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        idName = (EditText)view.findViewById(R.id.edit_name);
        idName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signup.setEnabled(validSignup());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView btnBack = (ImageView)view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(Constants.STACK_POP);
            }
        });

        signup = (Button) view.findViewById(R.id.btn_signup);
        signup.setEnabled(false);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate.validEmail(email.getText().toString())) {
                    Toast.makeText(getContext(), "이메일 주소가 유효하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validate.validPsaaword(password.getText().toString())) {
                    Toast.makeText(getContext(), "패스워드 길이가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                NetworkManager.getInstance().postUser(getContext(), email.getText().toString(), password.getText().toString(), idName.getText().toString(), new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(User result) {
                        //Log.d("SignupFragment", "email:" + result.data.email + "id:" + result.data.id + "token:" + result.token);
                        propertyManager.setToken(result.token);
                        propertyManager.setEmail(result.data.email);
                        propertyManager.setId(result.data.id);
                        propertyManager.setUser(result);
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });

        login = (Button)view.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(2, Constants.STACK_POP);
            }
        });

        return view;
    }

    private boolean validSignup() {
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("") && !idName.getText().toString().equals("")) {
            return true;
        }

        return false;
    }
}