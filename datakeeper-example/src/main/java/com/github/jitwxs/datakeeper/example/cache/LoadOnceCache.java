package com.github.jitwxs.datakeeper.example.cache;

import com.github.jitwxs.datakeeper.core.AbstractCache;
import com.github.jitwxs.datakeeper.core.CacheRefreshRate;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证缓存仅加载一次
 * @author jitwxs
 * @date 2021年04月05日 0:01
 */
@Service
public class LoadOnceCache extends AbstractCache<Integer, Integer> {
    @Override
    public Map<Integer, Integer> loadCache() throws Throwable {
        return new HashMap<Integer, Integer>(){{
            put(RandomUtils.nextInt(), RandomUtils.nextInt());
        }};
    }

    @Override
    public CacheRefreshRate refreshRate() {
        return null;
    }
}
