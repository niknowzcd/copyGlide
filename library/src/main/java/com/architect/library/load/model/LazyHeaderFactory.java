package com.architect.library.load.model;

/**
 * 源代码中这个接口只有一个地方调用，但又单独拿了出来
 * 私以为是为了测试方便
 */
public interface LazyHeaderFactory {

    String buildHeaders();
}
