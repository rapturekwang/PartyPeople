package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;

    public static final String[] SETTING_MENUS = new String[] { "푸쉬알림", "비밀번호 설정",
            "이용약관", "개인정보 취급방침", "자주 하는 질문", "버전정보",
            "로그아웃", "회원탈퇴", "rapturekwang@gmali.com", "" };
    ListView listView;
    ArrayAdapter<String> mAdapter;

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
                if(position<5)
                    activity.changeFragment(position);
                else {
                    Toast.makeText(getContext(), "pushed : " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, SETTING_MENUS);
        listView.setAdapter(mAdapter);
        return view;
    }


}
