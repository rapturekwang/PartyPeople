package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
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
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.CircleTransform;
import com.partypeople.www.partypeople.utils.CustomGlideUrl;

import com.partypeople.www.partypeople.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private static final String ARG_INDEX = "index";
    List<Party> partyList = new ArrayList<Party>();
    ListView listView;
    int index;

    UserAdapter mAdapter;
    ArrayList<String> followings, followers;
    TextView followingView, followerView, nameView, addressView;
    LinearLayout linearLayout;
    ImageView modify, profileView;

    public UserFragment() {
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

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
        linearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_user);
        ImageView btn = (ImageView)view.findViewById(R.id.btn_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        final User user = ((UserActivity)getActivity()).getUser();
        modify = (ImageView)view.findViewById(R.id.btn_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        profileView = (ImageView)view.findViewById(R.id.image_profile);
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

        nameView = (TextView)view.findViewById(R.id.text_name);
        addressView = (TextView)view.findViewById(R.id.text_address);

        LinearLayout linearLayout2 = (LinearLayout)view.findViewById(R.id.linearLayout2);
        if(!user.id.equals(PropertyManager.getInstance().getUser().id)) {
            modify.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
        } else {
            linearLayout.setVisibility(View.INVISIBLE);
        }

        ImageView takeFollow = (ImageView)view.findViewById(R.id.image_btn_follow);
        takeFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().takeFollow(getContext(), user.id, new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getContext(), "팔로우 하였습니다", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });
        ImageView takeMessage = (ImageView)view.findViewById(R.id.image_btn_message);
        takeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "아직 준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toast.makeText(getContext(), "네트워크 상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
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
        if(user.has_photo) {
//            Picasso picasso = new Picasso.Builder(getContext()).downloader(new CustomOkHttpDownloader(getContext())).build();
//            picasso.load(NetworkManager.getInstance().URL_SERVER + activity.getUser().photo)
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .placeholder(R.drawable.profile_img)
//                    .error(R.drawable.profile_img)
//                    .into(profileView);

            CustomGlideUrl customGlideUrl = new CustomGlideUrl();
            GlideUrl glideUrl = customGlideUrl.getGlideUrl(NetworkManager.getInstance().URL_SERVER + activity.getUser().photo);
            Glide.with(getContext())
                    .load(glideUrl)
                    .signature(new StringSignature(DateUtil.getInstance().getCurrentDate()))
                    .placeholder(R.drawable.profile_img)
                    .error(R.drawable.profile_img)
                    .transform(new CircleTransform(getContext()))
                    .into(profileView);
        }
        nameView.setText(user.name);
        addressView.setText(user.address);
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
                    for (int i = 0; i < result.length; i++) {
                        if (result[i].from.equals(user.id)) {
                            followings.add(result[i].to);
                        } else if (result[i].to.equals(user.id)) {
                            followers.add(result[i].from);
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