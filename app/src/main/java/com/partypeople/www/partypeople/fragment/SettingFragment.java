package com.partypeople.www.partypeople.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.activity.SettingActivity;
import com.partypeople.www.partypeople.adapter.SettingListAdapter;
import com.partypeople.www.partypeople.manager.PropertyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;

    ListView listView;
    SettingListAdapter mAdapter;

    public static SettingFragment newInstance(String name) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingActivity activity = (SettingActivity)getActivity();
                if(position<5) {
                    activity.changeFragment(position);
                } else if(position == 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setMessage("정말로 로그아웃 하시겠습니까?");
                    builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                            PropertyManager.getInstance().logout();
                            Intent intent = new Intent(getContext(), MainActivity.class);
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
                } else if(position == 7) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setMessage("정말로 회원탈퇴를 진행 하시겠습니까? 회원탈퇴시 회원님의 모든 기록이 삭제되며, 다시 복구할 수 없습니다");
                    builder.setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "회원탈퇴", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                } else if(position == 8) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:rapturekwang@gmail.com"));
                    startActivity(intent);
                }
            }
        });

        mAdapter = new SettingListAdapter();
        listView.setAdapter(mAdapter);

        return view;
    }


}
