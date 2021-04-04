package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.example.cache.LazyExampleCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 验证懒加载式缓存
 * @author jitwxs
 * @date 2021年04月05日 0:49
 */
@Service
public class LazyExampleSchedule {
    @Autowired
    private LazyExampleCache lazyExampleCache;

    @Scheduled(fixedDelay = 1000L, initialDelay = 1000L)
    public void readExchangeRate() {
        System.out.println("LazyExampleCache : " + lazyExampleCache.getValue(1));
        System.out.println("LazyExampleCache : " + lazyExampleCache.getAll());
    }
}
