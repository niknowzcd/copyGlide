package com.architect.library.load.engine;

import com.architect.library.Key;
import com.architect.library.util.Preconditions;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午4:32
 * <p>
 * 资源文件的包装对象
 * 该包装对象增加了一些计数和回收的行为，主要用于各个缓存使用
 */
public class EngineResource<Z> implements Resource<Z> {

    private boolean isMemoryCacheable;
    private boolean isRecyclable;
    public Resource<Z> resource;
    private ResourceListener listener;
    private Key key;

    //计数器
    private int acquired;
    //资源是否被回收
    private boolean isRecycled;


    public EngineResource(Resource<Z> resource, boolean isMemoryCacheable, boolean isRecyclable, ResourceListener listener, Key key) {
        this.isMemoryCacheable = isMemoryCacheable;
        this.isRecyclable = isRecyclable;
        this.resource = Preconditions.checkNotNull(resource);
//        this.listener = Preconditions.checkNotNull(listener);
        this.listener = listener;
        this.key = key;
    }


    synchronized void acquire() {
        if (isRecycled) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
        }
        ++acquired;
    }

    @Override
    public Class<Z> getResourceClass() {
        return null;
    }

    @Override
    public Z get() {
        return resource.get();
    }

    @Override
    public int getSize() {
        return resource.getSize();
    }

    @Override
    public synchronized void recycle() {
        if (acquired > 0) {
            throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
        }
        if (isRecycled) {
            throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
        }
        isRecycled = true;
        if (isRecyclable) {
            resource.recycle();
        }
    }

    //todo
    void release() {
        boolean release = false;
        synchronized (this) {
            if (acquired <= 0) {
                throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
            }

            if (--acquired == 0) {
                release = true;
            }
        }

        if (release) {
            listener.onResourceReleased(key, this);
        }
    }


    boolean isMemoryCacheable() {
        return isMemoryCacheable;
    }


    interface ResourceListener {
        void onResourceReleased(Key key, EngineResource<?> resource);
    }
}
