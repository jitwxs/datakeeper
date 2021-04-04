package com.github.jitwxs.datakeeper;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * 懒加载缓存基类，实现类需交由 Spring 容器进行管理
 * @author jitwxs
 * @date 2021年04月05日 0:12
 */
@Slf4j
public abstract class AbstractLazyCache<K, V> extends AbstractCache<K, V> implements DisposableBean {
    /**
     * 懒加载缓存
     */
    private final Map<K, Item<V>> lazyMap = new ConcurrentHashMap<>();

    /**
     * 加载单个缓存值，子类实现
     */
    public abstract V loadOne(K key);

    /**
     * 子类提供用户刷新的缓存线程池
     */
    public abstract ExecutorService refreshExecutor();

    @Override
    public Optional<V> getValue(K key) {
        final Item<V> item = this.lazyMap.compute(key, (k, v) -> {
            if (v == null) {
                return new Item<>(loadOne(key), refreshRate());
            } else {
                try {
                    return v;
                } finally {
                    if (v.isExpire()) {
                        this.refreshExecutor().execute(() -> reload(key, v));
                    }
                }
            }
        });

        return Optional.ofNullable(item.value);
    }

    @Override
    public Map<K, V> getAll() {
        final Map<K, V> result = Maps.newHashMapWithExpectedSize(lazyMap.size());
        lazyMap.forEach((k, v) -> result.put(k, v.value));
        return result;
    }

    /**
     * 对于懒加载的缓存来说，该方法无效
     */
    @Override
    public Map<K, V> loadCache() throws Throwable {
        return Collections.emptyMap();
    }

    @Override
    public void destroy() throws Exception {
        if (this.refreshExecutor() != null && !this.refreshExecutor().isShutdown()) {
            this.refreshExecutor().shutdown();
        }
    }

    /**
     * 重新加载缓存
     */
    private synchronized void reload(final K key, final Item<V> item) {
        try {
            if (item.isExpire()) {
                item.value = this.loadOne(key);
                item.setExpireTime(this.refreshRate());
            }
        } catch (final Exception e) {
            log.error("LazyLoadMapCache {} error, key: {}, item: {}", CACHE_NAME, key, item, e);
        }
    }

    private static class Item<V> {
        /**
         * 缓存值
         */
        private V value;

        /**
         * 缓存值过期时间
         */
        private long expireTime;

        /**
         * 缓存值创建时间
         */
        private final long createTime;

        public Item(final V value, final CacheRefreshRate refreshRate) {
            this.value = value;
            this.createTime = System.currentTimeMillis();
            setExpireTime(refreshRate);
        }

        /**
         * 设置过期时间
         *
         * {@link CacheRefreshRate#getDelay()}} 和 {@link CacheRefreshRate#getUnit()} 决定了缓存值的过期时间
         */
        public void setExpireTime(final CacheRefreshRate refreshRate) {
            this.expireTime = refreshRate == null ? -1 : refreshRate.getUnit().toMillis(refreshRate.getDelay()) + this.createTime;
        }

        /**
         * 判断缓存值是否过期
         */
        public boolean isExpire() {
            return this.expireTime != -1 && System.currentTimeMillis() > this.expireTime;
        }
    }
}
