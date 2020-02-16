package jit.wxs.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @date 2020年02月16日 18:42
 */
@Slf4j
public class LoopUtils {
    /**
     * 循环方法
     * @author jitwxs
     * @date 2020/2/16 18:46
     * @param millis 单次执行间隔
     * @param exceptionDesc 异常描述
     * @param callback 回调方法
     */
    public static void loop(long millis, String exceptionDesc, LoopCallback callback) {
        int times = 0;
        while (callback.execute(times)) {
            try {
                TimeUnit.MILLISECONDS.sleep(millis == 0L ? 500 : millis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted exception", exceptionDesc, e);
            }
            times++;
        }
        log.info("LoopUtils#loop success, times: {}", times);
    }

    public interface LoopCallback {
        /**
         * 回调接口
         * @author jitwxs
         * @date 2020/2/16 18:43
         * @return 是否继续执行；true：继续；false：退出
         */
        boolean execute(int times);
    }
}
