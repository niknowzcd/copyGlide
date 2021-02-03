package com.architect.library.load.engine;

import com.architect.library.Key;
import com.architect.library.load.cache.MemoryCache;
import com.architect.library.util.LruCache;

import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/1/8 下午3:03
 */

public class LruResourceCache extends LruCache<Key, EngineResource<?>> implements MemoryCache {
    /**
     * Constructor for LruCache.
     *
     * @param size The maximum size of the cache, the units must match the units used in {@link
     *             #getSize(Object)}.
     */
    public LruResourceCache(long size) {
        super(size);
    }

    @Override
    protected int getSize(@Nullable EngineResource<?> item) {
        return super.getSize(item);
    }


}
