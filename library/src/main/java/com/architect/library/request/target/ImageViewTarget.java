package com.architect.library.request.target;

import android.widget.ImageView;

import com.architect.library.request.Transition;

import androidx.annotation.NonNull;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> {

    public ImageViewTarget(@NonNull ImageView view) {
        super(view);
    }


    @Override
    public void onResourceReady(Z resource, Transition<? super Z> transition) {
        setResourceInternal(resource);
    }


    private void setResourceInternal(Z resource) {
        setResource(resource);
    }

    protected abstract void setResource(Z resource);
}
