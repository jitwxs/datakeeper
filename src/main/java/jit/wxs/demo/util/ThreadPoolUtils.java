package jit.wxs.demo.util;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author jitwxs
 * @date 2020年02月16日 18:14
 */
@Slf4j
public class ThreadPoolUtils {
    private static final long TIMEOUT = 100L;

    private static final long KEEP_ALIVE_TIME = 60L;

    public static ThreadPoolExecutor poolExecutor(int core, int max, Object... name) {
        ThreadFactory factory = Objects.nonNull(name) ?
                new ThreadFactoryBuilder().setNameFormat(Joiner.on(" ").join(name)).build() :
                new ThreadFactoryBuilder().build();

        return new ThreadPoolExecutor(core, max, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), factory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ScheduledThreadPoolExecutor scheduledExecutor(int core, Object... name) {
        ThreadFactory factory = Objects.nonNull(name) ?
                new ThreadFactoryBuilder().setNameFormat(Joiner.on(" ").join(name)).setDaemon(true).build() :
                new ThreadFactoryBuilder().setDaemon(true).build();

        return new ScheduledThreadPoolExecutor(core, factory);
    }

    public static void shutdown(ExecutorService executor) {
        if(executor != null) {
            executor.shutdown();

            try {
                while (!executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.info("ThreadPoolUtils#shutdown is not shutdown yet");
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                log.warn("ThreadPoolUtils#shutdown error", e);
            }
            log.info("ThreadPoolUtils#shutdown success");
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            log.info("ThreadPoolUtils#sleep error, timeout: {}", timeout, e);
        }
    }
}
