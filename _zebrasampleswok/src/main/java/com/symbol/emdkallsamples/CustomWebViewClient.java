package com.symbol.emdkallsamples;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by laure on 12/12/2017.
 */

public class CustomWebViewClient extends WebViewClient {
    public static final int PROCESS_LEVEL_NONE = 0;
    public static final int PROCESS_LEVEL_EMDKJAVASAMPLES = 1;

    private int mProcessingLevel = PROCESS_LEVEL_NONE;

    public CustomWebViewClient(int processinglevel)
    {
        mProcessingLevel = processinglevel;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        switch(mProcessingLevel)
        {
            case PROCESS_LEVEL_EMDKJAVASAMPLES:
                view.zoomBy(0.7f);
                view.loadUrl("javascript:var footer = document.getElementById(\"footer\"); footer.parentNode.removeChild(footer); var header = document.getElementById(\"header\"); header.parentNode.removeChild(header); var slider = document.getElementById(\"main-slider\"); slider.parentNode.removeChild(slider);");
            case PROCESS_LEVEL_NONE:
            default:
                break;
        }
    }

}