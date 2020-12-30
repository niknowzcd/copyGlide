package com.architect.library.manager;

import com.architect.library.util.Util;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ActivityFragmentLifecycle implements Lifecycle {

    /**
     * 弱引用的数组很容易会被回收，需要在使用之前需要判断一下
     * 这里采用的方式是，使用之前遍历一下所有数据，将其放到一个新的list下
     * see Util.getSnapshot(lifecycleListeners)
     */
    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());

    private boolean isStarted;
    private boolean isDestroyed;


    @Override
    public void addListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    void onStart() {
        System.out.println("ActivityFragmentLifecycle onStart");
        isStarted = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        System.out.println("ActivityFragmentLifecycle onStop");
        isStarted = false;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    void onDestory() {
        System.out.println("ActivityFragmentLifecycle onDestory");
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }
}
