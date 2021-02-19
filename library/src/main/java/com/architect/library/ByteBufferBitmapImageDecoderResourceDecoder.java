package com.architect.library;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.Source;


import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.engine.Resource;

import java.io.IOException;
import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * {@link ByteBuffer} specific implementation of {@link
 * ByteBufferBitmapImageDecoderResourceDecoder}.
 */
@RequiresApi(api = 28)
public final class ByteBufferBitmapImageDecoderResourceDecoder
        implements ResourceDecoder<ByteBuffer, Bitmap> {
//    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Override
    public boolean handles(@NonNull ByteBuffer source) throws IOException {
        return true;
    }

    @Nullable
    @Override
    public Resource<Bitmap> decode(
            @NonNull ByteBuffer buffer)
            throws IOException {
        Source source = ImageDecoder.createSource(buffer);

        return null;
//        return wrapped.decode(source, width, height, options);
    }
}
