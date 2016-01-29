package com.partypeople.www.partypeople.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.adapter.ReportAdapter;
import com.partypeople.www.partypeople.data.Report;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendReportFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private String name;
    ReportAdapter mAdapter;
    Spinner reportView;
    ArrayAdapter<String> mReportAdapter;
    TextView textCancel, textImgName;
    EditText questionView;
    File mSavedFile = null;
    LoadingDialogFragment dialogFragment;

    public static final int REQUEST_CODE_CROP_IMAGE = 20;

    public static SendReportFragment newInstance(String name) {
        SendReportFragment fragment = new SendReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public SendReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_send_report, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestExternalPermission();
        }

        questionView = (EditText)view.findViewById(R.id.edit_report);
        textCancel = (TextView)view.findViewById(R.id.text_cancel);
        textImgName = (TextView)view.findViewById(R.id.text_imgname);

        reportView = (Spinner)view.findViewById(R.id.spinner);
        mReportAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mReportAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] reports = getResources().getStringArray(R.array.report_category);
        for (int i = 0; i < reports.length; i++) {
            mReportAdapter.add(reports[i]);
        }
        reportView.setAdapter(mReportAdapter);

        reportView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    textCancel.setVisibility(View.VISIBLE);
                } else {
                    textCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getFragmentManager(), "loading");

                Report report = new Report();
                report.category = reportView.getSelectedItem().toString();
                report.question = questionView.getText().toString();
                NetworkManager.getInstance().sendReport(getContext(), report, new NetworkManager.OnResultListener<Report>() {
                    @Override
                    public void onSuccess(Report result) {
                        if(mSavedFile==null) {
                            Toast.makeText(getContext(), "신고/문의 가 완료되었습니다", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                            dialogFragment.dismiss();
                        } else {
                            NetworkManager.getInstance().putReportImage(getContext(), mSavedFile, result.id, new NetworkManager.OnResultListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Toast.makeText(getContext(), "신고/문의 가 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                    dialogFragment.dismiss();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "통신상태가 불안정 합니다", Toast.LENGTH_SHORT).show();
                                    dialogFragment.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getContext(), "통신상태가 불안정 합니다", Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }
                });
            }
        });

        ImageView imgBtn = (ImageView)view.findViewById(R.id.img_btn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    photoPickerIntent.putExtra("noFaceDetection", true);
                    startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP_IMAGE);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CROP_IMAGE && resultCode==getActivity().RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                mSavedFile = new File(filePath);
                textImgName.setText(mSavedFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void requestExternalPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION);
        }
    }
}
