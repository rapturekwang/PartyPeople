package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.NoticeActivity;
import com.partypeople.www.partypeople.dialog.ConfirmDialog;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakePartyFourFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;

    public static MakePartyFourFragment newInstance(String name) {
        MakePartyFourFragment fragment = new MakePartyFourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MakePartyFourFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_make_party_four, container, false);

        Button btn = (Button)view.findViewById(R.id.btn_make);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePartyActivity activity = (MakePartyActivity)getActivity();
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

                ConfirmDialog dialog = new ConfirmDialog(getContext(), activity.party);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        TextView textBtn = (TextView)view.findViewById(R.id.text_tos);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_TOS);
                startActivity(intent);
            }
        });

        return view;
    }
}
