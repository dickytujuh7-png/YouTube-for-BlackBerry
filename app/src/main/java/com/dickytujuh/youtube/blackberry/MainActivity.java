package com.dickytuj.youtube.blackberry;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private static final String YOUTUBE_URL = "https://m.youtube.com/";
    private WebView webView;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);

        // Use the real installed Chrome 68 engine for YouTube playback when available.
        // Android 4.3 does not support embedding another app's Chrome engine inside WebView.
        if (state == null && openChrome()) {
            finish();
            return;
        }

        // Fallback for devices where Chrome cannot be launched.
        getWindow().setFlags(1024, 1024);
        webView = new WebView(this);
        webView.setBackgroundColor(0xFF000000);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setBlockNetworkImage(false);
        s.setUserAgentString("Mozilla/5.0 (Linux; Android 4.3; Q10 Build/JLS36C) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.70 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);

        if (state != null && webView.restoreState(state) != null) {
            // Restore the last page.
        } else {
            webView.loadUrl(YOUTUBE_URL);
        }
    }

    private boolean openChrome() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL));
            intent.setPackage("com.android.chrome");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override protected void onSaveInstanceState(Bundle out) {
        if (webView != null) webView.saveState(out);
        super.onSaveInstanceState(out);
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
