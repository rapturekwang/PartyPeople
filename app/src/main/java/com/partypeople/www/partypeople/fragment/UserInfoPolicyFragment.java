package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.NoticeItem;
import com.partypeople.www.partypeople.view.NoticeBodyView;
import com.partypeople.www.partypeople.view.NoticeHeaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoPolicyFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    ListView listView;
    ListViewAdapter mAdapter;

    public static UserInfoPolicyFragment newInstance(String name) {
        UserInfoPolicyFragment fragment = new UserInfoPolicyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoPolicyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_info_policy, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new ListViewAdapter();
        listView.setAdapter(mAdapter);

        NoticeHeaderView noticeHeaderView = new NoticeHeaderView(getContext());
        String[] header = getResources().getStringArray(R.array.policy_header);
        noticeHeaderView.setItemData(header[0], header[1]);
        listView.addHeaderView(noticeHeaderView);

        String[] body = getResources().getStringArray(R.array.policy_body);
        for(int i=0;i<body.length;i+=2) {
            NoticeItem bodyItem = new NoticeItem();
            bodyItem.setItem(body[i], body[i+1]);
            mAdapter.add(bodyItem);
        }

        return view;
    }

    class ListViewAdapter extends BaseAdapter {
        public List<NoticeItem> list = new ArrayList<NoticeItem>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void add(NoticeItem item) {
            list.add(item);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoticeBodyView view;
            if(convertView == null) {
                view = new NoticeBodyView(parent.getContext());
            } else {
                view = (NoticeBodyView)convertView;
            }
            view.setItemData(list.get(position).header, list.get(position).body);
            return view;
        }
    }
}