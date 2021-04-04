package com.github.jitwxs.datakeeper.example.test;

import com.github.jitwxs.datakeeper.example.cache.ExchangeRateCache;
import com.github.jitwxs.datakeeper.CacheListener;
import org.springframework.stereotype.Service;

/**
 * 验证 {@link CacheListener}
 *
 * <p>每当 {@link ExchangeRateCache} 定时刷新后，{@link #onChange(Object, Object, Object)} 方法会打印出有变化的键的值</p>
 *
 * <p>当 {@link ExchangeRateCache} 的键减少时，可以观察到 {@code oldValue} 不是null，而 {@code newValue} 等于null</p>
 *
 * @author jitwxs
 * @date 2021年04月04日 20:37
 */
@Service
public class ExchangeRateCacheListener implements CacheListener<ExchangeRateCache> {
    @Override
    public void onChange(Object key, Object oldValue, Object newValue) {
        System.out.println("ExchangeRateCache Key: " + key + " changes, old: " + oldValue + ", new: " + newValue);
    }
}
