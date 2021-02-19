package com.architect.library.temp;

import android.graphics.Bitmap;

import com.architect.library.load.engine.Initializable;
import com.architect.library.load.engine.Resource;
import com.architect.library.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A resource wrapping a {@link android.graphics.Bitmap} object.
 */
public class BitmapResource implements Resource<Bitmap>, Initializable {
    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;

    /**
     * Returns a new {@link BitmapResource} wrapping the given {@link Bitmap} if the Bitmap is
     * non-null or null if the given Bitmap is null.
     *
     * @param bitmap A Bitmap.
     */
    @Nullable
    public static BitmapResource obtain(@Nullable Bitmap bitmap, @NonNull BitmapPool bitmapPool) {
        if (bitmap == null) {
            return null;
        } else {
            return new BitmapResource(bitmap, bitmapPool);
        }
    }

    public BitmapResource(@NonNull Bitmap bitmap, @NonNull BitmapPool bitmapPool) {
        this.bitmap = Preconditions.checkNotNull(bitmap, "Bitmap must not be null");
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool, "BitmapPool must not be null");
    }

    @NonNull
    @Override
    public Class<Bitmap> getResourceClass() {
        return Bitmap.class;
    }

    @NonNull
    @Override
    public Bitmap get() {
        return bitmap;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(bitmap);
    }

    @Override
    public void recycle() {
        bitmapPool.put(bitmap);
    }

    @Override
    public void initialize() {
        bitmap.prepareToDraw();
    }
}
