package com.architect.library.load.engine;

import com.architect.library.Key;
import com.architect.library.load.model.ResourceCallback;

public class Engine implements DecodeJob.Callback, EngineJobListener, EngineResource.ResourceListener {

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

    public <Drawable> void load(ResourceCallback resourceCallback, String urlString, Class<Drawable> transcodeClass) {
        this.resourceCallback = resourceCallback;

        EngineKey key = new EngineKey(urlString);
        EngineResource<?> resource = loadFromMemory(key, true);

        if (resource == null) {
            waitForExistingOrStartNewJob(key, urlString, transcodeClass);
        } else {
            this.resourceCallback.onResourceReady(resource);
        }
    }

    /**
     * 复用老的enginJob或者开启一个新的engineJob
     */
    private <Drawable> void waitForExistingOrStartNewJob(EngineKey key, String urlString, Class<Drawable> drawableClass) {
        EngineJob<?> current = jobs.get(key, false);
        if (current != null) {
            //todo
            return;
        }

        EngineJob<Drawable> engineJob = new EngineJob<>(key, true, this);
        DecodeJob<Drawable> decodeJob = new DecodeJob<>(urlString, engineJob, drawableClass);

        engineJob.start(decodeJob);
    }


    private EngineResource<?> loadFromMemory(EngineKey key, boolean isMemoryCacheable) {
        if (!isMemoryCacheable) return null;

        //从活动缓存加载
        EngineResource<?> active = loadFromActiveResources(key);
        if (active != null) return active;

        //从缓存中加载
        EngineResource<?> cache = loadFromCache(key);
        if (cache != null) return cache;

        return null;
    }

    private EngineResource<?> loadFromActiveResources(EngineKey key) {
        EngineResource<?> resource = activeResource.get(key);

        if (resource != null) {
            resource.acquire();
        }
        return resource;
    }

    /**
     * 内存缓存的加入时机在资源释放的时候，
     */
    private EngineResource<?> loadFromCache(EngineKey key) {
        EngineResource<?> cached = getEngineResourceFromCache(key);
        //从内存缓存取到的数据，放入到活动缓存中
        if (cached != null) {
            activeResource.activate(key, cached);
        }
        return cached;
    }

    private EngineResource<?> getEngineResourceFromCache(EngineKey key) {
        Resource<?> cached = memoryCache.remove(key);

        final EngineResource<?> result;
        if (cached instanceof EngineResource) {
            result = (EngineResource<?>) cached;
        } else {
            result = null;
        }

        return result;
    }

    @Override
    public void onResourceReady(Resource resource) {
        resourceCallback.onResourceReady(resource);
    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onEngineJobComplete(EngineJob engineJob, Key key, EngineResource<?> resource) {
//        if (resource != null) {
//            activeResource.activate(key, resource);
//        }
//        jobs.removeIfCurrent(key, engineJob);

        //todo 这里的回调后续要调整
        resourceCallback.onResourceReady(resource);
    }

    @Override
    public void onEngineJobCancelled(EngineJob engineJob, Key key) {
        jobs.removeIfCurrent(key, engineJob);
    }

    @Override
    public void onResourceReleased(Key key, EngineResource<?> resource) {
        activeResource.deactivate(key);
        if (resource.isMemoryCacheable()) {
            memoryCache.put(key, resource);
        }
    }
}
