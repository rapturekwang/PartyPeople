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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.LoginActivity;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.data.Data;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
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
    ImageView login;
    LoadingDialogFragment dialogFragment;

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
//                activity.goToFragment(3, Constants.STACK_ADD);
                Toast.makeText(getContext(), "서비스 준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
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

        login = (ImageView)view.findViewById(R.id.btn_login);
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

                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getFragmentManager(), "loading");
                NetworkManager.getInstance().authUser(getContext(), email.getText().toString(), password.getText().toString(), new NetworkManager.OnResultListener<UserResult>() {
                    @Override
                    public void onSuccess(final UserResult result1) {
                        NetworkManager.getInstance().getMyId(getContext(), result1.token, new NetworkManager.OnResultListener<UserResult>() {
                            @Override
                            public void onSuccess(UserResult result2) {
                                propertyManager.setToken(result1.token);
                                propertyManager.setUser(result2.data);
                                propertyManager.setLoginMethod(Constants.LOGIN_WITH_LOCAL);

                                User user = new User();
                                user.android_token = PropertyManager.getInstance().getRegistrationToken();
                                NetworkManager.getInstance().putUser(getContext(), user, new NetworkManager.OnResultListener<UserResult>() {
                                    @Override
                                    public void onSuccess(UserResult result) {

                                    }

                                    @Override
                                    public void onFail(String response) {

                                    }
                                });

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void onFail(String response) {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                dialogFragment.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                });
            }
        });

        TextView btn = (TextView)view.findViewById(R.id.btn_signup);
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

    @Override
    public void onPause() {
        super.onPause();
        if(dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }
}