package com.github.jitwxs.datakeeper.test;

import com.github.jitwxs.datakeeper.cache.ExchangeRateCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 验证 {@link com.github.jitwxs.datakeeper.AbstractCache} 定时刷新
 * @author jitwxs
 * @date 2021年04月04日 20:13
 */
@Service
public class ReadExchangeRateSchedule {
    @Autowired
    private ExchangeRateCache exchangeRateCache;

    @Scheduled(fixedDelay = 500L, initialDelay = 1000L)
    public void readExchangeRate() {
        System.out.println("ExchangeRateCache : " + exchangeRateCache.getAll());
    }
}
