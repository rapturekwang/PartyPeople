package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.PushAlarmItemView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PushAlarmFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    LinearLayout linearLayout;
    User user;

    public static PushAlarmFragment newInstance(String name) {
        PushAlarmFragment fragment = new PushAlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public PushAlarmFragment() {
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
        View view = inflater.inflate(R.layout.fragment_push_alarm, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_pushalarm);

        String[] pushes = getResources().getStringArray(R.array.pushalarm_list);
        for (int i = 0; i < pushes.length; i++) {
            final int temp = i;
            PushAlarmItemView pushAlarmItemView = new PushAlarmItemView(getContext());
            pushAlarmItemView.setItemData(pushes[i]);
            pushAlarmItemView.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    user.push[temp] = isChecked;
                    NetworkManager.getInstance().putUser(getContext(), user, new NetworkManager.OnResultListener<UserResult>() {
                        @Override
                        public void onSuccess(UserResult result) {

                        }

                        @Override
                        public void onFail(String response) {

                        }
                    });
                }
            });
            linearLayout.addView(pushAlarmItemView);
        }

        initData();

        return view;
    }

    void initData() {
        user = new User();

        for(int i=0;i<linearLayout.getChildCount();i++) {
            ((PushAlarmItemView)linearLayout.getChildAt(i)).switchCompat.setChecked(user.push[i]);
        }
    }
}
