package com.architect.library.load.engine;

import com.architect.library.Key;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EngineJob implements DecodeJob.Callback {

    private Key key;
    private boolean isCacheable;
    private EngineJobListener engineJobListener;

    public EngineJob(Key key, boolean isCacheable, EngineJobListener engineJobListener) {
        this.key = key;
        this.isCacheable = isCacheable;
        this.engineJobListener = engineJobListener;
    }

    public synchronized void start(DecodeJob decodeJob) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(decodeJob);
    }

    private void notifyCallbacksOfResult(EngineResource<?> resource) {
        engineJobListener.onEngineJobComplete(this, key, resource);

        //todo 这里会有个资源释放的过程
    }

    @Override
    public void onResourceReady(EngineResource<?> resource) {
        notifyCallbacksOfResult(resource);
    }

    @Override
    public void onLoadFailed() {

    }
}
