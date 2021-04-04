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
 *
 * <ul>
 *     <li>该类模拟了缓存的定时刷新，首次100ms，后续500ms的频率定时刷新</li>
 *     <li>当刷新次数超过 5 次时，移除了一个键，模拟了缓存的键变化</li>
 * </ul>
 *
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
