package com.architect.library.load.engine;

import com.architect.library.Key;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.VisibleForTesting;

public class EngineJob<Drawable> implements DecodeJob.Callback<Drawable> {

    private Key key;
    private boolean isCacheable;
    private EngineJobListener engineJobListener;
    private final EngineResourceFactory engineResourceFactory;

    public EngineJob(Key key, boolean isCacheable, EngineJobListener engineJobListener) {
        this.key = key;
        this.isCacheable = isCacheable;
        this.engineJobListener = engineJobListener;
        engineResourceFactory = new EngineResourceFactory();
    }

    public synchronized void start(DecodeJob<Drawable> decodeJob) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(decodeJob);
    }

    private void notifyCallbacksOfResult(Resource<Drawable> resource) {
        EngineResource<Drawable> engineResource = engineResourceFactory.build(resource, true, null, null);
        engineJobListener.onEngineJobComplete(this, key, engineResource);

        //todo 这里会有个资源释放的过程
    }

    @Override
    public void onResourceReady(Resource<Drawable> resource) {
        notifyCallbacksOfResult(resource);
    }

    @Override
    public void onLoadFailed() {

    }

    @VisibleForTesting
    static class EngineResourceFactory {
        public <R> EngineResource<R> build(
                Resource<R> resource, boolean isMemoryCacheable, Key key, EngineResource.ResourceListener listener) {
            return new EngineResource<>(resource, isMemoryCacheable, /*isRecyclable=*/ true, listener, key);
        }
    }
}
