package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.adapter.AutocompleteAdapter;
import com.partypeople.www.partypeople.adapter.MakePartyPagerAdapter;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;
import com.partypeople.www.partypeople.view.PhotoView;
import com.partypeople.www.partypeople.view.ThemeItemView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyOneFragment extends Fragment {
    private static final String ARG_NAME = "name";

    ArrayAdapter<String>[] mArrayAdapter = new ArrayAdapter[6];
    Spinner[] mSpinner = new Spinner[6];
    int[] spinnerId = {
            R.id.spinner_year,
            R.id.spinner_month,
            R.id.spinner_day,
            R.id.spinner_noon,
            R.id.spinner_hour,
            R.id.spinner_minute
    };
    String[] defaultValue = {
            "년", "월", "일", "오전", "시", "분"
    };
    int[] startValue = {
            2016, 1, 1, 0, 1, 0
    };
    int[] maxValue = {
            Constants.MAX_YEAR,
            Constants.NUM_OF_MONTH,
            Constants.MAX_DAY,
            0,
            Constants.NUM_OF_HOUR,
            Constants.NUM_OF_MINUTE
    };

    // TODO: Rename and change types of parameters
    private String name;
    File mSavedFile, imageDes;
    public static final int REQUEST_CODE_CROP_MAIN_IMAGE = 0;
    public static final int REQUEST_CODE_CROP_DES_IMAGE = 1;

    EditText nameView, desView, partyPasswordView;
    AutoCompleteTextView locationView;
    EditText detailLocationView;
    ViewPager pager;
    SwitchCompat switchCompat;
    MakePartyPagerAdapter mPagerAdapter;
    GridView gridView;
    gridAdapter mAdapter;
    int theme = -1, position;
    ImageView imgBtn;
    List<ImageView> photoBtn = new ArrayList<ImageView>();
    ArrayList<File> photos = new ArrayList<>();

    int imgBtns[] = {
            R.id.photo0,
            R.id.photo1,
            R.id.photo2,
            R.id.photo3
    };

    protected GoogleApiClient mGoogleApiClient;
    private AutocompleteAdapter mAutoAdapter;

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

        pager = (ViewPager)view.findViewById(R.id.pager);
        mPagerAdapter = new MakePartyPagerAdapter(getContext(), this);
        pager.setAdapter(mPagerAdapter);
        addViewOnPagerAdapter();

        locationView = (AutoCompleteTextView)view.findViewById(R.id.edit_location);
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), 0 /* clientId */, null)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
        mAutoAdapter = new AutocompleteAdapter(getContext(), mGoogleApiClient, null, null);
        locationView.setAdapter(mAutoAdapter);
        locationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str = locationView.getText().toString().split(" ");
                if (str[0].equals("대한민국")) {
                    locationView.setText("");
                    for (int i = 1; i < str.length; i++) {
                        locationView.append(str[i] + " ");
                    }
                }
                hideKeyboard();
                locationView.clearFocus();
            }
        });
        locationView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    locationView.setText("");
                    showKeyboard(locationView);
                }
            }
        });
        detailLocationView = (EditText)view.findViewById(R.id.edit_detail_location);

        nameView = (EditText)view.findViewById(R.id.edit_name);
        desView = (EditText)view.findViewById(R.id.edit_description);

        photoBtn.clear();
        for(int i=0;i<4;i++) {
            final int temp = i;
            photoBtn.add((ImageView)view.findViewById(imgBtns[temp]));
            photoBtn.get(temp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(temp);
                }
            });
        }

        imgBtn = (ImageView) view.findViewById(R.id.img_bottom_photo);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoto(-2);
            }
        });
        if(imageDes!=null) {
            setPhotoOnView(REQUEST_CODE_CROP_DES_IMAGE, imageDes, -1);
        }

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
                if(photos.size()==0) warningMessage="1개 이상의 사진을 첨부해 주세요.";
                if(warningMessage.equals("")) {
                    activity.party.name = nameView.getText().toString();
                    activity.party.themes = new int[1];
                    activity.party.themes[0] = theme+1;
                    activity.party.location = locationView.getText().toString() + detailLocationView.getText().toString();
                    activity.party.description = desView.getText().toString();
                    activity.party.privated = switchCompat.isChecked();
                    activity.party.imageFiles = photos;
                    activity.party.imageFile = imageDes;
                } else {
                    Toast.makeText(getContext(), warningMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (switchCompat.isChecked()) {
                    activity.party.password = partyPasswordView.getText().toString();
                }
                try {
                    activity.party.start_at = getSpinnerTime();
                    activity.nextFragment();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView img_btn = (ImageView)view.findViewById(R.id.btn_find_address);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationView.getText().toString().equals(""))
                    locationView.showDropDown();
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

        initSpinnerView(view);
        setDateSpinner();
        setSpinnerTime(DateUtil.getInstance().getDefaultSettingData(20, System.currentTimeMillis()));

//        restoreImage();

        return view;
    }

    public void setPhoto(int position) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        photoPickerIntent.putExtra("noFaceDetection", true);
        if(position!=-2) {
            photoPickerIntent.putExtra("aspectX", photoBtn.get(0).getWidth());
            photoPickerIntent.putExtra("aspectY", photoBtn.get(0).getHeight());
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP_MAIN_IMAGE);
        } else {
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP_DES_IMAGE);
        }

        this.position = position;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            setPhotoOnView(requestCode, mSavedFile, position);
        }
    }

    public void setPhotoOnView(int type, File image, int position) {
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(image), null, options);

            final int REQUIRED_SIZE = 360;
            int scale = 1;
            while(options.outWidth / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            bm = BitmapFactory.decodeStream(new FileInputStream(image), null, options2);
        }catch (Exception e) {

        }

        if(type == REQUEST_CODE_CROP_MAIN_IMAGE) {

            photos.add(image);
            photoBtn.get(position).setImageBitmap(bm);
            ((PhotoView) mPagerAdapter.getView(position)).setItemData(bm, position);
            if (position == 3) {
                return;
            } else {
                addViewOnPagerAdapter();
                photoBtn.get(position + 1).setVisibility(View.VISIBLE);
            }
        } else if(type == REQUEST_CODE_CROP_DES_IMAGE) {
//                bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            imgBtn.setImageBitmap(bm);
            imageDes = image;
        }
    }

    private Uri getTempUri() {
        mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000+".jpg");

        return Uri.fromFile(mSavedFile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedFile != null) {
            outState.putString("filename", mSavedFile.getAbsolutePath());
        }
    }

    private void addViewOnPagerAdapter() {
        PhotoView view = new PhotoView(getContext());
        ImageView imageBtn = (ImageView)view.findViewById(R.id.img_btn_photo);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoto(mPagerAdapter.getCount()-1);
            }
        });
        mPagerAdapter.addView(view);
    }

    private void setSpinnerTime(String date) {
        String[] str = new String(date).split(":");
        for(int i=0;i<6;i++) {
            int position = mArrayAdapter[i].getPosition(str[i]);
            mSpinner[i].setSelection(position);
        }
    }

    private String getSpinnerTime() {
        String time;

        int hour = Integer.parseInt(mSpinner[4].getSelectedItem().toString());
        if(mSpinner[3].getSelectedItem().toString().equals("오후")) {
            hour += 12;
        }
        time = mSpinner[0].getSelectedItem().toString() + "-" + mSpinner[1].getSelectedItem().toString() + "-"
                + mSpinner[2].getSelectedItem().toString() + "T" + hour + ":" + mSpinner[5].getSelectedItem().toString() + ":00";

        return DateUtil.getInstance().changeToPostFormat(time);
    }

    private void initSpinnerView(View view) {
        for(int i=0;i<6;i++) {
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

    private void setDateSpinner() {
        startValue[0] = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0;i<6;i++) {
            mArrayAdapter[i].add(defaultValue[i]);
            if(i==3) {
                mArrayAdapter[i].add("오후");
            } else {
                for(int j=startValue[i];j<startValue[i] + maxValue[i];j++) {
                    mArrayAdapter[i].add(String.format("%02d", j));
                }
            }
        }
    }

    public class gridAdapter extends BaseAdapter {
        private int[] ids = {R.drawable.theme_1, R.drawable.theme_2, R.drawable.theme_3, R.drawable.theme_4, R.drawable.theme_5};

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int position) {
            return ids[position];
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
            view.setGridItem(ids[position]);

//            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 110));

            return view;
        }
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showKeyboard(View view){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void restoreImage() {
        if(photos!=null && photos.size()!=0) {
            mPagerAdapter.removeAll();
            for(int i=0;i<photos.size();i++) {
                addViewOnPagerAdapter();
                setPhotoOnView(REQUEST_CODE_CROP_MAIN_IMAGE, photos.get(i), i);
            }
        }
        if(imageDes!=null) {
            setPhotoOnView(REQUEST_CODE_CROP_DES_IMAGE, imageDes, -1);
        }
    }
}
