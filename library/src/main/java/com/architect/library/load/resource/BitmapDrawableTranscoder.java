package com.architect.library.load.resource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.engine.Resource;
import com.architect.library.load.resource.bitmap.LazyBitmapDrawableResource;
import com.architect.library.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/2/4 下午5:39
 */

public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable> {

    private final Resources resources;

    public BitmapDrawableTranscoder(@NonNull Context context) {
        this(context.getResources());
    }

    public BitmapDrawableTranscoder(@NonNull Resources resources) {
        this.resources = Preconditions.checkNotNull(resources);
    }


    @Nullable
    @Override
    public Resource<BitmapDrawable> transcode(@NonNull Resource<Bitmap> toTranscode) {
        return LazyBitmapDrawableResource.obtain(resources, toTranscode);
    }
}
