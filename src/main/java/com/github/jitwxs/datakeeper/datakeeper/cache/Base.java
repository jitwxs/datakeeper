package com.github.jitwxs.datakeeper.datakeeper.cache;

import lombok.extern.slf4j.Slf4j;

/**
 * DataKeeper 缓存执行基类
 * @author jitwxs
 * @date 2019年08月18日 17:33
 */
@Slf4j
public abstract class Base {
    /**
     * 刷新最大可接受延时: 5s
     */
    private static final int MAX_ACCEPT_DELAY = 5_000;
    /**
     * 刷新日志打印间隔：20s
     */
    private static final int LOGGING_DELAY = 20_000;

    private final String name;

    private long lastTime = 0;

    Base(String name) {
        this.name = name;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        try {
            refresh();
        } catch (Exception e) {
            log.error("[DataKeeper] refresh {} exception", name, e);
        }
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        if(timeDiff > MAX_ACCEPT_DELAY) {
            log.warn("[DataKeeper] refresh {} delay, {} ms", name, timeDiff);
        }
        if((endTime - lastTime) > LOGGING_DELAY) {
            log.info("[DataKeeper] {} refreshed, {} ms", name, timeDiff);
            lastTime = endTime;
        }
    }

    /**
     * 缓存刷新逻辑
     * @author jitwxs
     * @date 2019/8/18 17:36
     */
    abstract void refresh();
}
