package com.architect.library.load.cache;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.Key;

/**
 * @Author: dly
 * @CreateDate: 2021/1/8 下午3:04
 */
public interface MemoryCache {


    BitmapDrawable remove(Key key);
}
