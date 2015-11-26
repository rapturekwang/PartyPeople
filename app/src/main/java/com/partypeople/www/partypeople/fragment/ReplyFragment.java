package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MessageActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReplyFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;

    public static ReplyFragment newInstance(String name) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public ReplyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);

//        EditText editText = (EditText)view.findViewById(R.id.edit_bank);
//        editText.setText("");

        final MessageActivity activity = (MessageActivity)getActivity();

//        Button btn = (Button)view.findViewById(R.id.btn_send);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "보내기 완료", Toast.LENGTH_SHORT).show();
//                activity.onBackPressed();
//            }
//        });

//        btn = (Button)view.findViewById(R.id.btn_cancel);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.onBackPressed();
//            }
//        });
        ImageView imageView = (ImageView)view.findViewById(R.id.image_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        return view;
    }


}
