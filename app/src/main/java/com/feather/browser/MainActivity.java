package com.feather.browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.net.Uri;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import okio.ByteString;

public class MainActivity extends Activity {
    OkHttpClient client;
    WebSocket ws;
    EditText host, token, url;
    ImageView page;
    TextView status;
    FrameLayout root, toolbar;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable hideToolbar;
    String sid = "q10-" + UUID.randomUUID().toString().substring(0, 8);
    float sx, sy, lx, ly;
    boolean moved;
    boolean connected = false;

    int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }

    Button button(String s) {
        Button b = new Button(this);
        b.setText(s);
        b.setTextSize(11);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        return b;
    }

    EditText field(String s, boolean password) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setTextSize(13);
        e.setHint(s);
        e.setTextColor(Color.WHITE);
        e.setHintTextColor(0xffaaaaaa);
        e.setPadding(dp(9), 0, dp(9), 0);
        e.setBackgroundColor(0xff303944);
        if (password) e.setInputType(129);
        return e;
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setFlags(1024, 1024);
        // Keep fullscreen, but do not draw page content underneath a system/runtime
        // navigation area if the BlackBerry Android Runtime keeps one visible.
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.BLACK);
        page = new ImageView(this);
        page.setScaleType(ImageView.ScaleType.FIT_XY);
        page.setBackgroundColor(Color.BLACK);
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        toolbar = new FrameLayout(this);
        toolbar.setBackgroundColor(0xee101820);
        LinearLayout bar = new LinearLayout(this);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(3), 0, dp(3), 0);
        url = field("Alamat atau kata kunci", false);
        url.setImeOptions(EditorInfo.IME_ACTION_GO);
        bar.addView(url, new LinearLayout.LayoutParams(0, dp(42), 1));
        Button go = button("Buka");
        Button back = button("‹");
        Button reload = button("↻");
        Button menu = button("⋮");
        bar.addView(go, new LinearLayout.LayoutParams(dp(48), dp(42)));
        bar.addView(back, new LinearLayout.LayoutParams(dp(42), dp(42)));
        bar.addView(reload, new LinearLayout.LayoutParams(dp(42), dp(42)));
        bar.addView(menu, new LinearLayout.LayoutParams(dp(42), dp(42)));
        toolbar.addView(bar, new FrameLayout.LayoutParams(-1, dp(48), Gravity.TOP));
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1, dp(48), Gravity.TOP);
        root.addView(toolbar, tp);
        toolbar.setVisibility(View.GONE);

        // A narrow top-edge strip reveals the controls without intercepting normal page taps.
        View reveal = new View(this);
        reveal.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams rp = new FrameLayout.LayoutParams(-1, dp(12), Gravity.TOP);
        root.addView(reveal, rp);
        reveal.setOnClickListener(v -> showToolbar());

        setContentView(root);
        page.setOnTouchListener((v, e) -> touch(e));
        go.setOnClickListener(v -> { navigate(); scheduleHide(); });
        url.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_GO) { navigate(); hideToolbar(); return true; }
            return false;
        });
        back.setOnClickListener(v -> { action("back"); scheduleHide(); });
        reload.setOnClickListener(v -> { action("refresh"); scheduleHide(); });
        menu.setOnClickListener(v -> showSettings());

        SharedPreferences prefs = getSharedPreferences("feather_settings", MODE_PRIVATE);
        String savedHost = prefs.getString("server", "");
        String savedToken = prefs.getString("token", "");
        if (!savedHost.isEmpty() && !savedToken.isEmpty()) {
            connect(savedHost, savedToken);
        } else {
            showToolbar();
            showSettings();
        }
    }

    void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        if (hideToolbar != null) handler.removeCallbacks(hideToolbar);
        scheduleHide();
    }

    void scheduleHide() {
        if (hideToolbar != null) handler.removeCallbacks(hideToolbar);
        hideToolbar = () -> {
            if (connected) toolbar.setVisibility(View.GONE);
        };
        handler.postDelayed(hideToolbar, 6000);
    }

    void hideToolbar() {
        if (hideToolbar != null) handler.removeCallbacks(hideToolbar);
        if (connected) toolbar.setVisibility(View.GONE);
    }

    void showSettings() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(8), dp(18), dp(4));
        host = field("Server Android, contoh http://192.168.1.8:8080", false);
        token = field("Token dari Termux", true);
        SharedPreferences p = getSharedPreferences("feather_settings", MODE_PRIVATE);
        host.setText(p.getString("server", ""));
        token.setText(p.getString("token", ""));
        box.addView(host, new LinearLayout.LayoutParams(-1, dp(44)));
        box.addView(token, new LinearLayout.LayoutParams(-1, dp(44)));
        status = new TextView(this);
        status.setText(connected ? "Terhubung" : "Belum terhubung");
        status.setTextColor(0xff333333);
        status.setPadding(0, dp(8), 0, dp(4));
        box.addView(status);
        new AlertDialog.Builder(this)
            .setTitle("Pengaturan FeatherBrowser")
            .setView(box)
            .setPositiveButton("Hubungkan", (d, w) -> connect(host.getText().toString(), token.getText().toString()))
            .setNegativeButton("Tutup", (d, w) -> { if (connected) hideToolbar(); })
            .show();
    }

    void connect(String server, String accessToken) {
        try {
            String s = server.trim();
            String savedToken = accessToken.trim();
            if (s.isEmpty() || savedToken.isEmpty()) {
                Toast.makeText(this, "Isi alamat server dan token", Toast.LENGTH_SHORT).show();
                showSettings();
                return;
            }
            getSharedPreferences("feather_settings", MODE_PRIVATE).edit()
                .putString("server", s).putString("token", savedToken).apply();
            if (!s.contains("://")) s = "http://" + s;
            Uri u = Uri.parse(s);
            String scheme = "https".equals(u.getScheme()) ? "wss" : "ws";
            String w = scheme + "://" + u.getHost() +
                (u.getPort() > 0 ? ":" + u.getPort() : "") +
                "/ws?sessionId=" + URLEncoder.encode(sid, "UTF-8") + "&flow=ack";
            Request r = new Request.Builder().url(w).header("Authorization", "Bearer " + savedToken).build();
            if (ws != null) ws.close(1000, "reconnect");
            connected = false;
            ws = client.newWebSocket(r, new WebSocketListener() {
                public void onOpen(WebSocket socket, Response response) {
                    runOnUiThread(() -> {
                        connected = true;
                        toolbar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Feather terhubung", Toast.LENGTH_SHORT).show();
                    });
                }
                public void onMessage(WebSocket socket, String message) {
                    try {
                        JSONObject j = new JSONObject(message);
                        if ("error".equals(j.optString("type"))) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, j.optString("message"), Toast.LENGTH_LONG).show());
                        }
                    } catch (Exception ignored) {}
                }
                public void onMessage(WebSocket socket, ByteString bytes) {
                    byte[] data = bytes.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) runOnUiThread(() -> page.setImageBitmap(bitmap));
                    socket.send("{\"type\":\"ack\"}");
                }
                public void onFailure(WebSocket socket, Throwable error, Response response) {
                    runOnUiThread(() -> {
                        connected = false;
                        Toast.makeText(MainActivity.this, "Koneksi gagal: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        showToolbar();
                    });
                }
                public void onClosed(WebSocket socket, int code, String reason) {
                    runOnUiThread(() -> connected = false);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Alamat server tidak valid", Toast.LENGTH_LONG).show();
            showSettings();
        }
    }

    void navigate() {
        String s = url.getText().toString().trim();
        if (s.isEmpty()) return;
        if (!s.contains("://")) {
            if (s.contains(" ") || !s.contains(".")) {
                try { s = "https://www.google.com/search?q=" + URLEncoder.encode(s, "UTF-8"); }
                catch (Exception ignored) {}
            } else s = "https://" + s;
        }
        url.setText(s);
        send("navigate", "url", s);
    }

    void action(String a) { send(a, null, null); }

    void send(String a, String key, String value) {
        try {
            if (ws == null) {
                Toast.makeText(this, "Hubungkan ke server dahulu", Toast.LENGTH_SHORT).show();
                showSettings();
                return;
            }
            JSONObject j = new JSONObject();
            j.put("type", "action");
            j.put("action", a);
            if (key != null) j.put(key, value);
            ws.send(j.toString());
        } catch (Exception ignored) {}
    }

    // The image is rendered with FIT_XY, so map touches independently on each axis.
    // This also keeps taps aligned when the bottom system bar reduces the usable height.
    float x(float v) {
        return page.getWidth() <= 0 ? 0 : Math.max(0, Math.min(720, v * 720f / page.getWidth()));
    }
    float y(float v) {
        return page.getHeight() <= 0 ? 0 : Math.max(0, Math.min(720, v * 720f / page.getHeight()));
    }

    boolean touch(MotionEvent e) {
        float a = x(e.getX()), b = y(e.getY());
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            sx = lx = a; sy = ly = b; moved = false; return true;
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = lx - a, dy = ly - b;
            if (Math.abs(a - sx) + Math.abs(b - sy) > 10) moved = true;
            if (moved && Math.abs(dx) + Math.abs(dy) > 1) {
                try {
                    JSONObject j = new JSONObject();
                    j.put("type", "action"); j.put("action", "wheel");
                    j.put("tapX", a); j.put("tapY", b);
                    j.put("deltaX", dx); j.put("deltaY", dy);
                    if (ws != null) ws.send(j.toString());
                } catch (Exception ignored) {}
                lx = a; ly = b;
            }
            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_UP && !moved) {
            try {
                JSONObject j = new JSONObject();
                j.put("type", "action"); j.put("action", "tap");
                j.put("tapX", a); j.put("tapY", b);
                if (ws != null) ws.send(j.toString());
            } catch (Exception ignored) {}
        }
        return true;
    }

    @Override public void onBackPressed() {
        if (toolbar.getVisibility() != View.VISIBLE) showToolbar();
        else action("back");
    }

    @Override protected void onDestroy() {
        if (hideToolbar != null) handler.removeCallbacks(hideToolbar);
        if (ws != null) ws.close(1000, "close");
        super.onDestroy();
    }
}