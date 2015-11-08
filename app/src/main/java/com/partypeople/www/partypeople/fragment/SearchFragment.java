package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.SearchActivity;
import com.partypeople.www.partypeople.location.Area;
import com.partypeople.www.partypeople.location.LocalAreaInfo;
import com.partypeople.www.partypeople.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;

    List<Area> areaList = new ArrayList<Area>();
    ArrayAdapter<String> mCityAdapter, mGuAdapter;

    SwipeRefreshLayout refreshLayout;
    PullToRefreshListView refreshView;

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

        setDateSpinner(view);

        mCityAdapter.add("시/도");
        mGuAdapter.add("군/구");
        NetworkManager.getInstance().getLocalInfo(getContext(), 1, "L", 0, new NetworkManager.OnResultListener<LocalAreaInfo>() {
            @Override
            public void onSuccess(LocalAreaInfo result) {
                for (Area s : result.areas.area) {
                    mCityAdapter.add(s.upperDistName);
                    areaList.add(s);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });

        return view;
    }

    private void setDateSpinner(View view) {
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_city);
        mCityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mCityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Area area;
                mGuAdapter.clear();
                mGuAdapter.add("군/구");
                for(int i=0; i<areaList.size(); i++) {
                    area = areaList.get(i);
                    if(area.upperDistName.equals(mCityAdapter.getItem(position))){
                        NetworkManager.getInstance().getLocalInfo(getContext(), 1, "M", area.upperDistCode, new NetworkManager.OnResultListener<LocalAreaInfo>() {
                            @Override
                            public void onSuccess(LocalAreaInfo result) {
                                for (Area s : result.areas.area) {
                                    if(!s.middleDistName.equals(""))
                                        mGuAdapter.add(s.middleDistName);
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    }
}
