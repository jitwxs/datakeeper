package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.example.cache.ExchangeRateCache;
import com.github.jitwxs.datakeeper.listener.CacheListener;
import org.springframework.stereotype.Service;

/**
 * @author jitwxs
 * @date 2021年04月04日 20:37
 */
@Service
public class ExchangeRateCacheListener implements CacheListener<ExchangeRateCache> {
    @Override
    public void onChange(Object key, Object oldValue, Object newValue) {
        System.out.println("Key: " + key + " changes, old: " + oldValue + ", new: " + newValue);
    }
}
