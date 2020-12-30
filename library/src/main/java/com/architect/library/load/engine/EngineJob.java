package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EngineJob implements DecodeJob.Callback {


    public synchronized void start(DecodeJob decodeJob) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(decodeJob);
    }


    @Override
    public void onResourceReady(BitmapDrawable bitmapDrawable) {

    }

    @Override
    public void onLoadFailed() {

    }
}
