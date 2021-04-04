package com.github.jitwxs.datakeeper.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 任务刷新频率
 * @author jitwxs
 * @date 2021年04月04日 18:23
 */
@Data
@AllArgsConstructor
public class CacheRefreshRate {
    /**
     * the time to delay first execution
     */
    private long initialDelay;

    /**
     * the delay between the termination of one execution and the commencement of the next
     */
    private long delay;

    /**
     * the time unit of the initialDelay and delay parameters
     */
    private TimeUnit unit;
}
