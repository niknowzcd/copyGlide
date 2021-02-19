package com.architect.library.load.engine;

import com.architect.library.GlideContext;
import com.architect.library.Registry;

/**
 * DecodeJob的协助类
 * 作用：
 * 1.存储着DecodeJob拥有的变量，在创建关联类的时候代替DecodeJob传入
 */
final class DecodeHelper<Drawable> {

    private GlideContext glideContext;
    private Class<?> resourceClass;
    private Class<Drawable> transcodeClass;

    public DecodeHelper(Class<?> resourceClass, Class<Drawable> transcodeClass) {
        this.resourceClass = resourceClass;
        this.transcodeClass = transcodeClass;
    }

    LoadPath<Object, ?, Drawable> getLoadPath(Class<Object> dataClass) {
        return Registry.getLoadPath(dataClass, resourceClass, transcodeClass);
    }
}
