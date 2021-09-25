package com.github.jitwxs.datakeeper.cache;

import com.github.jitwxs.datakeeper.AbstractLazyCache;
import com.github.jitwxs.datakeeper.CacheRefreshRate;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 懒加载缓存
 * @author jitwxs
 * @date 2021年04月05日 0:48
 */
@Service
public class LazyExampleCache extends AbstractLazyCache<Integer, Integer> {
    @Override
    public Integer loadOne(Integer integer) {
        return RandomUtils.nextInt();
    }

    @Override
    public ExecutorService refreshExecutor() {
        return Executors.newFixedThreadPool(1);
    }

    /**
     * {@link CacheRefreshRate#getDelay()}} 和 {@link CacheRefreshRate#getUnit()} 决定了缓存值的过期时间
     */
    @Override
    public CacheRefreshRate refreshRate() {
        return new CacheRefreshRate(0L, 500L, TimeUnit.MILLISECONDS);
    }
}
