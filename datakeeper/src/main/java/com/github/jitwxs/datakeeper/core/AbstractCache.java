package com.github.jitwxs.datakeeper.core;

import com.github.jitwxs.datakeeper.listener.CacheListener;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * 缓存业务类，实现类需交由 Spring 容器进行管理
 * @author jitwxs
 * @date 2021年04月04日 17:36
 */
@Slf4j
public abstract class AbstractCache<K, V> {
    /**
     * 缓存内容，在 master 和 slave 之间切换
     */
    protected volatile Map<K, V> masterMap = new HashMap<>(), slaveMap = new HashMap<>();

    private volatile boolean isInit = false;

    private final List<CacheListener> listenerList = new LinkedList<>();

    private final String CACHE_NAME = getClass().getSimpleName();

    /**
     * 抽象方法，加载缓存数据
     */
    public abstract Map<K, V> loadCache() throws Throwable;

    /**
     * 抽象方法，设置缓存刷新频率
     *
     * @return 缓存刷新频率，当返回 null 时，不定时刷新
     */
    public abstract CacheRefreshRate refreshRate();

    /**
     * 是否保留被删除的键，默认不保留
     *
     * <p/>例如，首次 {@link #loadCache()} 时存在名为 {@code foo} 的键。下次加载时该键不存在，如果返回 true 时，将仍然保留首次的值
     */
    public boolean retainLostKey() {
        return false;
    }

    public Map<K, V> getAll() {
        if (!this.isInit) {
            synchronized (this) {
                if (this.isInit) {
                    return this.masterMap;
                }
                log.warn("Datakeeper {} getAllCache() Before Init", CACHE_NAME);
                this.refreshCache();
            }
        }
        return this.masterMap;
    }

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @param defaultValue 当值不存在时，返回该值
     */
    public V getValue(final K key, final V defaultValue) {
        return this.getValue(key).orElse(defaultValue);
    }

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 被 {@link Optional} 包装的缓存值
     */
    public Optional<V> getValue(final K key) {
        if (!this.isInit) {
            synchronized (this) {
                if (this.isInit) {
                    return Optional.ofNullable(this.masterMap.get(key));
                }
                log.warn("Datakeeper {} getValue({}) Before Init", CACHE_NAME, key);
                this.refreshCache();
            }
        }

        return Optional.ofNullable(this.slaveMap.get(key));
    }

    /**
     * 刷新缓存
     */
    protected synchronized void refreshCache() {
        try {
            this.slaveMap = this.loadCache();

            this.switchMap();

            this.publishChanges();

            this.isInit = true;
        } catch (Throwable throwable) {
            log.error("Datakeeper {} refreshCache() failed", CACHE_NAME, throwable);
        }
    }

    protected void registerListener(CacheListener listener) {
        this.listenerList.add(listener);
    }

    /**
     * 切换 {@link #masterMap} 和 {@link #slaveMap} 值
     */
    private void switchMap() {
        if(retainLostKey()) {
            final Map<K, V> tempMap = Maps.newHashMapWithExpectedSize(masterMap.size() + slaveMap.size());
            tempMap.putAll(this.masterMap);
            tempMap.putAll(this.slaveMap);

            this.slaveMap = tempMap;
        }

        // 交换 master <-> slave
        final Map<K, V> tempMap = this.masterMap;
        this.masterMap = this.slaveMap;
        this.slaveMap = tempMap;
    }

    /**
     * 发布变化
     */
    private void publishChanges() {
        if(CollectionUtils.isEmpty(listenerList)) {
            return;
        }

        slaveMap.forEach((k, oldValue) -> {
            final V newValue = masterMap.get(k);
            listenerList.forEach(listener -> listener.onChange(k, oldValue, newValue));
        });

        masterMap.keySet().stream()
                .filter(e -> !slaveMap.containsKey(e))
                .forEach((k) -> listenerList.forEach(listener -> listener.onChange(k, null, masterMap.get(k))));
    }
}
