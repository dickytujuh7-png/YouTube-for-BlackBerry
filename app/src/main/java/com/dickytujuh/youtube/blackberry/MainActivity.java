package com.dickytujuh.youtube.blackberry;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;
    private static final String HOME = "<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no\"><style>*{box-sizing:border-box}body{margin:0;background:#101114;color:#f4f4f5;font-family:Arial,sans-serif}.top{height:58px;padding:0 16px;display:flex;align-items:center;background:#191a1f;border-bottom:1px solid #292a30}.logo{width:30px;height:22px;border-radius:7px;background:#e62117;position:relative;margin-right:10px}.logo:after{content:'';position:absolute;left:12px;top:6px;border-left:9px solid white;border-top:5px solid transparent;border-bottom:5px solid transparent}.brand{font-size:17px;font-weight:bold}.wrap{padding:18px 16px;max-width:560px;margin:auto}.hello{font-size:23px;font-weight:bold;margin:10px 0 6px}.sub{font-size:13px;color:#a9abb4;margin-bottom:20px}form{display:flex;background:#24252b;border:1px solid #3b3d45;border-radius:10px;padding:5px;margin:0 0 22px}input{min-width:0;flex:1;background:transparent;border:0;color:white;font-size:15px;padding:10px;outline:none}button{border:0;border-radius:7px;background:#e62117;color:white;font-weight:bold;padding:0 16px;font-size:14px}.heading{font-size:14px;font-weight:bold;color:#c9cad0;margin:18px 0 10px}.grid{display:grid;grid-template-columns:1fr 1fr;gap:9px}.tile{display:block;text-decoration:none;color:#f5f5f5;background:#202127;border:1px solid #303139;border-radius:10px;padding:14px 12px;min-height:65px}.tile b{display:block;font-size:14px;margin-bottom:5px}.tile small{color:#a5a6ae;font-size:11px}.note{font-size:11px;color:#777984;margin-top:24px;text-align:center}a:active,button:active{opacity:.75}</style></head><body><div class=\"top\"><div class=\"logo\"></div><div class=\"brand\">YouTube for BlackBerry</div></div><main class=\"wrap\"><div class=\"hello\">Mau menonton apa?</div><div class=\"sub\">Cari video dengan cepat dan ringan.</div><form action=\"https://m.youtube.com/results\" method=\"get\"><input name=\"search_query\" placeholder=\"Cari video YouTube...\" autocomplete=\"off\"><button type=\"submit\">Cari</button></form><div class=\"heading\">JELAJAHI</div><div class=\"grid\"><a class=\"tile\" href=\"https://m.youtube.com/feed/trending\"><b>Trending</b><small>Video populer</small></a><a class=\"tile\" href=\"https://m.youtube.com/channel/UC-9-kyTW8ZkZNDHQJ6FgpwQ\"><b>Musik</b><small>Lagu dan musik</small></a><a class=\"tile\" href=\"https://m.youtube.com/gaming\"><b>Gaming</b><small>Video permainan</small></a><a class=\"tile\" href=\"https://m.youtube.com/feed/subscriptions\"><b>Langganan</b><small>Kanal yang diikuti</small></a></div><div class=\"note\">Tampilan sederhana untuk BlackBerry Q10</div></main></body></html>";
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setFlags(1024, 1024);
        webView = new WebView(this);
        webView.setBackgroundColor(0xFF101114);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
        if (state != null && webView.restoreState(state) != null) {
            // Keep the previous page after an activity recreation.
        } else {
            webView.loadDataWithBaseURL("https://m.youtube.com/", HOME, "text/html", "UTF-8", null);
        }
    }
    @Override protected void onSaveInstanceState(Bundle out) { if (webView != null) webView.saveState(out); super.onSaveInstanceState(out); }
    @Override public void onBackPressed() { if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }
    @Override protected void onDestroy() { if (webView != null) { webView.stopLoading(); webView.destroy(); webView = null; } super.onDestroy(); }
}
