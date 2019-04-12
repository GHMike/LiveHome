package com.ting.a.livehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ting.a.livehome.R;

/**
 * 显示信息详情页面
 */
public class MessageActivity extends Activity {

    private TextView tv_title;//最上面的title
    private ProgressBar progressBar;//加载进度条
    private View left_but;//返回按鈕
    private WebView web_view;//显示新闻详情内容控件
    private String loadUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //寻找控件
        tv_title = findViewById(R.id.tv_title);
        left_but = findViewById(R.id.left_but);
        progressBar = findViewById(R.id.progressbar);
        left_but.setVisibility(View.VISIBLE);
        web_view = findViewById(R.id.message_context);

        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //从Intent中获取上个Activity传来的信息
        Intent intent = this.getIntent();//现获取Intent
        //再从Intent中获取指定参数
        loadUrl = intent.getStringExtra("newsUrl");
        String title = intent.getStringExtra("title");
        tv_title.setText(title == null ? "信息详情" : title);//判断是否为null，如果是就显示信息详情，如果不为null就赋值title字段
        showData();
    }

    //显示详情
    private void showData() {
        web_view.loadUrl(loadUrl);
        WebSettings settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        web_view.setWebViewClient(webViewClient);

    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页开始加载
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//网页加载完毕
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {//拦截指定为URL访问
            return super.shouldOverrideUrlLoading(view, request);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (web_view != null) {
            web_view.destroy();
            web_view = null;
        }
    }
}
