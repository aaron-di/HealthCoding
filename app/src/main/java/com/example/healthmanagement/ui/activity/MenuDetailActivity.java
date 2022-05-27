package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthmanagement.R;


public class MenuDetailActivity extends AppCompatActivity {
    private Activity myActivity;
    private WebView webView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_menu_detail);
        url =getIntent().getStringExtra("url");
        webView = findViewById(R.id.webView);

        initData();
    }

    private void initData() {
            WebSettings webSettings = webView.getSettings();
            // 开启 DOM storage API 功能
           // webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

    }
    //返回
    public void back(View view){
        finish();
    }
}
