package com.architect.library.load.engine;

import androidx.annotation.Nullable;

public interface DataFetcherGenerator {

    interface FetcherReadyCallback {
        void onDataFetcherReady(Object data);
    }

    boolean startNext();
}
