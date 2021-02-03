package com.architect.library.load.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.architect.library.SourceGenerator;

import java.io.InputStream;

public class DecodeJob implements Runnable, DataFetcherGenerator.FetcherReadyCallback {

    private DataFetcherGenerator currentGenerator;
    private String urlString;
    private Callback callback;

    public DecodeJob(String urlString, Callback callback) {
        this.urlString = urlString;
        this.callback = callback;
    }

    @Override
    public void run() {
        currentGenerator = getNextGenerator();
        currentGenerator.startNext();
    }

    private DataFetcherGenerator getNextGenerator() {

        return new SourceGenerator(urlString, this);
    }

    @Override
    public void onDataFetcherReady(Object data) {
        //这里有一系列的读取字符流过程,暂时省去
        InputStream inputStream = (InputStream) data;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

        EngineResource<BitmapDrawable> resource = new EngineResource<>(bitmapDrawable);
        callback.onResourceReady(resource);
    }

    public interface Callback {
        void onResourceReady(EngineResource<?> resource);

        void onLoadFailed();
    }
}
