package com.github.jitwxs.datakeeper;

import com.github.jitwxs.datakeeper.util.ThreadPoolUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jitwxs
 * @date 2020年04月11日 19:31
 */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolUtils.poolExecutor(1, 1, "demo-");
        executor.execute(() -> {
            double r = 1 / 0;
        });

        Thread.sleep(1000L);
    }
}
