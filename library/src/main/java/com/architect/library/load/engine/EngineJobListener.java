package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;

import com.architect.library.Key;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午3:29
 */
public interface EngineJobListener {

    void onEngineJobComplete(EngineJob engineJob, Key key, BitmapDrawable bitmapDrawable);

    void onEngineJobCancelled(EngineJob engineJob, Key key);
}
