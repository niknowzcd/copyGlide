package com.architect.library.load.data;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * @Author: dly
 * @CreateDate: 2021/2/19 下午2:59
 */

public class SingleDataRewinder<T> implements DataRewinder<T> {

    private T data;


    public SingleDataRewinder(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @NonNull
    @Override
    public T rewindAndGet() throws IOException {
        return null;
    }

    @Override
    public void cleanup() {

    }
}
