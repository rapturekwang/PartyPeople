package com.partypeople.www.partypeople.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.manager.NetworkManager;

public class PaymentActivity extends AppCompatActivity {

    private WebView webView;
    private static final String CONFIRM_URL = "http://partypeople.me:3000/pay.html?title=test&amount=1000&email=rapturekwang@gmail.com&name=정광희&tel=01031005883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        webView = (WebView)findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new PaymentWebClient());
//        webView.loadUrl(CONFIRM_URL);
        NetworkManager.getInstance().payment(getApplicationContext(), null, new NetworkManager.OnResultListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    class PaymentWebClient extends WebViewClient {

        public PaymentWebClient() {
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
}
