package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.Key;
import com.architect.library.load.model.ResourceCallback;

public class Engine implements DecodeJob.Callback, EngineJobListener {

    private static final String TAG = Engine.class.getSimpleName();

    private ResourceCallback resourceCallback;
    private final ActiveResource activeResource;
    private final LruResourceCache memoryCache;
    private static final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;

    private final Jobs jobs;

    public Engine() {
        activeResource = new ActiveResource();
        memoryCache = new LruResourceCache(MEMORY_MAX_SIZE);
        jobs = new Jobs();
    }

    public void load(ResourceCallback resourceCallback, String urlString) {
        this.resourceCallback = resourceCallback;

        EngineKey key = new EngineKey(urlString);
        BitmapDrawable bitmapDrawable = loadFromMemory(key, true);
        if (bitmapDrawable == null) {
            waitForExistingOrStartNewJob(key, urlString);
        } else {
            this.resourceCallback.onResourceReady(bitmapDrawable);
        }
    }

    /**
     * 复用老的enginJob或者开启一个新的engineJob
     */
    private void waitForExistingOrStartNewJob(EngineKey key, String urlString) {
        EngineJob current = jobs.get(key, false);
        if (current != null) {
            //todo
            return;
        }

        EngineJob engineJob = new EngineJob(key, true, this);
        DecodeJob decodeJob = new DecodeJob(urlString, engineJob);

        engineJob.start(decodeJob);
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

    /**
     * 磁盘缓存的加入时机在资源释放的时候添加的，
     */
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
    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onEngineJobComplete(EngineJob engineJob, Key key, BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            activeResource.activate(key, bitmapDrawable);
        }
        jobs.removeIfCurrent(key, engineJob);

        //todo 这里的回调后续要调整
        resourceCallback.onResourceReady(bitmapDrawable);
    }

    @Override
    public void onEngineJobCancelled(EngineJob engineJob, Key key) {
        jobs.removeIfCurrent(key, engineJob);
    }
}
