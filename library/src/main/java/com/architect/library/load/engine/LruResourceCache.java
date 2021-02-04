package com.architect.library.load.engine;

import com.architect.library.Key;
import com.architect.library.load.cache.MemoryCache;
import com.architect.library.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/1/8 下午3:03
 *
 * 第二级内存缓存
 */

public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {

    private ResourceRemovedListener listener;

    public LruResourceCache(long size) {
        super(size);
    }

    @Override
    protected int getSize(@Nullable Resource<?> item) {
        if (item == null) {
            return super.getSize(null);
        } else {
            return item.getSize();
        }
    }

    @Override
    public void setResourceRemovedListener(@NonNull ResourceRemovedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemEvicted(@NonNull Key key, @Nullable Resource<?> item) {
        if (listener != null && item != null) {
            listener.onResourceRemoved(item);
        }
    }

    @Override
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level == android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
                || level == android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            trimToSize(getMaxSize() / 2);
        }
    }
}
