package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.model.ResourceCallback;

public class Engine implements DecodeJob.Callback {

    private static final String TAG = Engine.class.getSimpleName();

    private ResourceCallback resourceCallback;
    private final ActiveResource activeResource;
    private final LruResourceCache memoryCache;
    private static final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;

    public Engine() {
        activeResource = new ActiveResource();
        memoryCache = new LruResourceCache(MEMORY_MAX_SIZE);
    }

    public void load(ResourceCallback resourceCallback, String urlString) {
        this.resourceCallback = resourceCallback;

        EngineKey key = new EngineKey(urlString);
        BitmapDrawable bitmapDrawable = loadFromMemory(key, true);
        if (bitmapDrawable == null) {
            DecodeJob decodeJob = new DecodeJob(urlString, this);
            EngineJob engineJob = new EngineJob();

            engineJob.start(decodeJob);
        } else {
            this.resourceCallback.onResourceReady(bitmapDrawable);
        }
    }

    private BitmapDrawable loadFromMemory(EngineKey key, boolean isMemoryCacheable) {
        if (!isMemoryCacheable) return null;

        //从活动缓存加载
        BitmapDrawable active = loadFromActiveResources(key);
        if (active != null) return active;

        //从缓存中加载
        BitmapDrawable cache = loadFromCache(key);
        if (cache != null) return cache;

        return null;
    }

    private BitmapDrawable loadFromActiveResources(EngineKey key) {
        BitmapDrawable active = activeResource.get(key);

        if (active != null) {
            //todo 计数
        }
        return active;
    }

    private BitmapDrawable loadFromCache(EngineKey key) {
        BitmapDrawable cached = memoryCache.remove(key);
        //从内存缓存取到的数据，放入到活动缓存中
        if (cached != null) {
            activeResource.activate(key, cached);
        }
        return cached;

    }

    @Override
    public void onResourceReady(BitmapDrawable bitmapDrawable) {
        resourceCallback.onResourceReady(bitmapDrawable);

        if (bitmapDrawable != null) {

        }
    }

    @Override
    public void onLoadFailed() {

    }
}
