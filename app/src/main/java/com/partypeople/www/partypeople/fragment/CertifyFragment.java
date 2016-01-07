package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.NoticeActivity;
import com.partypeople.www.partypeople.dialog.CertifyDialog;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class CertifyFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    Spinner bankView;
    ArrayAdapter<String> mBankAdapter;
    EditText accountView;

    public static CertifyFragment newInstance(String name) {
        CertifyFragment fragment = new CertifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public CertifyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_certify, container, false);

        bankView = (Spinner)view.findViewById(R.id.spinner_bank);
        mBankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mBankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] banks = getResources().getStringArray(R.array.bank_name);
        for (int i = 0; i < banks.length; i++) {
            mBankAdapter.add(banks[i]);
        }
        bankView.setAdapter(mBankAdapter);
        accountView = (EditText)view.findViewById(R.id.edit_account);

        ImageView imgBtn = (ImageView)view.findViewById(R.id.img_btn_phone);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "휴대폰 인증", Toast.LENGTH_SHORT).show();
                CertifyDialog dialog = new CertifyDialog(getContext(), true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        imgBtn = (ImageView)view.findViewById(R.id.img_btn_account);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "계좌 인증", Toast.LENGTH_SHORT).show();
                CertifyDialog dialog = new CertifyDialog(getContext(), false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        TextView textBtn = (TextView)view.findViewById(R.id.text_policy);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_POLICY);
                startActivity(intent);
            }
        });

        return view;
    }
}
