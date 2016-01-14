package com.partypeople.www.partypeople.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.activity.MainActivity;
import com.partypeople.www.partypeople.activity.MakePartyActivity;
import com.partypeople.www.partypeople.activity.NoticeActivity;
import com.partypeople.www.partypeople.data.PartyResult;
import com.partypeople.www.partypeople.dialog.CertifyDialog;
import com.partypeople.www.partypeople.manager.NetworkManager;
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
    Spinner bankView;
    ArrayAdapter<String> mBankAdapter;
    EditText accountView;

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
        final View view = inflater.inflate(R.layout.fragment_make_party_three, container, false);

        final MakePartyActivity activity = (MakePartyActivity)getActivity();

        bankView = (Spinner)view.findViewById(R.id.spinner_bank);
        mBankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        mBankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        String[] banks = getResources().getStringArray(R.array.bank_name);
        for (int i = 0; i < banks.length; i++) {
            mBankAdapter.add(banks[i]);
        }
        bankView.setAdapter(mBankAdapter);
        accountView = (EditText)view.findViewById(R.id.edit_account);

        ImageView imgBtn = (ImageView)view.findViewById(R.id.img_btn_phone);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "휴대폰 인증", Toast.LENGTH_SHORT).show();
                CertifyDialog dialog = new CertifyDialog(getContext(), true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        imgBtn = (ImageView)view.findViewById(R.id.img_btn_account);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "계좌 인증", Toast.LENGTH_SHORT).show();
                CertifyDialog dialog = new CertifyDialog(getContext(), false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

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
               activity.nextFragment();
            }
        });

        return view;
    }

    private class StartAccountConfirmTask extends AsyncTask<String, Void, String> {      //계좌 확인 타스크

        private final String ConfirmUrl = "http://api.partypeople.me/confirm_module/account_confirm/request.php";   //계좌 확인 요청할 url

        @Override
        protected String doInBackground(String... params) {
            String account = params[0];     //계좌번호
            String bankCode = "25";         //은행코드

            try{
                /* url 에 http connection해 계좌번호 인증하는 부분 */
                URL url = new URL(ConfirmUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");  //전송방식 POST

                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");   //form 형식으로 보낸다는 설정


                StringBuffer buffer = new StringBuffer();   //요청보낼 데이터 구성한다.
                buffer.append("service").append("=").append("3").append("&");   //서비스코드(3: 계좌 유효성확인)
                buffer.append("svcGbn").append("=").append("4").append("&");    //작업코드(4: 계좌 유효성확인)
                buffer.append("svc_cls").append("=").append("").append("&");    //내/외국인(빈칸으로 놔둔다)
                buffer.append("PERSONAL").append("=").append("1").append("&");  //개인or사업자 (개인 : 1, 사업자 : 2)
                buffer.append("strAccountNo").append("=").append(account).append("&");  //검사 요청할 계좌번호
                buffer.append("strBankCode").append("=").append(bankCode);              //검사 요청할 은행코드


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
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();  //토스트로 결과 표시
            Log.d("result", result);
//            resultTv.setText(result);
            super.onPostExecute(result);
        }
    }
}