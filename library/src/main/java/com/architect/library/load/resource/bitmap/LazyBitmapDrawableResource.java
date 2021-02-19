package com.architect.library.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.engine.Initializable;
import com.architect.library.load.engine.Resource;
import com.architect.library.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/2/4 下午5:45
 * <p>
 * 生成bitmapDrawable对象的地方
 */

public final class LazyBitmapDrawableResource implements Resource<BitmapDrawable>, Initializable {

    private final Resources resources;
    private final Resource<Bitmap> bitmapResource;

    @Nullable
    public static Resource<BitmapDrawable> obtain(
            @NonNull Resources resources, @Nullable Resource<Bitmap> bitmapResource) {
        if (bitmapResource == null) {
            return null;
        }
        return new LazyBitmapDrawableResource(resources, bitmapResource);
    }

    private LazyBitmapDrawableResource(
            @NonNull Resources resources, @NonNull Resource<Bitmap> bitmapResource) {
        this.resources = Preconditions.checkNotNull(resources);
        this.bitmapResource = Preconditions.checkNotNull(bitmapResource);
    }

    @Override
    public void initialize() {
        if (bitmapResource instanceof Initializable) {
            ((Initializable) bitmapResource).initialize();
        }
    }

    @Override
    public Class<BitmapDrawable> getResourceClass() {
        return null;
    }

    @Override
    public BitmapDrawable get() {
        return new BitmapDrawable(resources, bitmapResource.get());
    }

    @Override
    public int getSize() {
        return bitmapResource.getSize();
    }

    @Override
    public void recycle() {
        bitmapResource.recycle();
    }

}
