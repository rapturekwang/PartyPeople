package com.partypeople.www.partypeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.dialog.CertifyDialog;
import com.partypeople.www.partypeople.fragment.CertifyFragment;

public class IdentifyActivity extends AppCompatActivity {

    private WebView confirmWebView;
    private WebViewInterface wvi;
    private static final String CONFIRM_URL = "http://api.partypeople.me/confirm_module/own_confirm/checkplus_main.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        confirmWebView = (WebView)findViewById(R.id.webView);
        confirmWebView.getSettings().setJavaScriptEnabled(true);
        confirmWebView.getSettings().setDefaultTextEncodingName("euc-kr");
        confirmWebView.setWebViewClient(new ConfirmWebClient());
        confirmWebView.loadUrl(CONFIRM_URL);

        wvi = new WebViewInterface(confirmWebView, this);
        confirmWebView.addJavascriptInterface(wvi, "CONFIRM");
    }

    class ConfirmWebClient extends WebViewClient {

        public ConfirmWebClient() {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    class WebViewInterface {
        private WebView wView;
        private Activity context;

        public WebViewInterface(WebView wView, Activity context) {
            this.wView = wView;
            this.context = context;
        }

        /**
         * 인증완료 후 가져오는 데이터/콜되는 함수
         * 파라미터로 인증 결과값을 받아온다.
         * @param requestNum 요청번호
         * @param responseNum 응답번호
         * @param authType 인증수단(M: 휴대폰, C: 신용카드, X: 공인인증서)
         * @param name 인증자 이름
         * @param birth 인증자 생년월일(YYYYMMDD)
         * @param gender 인증자 성별 (0: 여성, 1: 남성)
         */
        @JavascriptInterface
        public void getConfirmData(String requestNum, String responseNum, String authType,
                                   String name, String birth, String gender){
            Log.d("CONFIRMOK_OK", "CONFIRM_ok: " + requestNum + "," + responseNum + "," + authType + "," + name + "," + birth + "," + gender);

            Intent intent = new Intent();
            intent.putExtra("identify", true);
            setResult(CertifyFragment.RESULT_CODE_IDENTIFY, intent);
            context.finish();
        }

        /**
         * 인증 실패시 가져오는 데이터/콜되는 함수
         * 파라미터로 인증 실패에 대한 결과값을 받아온다.
         * @param requestNum
         * @param errorCode
         * @param authType
         */
        @JavascriptInterface
        public void getErrorConfirmData(String requestNum, String errorCode, String authType){
            Log.d("CONFIRMOK_ERROR", "CONFIRM_error: "+requestNum+","+errorCode+","+authType);

            Intent intent = new Intent();
            intent.putExtra("identify", false);
            setResult(CertifyFragment.RESULT_CODE_IDENTIFY, intent);
            context.finish();
        }
    }

}
