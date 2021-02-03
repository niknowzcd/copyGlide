package com.architect.library.load.engine;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午4:31
 */
public interface Resource<Z> {


    Z get();

    void recycle();
}
