package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Property;
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
import com.partypeople.www.partypeople.data.Data;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.Validate;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    PropertyManager propertyManager = PropertyManager.getInstance();
    private static final String ARG_NAME = "name";
    private String name;
    Validate validate = Validate.getInstance();
    EditText email, password;
    Button login;

    public static LoginFragment newInstance(String name) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final LoginActivity activity = (LoginActivity)getActivity();

        ImageView btnBack = (ImageView)view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(Constants.STACK_POP);
            }
        });

        TextView btnFindPW = (TextView)view.findViewById(R.id.text_btn_findpw);
        btnFindPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(3, Constants.STACK_ADD);
            }
        });

        email = (EditText)view.findViewById(R.id.edit_email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login.setEnabled(validLogin());
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
                login.setEnabled(validLogin());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login = (Button)view.findViewById(R.id.btn_login);
        login.setEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
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
                JSONObject jsonObject = new JSONObject();
                try {
                    Log.d("LoginFragment", email.getText().toString() + ":" + password.getText().toString());
                    jsonObject.accumulate("password", password.getText().toString());
                    jsonObject.accumulate("email", email.getText().toString());
                } catch (Exception e) {}
                NetworkManager.getInstance().authUser(getContext(), jsonObject.toString(), new NetworkManager.OnResultListener<User>() {
                    @Override
                    public void onSuccess(final User result1) {
                        NetworkManager.getInstance().getMyId(getContext(), result1.token, new NetworkManager.OnResultListener<User>() {
                            @Override
                            public void onSuccess(User result2) {
                                result2.token = result1.token;
                                propertyManager.setUser(result2);

                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getContext(), "통신에 실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_signup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragment(1, Constants.STACK_POP);
            }
        });

        return view;
    }

    private boolean validLogin() {
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            return true;
        }

        return false;
    }
}