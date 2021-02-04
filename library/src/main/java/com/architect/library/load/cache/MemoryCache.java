package com.architect.library.load.cache;

import com.architect.library.Key;
import com.architect.library.load.engine.Resource;

import androidx.annotation.NonNull;

/**
 * @Author: dly
 * @CreateDate: 2021/1/8 下午3:04
 */
public interface MemoryCache {

    interface ResourceRemovedListener {
        void onResourceRemoved(@NonNull Resource<?> removed);
    }

    Resource<?> remove(Key key);

    void setResourceRemovedListener(@NonNull ResourceRemovedListener listener);

    void clearMemory();

    /**
     * 清理缓存到某个等级
     *
     * @param level 内存的等级，{@link android.content.ComponentCallbacks2}.
     */
    void trimMemory(int level);
}
