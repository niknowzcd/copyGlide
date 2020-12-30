package com.architect.library.load;

import com.architect.library.Priority;

public interface DataFetcher<T> {


    interface DataCallback<T> {
        void onDataReady(T data);

        void onLoadFailed(Exception e);
    }

    void loadData(Priority priority, DataCallback<? super T> callback);

    void cancel();

    void cleanup();

}
