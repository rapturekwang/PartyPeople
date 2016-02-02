package com.partypeople.www.partypeople.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.partypeople.www.partypeople.R;
import com.partypeople.www.partypeople.data.Party;
import com.partypeople.www.partypeople.dialog.LoadingDialogFragment;
import com.partypeople.www.partypeople.manager.NetworkManager;
import com.partypeople.www.partypeople.manager.PropertyManager;
import com.partypeople.www.partypeople.utils.Constants;
import com.partypeople.www.partypeople.utils.DateUtil;

import java.net.URISyntaxException;
import java.net.URLDecoder;

public class PaymentActivity extends AppCompatActivity {

    private WebView mainWebView;
    private WebViewInterface wvi;
    private final String APP_SCHEME = "http://api.partypeople.me/payresult.html";
    Party party;
    int selected;
    String name, tel, price;
    LoadingDialogFragment dialogFragment;

    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dialogFragment = new LoadingDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "loading");

        selected = getIntent().getIntExtra("selected", 0);
        price = getIntent().getStringExtra("price");
        party = (Party)getIntent().getSerializableExtra("party");
        name = getIntent().getStringExtra("name");
        tel = getIntent().getStringExtra("tel");

        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebViewClient(new InicisWebViewClient(this, mainWebView));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        wvi = new WebViewInterface(mainWebView, this);
        mainWebView.addJavascriptInterface(wvi, "JavaInterface");

        Intent intent = getIntent();
        Uri intentData = intent.getData();

        if ( intentData == null ) {
            mainWebView.loadUrl("http://api.partypeople.me/pay2.html");
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
            }
        }
    }

    class WebViewInterface {
        private WebView wView;
        private Activity context;

        public WebViewInterface(WebView wView, Activity context) {
            this.wView = wView;
            this.context = context;
        }

        @JavascriptInterface
        public void paymentResult(String response){
            response = response.split("\\?")[1];
            String result[] = response.split("&");
            for(int i=0;i<result.length;i++) {
                String array[] = result[i].split("=");
                try {
                    array[1] = URLDecoder.decode(array[1], "UTF-8");
                } catch (Exception e) {}

                if(array[0].equals("imp_success")) {
                    if(array[1].equals("true")) {
                        Intent intent = new Intent();
                        intent.putExtra("response", response);
                        intent.putExtra("result", true);
                        setResult(Constants.RESULT_CODE_PAYMENT, intent);
                        NetworkManager.getInstance().participate(PaymentActivity.this, party.id, party.amount_method.get(selected).title,
                                Integer.parseInt(price), new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                finish();
                            }

                            @Override
                            public void onFail(String response) {
                                finish();
                            }
                        });
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("response", response);
                        intent.putExtra("result", false);
                        setResult(Constants.RESULT_CODE_PAYMENT, intent);
                        finish();
                    }
                }
            }
        }

        @JavascriptInterface
        public void loadingFinish() {
            dialogFragment.dismiss();
        }
    }

    class InicisWebViewClient extends WebViewClient {

        private Activity activity;
        private AlertDialog alertIsp;
        private WebView target;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            target.loadUrl("javascript:start('" + party.name + "'," + price + ",'" + PropertyManager.getInstance().getUser().email +
                    "','" + name + "','" + tel + "','" + DateUtil.getInstance().getDueDate() + "')");
        }

        public InicisWebViewClient(Activity activity, WebView target) {
            this.activity = activity;
            this.target = target;

            this.alertIsp = new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("알림")
                .setMessage("모바일 ISP어플리케이션이 설치되어있지 않습니다. \n설치를 눌러 진행해주십시요.\n취소를 누르면 결제가 취소됩니다.")
                .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ISP
                        InicisWebViewClient.this.target.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(InicisWebViewClient.this.activity, "(-1)결제가 취소되었습니다..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).create();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            final WebView targetWebView = view;

            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
                Intent intent;

                if(url.startsWith("intent://inicis?orderKey=KPAY") || url.startsWith("market://details?id=com.inicis.kpay")) {
                    Toast.makeText(getApplicationContext(), "Kpay결제 지원을 준비중 입니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                } catch (URISyntaxException ex) {
                    return false;
                }

                Uri uri = Uri.parse(intent.getDataString());
                intent = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    activity.startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    if (url.startsWith("ispmobile://")) {
                        targetWebView.loadData("<html><body></body></html>", "text/html", "utf8");

                        alertIsp.show();
                        return true;
                    } else if( url.startsWith("intent://")) {//intent 형태의 스키마 처리
                        try {
                            Intent excepIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            String packageNm = excepIntent.getPackage();

                            excepIntent = new Intent(Intent.ACTION_VIEW);
                            excepIntent.setData(Uri.parse("market://search?q="+ packageNm));
                            activity.startActivity(excepIntent);

                            return true;
                        } catch (URISyntaxException e1) {}
                    }
                }
            }

            return false;
        }

    }
}
