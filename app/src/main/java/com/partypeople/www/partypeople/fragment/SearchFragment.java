package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.SearchActivity;
import com.partypeople.www.partypeople.location.LocalAreaInfo;
import com.partypeople.www.partypeople.location.LocationAdapter;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;

    ArrayAdapter<String> mCityAdapter, mGuAdapter;

    SwipeRefreshLayout refreshLayout;
    PullToRefreshListView refreshView;
    LocationAdapter mAdapter;

    public static SearchFragment newInstance(String name) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity activity = (SearchActivity) getActivity();
                activity.nextFragment();
            }
        });

        btn =(Button)view.findViewById(R.id.btn_get);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().useGetMethod(getContext(), 1, new NetworkManager.OnResultListener<LocalAreaInfo>() {
                    @Override
                    public void onSuccess(LocalAreaInfo result) {
//                        Toast.makeText(getContext(), "result : " + result.toString(), Toast.LENGTH_SHORT).show();
//                        for(String s : result.areas.area) {
//                            mCityAdapter.add(s);
//                            Log.d("SearchFragment", s);
//                        }
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
            }
        });

        setDateSpinner(view);

        return view;
    }

    private void setDateSpinner(View view) {
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_city);
        mCityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mCityAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        spinner = (Spinner)view.findViewById(R.id.spinner_gu);
        mGuAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mGuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mGuAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        mCityAdapter.add("시/도");
//        int num = 2015;
//        for (int i = num;i<num+ Constants.NUM_OF_CITY; i++) {
//            mCityAdapter.add(""+i);
//        }

        mGuAdapter.add("군/구");
        int num = 1;
        for (int i = num; i<num+Constants.NUM_OF_GU; i++) {
            mGuAdapter.add(""+i);
        }
    }
}
