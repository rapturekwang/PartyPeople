package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.EditProfileActivity;
import com.partypeople.www.partypeople.activity.FollowActivity;
import com.partypeople.www.partypeople.activity.MessageActivity;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.Follow;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.data.PartysResult;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.view.MainTabHeaderView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private static final String ARG_INDEX = "index";
    List<Party> partyList = new ArrayList<Party>();
    ListView listView;
    int index;

    UserAdapter mAdapter;
    ArrayList<String> followings, followers;
    TextView followingView, followerView;
    DisplayImageOptions options;

    public UserFragment() {
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();

        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new UserAdapter(getActivity(), index , new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                ((UserActivity)getActivity()).setTabCurrent(tabId);
            }
        });
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    ((UserActivity) getActivity()).setTabWidgetVisible(false);
                } else {
                    ((UserActivity) getActivity()).setTabWidgetVisible(true);
                }
            }
        });
        listView.setAdapter(mAdapter);

        listView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.view_user_header, null));
        ImageView btn = (ImageView)view.findViewById(R.id.btn_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        User user = ((UserActivity)getActivity()).getUser();
        btn = (ImageView)view.findViewById(R.id.btn_modify);
        if(!user.id.equals(PropertyManager.getInstance().getUser().id)) {
            btn.setVisibility(View.INVISIBLE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        btn = (ImageView)view.findViewById(R.id.image_profile);
//        ImageLoader.getInstance().displayImage(NetworkManager.getInstance().URL_USERS + "/" + user.id + "/photo", btn, options);
        TextView textBtn = (TextView)view.findViewById(R.id.text_btn_message);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), MessageActivity.class));
            }
        });
        followingView = (TextView)view.findViewById(R.id.text_following);
        followingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FollowActivity.class);
                i.putStringArrayListExtra("followings", followings);
                i.putStringArrayListExtra("followers", followers);
                getContext().startActivity(i);
            }
        });
        followerView = (TextView)view.findViewById(R.id.text_follower);
        followerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FollowActivity.class);
                i.putStringArrayListExtra("followings", followings);
                i.putStringArrayListExtra("followers", followers);
                getContext().startActivity(i);
            }
        });

        TextView textView = (TextView)view.findViewById(R.id.text_name);
        textView.setText(user.name);

        textView = (TextView)view.findViewById(R.id.text_address);
        textView.setText(user.address);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetworkManager.getInstance().getParty(getContext(), partyList.get(position-2).id, new NetworkManager.OnResultListener<PartyResult>() {
                    @Override
                    public void onSuccess(PartyResult result) {
                        Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                        Party party = result.data;
                        i.putExtra("party", party);
                        startActivity(i);
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });

        switch (getArguments().getInt(ARG_INDEX)) {
            case 0:
                initData(0);
                break;
            case 1:
                initData(1);
                break;
            case 2:
                initData(2);
                break;
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
    }

    private void initData(int index) {
        UserActivity activity = (UserActivity)getActivity();
        final User user = activity.getUser();
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

        followers = new ArrayList<String>();
        followings = new ArrayList<String>();
        NetworkManager.getInstance().getFollows(getContext(), new NetworkManager.OnResultListener<Follow[]>() {
            @Override
            public void onSuccess(Follow[] result) {
                if(result!=null) {
                    int following=0, follower=0;
                    for (int i = 0; i < result.length; i++) {
                        if (result[i].from.equals(user.id)) {
                            followings.add(result[i].to);
                            following++;
                        } else if (result[i].to.equals(user.id)) {
                            followers.add(result[i].from);
                            follower++;
                        }
                    }
                }
                followingView.setText("팔로잉 " + followings.size() + " |");
                followerView.setText("팔로워 " + followers.size());
            }

            @Override
            public void onFail(int code) {
                return;
            }
        });
    }
}