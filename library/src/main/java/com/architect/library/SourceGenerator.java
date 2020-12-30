package com.architect.library;

import com.architect.library.load.DataFetcher;
import com.architect.library.load.HttpUrlFetcher;
import com.architect.library.load.engine.DataFetcherGenerator;
import com.architect.library.load.model.GlideUrl;

import java.io.InputStream;

public class SourceGenerator implements DataFetcherGenerator {

    private String urlString;
    private final FetcherReadyCallback fetcherReadyCallback;

    public SourceGenerator(String urlString, FetcherReadyCallback fetcherReadyCallback) {
        this.urlString = urlString;
        this.fetcherReadyCallback = fetcherReadyCallback;
    }

    @Override
    public boolean startNext() {
        HttpUrlFetcher fetcher = new HttpUrlFetcher(new GlideUrl(urlString), 3000);
        fetcher.loadData(null, new DataFetcher.DataCallback<InputStream>() {
            @Override
            public void onDataReady(InputStream data) {
                fetcherReadyCallback.onDataFetcherReady(data);
            }

            @Override
            public void onLoadFailed(Exception e) {

            }
        });

        return false;
    }
}
