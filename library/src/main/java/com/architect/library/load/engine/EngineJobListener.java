package com.architect.library.load.engine;

import com.architect.library.Key;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午3:29
 */
public interface EngineJobListener {

    void onEngineJobComplete(EngineJob engineJob, Key key, EngineResource<?> resource);

    void onEngineJobCancelled(EngineJob engineJob, Key key);
}
