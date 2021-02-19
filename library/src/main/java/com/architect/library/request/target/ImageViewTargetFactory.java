package com.architect.library.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @Author: dly
 * @CreateDate: 2021/2/5 下午8:19
 */

public class ImageViewTargetFactory {

    @SuppressWarnings("unchecked")
    public <Z> ViewTarget<ImageView, Z> buildTarget(ImageView view, Class<Z> clazz) {
        if (Drawable.class.isAssignableFrom(clazz)) {
            return (ViewTarget<ImageView, Z>) new DrawableImageViewTarget(view);
        }
        throw new IllegalArgumentException(
                "Unhandled class: " + clazz + ", try .as*(Class).transcode(ResourceTranscoder)");
    }
}
