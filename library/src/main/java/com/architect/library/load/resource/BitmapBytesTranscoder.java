package com.architect.library.load.resource;

import android.graphics.Bitmap;

import com.architect.library.load.engine.Resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/2/4 下午5:27
 */

public class BitmapBytesTranscoder implements ResourceTranscoder<Bitmap, byte[]> {

    private final Bitmap.CompressFormat compressFormat;
    private final int quality;

    public BitmapBytesTranscoder() {
        this(Bitmap.CompressFormat.JPEG, 100);
    }

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public BitmapBytesTranscoder(@NonNull Bitmap.CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    @Nullable
    @Override
    public Resource<byte[]> transcode(@NonNull Resource<Bitmap> toTranscode) {
        return null;
    }
}
