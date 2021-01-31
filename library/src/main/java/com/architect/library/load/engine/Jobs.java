package com.architect.library.load.engine;

import com.architect.library.Key;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dly
 * @CreateDate: 2021/1/31 下午3:15
 * <p>
 * 考虑到并发效率的问题，代码中在很多地方都用了数据来做缓存
 */

public class Jobs {

    private final Map<Key, EngineJob> jobs = new HashMap<>();
    private final Map<Key, EngineJob> onlyCacheJobs = new HashMap<>();

    EngineJob get(Key key, boolean onlyRetrieveFromCache) {
        return getJobMap(onlyRetrieveFromCache).get(key);
    }

    void put(Key key, EngineJob job) {
        getJobMap(false).put(key, job);
    }

    void removeIfCurrent(Key key, EngineJob expected) {
        Map<Key, EngineJob> jobMap = getJobMap(false);
        if (expected.equals(jobMap.get(key))) {
            jobMap.remove(key);
        }
    }

    private Map<Key, EngineJob> getJobMap(boolean onlyRetrieveFromCache) {
        return onlyRetrieveFromCache ? onlyCacheJobs : jobs;
    }
}
