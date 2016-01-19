package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.LoginActivity;
import com.partypeople.www.partypeople.activity.SearchActivity;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.data.PartysResult;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.view.MainTabHeaderView;
import com.partypeople.www.partypeople.dialog.PasswordDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment extends Fragment {

    List<Party> partyList = new ArrayList<Party>();
    private static final String ARG_INDEX = "index";
    private int index;
    TextView warningView;
    ListView listView;
    PullToRefreshListView mListView;
    MainFragmentAdapter mAdapter;
    MainTabHeaderView header;
    String id, queryWord;
    LoadingDialogFragment dialogFragment;
    Button btnSetting;

    public static MainTabFragment newInstance(int index) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public MainTabFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (PullToRefreshListView)view.findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mAdapter = new MainFragmentAdapter(getContext());
        warningView = (TextView) view.findViewById(R.id.text_warning);

        btnSetting = (Button)view.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PropertyManager.getInstance().isLogin()) {
                    startActivity(new Intent(getContext(), SearchActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("startfrom", Constants.START_FROM_MAIN);
                    startActivity(intent);
                }
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String keyword = null;
                String parameter = null;
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                switch (getArguments().getInt(ARG_INDEX)) {
                    case 0:
                        keyword = "sort";
                        parameter = "LIKED";
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        keyword = "name";
                        parameter = queryWord;
                        break;
                }

                NetworkManager.getInstance().getPartys(getContext(), keyword, parameter, partyList.size(), new NetworkManager.OnResultListener<PartysResult>() {
                    @Override
                    public void onSuccess(PartysResult result) {
                        for (int i = 0; i < result.data.size(); i++) {
                            partyList.add(result.data.get(i));
                            mAdapter.add(result.data.get(i));
                        }
                        mListView.onRefreshComplete();
                    }

                    @Override
                    public void onFail(int code) {
                        mListView.onRefreshComplete();
                    }
                });
            }
        });

        listView = mListView.getRefreshableView();
        registerForContextMenu(listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                if (getArguments().getInt(ARG_INDEX) == 1) {
                    if (position == 0)
                        return;
                    position--;
                }
                final Party party = partyList.get(position);
                if (party.password != null && !party.password.equals("") && !party.password.equals("0000")) {
                    PasswordDialog passwordDialog = new PasswordDialog(getContext(), getFragmentManager());
                    passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    passwordDialog.setParty(party);
                    passwordDialog.show();
                } else {
                    dialogFragment = new LoadingDialogFragment();
                    dialogFragment.show(getFragmentManager(), "loading");
                    NetworkManager.getInstance().getParty(getContext(), party.id, new NetworkManager.OnResultListener<PartyResult>() {
                        @Override
                        public void onSuccess(PartyResult result) {
                            Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                            i.putExtra("party", result.data);
                            startActivity(i);
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "인터넷 연결이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                            dialogFragment.dismiss();
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void setQueryWord(String queryWord) {
        this.queryWord = queryWord;
        initData();
    }

    private void initData() {
        String keyword = null;
        String parameter = null;

        mAdapter.removeAll();
        partyList.clear();

        switch (getArguments().getInt(ARG_INDEX)) {
            case 0:
                keyword = "sort";
                parameter = "LIKED";
                break;
            case 1:
                User user = PropertyManager.getInstance().getUser();
                if(PropertyManager.getInstance().isLogin() && user.themes.length==0 && (user.favorite_address==null || user.favorite_address.equals(""))) {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("맞춤모임 설정이 필요합니다");
                    btnSetting.setVisibility(View.VISIBLE);
                    btnSetting.setText("맞춤모임 설정하기");
                    listView.setVisibility(View.INVISIBLE);
                }
                else if(PropertyManager.getInstance().isLogin()) {
                    listView.removeHeaderView(header);
                    header = new MainTabHeaderView(getContext());
                    header.setItemData(user.favorite_address, user.themes);
                    listView.addHeaderView(header);
                    warningView.setVisibility(View.GONE);
                    btnSetting.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("로그인이 필요한 서비스 입니다");
                    btnSetting.setVisibility(View.VISIBLE);
                    btnSetting.setText("로그인");
                    listView.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                break;
            case 3:
                keyword = "name";
                parameter = queryWord;
                break;
        }

        NetworkManager.getInstance().getPartys(getContext(), keyword, parameter, 0, new NetworkManager.OnResultListener<PartysResult>() {
            @Override
            public void onSuccess(final PartysResult result) {
                for (int i = 0; i < result.data.size(); i++) {
                    partyList.add(result.data.get(i));
                    mAdapter.add(result.data.get(i));
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(getContext(), "통신 연결상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }
}
