package com.architect.library.request.target;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.architect.library.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ViewTarget<T extends View,Z> extends BaseTarget<Z> {
    protected final T view;

    public ViewTarget(@NonNull T view) {
        this.view = Preconditions.checkNotNull(view);
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {

    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {

    }



    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
