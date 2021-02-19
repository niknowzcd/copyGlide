package com.architect.library.load.data;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * @Author: dly
 * @CreateDate: 2021/2/5 下午5:00
 */
public interface DataRewinder<T> {

    interface Factory<T> {
        @NonNull
        DataRewinder<T> build(@NonNull T data);

        @NonNull
        Class<T> getDataClass();
    }

    @NonNull
    T rewindAndGet() throws IOException;


    T getData();

    void cleanup();
}
