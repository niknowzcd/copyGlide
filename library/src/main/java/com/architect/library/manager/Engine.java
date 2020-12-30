package com.architect.library.manager;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.load.engine.DecodeJob;
import com.architect.library.load.engine.EngineJob;
import com.architect.library.load.model.ResourceCallback;

public class Engine implements DecodeJob.Callback {

    private ResourceCallback resourceCallback;


    public void load(ResourceCallback resourceCallback, String urlString) {
        this.resourceCallback = resourceCallback;

        //loadFromMemory(key, false, startTime);  从缓存中读取

        DecodeJob decodeJob = new DecodeJob(urlString, this);
        EngineJob engineJob = new EngineJob();

        engineJob.start(decodeJob);
    }


    @Override
    public void onResourceReady(BitmapDrawable bitmapDrawable) {
        resourceCallback.onResourceReady(bitmapDrawable);
    }

    @Override
    public void onLoadFailed() {

    }
}
