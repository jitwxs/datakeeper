package jit.wxs.demo.datakeeper;

import jit.wxs.demo.bean.User;
import jit.wxs.demo.datakeeper.cache.WhiteUserCache;
import jit.wxs.demo.service.WhiteUserService;
import jit.wxs.demo.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @date 2019年08月18日 17:40
 */
@Slf4j
@Component
public class DataKeeper {
    private static final long INITIAL_DELAY = 0;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private ScheduledExecutorService executorService;

    @Autowired
    private DataKeeperProperties properties;
    @Autowired
    private WhiteUserService whiteUserService;

    private WhiteUserCache whiteUserCache;

    @PostConstruct
    public void startDataKeeper() {
        log.info("[DataKeeper] dataKeeper will start");

        if(properties.isEnabled()) {
            executorService = ThreadPoolUtils.scheduledExecutor(getExecutorCoreSize(), "dataKeeper-scheduler-executor");

            DataKeeperProperties.FailoverTime failoverTime = properties.getFailoverTime();
            DataKeeperProperties.Interval interval = properties.getInterval();

            whiteUserCache = new WhiteUserCache(whiteUserService, failoverTime.getWhiteUser());
            executorService.scheduleAtFixedRate(() -> whiteUserCache.execute(), INITIAL_DELAY, interval.getWhiteUser(), TIME_UNIT);
        }

        log.info("[DataKeeper] dataKeeper started success, switch: {}", properties.isEnabled());
    }

    @PreDestroy
    private void destroy() {
        ThreadPoolUtils.shutdown(executorService);
    }

    public User getWhiteUser(long userId) {
        return whiteUserCache.get(userId);
    }

    private static int getExecutorCoreSize() {
        return Math.max(DataKeeperProperties.FailoverTime.class.getDeclaredFields().length, DataKeeperProperties.Interval.class.getDeclaredFields().length);
    }
}
