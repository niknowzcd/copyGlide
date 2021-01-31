package com.architect.library.util;

import android.util.Log;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午2:41
 */

public class MyLogger {

    public static final String TAG = "copy_glide";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }
}
