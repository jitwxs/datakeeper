package com.github.jitwxs.datakeeper;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 缓存管理
 * @author jitwxs
 * @date 2021年04月04日 18:13
 */
@Slf4j
@Component
@ConditionalOnBean(AbstractCache.class)
public class DatakeeperAutoConfiguration {
    /**
     * 业务定义的所有缓存
     */
    @Autowired(required = false)
    private List<AbstractCache> defineCacheList;

    /**
     * 业务定义的所有缓存监听器
     */
    @Autowired(required = false)
    private List<CacheListener> cacheListenerList;

    private ScheduledThreadPoolExecutor executor = null;

    @PostConstruct
    public void startup() {
        final Map<Boolean, List<AbstractCache>> groupRefreshRateMap = defineCacheList.stream()
                .collect(Collectors.groupingBy(e -> Objects.isNull(e.refreshRate())));

        if(groupRefreshRateMap.containsKey(true)) {
            // 非自动刷新缓存，手动触发一次刷新
            CompletableFuture.allOf(groupRefreshRateMap.get(true).stream()
                    .map(e -> CompletableFuture.runAsync(e::refreshCache)).toArray(CompletableFuture[]::new)).join();
        }

        if(groupRefreshRateMap.containsKey(false)) {
            // 自动刷新缓存，定时任务刷新
            final List<AbstractCache> autoRefreshCache = groupRefreshRateMap.get(false);

            final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("datakeeper-refresh-" + "%d")
                    .setUncaughtExceptionHandler((t, e) -> log.error("Datakeeper Refresh Cache error", e))
                    .build();
            executor = new ScheduledThreadPoolExecutor(autoRefreshCache.size(), threadFactory);

            autoRefreshCache.forEach(cache -> executor.scheduleWithFixedDelay(cache::refreshCache, cache.refreshRate().getInitialDelay(),
                    cache.refreshRate().getDelay(), cache.refreshRate().getUnit()));
        }

        if(CollectionUtils.isNotEmpty(cacheListenerList)) {
            final Map<Class, AbstractCache> defineCacheMap = defineCacheList.stream()
                    .collect(Collectors.toMap(AbstractCache::getClass, Function.identity()));

            cacheListenerList.forEach(listener -> {
                final Class actualType = fetchListenerActualType(listener);
                if(actualType != null && defineCacheMap.containsKey(actualType)) {
                    defineCacheMap.get(actualType).registerListener(listener);
                }
            });
        }
    }

    @PreDestroy
    public void destroy() {
        if(executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private Class fetchListenerActualType(CacheListener listener) {
        try {
            final Class<? extends CacheListener> clazz = listener.getClass();

            for (Type type : clazz.getGenericInterfaces()) {
                // 泛型属于 ParameterizedType，需要做类型转换
                if(type instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) type;

                    return (Class) parameterizedType.getActualTypeArguments()[0];
                }
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("DatakeeperAutoConfiguration resolve CacheListener type error", e);
        }
    }
}
