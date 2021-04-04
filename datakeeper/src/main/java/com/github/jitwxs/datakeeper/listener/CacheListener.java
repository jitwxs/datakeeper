package com.github.jitwxs.datakeeper.listener;

import com.github.jitwxs.datakeeper.core.AbstractCache;

/**
 * Cache Listener，实现类需交由 Spring 容器进行管理
 *
 * @param <T> 监听的目标 {@link AbstractCache}
 */
public interface CacheListener<T extends AbstractCache> {
    /**
     * 监听 Cache 变化
     *
     * @param key 发生变化的键
     * @param oldValue 变化前的旧值，不存在时为 null
     * @param newValue 变化后的新值，不存在时为 null
     */
    void onChange(Object key, Object oldValue, Object newValue);
}
