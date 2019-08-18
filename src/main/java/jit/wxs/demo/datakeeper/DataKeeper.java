package jit.wxs.demo.datakeeper;

import jit.wxs.demo.bean.User;
import jit.wxs.demo.datakeeper.cache.WhiteUserCache;
import jit.wxs.demo.service.WhiteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @date 2019年08月18日 17:40
 */
@Slf4j
@Component
public class DataKeeper {
    /**
     * DataKeeper任务执行线程数，保持与缓存数量一致
     */
    private static final int TASK_SCHEDULER_SIZE = 1;
    private static final long INITIAL_DELAY = 0;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    @Autowired
    private DataKeeperProperties properties;
    @Autowired
    private WhiteUserService whiteUserService;

    private WhiteUserCache whiteUserCache;

    @PostConstruct
    public void startDataKeeper() {
        log.info("[DataKeeper] dataKeeper will start, properties: {}", properties);

        if(properties.isEnabled()) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(TASK_SCHEDULER_SIZE);

            DataKeeperProperties.FailoverTime failoverTime = properties.getFailoverTime();
            DataKeeperProperties.Interval interval = properties.getInterval();

            whiteUserCache = new WhiteUserCache(whiteUserService, failoverTime.getWhiteUser());
            executorService.scheduleAtFixedRate(() -> whiteUserCache.execute(), INITIAL_DELAY, interval.getWhiteUser(), TIME_UNIT);
        }
    }

    public User getWhiteUser(long userId) {
        return whiteUserCache.get(userId);
    }
}
