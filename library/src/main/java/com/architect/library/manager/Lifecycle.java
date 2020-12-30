package com.architect.library.manager;

public interface Lifecycle {

    void addListener(LifecycleListener listener);

    void removeListener(LifecycleListener listener);
}
