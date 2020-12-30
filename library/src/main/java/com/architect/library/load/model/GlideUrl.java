package com.architect.library.load.model;

import android.net.Uri;
import android.text.TextUtils;

import com.architect.library.Key;
import com.architect.library.util.Preconditions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class GlideUrl implements Key {

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%;$";

    private final Headers headers;
    private final URL url;
    private final String stringUrl;

    private URL safeUrl;
    private String safeStringUrl;

    public GlideUrl(String url) {
        this(url, Headers.DEFAULT);
    }

    public GlideUrl(String url, Headers headers) {
        this.url = null;
        stringUrl = url;
        this.headers = Preconditions.checkNotNull(headers);
    }

    public URL toURL() throws MalformedURLException {
        return getSafeUrl();
    }

    private URL getSafeUrl() throws MalformedURLException {
        if (safeUrl == null) {
            safeUrl = new URL(getSafeStringUrl());
        }
        return safeUrl;
    }

    private String getSafeStringUrl() {
        if (TextUtils.isEmpty(safeStringUrl)) {
            String unsafeStringUrl = stringUrl;
            if (TextUtils.isEmpty(unsafeStringUrl)) {
                unsafeStringUrl = Preconditions.checkNotNull(url).toString();
            }
            safeStringUrl = Uri.encode(unsafeStringUrl, ALLOWED_URI_CHARS);
        }

        return safeStringUrl;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }
}
