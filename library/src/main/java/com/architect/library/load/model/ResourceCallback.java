package com.architect.library.load.model;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.engine.Resource;

public interface ResourceCallback {

    void onResourceReady(Resource<?> resource);

    void onLoadFailed();
}
