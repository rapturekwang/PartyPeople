package com.partypeople.www.partypeople.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.data.PayMethod;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyTwoFragment extends Fragment {
    Fragment[] list = {MakePartyChildFragment.newInstance(0),
            MakePartyChildFragment.newInstance(1),
            MakePartyChildFragment.newInstance(2)};
    private static final String ARG_NAME = "name";
    ArrayAdapter<String> mYearAdapter, mMonthAdapter, mDayAdapter;
    Spinner mYearSpinner, mMonthSpinner, mDaySpinner;
    String year = "2015", month;
    EditText expectPayView;
    RadioGroup radioGroup;

    // TODO: Rename and change types of parameters
    private String name;

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

        getChildFragmentManager().beginTransaction().replace(R.id.container, list[0]).commit();

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePartyActivity activity = (MakePartyActivity)getActivity();
                MakePartyChildFragment fragment = (MakePartyChildFragment)getChildFragmentManager().getFragments().get(0);
                String warningMessage="";
                if(expectPayView.getText().toString().equals("")) warningMessage="모임 설명을 입력해 주세요.";
                if(warningMessage.equals("")) {
                    activity.party.expect_pay = Double.parseDouble(expectPayView.getText().toString());
                } else {
                    Toast.makeText(getContext(), warningMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activity.party.pay_method != null)
                    activity.party.pay_method.clear();
                activity.party.pay_method = fragment.getItem();
                for(int i=0; i<activity.party.pay_method.size();i++) {
                    if(activity.party.pay_method.get(i).title.equals("")) {
                        Toast.makeText(getContext(), "포함사항 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(activity.party.pay_method.get(i).price == -1) {
                        Toast.makeText(getContext(), "포함사항 금액을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                try {
                    activity.party.pay_end_at = getDeadlineTime();
                    activity.nextFragment();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "날짜를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);

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

        setDateSpinner(view);

        return view;
    }

    private String getDeadlineTime() {
        String time;

        time = mYearSpinner.getSelectedItem().toString() + "-" + mMonthSpinner.getSelectedItem().toString() + "-" + mDaySpinner.getSelectedItem().toString() + "T00:00:00";

        return DateUtil.getInstance().changeToPostFormat(time);
    }

    private void setDateSpinner(View view) {
        mYearSpinner = (Spinner)view.findViewById(R.id.spinner_year);
        mYearAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mYearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mYearSpinner.setAdapter(mYearAdapter);
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = (String)parent.getItemAtPosition(position);
                if(year.equals("년")) {
                    mDayAdapter.clear();
                    mDayAdapter.add("일");
                }
                if(year != null && month != null && !year.equals("년") && !month.equals("월")) {
                    int DayOfMonth = DateUtil.getInstance().getDayOfMonth(year, month);
                    int num = 1;
                    mDayAdapter.clear();
                    mDayAdapter.add("일");
                    for (int i = num; i<num+DayOfMonth; i++) {
                        mDayAdapter.add(""+i);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMonthSpinner = (Spinner)view.findViewById(R.id.spinner_month);
        mMonthAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mMonthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mMonthSpinner.setAdapter(mMonthAdapter);
        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = (String)parent.getItemAtPosition(position);
                if(month.equals("월")) {
                    mDayAdapter.clear();
                    mDayAdapter.add("일");
                }
                if(year != null && month != null && !year.equals("년") && !month.equals("월")) {
                    int DayOfMonth = DateUtil.getInstance().getDayOfMonth(year, month);
                    int num = 1;
                    mDayAdapter.clear();
                    mDayAdapter.add("일");
                    for (int i = num; i<num+DayOfMonth; i++) {
                        mDayAdapter.add(""+i);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDaySpinner = (Spinner)view.findViewById(R.id.spinner_day);
        mDayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mDayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mDaySpinner.setAdapter(mDayAdapter);

        mYearAdapter.add("년");
        int num = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = num;i<num+ Constants.MAX_YEAR; i++) {
            mYearAdapter.add(""+i);
        }
        mMonthAdapter.add("월");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_MONTH; i++) {
            mMonthAdapter.add(""+i);
        }
        mDayAdapter.add("일");

    }
}