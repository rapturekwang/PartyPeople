package com.partypeople.www.partypeople.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
    String id;
    Bitmap bitmap;

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
        listView = (ListView)view.findViewById(R.id.listView);
        mAdapter = new MainFragmentAdapter(getContext());
        listView.setAdapter(mAdapter);
        warningView = (TextView)view.findViewById(R.id.text_warning);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), PartyDetailActivity.class);
                Party party = partyList.get(position);
                i.putExtra("party", party);
                startActivity(i);
            }
        });

//        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.removeAll();
        partyList.clear();
        initData();
    }

    private void initData() {
        switch (getArguments().getInt(ARG_INDEX)) {
            case 0:
                warningView.setVisibility(View.GONE);
                break;
            case 1:
                User user = PropertyManager.getInstance().getUser();
                if(PropertyManager.getInstance().isLogin()) {
                    listView.removeHeaderView(header);
                    header = new MainTabHeaderView(getContext());
                    header.setItemData(user.favorite_address, user.themes);
                    listView.addHeaderView(header);
                    warningView.setVisibility(View.GONE);
                } else if(user.themes==null && user.favorite_address==null) {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("맞춤모임 설정이 필요합니다");
                } else {
                    warningView.setVisibility(View.VISIBLE);
                    warningView.setText("로그인이 필요한 서비스 입니다");
                }
                break;
            case 2:
                warningView.setVisibility(View.GONE);
                break;
            case 3:
                warningView.setVisibility(View.GONE);
                break;
        }

        NetworkManager.getInstance().getPartys(getContext(), new NetworkManager.OnResultListener<PartysResult>() {
            @Override
            public void onSuccess(final PartysResult result) {
                for (int i = 0; i < result.data.size(); i++) {
                    partyList.add(result.data.get(i));
                    mAdapter.add(result.data.get(i));
                }
            }

            @Override
            public void onFail(int code) {
//                for (int i = 0; i < 5 ; i++) {
//                    Party d = new Party();
//                    d.name = "Come to House Party!";
//                    d.end_at = "2015-12-04T02:11:11";
//                    d.location = "서울시 서초구";
//                    d.expect_pay = 25000;
//                    partyList.add(d);
//                    mAdapter.add(d);
//                }
                Toast.makeText(getContext(), "통신 연결상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
