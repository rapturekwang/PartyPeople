package com.partypeople.www.partypeople.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.view.FollowItemView;
import com.partypeople.www.partypeople.view.MessageItemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageTabFragment extends Fragment {
    private static final String ARG_INDEX = "index";
    private int index;
    ListView listView;
    MessageListAdapter mAdapter;
    String id;

    public static MessageTabFragment newInstance(int index) {
        MessageTabFragment fragment = new MessageTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new MessageListAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "pushed : " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public class MessageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MessageItemView view;
            if (convertView == null) {
                view =  new MessageItemView(parent.getContext());
            } else {
                view = (MessageItemView) convertView;
            }
            view.setItemData("정광희", "10시간 전", "메시지 내용 한줄만...", "");

            return view;
        }
    }
}