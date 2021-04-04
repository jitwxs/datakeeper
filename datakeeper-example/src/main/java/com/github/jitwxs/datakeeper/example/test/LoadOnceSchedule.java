package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.example.cache.LoadOnceCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 验证 {@link com.github.jitwxs.datakeeper.core.AbstractCache} 定时刷新
 * @author jitwxs
 * @date 2021年04月04日 20:13
 */
@Service
public class LoadOnceSchedule {
    @Autowired
    private LoadOnceCache loadOnceCache;

    @Scheduled(fixedDelay = 500L, initialDelay = 1000L)
    public void readExchangeRate() {
        System.out.println("LoadOnceCache : " + loadOnceCache.getAll());
    }
}
