package com.architect.library.load.cache;

import com.architect.library.Key;
import com.architect.library.load.engine.Resource;

/**
 * @Author: dly
 * @CreateDate: 2021/1/8 下午3:04
 */
public interface MemoryCache {


    Resource remove(Key key);
}
