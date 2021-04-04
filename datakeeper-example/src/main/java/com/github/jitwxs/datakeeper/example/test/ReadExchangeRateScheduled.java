package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.example.cache.ExchangeRateCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author jitwxs
 * @date 2021年04月04日 20:13
 */
@Service
public class ReadExchangeRateScheduled {
    @Autowired
    private ExchangeRateCache exchangeRateCache;

    @Scheduled(fixedDelay = 500L, initialDelay = 1000L)
    public void readExchangeRate() {
        System.out.println(exchangeRateCache.getAll());
    }
}
