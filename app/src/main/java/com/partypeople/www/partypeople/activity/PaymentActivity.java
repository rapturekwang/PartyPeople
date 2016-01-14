package com.partypeople.www.partypeople.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.partypeople.www.partypeople.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");
        webView.loadUrl("https://service.iamport.kr/js/iamport.payment.js");
    }
}
