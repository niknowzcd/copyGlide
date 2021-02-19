package com.architect.library.temp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.Source;

import com.architect.library.Registry;
import com.architect.library.load.ResourceDecoder;
import com.architect.library.load.engine.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(api = 28)
public final class InputStreamBitmapImageDecoderResourceDecoder
        implements ResourceDecoder<InputStream, Bitmap> {
    private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

    @Override
    public boolean handles(@NonNull InputStream source) throws IOException {
        return true;
    }

    public static Bitmap bitmap;

    @Nullable
    @Override
    public Resource<Bitmap> decode(
            @NonNull InputStream stream)
            throws IOException {
        ByteBuffer buffer = ByteBufferUtil.fromStream(stream);
        Source source = ImageDecoder.createSource(buffer);

        bitmap = BitmapFactory.decodeStream(stream);
        System.out.println("111112 bitmap >> " + bitmap);

        return wrapped.decode(source);
    }
}
