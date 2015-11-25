package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.view.ThemeItemView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyOneFragment extends Fragment {
    private static final String ARG_NAME = "name";
    //Spinner spinner;
    ArrayAdapter<String> mYearAdapter, mMonthAdapter, mDayAdapter, mNoonAdapter, mHourAdapter, mMinuteAdapter;
    ArrayAdapter<String> mEYearAdapter, mEMonthAdapter, mEDayAdapter, mENoonAdapter, mEHourAdapter, mEMinuteAdapter;

    Spinner mYearSpinner, mMonthSpinner, mDaySpinner, mNoonSpinner, mHourSpinner, mMinuteSpinner;
    Spinner mEYearSpinner, mEMonthSpinner, mEDaySpinner, mENoonSpinner, mEHourSpinner, mEMinuteSpinner;

    // TODO: Rename and change types of parameters
    private String name;
    File mSavedFile;
    public static final int REQUEST_CODE_CROP = 0;
    EditText nameView, locationView, desView, partyPasswordView;
    SwitchCompat switchCompat;
    ImageView partyImage;
    GridView gridView;
    gridAdapter mAdapter;
    String year = "2015", month;
    String eYear = "2015", eMonth;
    int theme = -1;

    public static MakePartyOneFragment newInstance(String name) {
        MakePartyOneFragment fragment = new MakePartyOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MakePartyOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
        if (savedInstanceState != null) {
            String file = savedInstanceState.getString("filename");
            if (file != null) {
                mSavedFile = new File(file);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_party_one, container, false);

        nameView = (EditText)view.findViewById(R.id.edit_name);
        locationView = (EditText)view.findViewById(R.id.edit_location);
        desView = (EditText)view.findViewById(R.id.edit_description);

        partyImage = (ImageView)view.findViewById(R.id.image_party);
        partyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("crop", "true");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                photoPickerIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                photoPickerIntent.putExtra("aspectX", partyImage.getWidth());
                photoPickerIntent.putExtra("aspectY", partyImage.getHeight());
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePartyActivity activity = (MakePartyActivity) getActivity();
                String warningMessage="";
                if(desView.getText().toString().equals("")) warningMessage="모임 설명을 입력해 주세요.";
                if(locationView.getText().toString().equals("")) warningMessage="장소를 입력해 주세요";
                if(theme==-1) warningMessage="테마를 지정해 주세요.";
                if(nameView.getText().toString().equals("")) warningMessage="모임 이름을 입력해 주세요.";
                if(warningMessage.equals("")) {
                    activity.party.name = nameView.getText().toString();
                    activity.party.theme = new int[1];
                    activity.party.theme[0] = theme;
                    activity.party.location = locationView.getText().toString();
                    activity.party.description = desView.getText().toString();
                    activity.party.privated = switchCompat.isChecked();
                    activity.party.imageFile = mSavedFile;
                } else {
                    Toast.makeText(getContext(), warningMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (switchCompat.isChecked()) {
                    activity.party.password = partyPasswordView.getText().toString();
                }
                try {
                    activity.party.start_at = getStartTime();
                    activity.party.end_at = getEndTime();
                    activity.nextFragment();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
//                activity.party.imageFile = mSavedFile;
                activity.nextFragment();
            }
        });

        btn = (Button)view.findViewById(R.id.btn_find_address);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "주소 검색", Toast.LENGTH_SHORT).show();
            }
        });

        gridView = (GridView)view.findViewById(R.id.gridView);
        mAdapter = new gridAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                theme = position;
            }
        });

        partyPasswordView = (EditText)view.findViewById(R.id.edit_party_password);
        partyPasswordView.setEnabled(false);

        switchCompat = (SwitchCompat)view.findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    partyPasswordView.setEnabled(true);
                } else {
                    partyPasswordView.setEnabled(false);
                }
            }
        });

        setDateSpinner(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CROP && resultCode == getActivity().RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            partyImage.setImageBitmap(bm);
        }
    }

    private Uri getTempUri() {
        mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);

        return Uri.fromFile(mSavedFile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedFile != null) {
            outState.putString("filename", mSavedFile.getAbsolutePath());
        }
    }

    private String getStartTime() {
        String time;

        int hour = Integer.parseInt(mHourSpinner.getSelectedItem().toString());
        if(mNoonSpinner.getSelectedItem().toString().equals("오후")) {
            hour += 12;
        }
        time = mYearSpinner.getSelectedItem().toString() + "-" + mMonthSpinner.getSelectedItem().toString() + "-"
                + mDaySpinner.getSelectedItem().toString() + "T" + hour + ":" + mMinuteSpinner.getSelectedItem().toString() + ":00";

        Log.d("MakePartyOneFragment", time);

        return DateUtil.getInstance().changeToPostFormat(time);
    }

    private String getEndTime() {
        String time;

        int hour = Integer.parseInt(mEHourSpinner.getSelectedItem().toString());
        if(mENoonSpinner.getSelectedItem().toString().equals("오후")) {
            hour += 12;
        }
        time = mEYearSpinner.getSelectedItem().toString() + "-" + mEMonthSpinner.getSelectedItem().toString() + "-"
                + mEDaySpinner.getSelectedItem().toString() + "T" + hour + ":" + mEMinuteSpinner.getSelectedItem().toString() + ":00";

        Log.d("MakePartyOneFragment", time);

        return DateUtil.getInstance().changeToPostFormat(time);
    }

    private void setDateSpinner(View view) {
        mYearSpinner = (Spinner)view.findViewById(R.id.spinner_year);
        mYearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        mMonthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        mDayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDaySpinner.setAdapter(mDayAdapter);

        mNoonSpinner = (Spinner)view.findViewById(R.id.spinner_noon);
        mNoonAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mNoonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNoonSpinner.setAdapter(mNoonAdapter);

        mHourSpinner = (Spinner)view.findViewById(R.id.spinner_hour);
        mHourAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHourSpinner.setAdapter(mHourAdapter);

        mMinuteSpinner = (Spinner)view.findViewById(R.id.spinner_minute);
        mMinuteAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinuteSpinner.setAdapter(mMinuteAdapter);

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

        mNoonAdapter.add("오전");
        mNoonAdapter.add("오후");

        mHourAdapter.add("시");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_HOUR; i++) {
            mHourAdapter.add(""+i);
        }
        mMinuteAdapter.add("분");
        num = 0;
        for (int i = num; i<num+Constants.NUM_OF_MINUTE; i++) {
            mMinuteAdapter.add(""+i);
        }


        mEYearSpinner = (Spinner)view.findViewById(R.id.spinner_year_end);
        mEYearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mEYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEYearSpinner.setAdapter(mEYearAdapter);
        mEYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eYear = (String)parent.getItemAtPosition(position);
                if(eYear.equals("년")) {
                    mEDayAdapter.clear();
                    mEDayAdapter.add("일");
                }
                if(eYear != null && eMonth != null && !eYear.equals("년") && !eMonth.equals("월")) {
                    int DayOfMonth = DateUtil.getInstance().getDayOfMonth(eYear, eMonth);
                    int num = 1;
                    mEDayAdapter.clear();
                    mEDayAdapter.add("일");
                    for (int i = num; i<num+DayOfMonth; i++) {
                        mEDayAdapter.add(""+i);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEMonthSpinner = (Spinner)view.findViewById(R.id.spinner_month_end);
        mEMonthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mEMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEMonthSpinner.setAdapter(mEMonthAdapter);
        mEMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eMonth = (String)parent.getItemAtPosition(position);
                if(eMonth.equals("월")) {
                    mEDayAdapter.clear();
                    mEDayAdapter.add("일");
                }
                if(eYear != null && eMonth != null && !eYear.equals("년") && !eMonth.equals("월")) {
                    int DayOfMonth = DateUtil.getInstance().getDayOfMonth(eYear, eMonth);
                    int num = 1;
                    mEDayAdapter.clear();
                    mEDayAdapter.add("일");
                    for (int i = num; i<num+DayOfMonth; i++) {
                        mEDayAdapter.add(""+i);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEDaySpinner = (Spinner)view.findViewById(R.id.spinner_day_end);
        mEDayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mEDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEDaySpinner.setAdapter(mEDayAdapter);

        mENoonSpinner = (Spinner)view.findViewById(R.id.spinner_noon_end);
        mENoonAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mENoonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mENoonSpinner.setAdapter(mENoonAdapter);

        mEHourSpinner = (Spinner)view.findViewById(R.id.spinner_hour_end);
        mEHourAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mEHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEHourSpinner.setAdapter(mEHourAdapter);

        mEMinuteSpinner = (Spinner)view.findViewById(R.id.spinner_minute_end);
        mEMinuteAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mEMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEMinuteSpinner.setAdapter(mEMinuteAdapter);

        mEYearAdapter.add("년");
        num = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = num;i<num+ Constants.MAX_YEAR; i++) {
            mEYearAdapter.add(""+i);
        }
        mEMonthAdapter.add("월");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_MONTH; i++) {
            mEMonthAdapter.add(""+i);
        }
        mEDayAdapter.add("일");

        mENoonAdapter.add("오전");
        mENoonAdapter.add("오후");
        mEHourAdapter.add("시");
        num = 1;
        for (int i = num; i<num+Constants.NUM_OF_HOUR; i++) {
            mEHourAdapter.add(""+i);
        }
        mEMinuteAdapter.add("분");
        num = 0;
        for (int i = num; i<num+Constants.NUM_OF_MINUTE; i++) {
            mEMinuteAdapter.add(""+i);
        }
    }

    public class gridAdapter extends BaseAdapter {
        private String[] GRID_DATA = new String[] {
                "테마1",
                "테마2",
                "테마3",
                "테마4",
                "테마5"
        };

        @Override
        public int getCount() {
            return GRID_DATA.length;
        }

        @Override
        public Object getItem(int position) {
            return GRID_DATA[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ThemeItemView view;
            if (convertView == null) {
                view =  new ThemeItemView(parent.getContext());
            } else {
                view = (ThemeItemView) convertView;
            }
            view.setGridItem(GRID_DATA[position]);

            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 110));

            return view;
        }
    }
}
