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
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.view.ThemeItemView;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyOneFragment extends Fragment {
    private static final String ARG_NAME = "name";
    //Spinner spinner;
    ArrayAdapter<String> mYearAdapter, mMonthAdapter, mDayAdapter, mNoonAdapter, mHourAdapter, mMinuteAdapter;

    // TODO: Rename and change types of parameters
    private String name;
    File mSavedFile;
    public static final int REQUEST_CODE_CROP = 0;
    ImageView imageView;
    GridView gridView;
    gridAdapter mAdapter;
    String year = "2015", month;

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

    TextView nameView;

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

        imageView = (ImageView)view.findViewById(R.id.image_party);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("crop", "true");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                photoPickerIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                photoPickerIntent.putExtra("aspectX", imageView.getWidth());
                photoPickerIntent.putExtra("aspectY", imageView.getHeight());
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePartyActivity activity = (MakePartyActivity) getActivity();
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
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        final EditText editText = (EditText)view.findViewById(R.id.edit_party_password);
        editText.setEnabled(false);

        SwitchCompat switchCompat = (SwitchCompat)view.findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setEnabled(true);
                } else {
                    editText.setEnabled(false);
                }
            }
        });

        setDateSpinner(view);
//        nameView = (TextView)v.findViewById(R.id.text_name);
//        nameView.setText(name);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CROP && resultCode == getActivity().RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            imageView.setImageBitmap(bm);
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

    private void setDateSpinner(View view) {
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_year);
        mYearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mYearAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinner = (Spinner)view.findViewById(R.id.spinner_month);
        mMonthAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mMonthAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinner = (Spinner)view.findViewById(R.id.spinner_day);
        mDayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mDayAdapter);

        spinner = (Spinner)view.findViewById(R.id.spinner_noon);
        mNoonAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mNoonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mNoonAdapter);

        spinner = (Spinner)view.findViewById(R.id.spinner_hour);
        mHourAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mHourAdapter);

        spinner = (Spinner)view.findViewById(R.id.spinner_minute);
        mMinuteAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        mMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mMinuteAdapter);

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
//        num = 1;
//        for (int i = num; i<num+Constants.NUM_OF_DAY; i++) {
//            mDayAdapter.add(""+i);
//        }
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
