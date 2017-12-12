package com.symbol.emdkallsamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class InfoViewer extends AppCompatActivity {

    private String mAssetURL;
    private String mSampleTitle, mSampleTagID;
    private int mProcessingLevel;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_viewer);

        mWebView = (WebView)findViewById(R.id.wv_infoview);

        Intent receivedIntent = getIntent();
        mAssetURL = receivedIntent.getExtras().getString(Constants.BUNDLE_EXTRA_ASSET_URL);
        mSampleTitle = receivedIntent.getExtras().getString(Constants.BUNDLE_EXTRA_SAMPLE_TITLE);
        mSampleTagID = receivedIntent.getExtras().getString(Constants.BUNDLE_EXTRA_SAMPLE_TAGID);
        mProcessingLevel = receivedIntent.getExtras().getInt(Constants.BUNDLE_EXTRA_SAMPLE_PROCESSINGLEVEL, CustomWebViewClient.PROCESS_LEVEL_NONE);

        // No url, no infoview
        if(mAssetURL == null || mAssetURL.isEmpty())
            finish();

        setTitle(mSampleTitle);

        // Add custom web client to pre-process page
        CustomWebViewClient customWebClient = new CustomWebViewClient(mProcessingLevel);
        mWebView.setWebViewClient(customWebClient);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("file:///android_asset/" + mAssetURL);
    }
}
