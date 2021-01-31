package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;

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


    private void notifyCallbacksOfResult(BitmapDrawable bitmapDrawable) {
        engineJobListener.onEngineJobComplete(this, key, bitmapDrawable);
    }

    @Override
    public void onResourceReady(BitmapDrawable bitmapDrawable) {
        notifyCallbacksOfResult(bitmapDrawable);
    }

    @Override
    public void onLoadFailed() {

    }
}
