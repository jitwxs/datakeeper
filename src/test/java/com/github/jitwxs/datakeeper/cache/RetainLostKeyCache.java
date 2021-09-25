package com.github.jitwxs.datakeeper.cache;

import com.github.jitwxs.datakeeper.AbstractCache;
import com.github.jitwxs.datakeeper.CacheRefreshRate;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 保留已被删除的键
 * @author jitwxs
 * @date 2021年04月04日 23:51
 */
@Service
public class RetainLostKeyCache extends AbstractCache<Integer, BigDecimal> {
    @Override
    public Map<Integer, BigDecimal> loadCache() throws Throwable {
        return new HashMap<Integer, BigDecimal>(){{
           put(RandomUtils.nextInt(), BigDecimal.valueOf(RandomUtils.nextDouble()));
        }};
    }

    @Override
    public CacheRefreshRate refreshRate() {
        return new CacheRefreshRate(500L, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean retainLostKey() {
        return true;
    }
}
