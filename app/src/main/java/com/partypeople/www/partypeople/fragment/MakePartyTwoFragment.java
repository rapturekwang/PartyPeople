package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.utils.Constants;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyTwoFragment extends Fragment {
    Fragment[] list = {MakePartyChildFragment.newInstance(0),
            MakePartyChildFragment.newInstance(1),
            MakePartyChildFragment.newInstance(2)};
    private static final String ARG_NAME = "name";
    //FragmentManager fmanager = getChildFragmentManager();
    ArrayAdapter<String> mYearAdapter, mMonthAdapter, mDayAdapter;
    EditText expectPayView;

    // TODO: Rename and change types of parameters
    private String name;
//    TabLayout tabs;
//    WithoutSwipeViewPager pager;

    public static MakePartyTwoFragment newInstance(String name) {
        MakePartyTwoFragment fragment = new MakePartyTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MakePartyTwoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_make_party_two, container, false);

        expectPayView = (EditText)view.findViewById(R.id.edit_expect_pay);

        getChildFragmentManager().beginTransaction().add(R.id.container, list[0]).commit();

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePartyActivity activity = (MakePartyActivity)getActivity();
                activity.party.expect_pay = Double.parseDouble(expectPayView.getText().toString());
                activity.nextFragment();
            }
        });

        RadioButton radioButton = (RadioButton)view.findViewById(R.id.radioButton);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[0]).commit();
                }
            }
        });
        radioButton = (RadioButton)view.findViewById(R.id.radioButton2);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[1]).commit();
                }
            }
        });
        radioButton = (RadioButton)view.findViewById(R.id.radioButton3);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[2]).commit();
                }
            }
        });

//        tabs = (TabLayout)view.findViewById(R.id.tabs);
//        pager = (WithoutSwipeViewPager)view.findViewById(R.id.pager);
//        MakePartyTwoTabAdapter adpater = new MakePartyTwoTabAdapter(getChildFragmentManager());
//        pager.setAdapter(adpater);

//        tabs.setupWithViewPager(pager);

//        tabs.removeAllTabs();
//        String[] tabTitle = getResources().getStringArray(R.array.party_two_tab_name);
//        for (int i = 0; i < Constants.NUM_OF_PARTY_TWO_TAB; i++) {
//            tabs.addTab(tabs.newTab().setText(tabTitle[i]));
//        }

        setDateSpinner(view);

        return view;
    }

    private void setDateSpinner(View view) {
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_year);
        mYearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mYearAdapter);
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

        spinner = (Spinner)view.findViewById(R.id.spinner_month);
        mMonthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mMonthAdapter);
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

        spinner = (Spinner)view.findViewById(R.id.spinner_day);
        mDayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mDayAdapter);
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

        mYearAdapter.add("년");
        int num = 2015;
        for (int i = num;i<num+ Constants.MAX_YEAR; i++) {
            mYearAdapter.add(""+i);
        }
        mMonthAdapter.add("월");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_MONTH; i++) {
            mMonthAdapter.add(""+i);
        }
        mDayAdapter.add("일");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_DAY; i++) {
            mDayAdapter.add(""+i);
        }
    }
}