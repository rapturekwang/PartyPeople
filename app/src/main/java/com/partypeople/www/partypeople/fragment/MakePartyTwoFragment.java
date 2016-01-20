package com.partypeople.www.partypeople.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.dialog.ExampleDialog;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyTwoFragment extends Fragment {
    Fragment[] list = {MakePartyChildFragment2.newInstance(0),
            MakePartyChildFragment2.newInstance(1),
            MakePartyChildFragment.newInstance(2)};
    private static final String ARG_NAME = "name";

    ArrayAdapter<String>[] mArrayAdapter = new ArrayAdapter[3];
    Spinner[] mSpinner = new Spinner[3];
    int[] spinnerId = {
            R.id.spinner_year,
            R.id.spinner_month,
            R.id.spinner_day,
    };
    String[] defaultValue = {
            "년", "월", "일"
    };
    int[] startValue = {
            2016, 1, 1
    };
    int[] maxValue = {
            Constants.MAX_YEAR,
            Constants.NUM_OF_MONTH,
            Constants.MAX_DAY
    };

    EditText expectPayView;
    RadioButton radioButton1, radioButton2, radioButton3;

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
                Fragment fragment = getChildFragmentManager().getFragments().get(0);
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
                if(radioButton3.isChecked()) {
                    activity.party.pay_method = ((MakePartyChildFragment)fragment).getItem();
                } else {
                    activity.party.pay_method = ((MakePartyChildFragment2)fragment).getItem();
                }
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

        radioButton1 = (RadioButton)view.findViewById(R.id.radioButton);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[0]).commit();
                }
            }
        });
        radioButton2 = (RadioButton)view.findViewById(R.id.radioButton2);
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[1]).commit();
                }
            }
        });
        radioButton3 = (RadioButton)view.findViewById(R.id.radioButton3);
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.container, list[2]).commit();
                }
            }
        });

        ImageView imageBtn = (ImageView)view.findViewById(R.id.img_btn_example);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExampleDialog dialog = new ExampleDialog(getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        initSpinnerView(view);
        setDateSpinner();
        String start = ((MakePartyActivity)getActivity()).party.start_at;
        setSpinnerTime(DateUtil.getInstance().getDefaultSettingData(-5, DateUtil.getInstance().changeStringToLong(start)));

        return view;
    }

    private String getDeadlineTime() {
        String time;

        time = mSpinner[0].getSelectedItem().toString() + "-" + mSpinner[1].getSelectedItem().toString() + "-" + mSpinner[2].getSelectedItem().toString() + "T00:00:00";

        return DateUtil.getInstance().changeToPostFormat(time);
    }

    void initSpinnerView(View view) {
        for(int i=0;i<3;i++) {
            mSpinner[i] = (Spinner)view.findViewById(spinnerId[i]);
            mArrayAdapter[i] = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
            mArrayAdapter[i].setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinner[i].setAdapter(mArrayAdapter[i]);
        }

        mSpinner[2].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String year = mSpinner[0].getSelectedItem().toString();
                    String month = mSpinner[1].getSelectedItem().toString();
                    if(year.equals("년") || month.equals("월")) {
                        mArrayAdapter[2].clear();
                        mArrayAdapter[2].add("일");
                    } else {
                        int DayOfMonth = DateUtil.getInstance().getDayOfMonth(year, month);
                        mArrayAdapter[2].clear();
                        mArrayAdapter[2].add("일");
                        for (int i = 1; i < DayOfMonth + 1; i++) {
                            mArrayAdapter[2].add("" + i);
                        }
                    }
                }
                return false;
            }
        });
    }

    void setDateSpinner() {
        startValue[0] = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0;i<3;i++) {
            mArrayAdapter[i].add(defaultValue[i]);
            for(int j=startValue[i];j<startValue[i] + maxValue[i];j++) {
                mArrayAdapter[i].add(String.format("%02d", j));
            }
        }
    }

    void setSpinnerTime(String date) {
        String[] str = new String(date).split(":");
        for(int i=0;i<3;i++) {
            int position = mArrayAdapter[i].getPosition(str[i]);
            mSpinner[i].setSelection(position);
        }
    }
}