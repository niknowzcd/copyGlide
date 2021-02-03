package com.architect.library.request;

import android.graphics.drawable.Drawable;

import com.architect.library.load.engine.Resource;
import com.architect.library.manager.LifecycleListener;

import androidx.annotation.Nullable;


public interface Target<R> extends LifecycleListener {

    int SIZE_ORIGINAL = Integer.MIN_VALUE;

    void onLoadStarted(Drawable placeholder);

    void onLoadFailed(@Nullable Drawable errorDrawable);

    void onResourceReady(R resource, Transition<? super R> transition);

    void setRequest(@Nullable Request request);

    Request getRequest();
}
