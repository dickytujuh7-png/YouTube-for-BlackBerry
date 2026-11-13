package com.dickytuj.blackberryfox;

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

    @Override
    public void onCreate(Bundle state) {
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

        // One compact address bar: no tabs, bookmarks, downloads or extra menus.
        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(5), dp(4), dp(5), dp(4));
        top.setBackgroundColor(Color.rgb(15, 15, 17));

        address = new EditText(this);
        address.setSingleLine(true);
        address.setTextSize(14);
        address.setHint("Cari atau masukkan alamat");
        address.setHintTextColor(Color.LTGRAY);
        address.setTextColor(Color.WHITE);
        address.setImeOptions(EditorInfo.IME_ACTION_GO);
        address.setPadding(dp(9), 0, dp(9), 0);
        address.setBackgroundColor(Color.rgb(42, 42, 46));
        top.addView(address, new LinearLayout.LayoutParams(0, dp(42), 1));

        Button go = button("Buka");
        LinearLayout.LayoutParams goParams = new LinearLayout.LayoutParams(dp(58), dp(42));
        goParams.setMargins(dp(5), 0, 0, 0);
        top.addView(go, goParams);
        root.addView(top);

        GeckoView browser = new GeckoView(this);
        browser.setSession(session);
        root.addView(browser, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        // Only the two essential navigation controls remain.
        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setPadding(dp(5), dp(3), dp(5), dp(3));
        bottom.setBackgroundColor(Color.rgb(15, 15, 17));

        Button back = button("Kembali");
        Button home = button("Beranda");
        bottom.addView(back, new LinearLayout.LayoutParams(0, dp(40), 1));
        LinearLayout.LayoutParams homeParams = new LinearLayout.LayoutParams(0, dp(40), 1);
        homeParams.setMargins(dp(5), 0, 0, 0);
        bottom.addView(home, homeParams);
        root.addView(bottom);

        go.setOnClickListener(v -> loadAddress());
        address.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_GO) {
                loadAddress();
                return true;
            }
            return false;
        });
        back.setOnClickListener(v -> {
            if (session != null) session.goBack();
        });
        home.setOnClickListener(v -> loadHome());

        setContentView(root);
        if (state == null) loadHome();
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setPadding(dp(2), 0, dp(2), 0);
        b.setBackgroundColor(Color.rgb(38, 38, 42));
        return b;
    }

    private void loadHome() {
        String home = "https://www.google.com/";
        address.setText(home);
        session.loadUri(home);
    }

    private void loadAddress() {
        String value = address.getText().toString().trim();
        if (value.isEmpty()) return;

        if (!value.contains("://")) {
            if (value.contains(" ") || !value.contains(".")) {
                try {
                    value = "https://www.google.com/search?q=" +
                            java.net.URLEncoder.encode(value, "UTF-8");
                } catch (java.io.UnsupportedEncodingException ignored) {
                    value = "https://www.google.com/";
                }
            } else {
                value = "https://" + value;
            }
        }
        address.setText(value);
        session.loadUri(value);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onBackPressed() {
        if (session != null) session.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (session != null) {
            session.close();
            session = null;
        }
        super.onDestroy();
    }
}
