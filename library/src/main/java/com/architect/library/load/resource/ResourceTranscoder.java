package com.architect.library.load.resource;

import com.architect.library.load.engine.Resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/2/4 下午5:23
 *
 * 将给定的资源转化成另外一种资源，
 * Z 表示原资源
 * R 表示目标资源
 */
public interface ResourceTranscoder<Z, R> {

    @Nullable
    Resource<R> transcode(@NonNull Resource<Z> toTranscode);
}
