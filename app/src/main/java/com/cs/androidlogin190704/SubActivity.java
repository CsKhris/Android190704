package com.cs.androidlogin190704;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btn = (Button)findViewById(R.id.back);
        btn.setOnClickListener(view -> {
            finish();
        });

        WebView webView = (WebView)findViewById(R.id.webview);

        // Redirect 되는 URL을 Web View가 처리하도록 하는 설정
        webView.setWebViewClient(new WebViewClient());

        // Web View에서 JavaScript를 사용하고 확대/축소 Icon이 보이도록 설정
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        // 넘겨준 Data를 읽어서 출력
        String url = getIntent().getStringExtra("link");
        webView.loadUrl(url);

    }
}
