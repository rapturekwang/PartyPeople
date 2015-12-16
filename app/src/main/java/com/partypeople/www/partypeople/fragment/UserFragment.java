package com.partypeople.www.partypeople.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    int index;
    LinearLayout layout;
    TextView warningView;

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

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new UserAdapter();
        listView.setAdapter(mAdapter);

        warningView = (TextView)view.findViewById(R.id.text_warning);

        initData();

        if(index==0) {
            changeHeight();
        }

        return view;
    }

    private void initData() {
        UserActivity activity = (UserActivity)getActivity();
        User user = activity.getUser();
        if(user.groups.size()>0) {
            for (int i = 0; i < user.groups.size(); i++) {
                if (user.groups.get(i).role.equals("OWNER") && index == 0) {
                    partyList.add(user.groups.get(i));
                    mAdapter.add(user.groups.get(i));
                } else if (user.groups.get(i).role.equals("MEMBER") && index == 1) {
                    partyList.add(user.groups.get(i));
                    mAdapter.add(user.groups.get(i));
                } else if (user.groups.get(i).role.equals("WANNABE") && index == 2) {
                    partyList.add(user.groups.get(i));
                    mAdapter.add(user.groups.get(i));
                }
            }
        }
        if(partyList.size()==0) {
            warningView.setVisibility(View.VISIBLE);
            switch (index) {
                case 0:
                    warningView.setText("아직 개최한 모임이 없습니다");
                    break;
                case 1:
                    warningView.setText("아직 참여한 모임이 없습니다");
                    break;
                case 2:
                    warningView.setText("아직 '좋아요'한 모임이 없습니다");
                    break;
            }
        } else {
            warningView.setVisibility(View.GONE);
        }
    }

    public int changeHeight() {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = layout.getMeasuredHeight();
                Point size = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                int screenHeight = size.y - (int)Math.ceil(285 * getContext().getResources().getDisplayMetrics().density);
                //285 means statusbar + header + tablayout height
                if(screenHeight > height)
                    height = screenHeight;
                ((UserActivity) getActivity()).setPagerHeight(height);
            }
        });
        return 0;
    }
}
