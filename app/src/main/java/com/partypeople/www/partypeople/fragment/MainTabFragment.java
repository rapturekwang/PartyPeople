package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.MainFragmentAdapter;
import com.partypeople.www.partypeople.activity.PartyDetailActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.data.PartysResult;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
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
    MainFragmentAdapter mAdapter;
    MainTabHeaderView header;
    String id, queryWord;

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
        //((TextView)view.findViewById(R.id.text_name)).setText(mName);
        listView = (ListView)view.findViewById(R.id.listview);
        mAdapter = new MainFragmentAdapter(getContext());
        listView.setAdapter(mAdapter);
        warningView = (TextView) view.findViewById(R.id.text_warning);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final LoadingDialog dialog = new LoadingDialog(getContext());
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
                if (getArguments().getInt(ARG_INDEX) == 1) {
                    if (position == 0)
                        return;
                    position--;
                }
                Party party = partyList.get(position);
                if (party.password != null && !party.password.equals("") && !party.password.equals("0000")) {
                    PasswordDialog passwordDialog = new PasswordDialog(getContext());
                    passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    passwordDialog.setParty(party);
                    passwordDialog.show();
                } else {
                    Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                    i.putExtra("party", party);
                    startActivity(i);
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
                break;
            case 1:
                User user = PropertyManager.getInstance().getUser();
                if(PropertyManager.getInstance().isLogin() && user.themes.length==0 && (user.favorite_address==null || user.favorite_address.equals(""))) {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("맞춤모임 설정이 필요합니다");
                    listView.setVisibility(View.INVISIBLE);
                }
                else if(PropertyManager.getInstance().isLogin()) {
                    listView.removeHeaderView(header);
                    header = new MainTabHeaderView(getContext());
                    header.setItemData(user.favorite_address, user.themes);
                    listView.addHeaderView(header);
                    warningView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("로그인이 필요한 서비스 입니다");
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

        NetworkManager.getInstance().getPartys(getContext(), keyword, parameter, new NetworkManager.OnResultListener<PartysResult>() {
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
}
