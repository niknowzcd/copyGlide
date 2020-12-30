package com.architect.library.load.model;

import android.graphics.drawable.BitmapDrawable;

public interface ResourceCallback {

    void onResourceReady(BitmapDrawable bitmapDrawable);

    void onLoadFailed();
}
