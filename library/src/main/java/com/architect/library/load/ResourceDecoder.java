package com.architect.library.load;

import com.architect.library.load.engine.Resource;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: dly
 * @CreateDate: 2021/2/5 下午5:59
 */

public interface ResourceDecoder<T, Z> {

    boolean handles(@NonNull T source) throws IOException;

    @Nullable
    Resource<Z> decode(@NonNull T source) throws IOException;
}
