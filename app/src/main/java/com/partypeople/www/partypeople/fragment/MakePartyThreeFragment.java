package com.partypeople.www.partypeople.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.IdentifyActivity;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.NoticeActivity;
import com.partypeople.www.partypeople.data.Identity;
import com.partypeople.www.partypeople.data.User;
import com.partypeople.www.partypeople.data.UserResult;
import com.partypeople.www.partypeople.dialog.CertifyDialog;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class MakePartyThreeFragment extends Fragment {
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String name;
    Spinner bankView, nameSpinner;
    ArrayAdapter<String> mBankAdapter, mNameAdapter;
    EditText accountView, nameView, numView;
    ImageView accountBtn, phoneBtn;
    LoadingDialogFragment dialogFragment;
    CheckBox chboxPhone, chboxNext;
    RadioButton radioButton1, radioButton2;
    boolean phoneAuth = false;
    User user = new User();
    String tempBirth;

    public static MakePartyThreeFragment newInstance(String name) {
        MakePartyThreeFragment fragment = new MakePartyThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public MakePartyThreeFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_make_party_three, container, false);

        final MakePartyActivity activity = (MakePartyActivity)getActivity();

        nameView = (EditText)view.findViewById(R.id.edit_name);
        numView = (EditText)view.findViewById(R.id.edit_num);

        bankView = (Spinner)view.findViewById(R.id.spinner_bank);
        mBankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mBankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] banks = getResources().getStringArray(R.array.bank_name);
        for (int i = 0; i < banks.length; i++) {
            mBankAdapter.add(banks[i]);
        }
        bankView.setAdapter(mBankAdapter);

        nameSpinner = (Spinner)view.findViewById(R.id.spinner_name);
        mNameAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mNameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mNameAdapter.add("개인");
        mNameAdapter.add("사업자");
        nameSpinner.setAdapter(mNameAdapter);
        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    numView.setVisibility(View.GONE);
                    nameView.setEnabled(false);
                    nameView.setText(user.realname);
                } else if (position == 1) {
                    numView.setVisibility(View.VISIBLE);
                    nameView.setEnabled(true);
                    nameView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountView = (EditText)view.findViewById(R.id.edit_account);
        chboxPhone = (CheckBox)view.findViewById(R.id.chboxPhone);
        chboxNext = (CheckBox)view.findViewById(R.id.chbox_next);

        phoneBtn = (ImageView)view.findViewById(R.id.img_btn_phone);
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chboxPhone.isChecked()) {
                    startActivityForResult(new Intent(getContext(), IdentifyActivity.class), Constants.REQUEST_CODE_IDENTIFY);
                } else {
                    Toast.makeText(getContext(), "개인정보 취급방침에 동의해야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        accountBtn = (ImageView)view.findViewById(R.id.img_btn_account);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phoneAuth) {
                    Toast.makeText(getContext(), "휴대폰 본인인증을 먼저 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogFragment = new LoadingDialogFragment();
                dialogFragment.show(getFragmentManager(), "loading");

                StartAccountConfirmTask sact = new StartAccountConfirmTask();
                String[] params = new String[5];
                params[0] = accountView.getText().toString();
                String[] banks = getResources().getStringArray(R.array.bank_name);
                for (int i = 0; i < banks.length; i++) {
                    if(banks[i].equals(bankView.getSelectedItem().toString())) {
                        String[] bankCodes = getResources().getStringArray(R.array.bank_code);
                        params[1] = bankCodes[i];
                        break;
                    }
                }
                params[2] = nameView.getText().toString();
                if(nameSpinner.getSelectedItemPosition()==0) {
                    params[3] = "1";
                    params[4] = tempBirth;
                } else if(nameSpinner.getSelectedItemPosition()==1) {
                    params[3] = "2";
                    params[4] = numView.getText().toString();
                }

                sact.execute(params);
            }
        });

        if(PropertyManager.getInstance().getUser().auth) {
            phoneBtn.setImageResource(R.drawable.certi_phone_cg);
            accountBtn.setImageResource(R.drawable.certi_account_cg);
        }

        TextView textBtn = (TextView)view.findViewById(R.id.text_policy);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("call", Constants.CALL_POLICY);
                startActivity(intent);
            }
        });

        Button btn = (Button)view.findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton2.isChecked() && chboxNext.isChecked()) {
                    activity.nextFragment();
                } else if(radioButton2.isChecked()) {
                    Toast.makeText(getContext(), "상기 내용에 동의해주세요.", Toast.LENGTH_SHORT).show();
                } else if(radioButton1.isChecked() && PropertyManager.getInstance().getUser().auth) {
                    activity.nextFragment();
                } else if(radioButton1.isChecked()) {
                    Toast.makeText(getContext(), "본인 인증과 계좌 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final RelativeLayout relativeLayout1 = (RelativeLayout)view.findViewById(R.id.relativeLayout);
        final RelativeLayout relativeLayout2 = (RelativeLayout)view.findViewById(R.id.relativeLayout2);
        radioButton1 = (RadioButton)view.findViewById(R.id.radioButton);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout2.setVisibility(View.GONE);
                }
            }
        });
        radioButton2 = (RadioButton)view.findViewById(R.id.radioButton2);
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        viewEnable(false);

        return view;
    }

    void viewEnable(boolean enable) {
        bankView.setEnabled(enable);
        nameSpinner.setEnabled(enable);
        accountView.setEnabled(enable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE_IDENTIFY && resultCode==Constants.RESULT_CODE_IDENTIFY) {
            boolean result = data.getBooleanExtra("success", false);
            CertifyDialog dialog = new CertifyDialog(getContext(), result, Constants.CERTIFY_IDENTIFY, "");
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            if(result) {
                Identity identity = (Identity)data.getSerializableExtra("identity");
                phoneAuth = true;
                user.realname = identity.name;
                tempBirth = identity.birth.substring(2);
                user.tel = Double.parseDouble(identity.phone);

                phoneBtn.setImageResource(R.drawable.certi_phone_af);
                phoneBtn.setEnabled(false);
                chboxPhone.setEnabled(false);

                nameView.setText(identity.name);
                viewEnable(true);
            }
        }
    }

    private class StartAccountConfirmTask extends AsyncTask<String, Void, String> {      //계좌 확인 타스크

        private final String ConfirmUrl = "http://api.partypeople.me/confirm_module/account_confirm/request.php";   //계좌 확인 요청할 url

        @Override
        protected String doInBackground(String... params) {
            String account = params[0];     //계좌번호
            String bankCode = params[1];    //은행코드
            String name = params[2];        //이름
            String personal = params[3];    //개인 or 사업자
            String birth = params[4];       //생년월일 or 사업자번호

            try{
                /* url 에 http connection해 계좌번호 인증하는 부분 */
                URL url = new URL(ConfirmUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");  //전송방식 POST

                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");   //form 형식으로 보낸다는 설정

                StringBuffer buffer = new StringBuffer();   //요청보낼 데이터 구성한다.
                buffer.append("service").append("=").append("1").append("&");   //서비스코드(3: 계좌 유효성확인)
                buffer.append("svcGbn").append("=").append("5").append("&");    //작업코드(4: 계좌 유효성확인)
                buffer.append("svc_cls").append("=").append("").append("&");    //내/외국인(빈칸으로 놔둔다)
                buffer.append("PERSONAL").append("=").append(personal).append("&");  //개인or사업자 (개인 : 1, 사업자 : 2)
                buffer.append("strAccountNo").append("=").append(account).append("&");  //검사 요청할 계좌번호
                buffer.append("strBankCode").append("=").append(bankCode).append("&");  //검사 요청할 은행코드
                buffer.append("JUMINNO").append("=").append(birth).append("&"); //생년월일
                buffer.append("USERNM").append("=").append(name);

                /* 계좌 인증 요청한 결과값을 받아오는 부분 */
                OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "EUC-KR");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(buffer.toString());
                writer.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str);                     // View에 표시하기 위해 라인 구분자 추가
                }
                String myResult = builder.toString();

                return myResult;    //계좌 유효성 검사 결과 값 리턴

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
//            Log.d("result", response);
            dialogFragment.dismiss();
            super.onPostExecute(response);

            String[] code = response.split("/");
            boolean result = code[1].equals("응답코드:0000");

            if(result) {
                user.bank_name = bankView.getSelectedItem().toString();
                user.bank_account = Double.parseDouble(accountView.getText().toString());
                user.auth = true;
                NetworkManager.getInstance().putUser(getContext(), user, new NetworkManager.OnResultListener<UserResult>() {
                    @Override
                    public void onSuccess(UserResult result) {
                        CertifyDialog dialog = new CertifyDialog(getContext(), true, Constants.CERTIFY_ACCOUNT, "");
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        accountBtn.setImageResource(R.drawable.certi_account_af);
                        accountBtn.setEnabled(false);
                        viewEnable(false);
                    }

                    @Override
                    public void onFail(String response) {
                        Toast.makeText(getContext(), "네트워크 상황이 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                CertifyDialog dialog = new CertifyDialog(getContext(), false, Constants.CERTIFY_ACCOUNT, response);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        }
    }
}