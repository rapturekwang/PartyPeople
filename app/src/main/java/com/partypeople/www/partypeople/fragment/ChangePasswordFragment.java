package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.SettingActivity;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    EditText oldPassword, newPassword, newPassword2;

    public static ChangePasswordFragment newInstance(String name) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public ChangePasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        Button btn = (Button)view.findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().equals("") || newPassword.getText().toString().equals("") || newPassword2.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "모든 값을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if(!newPassword.getText().toString().equals(newPassword2.getText().toString())) {
                    Toast.makeText(getContext(), "변경할 비밀번호와 다시입력한 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else if(oldPassword.getText().toString().equals(newPassword.getText().toString())) {
                    Toast.makeText(getContext(), "기존 비밀번호와 새로운 비밀번호가 같습니다", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkManager.getInstance().changePassword(getContext(), oldPassword.getText().toString(),
                            newPassword.getText().toString(), new NetworkManager.OnResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getContext(), "비밀번호가 변경 되었습니다.", Toast.LENGTH_SHORT).show();
                            SettingActivity activity = (SettingActivity)getActivity();
                            activity.onBackPressed();
                        }

                        @Override
                        public void onFail(int code) {
                            if(code == 409) {
                                Toast.makeText(getContext(), "기존 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        oldPassword = (EditText)view.findViewById(R.id.old_password);
        newPassword = (EditText)view.findViewById(R.id.new_password);
        newPassword2 = (EditText)view.findViewById(R.id.new_password2);

        return view;
    }
}
