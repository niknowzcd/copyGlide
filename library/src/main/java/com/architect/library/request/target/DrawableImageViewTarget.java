package com.architect.library.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class DrawableImageViewTarget extends ImageViewTarget<Drawable> {


    public DrawableImageViewTarget(@NonNull ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(final Drawable resource) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setImageDrawable(resource);
            }
        });
    }
}
