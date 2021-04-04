package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.core.AbstractCache;
import com.github.jitwxs.datakeeper.example.cache.RetainLostKeyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 验证 {@link AbstractCache#retainLostKey()}
 * @author jitwxs
 * @date 2021年04月04日 23:53
 */
@Service
public class RetainLostKeyCacheSchedule {
    @Autowired
    private RetainLostKeyCache retainLostKeyCache;

    @Scheduled(fixedDelay = 1000L, initialDelay = 1200L)
    public void readExchangeRate() {
        System.out.println("RetainLostKeyCache Key Size: " + retainLostKeyCache.getAll().keySet().size());
    }
}
