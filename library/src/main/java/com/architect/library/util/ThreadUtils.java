package com.architect.library.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    public static ThreadFactory threadFactory(final String name, final int priority) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, name);
                thread.setPriority(priority);
                return thread;
            }
        };
    }


    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        long shutdownSeconds = 5;
        pool.shutdownNow();
        try {
            if (!pool.awaitTermination(shutdownSeconds, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(shutdownSeconds, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Failed to shutdown");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);
        }
    }
}
