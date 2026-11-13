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
import android.widget.TextView;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

public class MainActivity extends Activity {
    private GeckoRuntime runtime;
    private GeckoSession session;
    private EditText address;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        runtime = GeckoRuntime.create(this);
        session = new GeckoSession();
        session.open(runtime);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(6), dp(5), dp(6), dp(5));
        top.setBackgroundColor(Color.rgb(16,16,18));
        TextView brand = new TextView(this);
        brand.setText("BlackBerryFox");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(15);
        top.addView(brand, new LinearLayout.LayoutParams(-2, dp(40)));

        address = new EditText(this);
        address.setSingleLine(true);
        address.setTextSize(13);
        address.setHint("Cari atau masukkan alamat");
        address.setHintTextColor(Color.GRAY);
        address.setTextColor(Color.WHITE);
        address.setImeOptions(EditorInfo.IME_ACTION_GO);
        address.setPadding(dp(8), 0, dp(8), 0);
        address.setBackgroundColor(Color.rgb(42,42,46));
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(0, dp(40), 1);
        ap.setMargins(dp(7), 0, dp(5), 0);
        top.addView(address, ap);
        Button go = button("Buka");
        top.addView(go, new LinearLayout.LayoutParams(dp(54), dp(40)));
        root.addView(top);

        GeckoView view = new GeckoView(this);
        view.setSession(session);
        root.addView(view, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setPadding(dp(4), dp(3), dp(4), dp(3));
        bottom.setBackgroundColor(Color.rgb(16,16,18));
        Button back = button("‹");
        Button forward = button("›");
        Button home = button("Beranda");
        Button reload = button("Muat ulang");
        bottom.addView(back, new LinearLayout.LayoutParams(0, dp(42), 1));
        bottom.addView(forward, new LinearLayout.LayoutParams(0, dp(42), 1));
        bottom.addView(home, new LinearLayout.LayoutParams(0, dp(42), 1.4f));
        bottom.addView(reload, new LinearLayout.LayoutParams(0, dp(42), 1.4f));
        root.addView(bottom);

        go.setOnClickListener(v -> loadAddress());
        address.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_GO) { loadAddress(); return true; }
            return false;
        });
        back.setOnClickListener(v -> session.goBack());
        forward.setOnClickListener(v -> session.goForward());
        home.setOnClickListener(v -> loadHome());
        reload.setOnClickListener(v -> session.reload());
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
        b.setBackgroundColor(Color.rgb(38,38,42));
        return b;
    }

    private void loadHome() {
        address.setText("https://www.google.com/");
        session.loadUri("https://www.google.com/");
    }

    private void loadAddress() {
        String value = address.getText().toString().trim();
        if (value.length() == 0) return;
        if (!value.contains("://")) {
            if (value.contains(" ") || !value.contains(".")) {
                value = "https://www.google.com/search?q=" + value.replace(" ", "+");
            } else value = "https://" + value;
        }
        address.setText(value);
        session.loadUri(value);
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override public void onBackPressed() {
        if (session != null) session.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (session != null) { session.close(); session = null; }
        super.onDestroy();
    }
}
