package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.activity.UserActivity;
import com.partypeople.www.partypeople.adapter.UserAdapter;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.dialog.PasswordDialog;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;

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
    Button btnMake;
    UserAdapter mAdapter;
    LoadingDialogFragment dialogFragment;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getFragmentManager(), "loading");
                NetworkManager.getInstance().getParty(getContext(), partyList.get(position).id, new NetworkManager.OnResultListener<PartyResult>() {
                    @Override
                    public void onSuccess(PartyResult result) {
                        final Party party = result.data;
                        if(party.password != null && !party.password.equals("") && !party.password.equals("0000")) {
                            dialogFragment.dismiss();
                            PasswordDialog passwordDialog = new PasswordDialog(getContext(), getFragmentManager());
                            passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            passwordDialog.setParty(party);
                            passwordDialog.show();
                        } else {
                            Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                            i.putExtra("party", result.data);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                });
            }
        });

        warningView = (TextView)view.findViewById(R.id.text_warning);
        btnMake = (Button)view.findViewById(R.id.btn_make);
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MakePartyActivity.class);
                startActivity(intent);
            }
        });

        initData();

        if(index==0) {
            ((UserActivity)getActivity()).changeHeight(partyList.size());
        }

        return view;
    }

    private void initData() {
        UserActivity activity = (UserActivity)getActivity();
        User user = activity.getUser();
        if(user.groups.size()>0) {
            for (int i = 0; i < user.groups.size(); i++) {
                for(int j=0; j<user.groups.get(i).members.size(); j++) {
                    if(user.groups.get(i).themes == null || user.groups.get(i).themes.length==0) {
                        user.groups.get(i).themes = new int[1];
                        user.groups.get(i).themes[0] = 1;
                    }
                    if(user.groups.get(i).photos==null || user.groups.get(i).photos.size()==0) {
                        user.groups.get(i).photos = new ArrayList<>();
                        user.groups.get(i).photos.add("/images/icon_member.png");
                    }
                    if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("OWNER") && index == 0) {
                        partyList.add(user.groups.get(i));
                        mAdapter.add(user.groups.get(i));
                    } else if (user.id.equals(user.groups.get(i).members.get(j).id) && user.groups.get(i).members.get(j).role.equals("MEMBER") && index == 1) {
                        partyList.add(user.groups.get(i));
                        mAdapter.add(user.groups.get(i));
                    }
                }
            }
        }
        if(index == 2 && user.likes.size()>0) {
            for (int i=0; i<user.likes.size(); i++) {
                NetworkManager.getInstance().getParty(getContext(), user.likes.get(i).group, new NetworkManager.OnResultListener<PartyResult>() {
                    @Override
                    public void onSuccess(PartyResult result) {
                        partyList.add(result.data);
                        mAdapter.add(result.data);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if(partyList.size()==0) {
            warningView.setVisibility(View.VISIBLE);
            switch (index) {
                case 0:
                    warningView.setText("아직 개최한 모임이 없습니다");
                    if(user.id.equals(PropertyManager.getInstance().getUser().id))
                        btnMake.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    warningView.setText("아직 참여한 모임이 없습니다");
                    break;
                case 2:
                    if(user.likes.size()>0) {
                        warningView.setVisibility(View.INVISIBLE);
                    }
                    warningView.setText("아직 관심모임이 없습니다");
                    break;
            }
        } else {
            warningView.setVisibility(View.GONE);
            btnMake.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }
}
