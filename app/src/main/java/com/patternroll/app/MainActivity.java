package com.patternroll.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Everything the app needs is bundled; never navigate away from it.
                return !request.getUrl().toString().startsWith("file:///android_asset/");
            }
        });
        web.addJavascriptInterface(new Bridge(), "AndroidSave");
        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl("file:///android_asset/index.html");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    /** Called from the page: saves a PNG or GIF into Pictures/PatternRoll, visible in the gallery. */
    private class Bridge {
        @JavascriptInterface
        public boolean save(String filename, String mime, String base64) {
            try {
                byte[] data = Base64.decode(base64, Base64.DEFAULT);
                String safe = filename.replaceAll("[^A-Za-z0-9._-]", "_");
                int dot = safe.lastIndexOf('.');
                String name = (dot > 0 ? safe.substring(0, dot) : safe) + "-" + (System.currentTimeMillis() / 1000)
                        + (dot > 0 ? safe.substring(dot) : "");
                ContentValues v = new ContentValues();
                v.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                v.put(MediaStore.MediaColumns.MIME_TYPE, mime);
                v.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PatternRoll");
                v.put(MediaStore.MediaColumns.IS_PENDING, 1);
                ContentResolver r = getContentResolver();
                Uri uri = r.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);
                if (uri == null) return false;
                OutputStream out = r.openOutputStream(uri);
                if (out == null) return false;
                out.write(data);
                out.close();
                v.clear();
                v.put(MediaStore.MediaColumns.IS_PENDING, 0);
                r.update(uri, v, null, null);
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Saved to Pictures/PatternRoll", Toast.LENGTH_SHORT).show());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
