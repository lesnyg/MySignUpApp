package com.example.mysignupapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class DaumWebViewActivity extends AppCompatActivity {

    private WebView daum_webView;

    private TextView daum_result;

    private Handler handler;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daum_web_view);

        daum_result = (TextView) findViewById(R.id.daum_result);


        // WebView 초기화

        init_webView();


        // 핸들러를 통한 JavaScript 이벤트 반응

        handler = new Handler();

    }


    public void init_webView() {

        // WebView 설정

        daum_webView = (WebView) findViewById(R.id.daum_webview);


        // JavaScript 허용

        daum_webView.getSettings().setJavaScriptEnabled(true);


        // JavaScript의 window.open 허용

        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");


        // web client 를 chrome 으로 설정

        daum_webView.setWebChromeClient(new WebChromeClient());


        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // webview url load. php 파일 주소
        daum_webView.loadUrl("http://surefin1.cafe24.com:8085/smartrotest/daumphp.jsp");
//        daum_webView.loadUrl("https://www.naver.com/");

    }


    private class AndroidBridge {

        @JavascriptInterface

        public void setAddress(final String arg1, final String arg2, final String arg3, final String arg4) {

            handler.post(new Runnable() {

                @Override

                public void run() {

                    daum_result.setText(String.format("(%s) %s %s %s", arg1, arg2, arg3, arg4));

                    // WebView를 초기화 하지않으면 재사용할 수 없음

                    init_webView();
                    Intent intent = new Intent();
                    intent.putExtra("result_arg1", arg1);
                    intent.putExtra("result_arg2", arg2);
                    intent.putExtra("result_arg3", arg3);
                    intent.putExtra("result_arg4", arg4);
                    setResult(RESULT_OK, intent);
                    finish();

                }

            });

        }

    }

}
