package com.yiyun.rmj.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.StatusBarUtil;

public class MyWebActivity extends BaseActivity {

    private BridgeWebView mWebView;
    private String pageUrl;
    private String titleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    private void initTitleView(){
        ImageView imageBack = findViewById(R.id.iv_back);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(titleBar);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setTextColor(getResources().getColor(R.color.textcolor_black));

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        pageUrl = intent.getStringExtra("url");
        titleBar = intent.getStringExtra("title");
        initTitleView();

        mWebView = findViewById(R.id.web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        // 设置可以支持缩放sse
        mWebView.getSettings().setSupportZoom(true);
        // 扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setUserAgentString("pc");

//本地测试
//        String url = "http://192.168.101.214:8080/api/applyDetail.html" +
//                "?userToken=" + SpfUtils.getSpfUtils(this).getToken() +
//                "&productId=" + productId +
//                "&companyId=" + companyId +
//                "&userAppId=" + userAppId +
//                "&redMoney=" + redMoney +
//                "&channelId=" + channelId +
//                "&redShow=" + redShow +
//                "&userAppIdHq=" + SpfUtils.getSpfUtils(MyApplyWebActivity.this).getUserId() +
//                "&device=" + "android";

        mWebView.loadUrl(pageUrl);
        //mWebView.loadUrl("file:///android_asset/demo.html");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.registerHandler("applyBack", new BridgeHandler() {
            @Override
            public void handler(final String data, CallBackFunction function) {
                Log.e("bczsyq", "applyBack from js:" + data);
                finish();
            }
        });

    }

    @Override
    protected void setStatusBar(){
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }

    @Override
    protected void initData() {

    }

    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("syqdata", url);

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                showDialogLoading();
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();// 接受所有网站的证书
        }

    }
}
