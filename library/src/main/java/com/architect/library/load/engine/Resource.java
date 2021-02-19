package com.architect.library.load.engine;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午4:31
 */
public interface Resource<Z> {

    Class<Z> getResourceClass();

    Z get();

    int getSize();

    void recycle();
}
