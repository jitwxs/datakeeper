package com.github.jitwxs.datakeeper.example.cache;

import com.github.jitwxs.datakeeper.core.AbstractCache;
import com.github.jitwxs.datakeeper.core.CacheRefreshRate;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 汇率缓存
 * @author jitwxs
 * @date 2021年04月04日 19:31
 */
@Service
public class ExchangeRateCache extends AbstractCache<Pair<String, String>, BigDecimal> {
    volatile int count = 0;

    @Override
    public Map<Pair<String, String>, BigDecimal> loadCache() throws Throwable {
        Map<Pair<String, String>, BigDecimal> result = new HashMap<>();

        result.put(Pair.of("CNY", "USD"), BigDecimal.valueOf(RandomUtils.nextDouble()));
        result.put(Pair.of("FRF", "HKT"), BigDecimal.valueOf(RandomUtils.nextDouble()));

        if(count++ > 5) {
            result.remove(Pair.of("CNY", "USD"));
        }

        return result;
    }

    @Override
    public CacheRefreshRate refreshRate() {
        return new CacheRefreshRate(100L, 500L, TimeUnit.MILLISECONDS);
    }
}
