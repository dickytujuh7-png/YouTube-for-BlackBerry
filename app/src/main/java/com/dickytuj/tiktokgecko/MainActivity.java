package com.dickytuj.tiktokgecko;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

public class MainActivity extends Activity {
    private GeckoRuntime runtime;
    private GeckoSession session;
    private EditText address;
    private LinearLayout toolbar;
    private static final String HOME = "https://www.tiktok.com/";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        runtime = GeckoRuntime.create(this);
        session = new GeckoSession();
        session.open(runtime);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);

        toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(5), dp(3), dp(5), dp(3));
        toolbar.setBackgroundColor(Color.rgb(18,18,20));

        address = new EditText(this);
        address.setSingleLine(true);
        address.setTextSize(12);
        address.setHint("TikTok");
        address.setHintTextColor(Color.LTGRAY);
        address.setTextColor(Color.WHITE);
        address.setImeOptions(EditorInfo.IME_ACTION_GO);
        address.setPadding(dp(7), 0, dp(7), 0);
        address.setBackgroundColor(Color.rgb(43,43,47));
        toolbar.addView(address, new LinearLayout.LayoutParams(0, dp(34), 1));

        Button go = button("Buka", dp(48));
        Button reload = button("↻", dp(38));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dp(48), dp(34));
        p.setMargins(dp(5),0,0,0);
        toolbar.addView(go,p);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(dp(38),dp(34));
        rp.setMargins(dp(4),0,0,0);
        toolbar.addView(reload,rp);
        root.addView(toolbar);

        GeckoView browser = new GeckoView(this);
        browser.setSession(session);
        root.addView(browser, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        go.setOnClickListener(v -> openAddress());
        reload.setOnClickListener(v -> session.reload());
        address.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_GO) {
                openAddress();
                return true;
            }
            return false;
        });

        session.setContentDelegate(new GeckoSession.ContentDelegate() {
            @Override public void onFullScreen(GeckoSession s, boolean fullScreen) {
                runOnUiThread(() -> toolbar.setVisibility(fullScreen ? android.view.View.GONE : android.view.View.VISIBLE));
            }
        });

        setContentView(root);
        if (state == null) {
            address.setText(HOME);
            session.loadUri(HOME);
        }
    }

    private Button button(String label, int width) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setPadding(dp(2),0,dp(2),0);
        b.setBackgroundColor(Color.rgb(45,45,50));
        return b;
    }

    private void openAddress() {
        String value = address.getText().toString().trim();
        if (value.isEmpty()) return;
        if (!value.contains("://")) value = value.contains(".")
                ? "https://" + value : "https://www.google.com/search?q=" + android.net.Uri.encode(value);
        address.setText(value);
        session.loadUri(value);
    }

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override public void onBackPressed() {
        if (session != null && session.canGoBack()) session.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (session != null) { session.close(); session = null; }
        super.onDestroy();
    }
}