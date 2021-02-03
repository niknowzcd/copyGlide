package com.architect.library.load.engine;

import android.graphics.drawable.BitmapDrawable;
import android.os.Process;

import com.architect.library.Key;
import com.architect.library.util.ThreadUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: dly
 * @CreateDate: 2021/1/7 下午7:46
 * <p>
 * 活动缓存也是第一级缓存
 * <p>
 * WeakReference 参考
 * https://blog.csdn.net/gdutxiaoxu/article/details/80738581
 * https://www.jianshu.com/p/f86d3a43eec5
 * <p>
 * Q:MemoryCache底层用的是LinkedHashMap.为什么还要多一层ActiveCache呢?
 * A:ActiveCache可以理解为MemoryCache的备用空间.
 * 当需要用到某个图片的时候，将图片移动到ActiveCache，而将MemoryCache对应的图片删除。这样能延迟MemoryCache因为内存过大，进行的清理工作。
 * <p>
 * Q:那为什么有了ActiveCache，又要MemoryCache呢？
 * A:因为ActiveCache采用的是WeakReference虚引用，在很多情况下都容易被清理掉，这个时候如果没有MemoryCache的话，就只能去磁盘或者网络获取了。
 * <p>
 * Q:MemoryCache不是LRU算法的吗？应该会自动清理内存才对？
 * A:MemoryCache内部实现是一个LinkedHashMap,初始化大小是固定的，随着存储的内容增加自动扩容，增加ActiveCache也是为了延迟扩容的时机
 * <p>
 * 举个例子说明，假设MemoryCache初始化大小为8，最大容量为16。这个时候界面上已经加载了8个数据了，需要新增加一个数据，如果没有ActiveCache的话
 * MemoryCache就需要去扩容，而有了ActiveCache。8个数据的引用都在ActiveCache上，而MemoryCache只有新增加的一个数据。
 */

final class ActiveResource {

    private final Executor monitorClearedResourcesExecutor =
            Executors.newSingleThreadExecutor(ThreadUtils.threadFactory("glide-active-resources", Process.THREAD_PRIORITY_BACKGROUND));
    final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<>();
    private final ReferenceQueue<EngineResource<?>> resourceReferenceQueue = new ReferenceQueue<>();

    private volatile boolean isShutdown;

    public ActiveResource() {

        monitorClearedResourcesExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cleanReferenceQueue();
            }
        });
    }

    //激活储存的，相当于put
    synchronized void activate(Key key, EngineResource<?> resource) {
        ResourceWeakReference toPut = new ResourceWeakReference(resource, resourceReferenceQueue, key, true);

        //map在put数据的时候，如果同一个key下已经有了数据，在覆盖的同时会将被覆盖的数据返回回来
        ResourceWeakReference removed = activeEngineResources.put(key, toPut);

        if (removed != null) removed.reset();
    }

    //释放缓存
    synchronized void deactivate(Key key) {
        ResourceWeakReference removed = activeEngineResources.remove(key);
        if (removed != null) removed.reset();
    }

    synchronized EngineResource<?> get(Key key) {
        ResourceWeakReference activeRef = activeEngineResources.get(key);
        if (activeRef == null) return null;

        EngineResource<?> active = activeRef.get();
        if (active == null) {
            cleanupActiveReference(activeRef);
        }
        return active;
    }

    /**
     * 创建ActiveResource的时候就开启一个线程，不断的轮训 resourceReferenceQueue 队列
     * 如果 resourceReferenceQueue 队列里面有对象，就说明这个对象已经被回收了，这时需要手动去清理 activeEngineResources的引用
     * <p>
     * queue.remove() 内部是一个阻塞式方法,所以正常流程下，这个线程可以一直开着
     */
    private void cleanReferenceQueue() {
        while (!isShutdown) {
            try {
                Reference<? extends EngineResource<?>> remove = resourceReferenceQueue.remove();
                ResourceWeakReference ref = (ResourceWeakReference) remove;
                cleanupActiveReference(ref);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void cleanupActiveReference(ResourceWeakReference activeRef) {
        synchronized (this) {
            activeEngineResources.remove(activeRef.key);

            //todo
        }
    }

    void shutDown() {
        isShutdown = true;
        if (monitorClearedResourcesExecutor instanceof ExecutorService) {
            ExecutorService service = (ExecutorService) monitorClearedResourcesExecutor;
            ThreadUtils.shutdownAndAwaitTermination(service);
        }
    }

    /**
     * WeakReference.super 可以接受两个参数，第二个参数是一个ReferenceQueue队列。
     * 当referent被回收的时候，会把这个对象放到ReferenceQueue中 ，可以通过System.gc()来手动回收
     */
    static final class ResourceWeakReference extends WeakReference<EngineResource<?>> {
        final Key key;
        final boolean isCacheable;
        Resource<?> resource;


        public ResourceWeakReference(EngineResource<?> referent, ReferenceQueue<? super EngineResource<?>> q, Key key, boolean isCacheable) {
            super(referent, q);
            this.key = key;
            this.isCacheable = isCacheable;
        }

        void reset() {
            resource = null;
            clear();
        }
    }

}
