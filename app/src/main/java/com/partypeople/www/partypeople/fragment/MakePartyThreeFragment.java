package com.partypeople.www.partypeople.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.manager.NetworkManager;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyThreeFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;
    EditText bankView, accountView, phoneView;

    public static MakePartyThreeFragment newInstance(String name) {
        MakePartyThreeFragment fragment = new MakePartyThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MakePartyThreeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_make_party_three, container, false);

        bankView = (EditText)view.findViewById(R.id.edit_bank);
        accountView = (EditText)view.findViewById(R.id.edit_account);
        phoneView = (EditText)view.findViewById(R.id.edit_phone);

        Button btn = (Button)view.findViewById(R.id.btn_make);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("정말로 만드시겠습니까?");
                builder.setPositiveButton("만들기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MakePartyActivity activity = (MakePartyActivity)getActivity();
                        activity.party.bank = bankView.getText().toString();
                        activity.party.account = Double.parseDouble(accountView.getText().toString());
                        activity.party.phone = Double.parseDouble(phoneView.getText().toString());
                        NetworkManager.getInstance().postJson(getContext(), activity.party, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getActivity(), "모임 생성이 실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        return view;
    }
}