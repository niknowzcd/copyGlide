package com.architect.library.load.engine;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.resource.bitmap.LazyBitmapDrawableResource;
import com.architect.library.util.Preconditions;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * @Author: dly
 * @CreateDate: 2021/2/7 下午1:55
 */

public class BitmapDrawableDecoder<DataType> implements ResourceDecoder<DataType, BitmapDrawable> {

    private final ResourceDecoder<DataType, Bitmap> decoder;
    private final Resources resources;

    // Public API.
    @SuppressWarnings({"unused", "WeakerAccess"})
    public BitmapDrawableDecoder(Context context, ResourceDecoder<DataType, Bitmap> decoder) {
        this(context.getResources(), decoder);
    }

    public BitmapDrawableDecoder(@NonNull Resources resources, @NonNull ResourceDecoder<DataType, Bitmap> decoder) {
        this.resources = Preconditions.checkNotNull(resources);
        this.decoder = Preconditions.checkNotNull(decoder);
    }

    @Override
    public boolean handles(@NonNull DataType source) throws IOException {
        return decoder.handles(source);
    }

    @Override
    public Resource<BitmapDrawable> decode(@NonNull DataType source) throws IOException {
        Resource<Bitmap> bitmapResource = decoder.decode(source);
        return LazyBitmapDrawableResource.obtain(resources, bitmapResource);
    }
}
