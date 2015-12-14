package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private static final String ARG_INDEX = "index";
    List<Party> partyList = new ArrayList<Party>();
    ListView listView;
    int index, height;
    LinearLayout layout;

    UserAdapter mAdapter;

    public static UserFragment newInstance(int index) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        layout = (LinearLayout)view.findViewById(R.id.root_layout);
//        ViewTreeObserver vto = layout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                height = layout.getMeasuredHeight();
////                ((UserActivity) getActivity()).setPagerHeight(height);
//            }
//        });

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new UserAdapter();
        listView.setAdapter(mAdapter);

        initData();

        return view;
    }

    private void initData() {
        UserActivity activity = (UserActivity)getActivity();
        User user = activity.getUser();
        for(int i=0;i<user.groups.size();i++) {
            if(user.groups.get(i).role.equals("OWNER") && index==0) {
                partyList.add(user.groups.get(i));
                mAdapter.add(user.groups.get(i));
            } else if(user.groups.get(i).role.equals("MEMBER") && index==1) {
                partyList.add(user.groups.get(i));
                mAdapter.add(user.groups.get(i));
            } else if(user.groups.get(i).role.equals("WANNABE") && index==2) {
                partyList.add(user.groups.get(i));
                mAdapter.add(user.groups.get(i));
            }
        }
    }

    public int changeHeight() {
//        Log.d("UserFragment", "height:" + height);
//        return height;
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = layout.getMeasuredHeight();
                ((UserActivity) getActivity()).setPagerHeight(height);
            }
        });
        return 0;
    }
}
