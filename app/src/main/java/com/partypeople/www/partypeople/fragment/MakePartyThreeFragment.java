package com.partypeople.www.partypeople.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.NoticeActivity;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyThreeFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;
    Spinner bankView;
    ArrayAdapter<String> mBankAdapter;
    EditText accountView, phoneView;

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
        final View view = inflater.inflate(R.layout.fragment_make_party_three, container, false);

        bankView = (Spinner)view.findViewById(R.id.spinner_bank);
        mBankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mBankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] banks = getResources().getStringArray(R.array.bank_name);
        for (int i = 0; i < banks.length; i++) {
            mBankAdapter.add(banks[i]);
        }
        bankView.setAdapter(mBankAdapter);
        accountView = (EditText)view.findViewById(R.id.edit_account);

        ImageView imgBtn = (ImageView)view.findViewById(R.id.btn_tos);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_TOS);
                startActivity(intent);
            }
        });

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
                        final MakePartyActivity activity = (MakePartyActivity)getActivity();
                        try {
                            activity.party.account = Double.parseDouble(accountView.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "계좌번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CheckBox chbox = (CheckBox)view.findViewById(R.id.chbox_policy);
                        if(!chbox.isChecked()) {
                            Toast.makeText(getContext(), "약관에 동의해 주십시오", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        chbox = (CheckBox)view.findViewById(R.id.chbox_agree);
                        if(!chbox.isChecked()) {
                            Toast.makeText(getContext(), "모금 사항에 동의하여 주십시오.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        NetworkManager.getInstance().postJson(getContext(), activity.party, new NetworkManager.OnResultListener<PartyResult>() {
                            @Override
                            public void onSuccess(PartyResult result) {
                                if (activity.party.imageFile != null) {
                                    Log.d("MakePartyThree", result.data.id);
                                    NetworkManager.getInstance().putGroupImage(getContext(), activity.party.imageFile, result.data.id, new NetworkManager.OnResultListener<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "사진 업로드가 실패하였습니다", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "모임이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getActivity(), "모임 생성이 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
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