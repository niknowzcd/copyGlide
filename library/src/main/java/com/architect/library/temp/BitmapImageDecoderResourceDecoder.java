package com.architect.library.temp;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;

import com.architect.library.ImageDecoderResourceDecoder;
import com.architect.library.load.engine.Resource;
import com.architect.library.util.MyLogger;

import java.io.IOException;

import androidx.annotation.RequiresApi;

@RequiresApi(api = 28)
public final class BitmapImageDecoderResourceDecoder extends ImageDecoderResourceDecoder<Bitmap> {
    private static final String TAG = "BitmapImageDecoder";
    private final BitmapPool bitmapPool = new BitmapPoolAdapter();

    @Override
    protected Resource<Bitmap> decode(
            Source source,
            OnHeaderDecodedListener listener)
            throws IOException {
        Bitmap result = null;
        try {
            result = ImageDecoder.decodeBitmap(source, listener);
        } catch (IOException e) {
            MyLogger.d(BitmapImageDecoderResourceDecoder.TAG, "error >> " + e.getMessage() + " source >> " + source);
            e.printStackTrace();
        }
        return new BitmapResource(result, bitmapPool);
    }
}
